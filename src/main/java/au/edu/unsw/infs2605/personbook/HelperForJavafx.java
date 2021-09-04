/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.unsw.infs2605.personbook;

import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author blair
 */
public class HelperForJavafx {
    
    // TIP-03: Button icons
    public static void setupIconForButton(ButtonBase btn, String iconName) {
        Image img = new Image(App.class.getResourceAsStream(iconName));
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(16);
        imgView.setPreserveRatio(true);
        btn.setGraphic(imgView);
    }
    
    // TIP-04: Disabled but editable
    public static void setTextboxEditable(TextInputControl textInputControl, boolean isEditable) {
        textInputControl.setEditable(isEditable);
        
        if (isEditable) {
            textInputControl.setStyle("-fx-control-inner-background: white;");
        } else {
            textInputControl.setStyle("-fx-control-inner-background: #f3f3f3;");
        }
    }
    public static void setTextboxesEditable(TextField[] textFields, boolean isEditable) {
        for (TextField textField : textFields) {
            setTextboxEditable(textField, isEditable);
        }
    }
    
    public static void alertDataEntryError(String errorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Data entry error");
        alert.setHeaderText("Data entry error");
        alert.setContentText(errorText);
        alert.show();
    }
    
    public static void setNodesDisabled(Node[] nodes, boolean isDisabled) {
        for (Node node : nodes) {
            node.setDisable(isDisabled);
        }
    }
    
    // TIP-01: This is needed to both set the item visible and reorganise layouts.
    // https://stackoverflow.com/questions/28558165/javafx-setvisible-hides-the-element-but-doesnt-rearrange-adjacent-nodes
    public static void setNodeHidden(Node node, boolean isHidden) {
        node.setVisible(!isHidden);
        node.setManaged(!isHidden);
    }
    public static void setNodesHidden(Node[] nodes, boolean isHidden) {
        for (Node node : nodes) {
            setNodeHidden(node, isHidden);
        }
    }
    
    // TIP-02: do not allow empty full name!
    // fancy blankness checker from https://stackoverflow.com/questions/3247067/how-do-i-check-that-a-java-string-is-not-all-whitespaces
    public static void disableButtonIfTextIsBlank(Button button, String text) {
        if (!text.trim().isEmpty()) {
            button.setDisable(false);
        } else {
            button.setDisable(true);
        }
    }
}
