package org.example.loancalculator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

public class ExportUtil {
    static Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    public static void exportAmortizationScheduleToCSV(Loan loan, String filename) {
        LOGGER.info("Amortization schedule CSV export started");

        double monthlyRate = (loan.getInterestRate() / 100) / 12;
        int totalPayments = (int) loan.getMonths();

        double monthlyPayment = loan.getMonthlyPayment();
        double totalAmount = loan.getTotalAmount();

        try (FileWriter writer = new FileWriter(filename)) {
            // Write headers
            writer.append("Loan Amount,\"").append(NumberFormat.getCurrencyInstance().format(loan.getPrincipal())).append("\"\n");
            writer.append("Annual Interest Rate,\"").append(String.valueOf(loan.getInterestRate())).append("%").append("\"\n");
            writer.append("Term,").append(String.valueOf((loan.getMonths() / 12))).append(" years").append("\n");
            writer.append("Monthly Payment,\"").append(NumberFormat.getCurrencyInstance().format(monthlyPayment)).append("\"\n");
            writer.append("Total Payment,\"").append(NumberFormat.getCurrencyInstance().format(totalAmount)).append("\"\n\n");

            // Write column headers
            writer.append("Month,Payment,Interest,Remaining Balance\n");

            double balance = loan.getPrincipal();
            for (int month = 1; month <= totalPayments; month++) {
                double interestPayment = balance * monthlyRate;
                double principalPayment = monthlyPayment - interestPayment;
                balance -= principalPayment;


                // Write row data
                writer.append(String.valueOf(month)).append(",")
                        .append("\"").append(NumberFormat.getCurrencyInstance().format(monthlyPayment)).append("\",")
                        .append("\"").append(NumberFormat.getCurrencyInstance().format(interestPayment)).append("\",")
                        .append("\"").append(NumberFormat.getCurrencyInstance().format(balance > 0 ? balance : 0))
                        .append("\"\n");
            }

            LOGGER.info("Amortization schedule CSV export completed");
        } catch (IOException e) {
            LOGGER.error("Error generating cs file {}", e.getMessage());
        }
    }

}
