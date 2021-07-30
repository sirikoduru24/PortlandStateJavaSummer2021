package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Class to dump the Contents to a PrintWriter
 */
public class TextDumper implements AppointmentBookDumper<AppointmentBook> {
    private final Writer writer;
    public TextDumper(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void dump(AppointmentBook appointmentBook) throws IOException {

        PrintWriter pw = new PrintWriter(this.writer);
        pw.println("Appointments for " + appointmentBook.getOwnerName());
        for (Appointment appointment : appointmentBook.getAppointments()) {
            pw.println(appointment.getDescription()+","+appointment.getBeginTimeString()+","+appointment.getEndTimeString());
        }
        pw.flush();
    }
}