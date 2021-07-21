package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class PrettyPrint implements AppointmentBookDumper {
    private String fileName = null;

    /**
     * Constructor PrettyPrint with a file name.
     * @param fileName
     * @throws IllegalArgumentException
     */
    public PrettyPrint(String fileName) throws IllegalArgumentException {
        if(fileName.trim().isEmpty() || fileName == null) {
            throw new IllegalArgumentException("Expected a file name, but found null");
        }
        this.fileName = fileName;
    }

    /**
     * Dumps the Appointment information in a pretty format to an inputted textfile.
     * @param abstractAppointmentBook
     * @throws IOException
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file);
            Collection<Appointment> appointments = abstractAppointmentBook.getAppointments();
            fw.append("Owner:"+abstractAppointmentBook.getOwnerName()+"\n");
            fw.append(abstractAppointmentBook.toString()+"\n");
            int count = 1;
            for (Appointment appointment : appointments) {
                fw.append("Appointment: "+count+"\n");
                fw.append("---Description:"+appointment.getDescription()+"\n");
                fw.append("---Begin Date: "+appointment.getBeginTime()+"\n");
                fw.append("---End Date:   "+appointment.getEndTime()+"\n");
                fw.append("---Duration:   "+appointment.duration()+" minutes\n");
                count++;
            }
            fw.flush();
            fw.close();
            System.out.println("Successfully dumped all appointments in pretty format to the chosen file");
        } catch (IOException io) {
            System.err.println("No valid directory found for creating the file\nExiting...");
            System.exit(1);
        }
    }
}

