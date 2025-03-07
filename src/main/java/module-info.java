module org.example.loancalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.security.jgss;


    opens org.example.loancalculator to javafx.fxml;
    exports org.example.loancalculator;
    exports org.example.loancalculator.utils;
    opens org.example.loancalculator.utils to javafx.fxml;
}