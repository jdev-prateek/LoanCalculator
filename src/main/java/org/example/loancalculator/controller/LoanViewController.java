package org.example.loancalculator.controller;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.example.loancalculator.appender.JavaFXAppender;
import org.example.loancalculator.task.ExportTask;
import org.example.loancalculator.utils.AppConstants;
import org.example.loancalculator.utils.Validator;
import org.example.loancalculator.utils.Loan;
import org.example.loancalculator.utils.AppState;
import org.example.loancalculator.utils.NumerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Controller
public class LoanViewController {
    private static final Logger log = LoggerFactory.getLogger(LoanViewController.class);

    @FXML
    private TabPane tabPane;

    @FXML
    private TextArea logTextArea;

    @FXML
    private MenuItem menuItemSettings;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private MenuItem menuItemIssue;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private TextField txtInterestRate;

    @FXML
    private TextField txtNoOfYears;

    @FXML
    private TextField txtLoanAmount;

    @FXML
    private Label lblNumbersInWords;

    @FXML
    private Button btnCalculate;

    @FXML
    private Button btnCancel;

    @FXML
    private TextArea loanDetails;

    @FXML
    private Button btnExport;

    private Stage primaryStage;
    private Loan currentLoan;
    DirectoryChooser directoryChooser = new DirectoryChooser();
    ExportTask exportTask;

    private final MessageBoxController messageBoxController;
    private final MessageProgressBoxController messageProgressBoxController;
    private final SettingsViewController settingsViewController;
    private final AboutViewController aboutViewController;

    public LoanViewController(MessageBoxController messageBoxController,
                              MessageProgressBoxController messageProgressBoxController,
                              SettingsViewController settingsViewController,
                              AboutViewController aboutViewController) {
        this.messageBoxController = messageBoxController;
        this.messageProgressBoxController = messageProgressBoxController;
        this.settingsViewController = settingsViewController;
        this.aboutViewController = aboutViewController;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;

        if (messageBoxController != null) {
            messageBoxController.setPrimaryStage(stage);
        }

        if (messageProgressBoxController != null) {
            messageProgressBoxController.setPrimaryStage(stage);
        }

        if(settingsViewController != null){
            settingsViewController.setPrimaryStage(stage);
        }

        if(aboutViewController != null){
            aboutViewController.setPrimaryStage(stage);
        }
    }

    @FXML
    private void initialize() {
        try {
            JavaFXAppender.setLogArea(logTextArea);
//
//            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/org/example/loancalculator/message-box-view.fxml"));
//            VBox messageBoxNode = loader1.load();
//            messageBoxController = loader1.getController();
//
//            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/org/example/loancalculator/message-progress-box-view.fxml"));
//            VBox messageProgressBoxNode = loader2.load();
//            messageProgressBoxController = loader2.getController();
//
//            FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/org/example/loancalculator/settings-view.fxml"));
//            SplitPane messageProgressBoxNode1 = settingsLoader.load();
//            settingsViewController = settingsLoader.getController();
//
//            FXMLLoader aboutLoader = new FXMLLoader(getClass().getResource("/org/example/loancalculator/about-view.fxml"));
//            VBox messageProgressBoxNode2 = aboutLoader.load();
//            aboutViewController = aboutLoader.getController();

//            Validator.setMessageBoxController(messageBoxController);
        } catch (RuntimeException e) {
            log.error("", e);
        }

        txtLoanAmount.textProperty().addListener(this::onLoanAmountChange);
    }

    @FXML
    protected void onCalculateClick(ActionEvent event) {
        if (!Validator.isDouble(txtInterestRate, "Interest rate must be a number")
                || !Validator.isDouble(txtNoOfYears, "Years must be a number")
                || !Validator.isDouble(txtLoanAmount, "Loan amount must be a number")
        ) {
            log.warn("Validation failed: One or more input fields contain invalid values.");
            return;
        }

        log.info("all inputs corrects.");

        double principal = Double.parseDouble(txtLoanAmount.getText());
        double years = Double.parseDouble(txtNoOfYears.getText());
        double interestRate = Double.parseDouble(txtInterestRate.getText());

        log.info(String.format("Parsed principal: %.2f", principal));
        log.info(String.format("Parsed interest rate: %.2f%%", interestRate));
        log.info(String.format("Parsed years: %d", (int) years));

        currentLoan = new Loan(interestRate, principal, years * 12);

        double monthlyPayment = currentLoan.getMonthlyPayment();
        double totalAmount = currentLoan.getTotalAmount();

        log.info(String.format("Calculated Monthly Payment: %.2f", monthlyPayment));
        log.info(String.format("Calculated Total Payment: %.2f", totalAmount));

        loanDetails.setText(currentLoan.printAmortizationSchedule());

        log.info("Generated Amortization schedule");
    }

    @FXML
    protected void onClearClick(ActionEvent  e) {
        txtInterestRate.setText("");
        txtNoOfYears.setText("");
        txtLoanAmount.setText("");
        loanDetails.setText("");
        lblNumbersInWords.setText("");

        currentLoan = null;
        log.info("All inputs/outputs cleared");
    }

    @FXML
    protected void onExportClick(ActionEvent  e) {
        if(currentLoan == null){
            messageBoxController.show("Calculate loan first", "Error");
            return;
        }

        String saveDir = AppState.getSettings().get(AppConstants.Settings.EXPORT_DIR);
        Path path = Paths.get(saveDir, LocalDateTime.now() + ".csv");

        exportTask = new ExportTask(currentLoan, path.toString());
        messageProgressBoxController.setExportTask(exportTask);
        messageProgressBoxController.show("", "File Export");
        new Thread(exportTask).start();
    }

    public void onLoanAmountChange(ObservableValue<? extends String> observableValue, String oldVal,
                                   String newVal){
        if (newVal.isEmpty()) {
            return;
        }

        try {
            lblNumbersInWords.setText(NumerUtil.getToWords(Double.parseDouble(newVal)));
        } catch (NumberFormatException e) {
            log.warn("Parsing failed for loan amount {}", newVal);
            lblNumbersInWords.setText("Invalid number");
        }
    }

    public void onMenuItemClick(ActionEvent actionEvent) {
        if(actionEvent.getSource() == menuItemSettings){
            log.info("Opening settings window");
            settingsViewController.show();
        }

        if(actionEvent.getSource() == menuItemClose){
            log.info("Closing primary stage");
            primaryStage.close();
        }

        if(actionEvent.getSource() == menuItemIssue){
            log.info("Opening GITHUB_ISSUE_URL");
            AppState.getHostServices().showDocument(AppConstants.GITHUB_ISSUE_URL);
        }

        if(actionEvent.getSource() == menuItemAbout){
            log.info("Opening about window");
            aboutViewController.show(AppConstants.ABOUT, "About");
        }
    }
}