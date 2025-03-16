package org.example.loancalculator.utils;

import javafx.scene.control.TextField;
import org.example.loancalculator.controller.MessageBoxController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);
    private static MessageBoxController messageBoxController;

    public Validator(MessageBoxController messageBoxController) {
        Validator.messageBoxController = messageBoxController;
    }

    public static MessageBoxController getMessageBoxController() {
        return messageBoxController;
    }

    public static void setMessageBoxController(MessageBoxController messageBoxController) {
        Validator.messageBoxController = messageBoxController;
    }

    public static boolean isInt(TextField textField, String message) {
        try{
            Integer.parseInt(textField.getText());
            LOGGER.info("Parsed textfield {}", textField.getId());
            return true;
        }
        catch (NumberFormatException e){

            messageBoxController.show(message, "Validation Error");
            textField.requestFocus();
            return false;
        }
    }

    public static boolean isDouble(TextField textField, String message) {
        try{
            Double.parseDouble(textField.getText());
            LOGGER.info("Parsed text field value {}", textField.getText());
            return true;
        }
        catch (NumberFormatException e){
            LOGGER.error("Parsing failed for text field value {}", textField.getText());
            messageBoxController.show(message, "Validation Error");
            textField.requestFocus();
            return false;
        }
    }

}
