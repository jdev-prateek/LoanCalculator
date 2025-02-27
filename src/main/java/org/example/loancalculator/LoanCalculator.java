package org.example.loancalculator;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class LoanCalculator extends Application {
    private final TextField tfAnnalInterestRate = new TextField();
    private final TextField tfNumberOfYears = new TextField();
    private final TextField tfLoanAmount = new TextField();
    private final Label lblMonthlyPayment = new Label();
    private final Label lblTotalPayment = new Label();
    private final Button btCalculate = new Button("Calculate");

    Locale locale = Locale.getDefault();
    NumberFormat numberFormatInstance = NumberFormat.getInstance(locale);
    Currency currency = Currency.getInstance(locale);
    final String NON_NUMBERS_REGEX = "[^\\d.]";

    @Override
    public void start(Stage stage) throws Exception {
        numberFormatInstance.setMaximumFractionDigits(2);

        GridPane gridPane1 = new GridPane();
        gridPane1.setHgap(5);
        gridPane1.setVgap(5);

        gridPane1.add(new Label("Annual Interest Rate (%)"), 0, 0);
        gridPane1.add(tfAnnalInterestRate, 1, 0);
        gridPane1.add(new Label("Number of years:"), 0, 1);
        gridPane1.add(tfNumberOfYears, 1, 1);
        gridPane1.add(new Label("Loan Amount:"), 0, 2);
        gridPane1.add(tfLoanAmount, 1, 2);

        gridPane1.add(btCalculate, 1, 3);

        GridPane gridPane2 = new GridPane();
        gridPane2.setHgap(5);
        gridPane2.setVgap(5);

        gridPane2.add(new Label("Monthly Payment:"), 0, 0);
        gridPane2.add(lblMonthlyPayment, 1, 0);
        gridPane2.add(new Label("Total Payment:"), 0, 1);
        gridPane2.add(lblTotalPayment, 1, 1);

        VBox vBox = new VBox(10, gridPane1, gridPane2);
        vBox.setAlignment(Pos.CENTER);

        // ui properties
        gridPane1.setAlignment(Pos.CENTER);
        gridPane2.setAlignment(Pos.BOTTOM_CENTER);
        gridPane2.setVisible(false);

        tfLoanAmount.textProperty().addListener((observableValue, oldVal, newVal) -> {
            newVal = newVal.replaceAll(NON_NUMBERS_REGEX, "");
            tfLoanAmount.setText(currency.getSymbol() + " " + numberFormatInstance.format(Double.parseDouble(newVal)));
        });

        btCalculate.setOnAction(actionEvent -> {
            System.out.println(tfAnnalInterestRate.getText());
            double interestRate = Double.parseDouble(tfAnnalInterestRate.getText());
            double years = Double.parseDouble(tfNumberOfYears.getText());

            String loanAmount = tfLoanAmount.getText();
            loanAmount = loanAmount.replaceAll(NON_NUMBERS_REGEX, "");
            double principal = Double.parseDouble(loanAmount);

            Loan loan = new Loan(interestRate, principal, years * 12);
            lblMonthlyPayment.setText(currency.getSymbol() + " " + numberFormatInstance.format(loan.getMonthlyPayment()));
            lblTotalPayment.setText(currency.getSymbol() + " " + numberFormatInstance.format(loan.getTotalAmount()));
            gridPane2.setVisible(true);
        });

        Scene scene = new Scene(vBox, 400, 250);
        stage.setScene(scene);
        stage.setTitle("Loan Calculator");
        stage.show();
    }
}
