module org.example.loancalculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.loancalculator to javafx.fxml;
    exports org.example.loancalculator;
}