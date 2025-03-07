package org.example.loancalculator.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

public class ExportUtil {
    public static void exportAmortizationScheduleToCSV(Loan loan, String filename) {
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

            System.out.println("Amortization schedule saved to: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
