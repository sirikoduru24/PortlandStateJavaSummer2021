package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

public class AppointmentBook extends AbstractAppointmentBook {
    /**
     * String variable ownerName to store the name of owner of Appointment.
     * ArrayList Appointment to store all the appointments for the particular owner.
     */
    private String ownerName;
    private ArrayList<Appointment> appt = new ArrayList<>();

    /**
     * empty constructor AppointmentBook.
     */
    public AppointmentBook() {
        super();
    }

    /**
     * This method assigns the owner name to the variable ownerName and adds the appointment to ArrayList.
     * @param appointment
     * @param name
     */
    public AppointmentBook(Appointment appointment, String name) {
        this.ownerName = name;
        addAppointment(appointment);
    }

    /**
     * @return ownerName
     */
    @Override
    public String getOwnerName() {
        return ownerName;
    }

    /**
     *
     * @return appointment
     */
    @Override
    public Collection getAppointments() {
       // System.out.println(appt);
        return appt;
    }

    /**
     * This method adds the appointment to the ArrayList of appointments.
     * @param abstractAppointment
     */
    @Override
    public void addAppointment(AbstractAppointment abstractAppointment) {
        appt.add((Appointment) abstractAppointment);
    }
}
