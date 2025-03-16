package org.example.loancalculator.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageBoxController {
    @FXML
    private VBox messageBox;

    @FXML
    private Label lblDescription;

    @FXML
    private Button btnOk;

    private Stage primaryStage;

    private Stage stage;

    public MessageBoxController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/loancalculator/message-box-view.fxml"));
        loader.setController(this);
        try {
            messageBox = loader.load();
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
            stage = new Stage();
            Scene scene = new Scene(messageBox);
            stage.setScene(scene);
            stage.initOwner(primaryStage);
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
        }

        stage.showAndWait();
    }

    @FXML
    private void btnClose() {
        stage.close();
    }
}
