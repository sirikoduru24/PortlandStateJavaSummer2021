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
  public Appointment(String[] args) {
    this.ownerName = args[1];
    this.description = args[2];
    this.beginTime = args[3] + " " + args[4];
    this.endTime = args[5] + " " + args[6];
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
