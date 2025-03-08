package org.example.loancalculator.utils;

public class AppConstants {
    public static final String ABOUT = """
Loan Calculator helps you estimate monthly payments,
interest, and total repayment for various loans. It features
an amortization schedule and CSV export for easy tracking.

License: This software is open-source under the MIT License.
Feel free to use, modify, and distribute it.

Github: https://github.com/jdev-prateek/LoanCalculator
""";

    public static  class ExportStatus{
        public static final String EXPORT_STARTED = "EXPORT_STARTED";
        public static final String EXPORT_IN_PROGRESS = "EXPORT_IN_PROGRESS";
        public static final String EXPORT_COMPLETE = "EXPORT_COMPLETE";
        public static final String EXPORT_INTERRUPTED = "EXPORT_INTERRUPTED";
    }
}
