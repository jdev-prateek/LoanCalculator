package org.example.loancalculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.loancalculator.ui.MessageBox;
import org.example.loancalculator.utils.ExportUtil;
import org.example.loancalculator.utils.Loan;
import org.example.loancalculator.utils.NumerUtil;
import org.example.loancalculator.utils.Validator;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class LoanCalculator extends Application {
    Stage currentStage;

    private final int BUTTON_WIDTH = 120;
    private final int HBOX_SPACING = 40;
    private final int LBL_PREF_WIDTH = 200;
    private final int TF_PREF_WIDTH = 250;

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

    Locale locale = Locale.getDefault();
    NumberFormat numberFormatInstance = NumberFormat.getInstance(locale);
    Currency currency = Currency.getInstance(locale);
    final String NON_NUMBERS_REGEX = "[^\\d.]";

    @Override
    public void start(Stage stage) throws Exception {
        currentStage = stage;
        numberFormatInstance.setMaximumFractionDigits(2);

        MessageBox.setPrimaryStage(currentStage);

        BorderPane rootPane = new BorderPane();

        HBox interestPane = getInterestPane();
        HBox yearsPane = getYearsPane();
        HBox loanAmountPane = getLoanAmountPane();
        HBox buttonPane = getButtonPane();
        HBox monthlyPaymentPane = getMonthlyPaymentPane();
        HBox totalPaymentPane = getTotalPaymentPane();
        HBox loanAmountInWordsPane = getLoanAmountInWordsPane();
        VBox amortizationPane = getAmortizationPane();

        VBox inputPane = new VBox(10);
        inputPane.getChildren().addAll(interestPane,
                yearsPane, loanAmountPane, loanAmountInWordsPane, buttonPane,
                monthlyPaymentPane, totalPaymentPane, amortizationPane
        );

        rootPane.setPadding(new Insets(20));
        rootPane.setTop(inputPane);

        tfLoanAmount.textProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                return;
            }

            try {
                lblLoanAmountInWords.setText(NumerUtil.getToWords(Double.parseDouble(newVal)));
            } catch (NumberFormatException e) {
                lblLoanAmountInWords.setText("Invalid number");
            }
        });

        btnCalculate.setOnAction(actionEvent -> {
            if (!Validator.isDouble(tfAnnalInterestRate, "Interest rate must be a number")
                    || !Validator.isDouble(tfNumberOfYears, "Years must be a number")
                    || !Validator.isDouble(tfLoanAmount, "Loan amount must be a number")
            ) {
                return;
            }

            double interestRate = Double.parseDouble(tfAnnalInterestRate.getText());
            double years = Double.parseDouble(tfNumberOfYears.getText());

            String loanAmount = tfLoanAmount.getText();
            loanAmount = loanAmount.replaceAll(NON_NUMBERS_REGEX, "");
            double principal = Double.parseDouble(loanAmount);

            currentLoan = new Loan(interestRate, principal, years * 12);
            lblMonthlyPaymentVal.setText(currency.getSymbol() + " " + numberFormatInstance.format(currentLoan.getMonthlyPayment()));
            lblTotalPaymentVal.setText(currency.getSymbol() + " " + numberFormatInstance.format(currentLoan.getTotalAmount()));

            taLoanSummary.setText(currentLoan.printAmortizationSchedule());
        });

        btnClear.setOnAction(e -> {
            tfAnnalInterestRate.setText("");
            tfNumberOfYears.setText("");
            tfLoanAmount.setText("");
            lblMonthlyPaymentVal.setText("NA");
            lblTotalPaymentVal.setText("NA");
            taLoanSummary.setText("Loan amortization detail will appear here");
        });

        btnExport.setOnAction(e -> {
            exportToCSV();
        });

        Scene scene = new Scene(rootPane, 600, 550);
        currentStage.setScene(scene);
        currentStage.setTitle("Loan Calculator");
        currentStage.show();
    }

    private void exportToCSV() {
        ExportUtil.exportAmortizationScheduleToCSV(currentLoan, "file.csv");
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
        taLoanSummary.setMinHeight(200);

        btnExport = new Button("Export");
        btnExport.setPrefWidth(BUTTON_WIDTH);


        VBox pane = new VBox(10, lbl, taLoanSummary, btnExport);
        return pane;
    }
}
