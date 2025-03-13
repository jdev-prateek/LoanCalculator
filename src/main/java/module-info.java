module org.example.loancalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.security.jgss;
    requires org.slf4j;
    requires java.desktop;


    opens org.example.loancalculator to javafx.fxml;
    exports org.example.loancalculator;
    exports org.example.loancalculator.controller;
    exports org.example.loancalculator.utils;
    opens org.example.loancalculator.utils to javafx.fxml;
    opens org.example.loancalculator.controller to javafx.fxml;
}