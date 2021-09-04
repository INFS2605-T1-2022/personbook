/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.unsw.infs2605.personbook;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 *
 * @author blair
 */
public class HelperForPersonGUI {
    public static void populatePersonDetails(
        Person person,
        TextField txtFullName,
        TextField txtBdayDay, TextField txtBdayMonth, TextField txtBdayYear,
        CheckBox chkPersonal, CheckBox chkBusiness
    ) {
         // set name if available
        // TIP: always do null != <value to check> rather than the other way around!
        if (null != person.getFullName()) {
            txtFullName.setText(person.getFullName());
        } else {
            txtFullName.setText("");
        }
        
        // set birthday MonthDay if available.
        if (null != person.getBdayMonthDay()) {
            // TIP: to quickly convert an int to a string without too much fuss, concatenate with an empty string
            txtBdayDay.setText("" + person.getBdayMonthDay().getDayOfMonth());
            txtBdayMonth.setText("" + person.getBdayMonthDay().getMonthValue());
        } else {
            txtBdayDay.setText("");
            txtBdayMonth.setText("");
        }
        
        
        // set birthday year if available.
        if (null != person.getBirthdayYear()) {
            txtBdayYear.setText("" + person.getBirthdayYear());
        } else {
            txtBdayYear.setText("");
        }
        
        // personal and business checkboxes
        chkPersonal.setSelected(person.isImportantPersonal());
        chkBusiness.setSelected(person.isImportantBusiness());
    }
    
    public static boolean updatePersonDetails(
        Person person,
        TextField txtFullName,
        TextField txtBdayDay, TextField txtBdayMonth, TextField txtBdayYear,
        CheckBox chkPersonal, CheckBox chkBusiness
    ) {
        boolean successfulSoFar = true;
        
        // try set birthday; if bad birthday is set, prevent further progress
        String monthString = txtBdayMonth.getText();
        String dayString = txtBdayDay.getText();
        if (!monthString.trim().isEmpty() || !dayString.trim().isEmpty()) {
            successfulSoFar = HelperForData.trySetPersonBdayMonthDay(person, monthString, dayString);
        }
        
        // try set birth year; if bad birth year is set, prevent further progress
        String yearString = txtBdayYear.getText();
        if (!yearString.trim().isEmpty()) {
            successfulSoFar = HelperForData.trySetPersonBdayYear(person, yearString);
        }
        
        // save
        if (successfulSoFar) {    
            person.setFullName(txtFullName.getText());
        
            person.setImportantPersonal(chkPersonal.isSelected());
            person.setImportantBusiness(chkBusiness.isSelected());
            
            if (person.isNewContactNotYetSaved()) {
                person.setNewContactNotYetSaved(false);
            }
        }
        
        return successfulSoFar;
    }
}
