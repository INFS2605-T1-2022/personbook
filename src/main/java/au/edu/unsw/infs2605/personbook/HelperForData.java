/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.unsw.infs2605.personbook;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author blair
 */
public class HelperForData {
    public static List<Person> generateSamplePersonRecords() {
        List<Person> people = new ArrayList<>();
        
        
        Person andrewBob = new Person("Andrew Bob");
        andrewBob.setBdayMonthDay(MonthDay.of(9, 25));
        
        // TIP-07: CaseNotes automatically capture the time
        CaseNote cn = new CaseNote();
        cn.setCaseNoteText("This case note was created at the time of program execution! :)");
        andrewBob.addCaseNote(cn);
        people.add(andrewBob);
        
        
        people.add(new Person("Charlie Dennis"));
        people.add(new Person("Evan Foucault"));
        people.add(new Person("Gary Habermas"));
        people.add(new Person("Ivan Jung"));
        people.add(new Person("Kevin Larry"));
        people.add(new Person("Michael Noland"));
        
        return people;
    }
    
    // TIP-06: Alerts
    public static boolean trySetPersonBdayMonthDay(Person person, String monthString, String dayString) {
        boolean success = false;
        try {
            MonthDay bdayMonthDay = HelperForData.attemptToEstablishBirthdayFromStrings(monthString, dayString);
            person.setBdayMonthDay(bdayMonthDay);
            success = true;
        } catch (NumberFormatException nfe) {
            HelperForJavafx.alertDataEntryError(
                "We were not able to figure out this person's birthday based on the entered Month and Day."
                + " Please confirm that the entered Month and Day are both numbers."
            );
            success = false;
        } catch (DateTimeException dte) {
            HelperForJavafx.alertDataEntryError(
                "We were not able to figure out this person's birthday based on the entered Month and Day."
                + " Please confirm that the entered Month and Day match with an actual calendar Month and Day."
            );
            success = false;
        }
        
        return success;
    }
    
    public static boolean trySetPersonBdayYear(Person person, String yearString) {
        boolean success = false;
        
        try {
            Year bdayYear = HelperForData.attemptToEstablishBirthYearFromString(yearString);
            person.setBirthdayYear(bdayYear);
            success = true;
        } catch (NumberFormatException nfe) {
            HelperForJavafx.alertDataEntryError(
                "We were not able to figure out this person's birth year based on the entered Year."
                + " Please confirm that the entered Year is a number."
            );
            success = false;
        } catch (DateTimeException dte) {
            HelperForJavafx.alertDataEntryError(
                "We were not able to figure out this person's birth year based on the entered Year."
                + " Please confirm that the entered Year matches with an actual calendar Year."
            );
            success = false;
        }
        
        return success;
    }
    
    // TIP-05: MonthDay validation, demonstrating Exception handling
    public static MonthDay attemptToEstablishBirthdayFromStrings(String monthString, String dayString) throws NumberFormatException, DateTimeException {
        int monthInt = Integer.valueOf(monthString);
        int dayInt = Integer.valueOf(dayString);
        
        MonthDay bdayMonthDay = MonthDay.of(monthInt, dayInt);
        return bdayMonthDay;
    }
    
    public static Year attemptToEstablishBirthYearFromString(String yearString) throws NumberFormatException, DateTimeException {
        int yearInt = Integer.valueOf(yearString);
        
        Year bdayYear = Year.of(yearInt);
        return bdayYear;
    }
    
    public static String formatLocalDateTime(LocalDateTime ldt) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        return ldt.format(dtf);
    }
}
