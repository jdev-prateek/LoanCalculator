module org.example.loancalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.security.jgss;
    requires org.slf4j;
    requires java.desktop;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires spring.context;
    requires spring.core;
    requires spring.beans;


    opens org.example.loancalculator;
    exports org.example.loancalculator;
    exports org.example.loancalculator.controller;
    exports org.example.loancalculator.utils;
    opens org.example.loancalculator.utils to javafx.fxml;
    opens org.example.loancalculator.controller ;
    exports org.example.loancalculator.appender;
    opens org.example.loancalculator.appender to javafx.fxml;
    opens org.example.loancalculator.config;
}