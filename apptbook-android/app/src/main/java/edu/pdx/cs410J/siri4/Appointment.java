package edu.pdx.cs410j.siri4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import edu.pdx.cs410J.AbstractAppointment;

public class Appointment extends AbstractAppointment {
    private String ownerName;
    private String description;
    private String beginTime;
    private String endTime;

    public Appointment(String ownerName, String description, String beginDate, String endDate) {
        this.ownerName = ownerName;
        this.description = description;
        this.beginTime = beginDate;
        this.endTime = endDate;
    }

    @Override
    public String getBeginTimeString() {
        return beginTime;
    }

    @Override
    public String getEndTimeString() {
        return endTime;
    }

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
