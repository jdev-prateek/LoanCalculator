package org.example.loancalculator;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoanCalculator extends Application {
    private TextField tfAnnalInterestRate = new TextField();
    private TextField tfNumberOfYears = new TextField();
    private TextField tfLoanAmount = new TextField();
    private TextField tfMonthlyPayment = new TextField();
    private TextField tfTotalPayment = new TextField();
    private Button btCalculate = new Button("Calculate");

    @Override
    public void start(Stage stage) throws Exception {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        gridPane.add(new Label("Annual Interest Rate"), 0, 0);
        gridPane.add(tfAnnalInterestRate, 1, 0);
        gridPane.add(new Label("Number of years:"), 0, 1);
        gridPane.add(tfNumberOfYears, 1, 1);
        gridPane.add(new Label("Loan Amount:"), 0, 2);
        gridPane.add(tfLoanAmount, 1, 2);
        gridPane.add(new Label("Monthly Payment:"), 0, 3);
        gridPane.add(tfMonthlyPayment, 1, 3);
        gridPane.add(new Label("Total Payment:"), 0, 4);
        gridPane.add(tfTotalPayment, 1, 4);
        gridPane.add(btCalculate, 1, 5);

        // ui properties
        gridPane.setAlignment(Pos.CENTER);
        GridPane.setHalignment(btCalculate, HPos.LEFT);
        tfTotalPayment.setEditable(false);

        btCalculate.setOnAction(actionEvent -> {
            System.out.println(tfAnnalInterestRate.getText());
            double interestRate = Double.parseDouble(tfAnnalInterestRate.getText());
            double years = Double.parseDouble(tfNumberOfYears.getText());
            double principal = Double.parseDouble(tfLoanAmount.getText());

            Loan loan = new Loan(interestRate, principal, years * 12);
            tfMonthlyPayment.setText(String.format("%.2f", loan.getMonthlyPayment()));
            tfTotalPayment.setText(String.format("%.2f", loan.getTotalAmount()));
        });

        Scene scene = new Scene(gridPane, 400, 250);
        stage.setScene(scene);
        stage.setTitle("Loan Calculator");
        stage.show();
    }
}
