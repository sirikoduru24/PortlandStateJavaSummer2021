package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

public class AppointmentBook extends AbstractAppointmentBook {

    private String ownerName;
    private ArrayList<Appointment> appt = new ArrayList<>();

    public AppointmentBook() {
        super();
    }

    public AppointmentBook(Appointment appointment, String name) {
        this.ownerName = name;
        addAppointment(appointment);
    }

    @Override
    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public Collection getAppointments() {
        System.out.println(appt);
        return appt;
    }

    @Override
    public void addAppointment(AbstractAppointment abstractAppointment) {
        appt.add((Appointment) abstractAppointment);
    }
}
