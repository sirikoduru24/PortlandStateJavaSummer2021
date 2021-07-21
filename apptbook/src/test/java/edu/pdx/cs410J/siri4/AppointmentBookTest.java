package edu.pdx.cs410J.siri4;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppointmentBookTest {

    @Test
    public void getOwnerNameReturnsName() {
        AppointmentBook apptBook = new AppointmentBook(new Appointment(new String[]{"-print","siri","have appointment with Lisa","7/15/2021" ,"2:30","am","7/15/2021" ,"4:30","am"},0),"siri");
        assertThat(apptBook.getOwnerName(), containsString("siri"));
    }
}
