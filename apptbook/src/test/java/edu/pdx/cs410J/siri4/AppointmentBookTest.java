package edu.pdx.cs410J.siri4;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppointmentBookTest {

    @Test
    public void getOwnerNameReturnsName() {
        AppointmentBook apptBook = new AppointmentBook(new Appointment(new String[]{"-print","siri","have appointment with Lisa","7/15/2021" ,"12:30", "7/15/2021" ,"14:30"},0),"siri");
        assertThat(apptBook.getOwnerName(), containsString("siri"));
    }
}
