package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AbstractAppointment;

public class Appointment extends AbstractAppointment {
  private String ownerName;
  private String description;
  private String beginTime;
  private String endTime;

  public Appointment() {
    super();
    System.err.println("Invoked with no arguments");
  }

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
