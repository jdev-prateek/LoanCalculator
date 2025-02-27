package org.example.loancalculator;

class Loan{
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
}