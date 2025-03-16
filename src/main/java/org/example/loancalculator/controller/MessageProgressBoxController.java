package org.example.loancalculator.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.loancalculator.task.ExportTask;
import org.example.loancalculator.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageProgressBoxController {
    private static final Logger log = LoggerFactory.getLogger(MessageProgressBoxController.class);

    @FXML
    private VBox messageProgressBox;

    @FXML
    private Label lblDescription;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button btnOk;

    @FXML
    private Button btnCancel;

    private Stage primaryStage;
    private Stage stage;
    private ExportTask exportTask;

    public MessageProgressBoxController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/loancalculator/message-progress-box-view.fxml"));
        loader.setController(this);
        try {
            messageProgressBox = loader.load();
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

    public ExportTask getExportTask() {
        return exportTask;
    }

    public void setExportTask(ExportTask exportTask) {
        this.exportTask = exportTask;
    }

    @FXML
    private void initialize() {
    }

    public void show(String message, String title) {

        if(exportTask == null){
            throw new IllegalArgumentException("exportTask can't ben null");
        }

        lblDescription.textProperty().bind(exportTask.messageProperty());
        progressBar.progressProperty().bind(exportTask.progressProperty());

        exportTask.messageProperty().addListener((observableValue, oldVal, newVal) -> {
            btnOk.setDisable(newVal.equals(AppConstants.ExportStatus.EXPORT_IN_PROGRESS));
        });

        exportTask.messageProperty().addListener((observableValue, oldVal, newVal) -> {
            btnCancel.setDisable(newVal.equals(AppConstants.ExportStatus.EXPORT_COMPLETE));
        });

        if (stage == null) {
            stage = new Stage();
            Scene scene = new Scene(messageProgressBox);
            stage.setScene(scene);
            stage.initOwner(primaryStage);
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);

        }

        stage.show();
    }

    public void onBtnOK(ActionEvent actionEvent) {
        stage.close();
        log.info("Export window closed");
    }

    public void onBtnCancel(ActionEvent actionEvent) {
        exportTask.cancel();
        stage.close();
        log.info("Export task cancelled");
    }
}
