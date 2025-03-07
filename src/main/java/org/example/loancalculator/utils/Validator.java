package org.example.loancalculator.utils;

import javafx.scene.control.TextField;
import org.example.loancalculator.ui.MessageBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Validator {
    static Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    public static boolean isInt(TextField textField, String message){
        try{
            Integer.parseInt(textField.getText());
            LOGGER.info("Parsed textfield {}", textField.getId());
            return true;
        }
        catch (NumberFormatException e){
            MessageBox.show(message, "Validation Error");
            textField.requestFocus();
            return false;
        }
    }

    public static boolean isDouble(TextField textField, String message){
        try{
            Double.parseDouble(textField.getText());
            LOGGER.info("Parsed text field value {}", textField.getText());
            return true;
        }
        catch (NumberFormatException e){
            LOGGER.error("Parsing failed for text field value {}", textField.getText());
            MessageBox.show(message, "Validation Error");
            textField.requestFocus();
            return false;
        }
    }

}
