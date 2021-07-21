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
      System.err.println("Exiting program due to this error");
      System.err.println(Project3.USAGE_MSG);
      System.exit(1);
    }
  }

  public Appointment(String[] args) {
    this.ownerName = args[0];
    this.description = args[1];
    this.beginTime = args[2];
    this.endTime = args[3];
    if(getBeginTime().after(getEndTime()) || getBeginTime().equals(getEndTime())) {
      System.err.println("The inputted end time is equal to or before the start time.");
      System.err.println("Exiting program due to this error");
      System.err.println(Project3.USAGE_MSG);
      System.exit(1);
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
      System.exit(1);
    }
    return null;
  }
}
