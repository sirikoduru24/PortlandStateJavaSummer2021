package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.ArrayList;

/**
 * Class TextParser for parsing the contents of the given file and then adding the Appointments
 * in the file to the AppointmentBook if all checks are valid.
 */
public class TextParser implements AppointmentBookParser {
    private String fileName = "";
    private String ownerName = "";

    /**
     * Constructor for TextParser with file name and owner name
     * @param fileName
     * @param ownerName
     */
    public TextParser(String fileName, String ownerName) {
        if(fileName == null || fileName.trim().isEmpty()) {
            System.err.println("Entered an invalid file name");
            System.err.println(Project2.USAGE_MSG);
            System.exit(1);
        }
        this.fileName = fileName;
        this.ownerName = ownerName;
    }

    /**
     * This method parse reads the contents of the file if all are valid, creates an AppointmentBook and returns it.
     * @return AbstractAppointmentBook
     * @throws ParserException
     */
    @Override
    public AbstractAppointmentBook parse() throws ParserException {
        FileReader fr = null;
        BufferedReader br = null;
        String str = null;
        ArrayList<String> list = new ArrayList();
        AppointmentBook apptbook = new AppointmentBook(ownerName);
        try {
            File file = new File(fileName);
            fr = new FileReader(file);
        } catch(FileNotFoundException fne) {
            System.err.println("File does not exist for reading contents");
            System.out.println("Creating the file inorder to write contents");
            return apptbook;
        }
        try {
            br = new BufferedReader(fr);
            while((str = br.readLine())!= null) {
                list.add(str);
            }
        } catch (IOException e) {
            throw new ParserException("Error while reading from file. Malformatted file.");
        }
        if(list == null){return apptbook;}
        else{
            int lineNumber = 1;
            for(String appointments:list) {
                String[] currentAppointment = appointments.split(",");
                if(currentAppointment[0].equalsIgnoreCase(apptbook.getOwnerName())) {
                    if(currentAppointment.length == 6) {
                        ValidateArguments vargs = new ValidateArguments();
                        if (vargs.checkIfArgsAreValid(currentAppointment[0], currentAppointment[1], currentAppointment[2], currentAppointment[3], currentAppointment[4], currentAppointment[5])) {
                            apptbook.addAppointment(new Appointment(currentAppointment, -1));
                        } else {
                            System.err.println("The above arguments in text file in line: "+lineNumber+" are wrong");
                            System.err.println("\nReading from text file stopped and exiting as it has malformed contents.");
                            System.exit(1);
                        }
                    } else {
                        System.err.println("Invalid arguments present in line number "+lineNumber+" in the file");
                        System.err.println(Project2.USAGE_MSG);
                        System.exit(1);
                    }
                } else {
                    System.err.println("Owner names in command line and file name do not match");
                    System.exit(1);
                }
                lineNumber++;
            }
            return apptbook;
        }
    }
}

