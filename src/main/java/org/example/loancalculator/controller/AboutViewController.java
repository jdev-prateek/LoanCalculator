package org.example.loancalculator.controller;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.loancalculator.utils.AppConstants;
import org.example.loancalculator.utils.AppState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AboutViewController {
    private static final Logger log = LoggerFactory.getLogger(AboutViewController.class);
    public VBox aboutBox;
    public Label lblDescription;
    public Button btnOk;
    public Hyperlink githubLink;

    private Stage primaryStage;

    private Stage stage;

    public AboutViewController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/loancalculator/about-view.fxml"));
        loader.setController(this);
        try {
            aboutBox = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load message box FXML", e);
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show(String message, String title) {
        lblDescription.setText(message);

        if (stage == null) {
            githubLink.setText(AppConstants.GITHUB_LINK);
            stage = new Stage();
            Scene scene = new Scene(aboutBox);
            stage.setScene(scene);
            stage.initOwner(primaryStage);
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
        }

        stage.showAndWait();
    }

    public void btnClose(ActionEvent actionEvent) {
        log.info("Closing about window");
        stage.close();
    }

    public void onGithubLinkClick(ActionEvent actionEvent) {
        log.info("Opening GITHUB_LINK link");
        HostServices hostServices = AppState.getHostServices();
        hostServices.showDocument(githubLink.getText());
    }
}
