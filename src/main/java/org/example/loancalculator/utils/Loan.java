package org.example.loancalculator.utils;

import java.text.NumberFormat;

public class Loan{
    private double interestRate;
    private double principal;
    private double months;

    public Loan(double interestRate, double principal, double months) {
        this.interestRate = interestRate;
        this.principal = principal;
        this.months = months;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getMonths() {
        return months;
    }

    public void setMonths(double months) {
        this.months = months;
    }

    public double getMonthlyPayment(){
        double rate = interestRate / 12 / 100;
        return principal * rate * Math.pow(1 + rate, months) /
                ( Math.pow(1 + rate, months) - 1 );
    }

    public double getTotalAmount(){
        return getMonthlyPayment() * months;
    }

    public String printAmortizationSchedule() {
        double monthlyRate = (interestRate / 100) / 12;
        int totalPayments = (int) months;
        StringBuilder out = new StringBuilder();

        // Calculate Monthly Payment (M)
        double monthlyPayment = getMonthlyPayment();
        double totalAmount = getTotalAmount();

        out.append("Loan Amount: ").append(NumberFormat.getCurrencyInstance().format(principal)).append("\n");
        out.append("Annual Interest Rate: ").append(interestRate).append("%\n");
        out.append("Term: ").append(months / 12).append(" years\n");
        out.append("Monthly Payment: ").append(NumberFormat.getCurrencyInstance().format(monthlyPayment)).append("\n");;
        out.append("Total Payment: ").append(NumberFormat.getCurrencyInstance().format(totalAmount)).append("\n");;
        out.append("\nAmortization Schedule: \n");
        out.append(String.format("%-10s %-15s %-15s %-15s%n", "Month", "Payment", "Interest", "Remaining Balance"));

        double balance = principal;
        for (int month = 1; month <= totalPayments; month++) {
            double interestPayment = balance * monthlyRate;
            double principalPayment = monthlyPayment - interestPayment;
            balance -= principalPayment;

            out.append(String.format("%-10d %-15s %-15s %-15s%n", month,
                    NumberFormat.getCurrencyInstance().format(monthlyPayment),
                    NumberFormat.getCurrencyInstance().format(interestPayment),
                    NumberFormat.getCurrencyInstance().format(balance > 0 ? balance : 0)));
        }

        return out.toString();
    }

}