package edu.pdx.cs410J.siri4;
import java.io.IOException;

/**
 * Class to Pretty Print the Appointments on the Console.
 */
public class PrettyPrint  {
    /**
     * This method takes an Appointment Book as input and pretty prints all of the Appointments.
     * @param book
     * @return
     * @throws IOException
     */
    public String getPrettyAppointments(AppointmentBook book) throws IOException {
        String pretty = "";
        pretty = pretty + "Appointments for " + book.getOwnerName()+"\n";
        int count = 1;
        for (Appointment appointment : book.getAppointments()) {
            pretty = pretty+ "Appointment - "+count+"\n"+
            "---description:" + appointment.getDescription()+"\n"+
            "---duration:   "+  appointment.duration()+"\n"+
            "---BeginTime:  "+ appointment.getBeginTime()+"\n"+
            "---EndTime:    "+appointment.getEndTime()+"\n";
            count++;
        }
        return pretty;
    }
}
