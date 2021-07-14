package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Appointment} class.
 *
 * You'll need to update these unit tests as you build out your program.
 */
public class AppointmentTest {

  @Test
  void getBeginTimeReturnsBeginTimeAlongWithDate() {
    Appointment appointment = new Appointment(new String[]{"-print","siri","have appointment with Lisa","7/15/2021" ,"12:30", "7/15/2021" ,"14:30"},0);
    assertThat(appointment.getBeginTimeString(), equalTo("7/15/2021,12:30"));
  }

  @Test
  void getEndTimeReturnsDateAlongWithTime() {
    Appointment appointment = new Appointment(new String[]{"-print","siri","have appointment with Lisa","7/15/2021" ,"12:30", "7/15/2021","14:30"},0);
    assertThat(appointment.getEndTimeString(), equalTo("7/15/2021,14:30"));
  }

  @Test
  void getDescriptionReturnsDescription() {
    Appointment appointment = new Appointment(new String[]{"-print","siri","have appointment with Lisa","7/15/2021" ,"12:30", "7/15/2021","14:30"},0);
    assertThat(appointment.getDescription(), equalTo("have appointment with Lisa"));
  }

}
