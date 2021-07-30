package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Class to Parse the contents in a Appointment Book.
 */
public class TextParser implements AppointmentBookParser<AppointmentBook> {
    private final Reader reader;

    public TextParser(Reader reader) {
        this.reader = reader;
    }
    @Override
    public AppointmentBook parse() throws ParserException {
        BufferedReader br = new BufferedReader(this.reader);
        try {
            String owner = br.readLine();
            owner = owner.replace("Appointments for ","");
            AppointmentBook book = new AppointmentBook(owner);
            String line = "";
            while((line = br.readLine())!=null) {
                String[] args = line.split(",");
                book.addAppointment(new Appointment(new String[]{owner,args[0],args[1],args[2]}));
            }
            if(book.getAppointments().size() == 0) {return null;}
            return book;

        } catch (IOException e) {
            throw new ParserException("While reading text", e);
        }
    }
}
