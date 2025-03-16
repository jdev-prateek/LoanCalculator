package org.example.loancalculator.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SettingsViewController {
    private static final Logger log = LoggerFactory.getLogger(SettingsViewController.class);
    @FXML
    public SplitPane splitPane;

    public AnchorPane leftPane;

    @FXML
    private ListView<String> settingsList;

    @FXML
    private SplitPane settingsPane;

    @FXML
    private AnchorPane contentPane;

    private final GeneralSettingsViewController generalSettingsViewController;

    private Stage stage;
    private Stage primaryStage;

    public SettingsViewController(GeneralSettingsViewController generalSettingsViewController) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/loancalculator/settings-view.fxml"));
        loader.setController(this);
        try {
            splitPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load message box FXML", e);
        }

        this.generalSettingsViewController = generalSettingsViewController;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize(){

        settingsList.getItems().addAll("General");

        settingsList.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldVal, newVal) -> {
                    loadContent(newVal);
        });
    }

    public void show(){
        if(settingsList.getSelectionModel().getSelectedItem() == null){
            settingsList.getSelectionModel().select(0);
            loadContent("General");
        }

        if (stage == null) {
            stage = new Stage();
            Scene scene = new Scene(settingsPane, 650, 400);
            stage.setScene(scene);
            stage.initOwner(primaryStage);
            stage.setTitle("Settings");
            stage.setMinWidth(650);
//            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
        }

        stage.showAndWait();
    }

    public void loadContent(String selectMenuItem){
        Pane newContent;
        FXMLLoader fxmlLoader;
        try {
            newContent = switch (selectMenuItem) {
                case "General" -> {
                    fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/loancalculator/general-settings-view.fxml"));
                    fxmlLoader.setController(generalSettingsViewController);
                    yield fxmlLoader.load();
                }

                default -> {
                    fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/loancalculator/general-settings-view.fxml"));
                    yield fxmlLoader.load();
                }
            };

            contentPane.getChildren().clear();
            contentPane.getChildren().addAll(newContent);

        } catch (IOException e){
            log.error("{0}", e);
        }
    }
}
