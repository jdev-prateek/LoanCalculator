package org.example.loancalculator;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.loancalculator.controller.LoanViewController;
import org.example.loancalculator.utils.AppState;
import org.example.loancalculator.utils.PropertiesUtil;

import java.io.IOException;
import java.util.Map;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        Map<String, String> mapping = propertiesUtil.load();
        AppState.setSettings(mapping);

        System.out.println(mapping);

        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("loan-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 440);
        LoanViewController hc = fxmlLoader.getController();
        hc.setPrimaryStage(primaryStage);
        System.out.println(hc);

        AppState.setHostServices(getHostServices());

        stage.setTitle("Loan Calculator");
        stage.setScene(scene);
        stage.show();

        HostServices hostServices = getHostServices();
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}