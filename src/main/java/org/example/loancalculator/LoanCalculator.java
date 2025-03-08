package org.example.loancalculator;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.example.loancalculator.ui.MessageBox;
import org.example.loancalculator.ui.MessageProgressBar;
import org.example.loancalculator.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;

public class LoanCalculator extends Application {
    Logger LOGGER = LoggerFactory.getLogger(LoanCalculator.class);
    Stage currentStage;
    BooleanProperty isLoanNull = new SimpleBooleanProperty(true);
    SimpleStringProperty csvGenerationInProgess  = new SimpleStringProperty("Export");

    private final int BUTTON_WIDTH = 120;
    private final int HBOX_SPACING = 40;
    private final int LBL_PREF_WIDTH = 200;
    private final int TF_PREF_WIDTH = 250;
    private final String GITHUB_ISSUE_URL = "https://github.com/jdev-prateek/LoanCalculator/issues";

    private TextField tfAnnalInterestRate;
    private TextField tfNumberOfYears;
    private TextField tfLoanAmount;

    private TextArea taLoanSummary;

    private Button btnCalculate;
    private Button btnClear;
    private Button btnExport;

    private Loan currentLoan;

    Label lblAnnualInterestRate;
    Label lblNumberOfYears;
    Label lblLoanAmount;
    Label lblMonthlyPayment;
    Label lblTotalPayment;
    Label lblLoanAmountInWords;
    Label lblMonthlyPaymentVal;
    Label lblTotalPaymentVal;

    // menu
    MenuBar menuBar;

    // file menu
    Menu fileMenu;
    MenuItem menuItemSettings;
    MenuItem menuItemQuit;

    // Help menu
    Menu helpMenu;
    MenuItem menuItemIssue;
    MenuItem menuItemAbout;


    ExportTask exportTask;
    DirectoryChooser directoryChooser = new DirectoryChooser();

    Locale locale = Locale.getDefault();
    NumberFormat numberFormatInstance = NumberFormat.getInstance(locale);
    Currency currency = Currency.getInstance(locale);


    @Override
    public void start(Stage stage) throws Exception {
        currentStage = stage;
        numberFormatInstance.setMaximumFractionDigits(2);

        MessageBox.setPrimaryStage(currentStage);
        MessageProgressBar.setPrimaryStage(currentStage);

        BorderPane rootPane = new BorderPane();

        MenuBar menuBar = getMenuBar();
        HBox interestPane = getInterestPane();
        HBox yearsPane = getYearsPane();
        HBox loanAmountPane = getLoanAmountPane();
        HBox buttonPane = getButtonPane();
        HBox monthlyPaymentPane = getMonthlyPaymentPane();
        HBox totalPaymentPane = getTotalPaymentPane();
        HBox loanAmountInWordsPane = getLoanAmountInWordsPane();
        VBox amortizationPane = getAmortizationPane();

        VBox mainPane = new VBox(10);
        mainPane.setPadding(new Insets(20));
        mainPane.getChildren().addAll(interestPane,
                yearsPane, loanAmountPane, loanAmountInWordsPane, buttonPane,
                monthlyPaymentPane, totalPaymentPane, amortizationPane
        );

//        rootPane.setPadding(new Insets(20));
        rootPane.setTop(menuBar);
        rootPane.setCenter(mainPane);

        tfLoanAmount.textProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                return;
            }

            try {
                lblLoanAmountInWords.setText(NumerUtil.getToWords(Double.parseDouble(newVal)));
            } catch (NumberFormatException e) {
                LOGGER.warn("Parsing failed for loan amount {}", newVal);
                lblLoanAmountInWords.setText("Invalid number");
            }
        });

        btnCalculate.setOnAction(actionEvent -> {
            if (!Validator.isDouble(tfAnnalInterestRate, "Interest rate must be a number")
                    || !Validator.isDouble(tfNumberOfYears, "Years must be a number")
                    || !Validator.isDouble(tfLoanAmount, "Loan amount must be a number")
            ) {
                LOGGER.warn("Validation failed: One or more input fields contain invalid values.");
                return;
            }

            LOGGER.info("all inputs corrects.");

            double principal = Double.parseDouble(tfLoanAmount.getText());
            double years = Double.parseDouble(tfNumberOfYears.getText());
            double interestRate = Double.parseDouble(tfAnnalInterestRate.getText());

            LOGGER.info(String.format("Parsed principal: %.2f", principal));
            LOGGER.info(String.format("Parsed interest rate: %.2f%%", interestRate));
            LOGGER.info(String.format("Parsed years: %d", (int)years));

            currentLoan = new Loan(interestRate, principal, years * 12);

            double monthlyPayment = currentLoan.getMonthlyPayment();
            double totalAmount = currentLoan.getTotalAmount();

            lblMonthlyPaymentVal.setText(currency.getSymbol() + " " + numberFormatInstance.format(monthlyPayment));
            lblTotalPaymentVal.setText(currency.getSymbol() + " " + numberFormatInstance.format(totalAmount));

            LOGGER.info(String.format("Calculated Monthly Payment: %.2f", monthlyPayment));
            LOGGER.info(String.format("Calculated Total Payment: %.2f", totalAmount));

            taLoanSummary.setText(currentLoan.printAmortizationSchedule());
            isLoanNull.set(false);

            LOGGER.info("Generated Amortization schedule");
        });

        btnClear.setOnAction(e -> {
            tfAnnalInterestRate.setText("");
            tfNumberOfYears.setText("");
            tfLoanAmount.setText("");
            lblMonthlyPaymentVal.setText("NA");
            lblTotalPaymentVal.setText("NA");
            taLoanSummary.setText("Loan amortization detail will appear here");
            lblLoanAmountInWords.setText("");

            currentLoan = null;
            isLoanNull.set(true);

            LOGGER.info("All inputs/outputs cleared");
        });

        btnExport.setOnAction(e -> {
            exportToCSV();
        });

        menuItemSettings.setOnAction(e -> {
            stage.close();
        });

        menuItemQuit.setOnAction(e -> {
            stage.close();
        });

        menuItemIssue.setOnAction(actionEvent -> {
            getHostServices().showDocument(GITHUB_ISSUE_URL);
        });

        menuItemAbout.setOnAction(actionEvent -> {
            MessageBox.show(AppConstants.ABOUT, "About");
        });

        Scene scene = new Scene(rootPane, 600, 580);
        currentStage.setScene(scene);
        currentStage.setTitle("Loan Calculator");
        currentStage.show();
    }

    private void exportToCSV() {
        File dirPath = directoryChooser.showDialog(currentStage);
        Path savePath = Paths.get(dirPath.toString(), LocalDateTime.now().toString());
        exportTask = new ExportTask(currentLoan, savePath.toString() + ".csv");
        MessageProgressBar.setTask(exportTask);
        MessageProgressBar.show("", "File Export");
        new Thread(exportTask).start();
    }

    public MenuBar getMenuBar(){
        menuBar = new MenuBar();

        // file menu
        fileMenu = new Menu("_File");
        menuItemSettings = new MenuItem("_Settings");
        menuItemQuit = new MenuItem("_Quit");
        fileMenu.getItems().addAll(menuItemSettings, new SeparatorMenuItem(), menuItemQuit);

        // help menu
        helpMenu = new Menu("_Help");
        menuItemIssue = new MenuItem("_Report Issue");
        menuItemAbout = new MenuItem("_About");
        helpMenu.getItems().addAll(menuItemIssue, menuItemAbout);

        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;
    }

    public HBox getInterestPane() {
        // interest row
        lblAnnualInterestRate = new Label("Annual Interest Rate (%)");
        lblAnnualInterestRate.setPrefWidth(LBL_PREF_WIDTH);
        tfAnnalInterestRate = new TextField();
        tfAnnalInterestRate.setPromptText("Enter interest like 8.5");
        HBox.setHgrow(tfAnnalInterestRate, Priority.ALWAYS);

        HBox interestPane = new HBox(HBOX_SPACING);
        interestPane.getChildren().addAll(lblAnnualInterestRate, tfAnnalInterestRate);
        interestPane.setAlignment(Pos.CENTER_LEFT);
        return interestPane;
    }

    public HBox getYearsPane() {
        // years row
        lblNumberOfYears = new Label("Number of years:");
        lblNumberOfYears.setPrefWidth(LBL_PREF_WIDTH);
        tfNumberOfYears = new TextField();
        tfNumberOfYears.setPromptText("Enter years like 10");
        tfNumberOfYears.setPrefWidth(TF_PREF_WIDTH);
        HBox.setHgrow(tfNumberOfYears, Priority.ALWAYS);

        HBox yearsPane = new HBox(HBOX_SPACING);
        yearsPane.getChildren().addAll(lblNumberOfYears, tfNumberOfYears);
        yearsPane.setAlignment(Pos.CENTER_LEFT);
        return yearsPane;
    }

    public HBox getLoanAmountPane() {
        // years row
        lblLoanAmount = new Label("Loan Amount:");
        lblLoanAmount.setPrefWidth(LBL_PREF_WIDTH);
        tfLoanAmount = new TextField();
        tfLoanAmount.setPromptText("Enter amount like 1000");
        tfLoanAmount.setPrefWidth(TF_PREF_WIDTH);
        HBox.setHgrow(tfLoanAmount, Priority.ALWAYS);


        HBox pane = new HBox(HBOX_SPACING);
        pane.getChildren().addAll(lblLoanAmount, tfLoanAmount);
        pane.setAlignment(Pos.CENTER_LEFT);
        return pane;
    }

    public HBox getLoanAmountInWordsPane() {
        lblLoanAmountInWords = new Label("");
        lblLoanAmountInWords.setAlignment(Pos.BOTTOM_RIGHT);

        HBox.setHgrow(lblLoanAmountInWords, Priority.ALWAYS);
        HBox pane = new HBox(HBOX_SPACING);
        pane.getChildren().addAll(lblLoanAmountInWords);
        pane.setAlignment(Pos.CENTER_RIGHT);
        return pane;
    }

    public HBox getButtonPane() {
        btnCalculate = new Button("Calculate");
        btnCalculate.setPrefWidth(BUTTON_WIDTH);

        btnClear = new Button("Clear");
        btnClear.setPrefWidth(BUTTON_WIDTH);


        HBox pane = new HBox(20);
        pane.getChildren().addAll(btnCalculate, btnClear);
        pane.setAlignment(Pos.BOTTOM_RIGHT);
        return pane;
    }

    public HBox getMonthlyPaymentPane() {
        lblMonthlyPayment = new Label("Monthly Payment:");
        lblMonthlyPayment.setPrefWidth(LBL_PREF_WIDTH);

        lblMonthlyPaymentVal = new Label("NA");
        lblMonthlyPaymentVal.setPrefWidth(TF_PREF_WIDTH);

        HBox.setHgrow(lblMonthlyPaymentVal, Priority.ALWAYS);
        HBox pane = new HBox(HBOX_SPACING);
        pane.getChildren().addAll(lblMonthlyPayment, lblMonthlyPaymentVal);
        return pane;
    }

    public HBox getTotalPaymentPane() {
        lblTotalPayment = new Label("Total Payment:");
        lblTotalPayment.setPrefWidth(LBL_PREF_WIDTH);

        lblTotalPaymentVal = new Label("NA");
        lblTotalPaymentVal.setPrefWidth(TF_PREF_WIDTH);

        HBox pane = new HBox(HBOX_SPACING);
        pane.getChildren().addAll(lblTotalPayment, lblTotalPaymentVal);
        pane.setAlignment(Pos.CENTER_LEFT);
        return pane;
    }

    public VBox getAmortizationPane(){
        Label lbl = new Label("Amortization details");

        taLoanSummary = new TextArea("Loan amortization detail will appear here");
        taLoanSummary.setStyle("-fx-font-family: 'Monospaced';");
        taLoanSummary.setEditable(false);
        taLoanSummary.setMinHeight(250);

        btnExport = new Button("Export");
        btnExport.setPrefWidth(BUTTON_WIDTH);
        btnExport.disableProperty().bind(isLoanNull);
        btnExport.textProperty().bind(csvGenerationInProgess);

        VBox pane = new VBox(10, lbl, taLoanSummary, btnExport);
        return pane;
    }
}
