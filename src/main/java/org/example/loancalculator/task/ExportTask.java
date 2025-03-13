package org.example.loancalculator.task;

import javafx.concurrent.Task;
import org.example.loancalculator.utils.AppConstants;
import org.example.loancalculator.utils.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

public class ExportTask extends Task<Double> {
    Logger LOGGER = LoggerFactory.getLogger(ExportTask.class);
    private final Loan loan;
    private final String filename;

    public ExportTask(Loan loan, String filename) {
        this.loan = loan;
        this.filename = filename;
    }

    @Override
    protected Double call() {
        updateMessage(AppConstants.ExportStatus.EXPORT_IN_PROGRESS);
        LOGGER.info("Amortization schedule CSV export started");

        double monthlyRate = (loan.getInterestRate() / 100) / 12;
        int totalPayments = (int) loan.getMonths();

        double monthlyPayment = loan.getMonthlyPayment();
        double totalAmount = loan.getTotalAmount();

        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("Loan Amount,\"").append(NumberFormat.getCurrencyInstance().format(loan.getPrincipal())).append("\"\n");
            writer.append("Annual Interest Rate,\"").append(String.valueOf(loan.getInterestRate())).append("%").append("\"\n");
            writer.append("Term,").append(String.valueOf((loan.getMonths() / 12))).append(" years").append("\n");
            writer.append("Monthly Payment,\"").append(NumberFormat.getCurrencyInstance().format(monthlyPayment)).append("\"\n");
            writer.append("Total Payment,\"").append(NumberFormat.getCurrencyInstance().format(totalAmount)).append("\"\n\n");

            // col headers
            writer.append("Month,Payment,Interest,Remaining Balance\n");

            double balance = loan.getPrincipal();
            for (int month = 1; month <= totalPayments; month++) {
//                try {
//                    Thread.sleep(1_00);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }

                if(isCancelled()){
                    LOGGER.info("Export task interrupted");
                    updateMessage(AppConstants.ExportStatus.EXPORT_INTERRUPTED);
                    break;
                }

                double interestPayment = balance * monthlyRate;
                double principalPayment = monthlyPayment - interestPayment;
                balance -= principalPayment;

                // write row
                writer.append(String.valueOf(month)).append(",")
                        .append("\"").append(NumberFormat.getCurrencyInstance().format(monthlyPayment)).append("\",")
                        .append("\"").append(NumberFormat.getCurrencyInstance().format(interestPayment)).append("\",")
                        .append("\"").append(NumberFormat.getCurrencyInstance().format(balance > 0 ? balance : 0))
                        .append("\"\n");

                updateProgress(month, totalPayments);
            }

            LOGGER.info("Amortization schedule CSV export completed");
        } catch (IOException e) {
            LOGGER.error("Error generating csv file {}", e.getMessage());
        } finally {
            if(!isCancelled()){
                updateMessage(AppConstants.ExportStatus.EXPORT_COMPLETE);
            }
        }

        return null;
    }
}
