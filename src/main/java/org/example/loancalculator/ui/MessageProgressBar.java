package org.example.loancalculator.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.loancalculator.utils.AppConstants;
import org.example.loancalculator.utils.ExportTask;

public class MessageProgressBar {
    static Stage primaryStage;
    static ExportTask task;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void setTask(ExportTask tsk) {
        task = tsk;
    }

    public static void show(String message, String title) {
        if(task == null){
            throw new IllegalArgumentException("task is null");
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(250);

        if (primaryStage != null) {
            stage.initOwner(primaryStage);
        }

        Label label = new Label();
        label.setText(message);
        label.textProperty().bind(task.messageProperty());

        ProgressBar progressBar = new ProgressBar(0);

        progressBar.progressProperty().bind(task.progressProperty());

        Button btnOK = new Button("OK");
        btnOK.setPrefWidth(100);

        task.messageProperty().addListener((observableValue, oldVal, newVal) -> {
            btnOK.setDisable(newVal.equals(AppConstants.ExportStatus.EXPORT_IN_PROGRESS));
        });

        btnOK.setOnAction(e -> {
            stage.close();
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setPrefWidth(100);

        task.messageProperty().addListener((observableValue, oldVal, newVal) -> {
            btnCancel.setDisable(newVal.equals(AppConstants.ExportStatus.EXPORT_COMPLETE));
        });

        btnCancel.setOnAction(e -> {
            task.cancel();
            stage.close();
        });

        HBox btnPane = new HBox(20, btnOK, btnCancel);
        btnPane.setAlignment(Pos.CENTER);

        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.getChildren().addAll(label, progressBar, btnPane);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
