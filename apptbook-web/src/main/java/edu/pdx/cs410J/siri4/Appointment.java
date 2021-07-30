package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AbstractAppointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Appointment extends AbstractAppointment {
    /**
     * local variables Strings to assign the owner name, description, begin time and end time.
     */
    private String ownerName;
    private String description;
    private String beginTime;
    private String endTime;

    public Appointment() {
        super();
    }

    /**
     * When the constructor appointment is invoked the local variables are assigned with the values from appointment.
     * @param args
     */
    public Appointment(String[] args, int startPosition) {
        this.ownerName = args[startPosition+1];
        this.description = args[startPosition+2];
        this.beginTime = args[startPosition+3] + " " + args[startPosition+4]+ " "+ args[startPosition+5];
        this.endTime = args[startPosition+6] + " " + args[startPosition+7]+" "+args[startPosition+8];
        if(getBeginTime().after(getEndTime()) || getBeginTime().equals(getEndTime())) {
            System.err.println("The inputted end time is equal to or before the start time.");
            System.exit(1);
        }
    }

    /**
     * Appointment constructor with different args
     * @param args
     */
    public Appointment(String[] args) {
        this.ownerName = args[0];
        this.description = args[1];
        if(validateDateAndTime(args[2])) {
            this.beginTime = args[2];
        } else {
            this.beginTime = "";
            System.err.println("Invalid start time found");}
        if(validateDateAndTime(args[3]))
        { this.endTime = args[3]; }
        else {
            this.endTime = "";
            System.err.println("Invalid end time found");}
    }
    /**
     * Method to check if dates are valid
     * @return Boolean
     */
    public boolean checkIfDatesAreCorrect() {
        if(getEndTime()!=null && getBeginTime()!=null) {
            if (getBeginTime().after(getEndTime()) || getBeginTime().equals(getEndTime())) {
                return false;
            } else {
                return true;
            }
        } else {return false;}
    }

    /**
     * Validate date and time from String
     * @param arg
     * @return Boolean
     */
    private boolean validateDateAndTime(String arg) {
        if(arg == null) {
            return false;
        }
        else {
            String[] date = arg.split(" ");
            if (date.length < 3 || date.length >= 4) {
                System.err.println("Expected date in the format of mm/dd/yyyy hh:mm am/pm");
                return false;
            } else {
                ValidateArguments va = new ValidateArguments();
                if (va.checkIfDateIsValid(date[0]) && va.checkIfTimeIsValid(date[1]) && va.checkIfTimeFormatIsValid(date[2])) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     *
     * @return beginTime
     */
    @Override
    public String getBeginTimeString() {
        return beginTime;
    }

    /**
     *
     * @return endTime
     */
    @Override
    public String getEndTimeString() {
        return endTime;
    }

    /**
     *
     * @return description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return beginTime in date format
     */
    @Override
    public Date getBeginTime() {
        return parseDate(this.beginTime);
    }

    /**
     *
     * @return endTime in date format
     */
    @Override
    public Date getEndTime() {
        return parseDate(this.endTime);
    }

    /**
     * Calculates the appointment duration in minutes.
     * @return
     */
    public long duration() {
        long diff = Math.abs(getBeginTime().getTime() - getEndTime().getTime());
        return TimeUnit.MINUTES.convert(diff,TimeUnit.MILLISECONDS);
    }

    /**
     *
     * @return ownerName
     */
    public String getOwner() {
        return this.ownerName;
    }
    /**
     * This method parses the date to a Simple Date Format for pretty printing the output
     * @param date
     * @return
     */
    public Date parseDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            Date result = sdf.parse(date);
            return result;
        } catch(ParseException pex) {
            System.err.println("Invalid Date format");
            return null;
        }
    }
}