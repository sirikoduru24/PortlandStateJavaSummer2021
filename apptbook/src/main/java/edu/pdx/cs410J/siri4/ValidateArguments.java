package edu.pdx.cs410J.siri4;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

public class ValidateArguments {

    /**
     * Method to check if the arguments are valid by invoking their respective checkArgs methods.
     * @param name
     * @param description
     * @param date1
     * @param time1
     * @param date2
     * @param time2
     * @return Boolean
     */
    public boolean checkIfArgsAreValid(String name, String description, String date1, String time1, String format1, String date2 , String time2, String format2) {
        if(name.trim().isEmpty()) { System.err.println("Entered an invalid name."); }
        if(description.trim().isEmpty()) { System.err.println("Entered an invalid description"); }
        if(!checkIfDateIsValid(date1)) { System.err.println("Entered start date is invalid"); }
        if(!checkIfTimeIsValid(time1)) { System.err.println("Entered start time is invalid");}
        if(!checkIfDateIsValid(date2)) { System.err.println("Entered end date is invalid"); }
        if(!checkIfTimeIsValid(time2)) {System.err.println("Entered end time is invalid");}
        if(!checkIfTimeFormatIsValid(format1)) {System.err.println("Entered format for start time is invalid. Expected am/pm");}
        if(!checkIfTimeFormatIsValid(format2)) {System.err.println("Entered format for end time is invalid. Expected am/pm");}
        if(!name.trim().isEmpty() && !description.trim().isEmpty() && checkIfDateIsValid(date1) && checkIfDateIsValid(date2) &&
                checkIfTimeIsValid(time1) && checkIfTimeIsValid(time2) && checkIfTimeFormatIsValid(format1) && checkIfTimeFormatIsValid(format2)) {
            return true;
        } else { return false; }
    }
    /**
     * Method to check if the date is valid in the format mm/dd/yyyy.
     * @param date
     * @return Boolean
     */
    public static boolean checkIfDateIsValid(String date) {
        try {
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("M/d/uuuu")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * This method takes time as a parameter and validates if it is in the format hh:mm.
     * @param time
     * @return
     */
    public static boolean checkIfTimeIsValid(String time) {
        String regex = "^([0]?[1-9]|1[0-2]):[0-5][0-9]$";
        if(Pattern.matches(regex, time)) { return true; } else { return false; }
    }

    public static boolean checkIfTimeFormatIsValid(String format) {
        String regex = "^[AaPp][Mm]$";
        if(Pattern.matches(regex, format)) {return true;} else {return false;}
    }
}
