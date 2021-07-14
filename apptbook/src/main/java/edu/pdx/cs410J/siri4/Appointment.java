package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AbstractAppointment;

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
    this.beginTime = args[startPosition+3] + "," + args[startPosition+4];
    this.endTime = args[startPosition+5] + "," + args[startPosition+6];
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
}
