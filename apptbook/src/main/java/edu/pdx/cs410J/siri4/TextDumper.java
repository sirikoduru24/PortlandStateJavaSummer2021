package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Class TextDumper which facilitates methods to dump contents of an AppointmentBook to textFile
 */
public class TextDumper implements AppointmentBookDumper {
    private String fileName = null;

    /**
     * Constructor TextFile with a file name.
     * @param fileName
     * @throws IllegalArgumentException
     */
    public TextDumper(String fileName) throws IllegalArgumentException {
        if(fileName.trim().isEmpty() || fileName == null) {
            throw new IllegalArgumentException("Expected a file name, but found null");
        }
        this.fileName = fileName;
    }

    /**
     * Method dump writes the contents of the abstractAppointmentBook to a file provided
     * @param abstractAppointmentBook
     * @throws IOException
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file);
            Collection<Appointment> appointments =  abstractAppointmentBook.getAppointments();
            //fw.append(abstractAppointmentBook.toString()+"\n");
            for (Appointment appointment:appointments) {
                fw.append(abstractAppointmentBook.getOwnerName()+","+appointment.getDescription()+","+
                        appointment.getBeginTimeString()+","+appointment.getEndTimeString()+"\n");
            }
            fw.flush();
            fw.close();

         } catch (IOException io) {
            System.err.println("No valid directory found for creating the file\nExiting...");
            System.exit(1);
        }
    }
}
