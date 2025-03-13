package org.example.loancalculator.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.example.loancalculator.utils.AppConstants;
import org.example.loancalculator.utils.AppState;
import org.example.loancalculator.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

public class GeneralSettingsViewController {
    private static final Logger log = LoggerFactory.getLogger(GeneralSettingsViewController.class);

    @FXML
    private TextField txtExportDir;

    @FXML
    private Button btnDirSelector;

    private Stage primaryStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize(){
        Map<String, String> settings = AppState.getSettings();
        txtExportDir.setText(settings.get(AppConstants.Settings.EXPORT_DIR));
    }

    public void onDirSelector(ActionEvent e) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(primaryStage);
        Map<String, String> settings = AppState.getSettings();
        settings.put(AppConstants.Settings.EXPORT_DIR, file.toString());

        PropertiesUtil propertiesUtil = new PropertiesUtil();
        propertiesUtil.dump();

        txtExportDir.setText(file.toString());
    }
}
