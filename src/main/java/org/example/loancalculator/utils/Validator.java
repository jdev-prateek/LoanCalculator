package org.example.loancalculator.utils;

import javafx.scene.control.TextField;
import org.example.loancalculator.ui.MessageBox;


public class Validator {

    public static boolean isInt(TextField textField, String message){
        try{
            Integer.parseInt(textField.getText());
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
            return true;
        }
        catch (NumberFormatException e){
            MessageBox.show(message, "Validation Error");
            textField.requestFocus();
            return false;
        }
    }

}
