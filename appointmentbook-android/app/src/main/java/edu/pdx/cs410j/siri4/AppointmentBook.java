package edu.pdx.cs410j.siri4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import edu.pdx.cs410J.AbstractAppointmentBook;

public class AppointmentBook extends AbstractAppointmentBook <Appointment>{
    private String ownerName;
    private ArrayList appt = new ArrayList<Appointment>();
    public AppointmentBook() {
        super();
    }

    public AppointmentBook(String name) {
        this.ownerName = name;
    }
    public AppointmentBook(Appointment appointment, String name) {
        this.ownerName = name;
        addAppointment(appointment);
    }

    @Override
    public String getOwnerName() {
        return this.ownerName;
    }

    @Override
    public Collection<Appointment> getAppointments() {
        Collections.sort(appt,CompareAppointments);
        return this.appt;

    }

    @Override
    public void addAppointment(Appointment appointment) {
        this.appt.add(appointment);
    }

    public static Comparator<Appointment> CompareAppointments = new Comparator<Appointment>() {
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
