package org.example.loancalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.loancalculator.config.AppConfig;
import org.example.loancalculator.controller.LoanViewController;
import org.example.loancalculator.utils.AppConstants;
import org.example.loancalculator.utils.AppState;
import org.example.loancalculator.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static java.nio.file.Files.exists;


public class MainApp extends Application {
    private static final Logger log = LoggerFactory.getLogger(MainApp.class);
    private AnnotationConfigApplicationContext context;

    @Override
    public void init() throws Exception {
        setUpConfigDir();
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("loan-view.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        AnchorPane anchorPane = fxmlLoader.load();
        LoanViewController loanViewController = fxmlLoader.getController();
        loanViewController.setPrimaryStage(stage);

        AppState.setHostServices(getHostServices());

        Scene scene = new Scene(anchorPane, 620, 440);
        stage.setTitle("Loan Calculator");
        stage.setScene(scene);
        stage.show();
    }

    private void setUpConfigDir() {
        try{
            String homeDir = System.getProperty("user.home");
            InputStream sourceStream = getClass().getResourceAsStream(AppConstants.SOURCE_SETTINGS_PROPERTIES);
            Path targetPath = Paths.get(homeDir, AppConstants.CONFIG_DIR, AppConstants.SETTINGS_PROPERTIES);

            if(!exists(targetPath)){
                Files.createDirectory(Paths.get(homeDir, AppConstants.CONFIG_DIR));
                Files.copy(sourceStream, targetPath);

                PropertiesUtil.load();
                Map<String, String> settings = AppState.getSettings();
                settings.put(AppConstants.Settings.EXPORT_DIR, System.getProperty("user.home"));
                PropertiesUtil.dump();

                log.info("Config dir setup complete");
            } else {
                PropertiesUtil.load();
                log.info("Config dir already exists");
            }

        } catch (IOException e){
            log.error("Failed to setup config dir ", e);
        }
    }


    public static void main(String[] args) {
        launch();
    }
}