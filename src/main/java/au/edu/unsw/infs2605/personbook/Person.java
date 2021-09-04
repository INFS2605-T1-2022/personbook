/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.unsw.infs2605.personbook;

import java.time.MonthDay;
import java.time.Year;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author blair
 */
public class Person {
    
    private String fullName;
    private boolean importantPersonal = false;
    private boolean importantBusiness = false;
    private boolean newContactNotYetSaved = false;
    private MonthDay bdayMonthDay;
    private Year birthdayYear;
    
    // TIP: each Person starts with an empty list of case notes, rather than a null for the entire case notes property!
    private ObservableList<CaseNote> caseNotes = FXCollections.observableArrayList();

    @Override
    public String toString() {
        if (this.newContactNotYetSaved) {
            return "(new contact)";
        } else {
            return fullName;
        }
    }
    
    public Person() {
        this.newContactNotYetSaved = true;
    }
    
    public Person(String fullName) {
        this.fullName = fullName;
    }

    public void addCaseNote(CaseNote cn) {
        this.caseNotes.add(cn);
    }
    
    public void sortCaseNotes() {
        // TIP: sort reversed hence the -1, because we want newest on top
        this.caseNotes.sort((c1, c2) -> -1 * c1.getCreateTime().compareTo(c2.getCreateTime()));
    }
    
    
    // ALL THE GETTERS
    
    public String getFullName() {
        return fullName;
    }

    public boolean isImportantPersonal() {
        return importantPersonal;
    }

    public boolean isImportantBusiness() {
        return importantBusiness;
    }

    public boolean isNewContactNotYetSaved() {
        return newContactNotYetSaved;
    }

    public MonthDay getBdayMonthDay() {
        return bdayMonthDay;
    }

    public Year getBirthdayYear() {
        return birthdayYear;
    }

    public ObservableList<CaseNote> getCaseNotes() {
        return caseNotes;
    }
    
    // ALL THE SETTERS

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setImportantPersonal(boolean importantPersonal) {
        this.importantPersonal = importantPersonal;
    }

    public void setImportantBusiness(boolean importantBusiness) {
        this.importantBusiness = importantBusiness;
    }

    public void setNewContactNotYetSaved(boolean newContactNotYetSaved) {
        this.newContactNotYetSaved = newContactNotYetSaved;
    }

    public void setBdayMonthDay(MonthDay bdayMonthDay) {
        this.bdayMonthDay = bdayMonthDay;
    }

    public void setBirthdayYear(Year birthdayYear) {
        this.birthdayYear = birthdayYear;
    }
    
    
}