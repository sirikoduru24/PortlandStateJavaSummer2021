package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.*;

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

    public AppointmentBook(String name) {
        this.ownerName = name;
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
        return this.ownerName;
    }

    /**
     * This method sorts the Appointments.
     * @return appointment
     */
    @Override
    public Collection getAppointments() {
        Collections.sort(appt,CompareAppointments);
        return this.appt;
    }

    /**
     * This method adds the appointment to the ArrayList of appointments.
     * @param abstractAppointment
     */
    @Override
    public void addAppointment(AbstractAppointment abstractAppointment) {
        this.appt.add((Appointment) abstractAppointment);
    }

    /**
     * To sort Appointments
     */
    public static Comparator<Appointment> CompareAppointments = new Comparator<Appointment>() {
        /**
         * Compares the start date of 2 appointments, if they are same
         * Compares the end date of 2 appointments, if they are too same
         * Sorts by description.
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(Appointment o1, Appointment o2) {
            Date dates1 = o1.getBeginTime();
            Date dates2 = o1.getEndTime();
            Date datee1 = o2.getBeginTime();
            Date datee2 = o2.getEndTime();
            String desc1 = o1.getDescription();
            String desc2 = o2.getDescription();
            if(dates1.compareTo(datee1) == 0) {
                if(dates2.compareTo(datee2) == 0) {
                    return desc1.compareTo(desc2);
                } else {
                    return dates2.compareTo(datee2);
                }
            } else {
                return dates1.compareTo(datee1);
            }
        }
    };
}
