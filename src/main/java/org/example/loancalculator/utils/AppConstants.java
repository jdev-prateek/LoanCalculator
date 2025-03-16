package org.example.loancalculator.utils;

public class AppConstants {
    public static final String ABOUT = """
            Loan Calculator helps you estimate monthly payments,
            interest, and total repayment for various loans. It features
            an amortization schedule and CSV export for easy tracking.
            
            License: This software is open-source under the MIT License.
            Feel free to use, modify, and distribute it.
            """;

    public static final String GITHUB_ISSUE_URL = "https://github.com/jdev-prateek/LoanCalculator/issues";
    public static final String GITHUB_LINK = "https://github.com/jdev-prateek/LoanCalculator";
    public static final String CONFIG_DIR = ".configB";
    public static final String SETTINGS_PROPERTIES = "settings.properties";
    public static final String SOURCE_SETTINGS_PROPERTIES = "/org/example/loancalculator/settings.properties";

    public static class ExportStatus {
        public static final String EXPORT_STARTED = "EXPORT_STARTED";
        public static final String EXPORT_IN_PROGRESS = "EXPORT_IN_PROGRESS";
        public static final String EXPORT_COMPLETE = "EXPORT_COMPLETE";
        public static final String EXPORT_INTERRUPTED = "EXPORT_INTERRUPTED";
    }

    public static class Settings {
        public static final String EXPORT_DIR = "EXPORT_DIR";
    }

}
