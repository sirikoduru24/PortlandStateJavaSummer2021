package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Main Class Project-2
 */
public class Project2 {
    /**
     * Strings for storing some error messages.
     */
    public static final String USAGE_MSG = "usage: java apptBook \n" +
            "options: (can be in any order) [-print -ReadMe -textFile filename] \n" +
            "arguments: owner description startdate startime enddate endtime";
    public static final String MISSING_ARGS = "Missing command line arguments. Expected some options and arguments in the below order";
    public static final String TOO_MANY_ARGS = "Too many arguments found. Expected only a few in the below order";
    public static final String TOO_FEW_ARGS = "Found less arguments. Expected more arguments in the below order.";
    public static final String NO_OPTIONS_TOO_FEW_ARGS = "There are no options entered and the arguments are very few to add to an Appointment Book.";
    public static final String NO_OPTIONS_TOO_MANY_ARGS = "There are no options entered and the arguments are more than expected to be added to an Appointment Book.";
    public static final String PRINT_TOO_FEW_ARGS = "Option print has been chosen but there are less than expected number of arguments to perform -print";
    public static final String PRINT_TOO_MANY_ARGS = "Option print has been chosen but there are more than the expected number of arguments to perform -print";
    public static final String TOO_FEW_ARGS_WITH_TWO_OPTNS = "Options -print and -textfile have been chosen but there are less than the expected number of arguments";
    public static final String TOO_MANY_ARGS_WITH_TWO_OPTNS = "Options -print and -textfile have been chosen but there are more than the expected number of arguments";

    /**
     * Main method for the class Project-2
     * checks for the command line:
     * checks if there are any arguments present
     * Prints - Missing command line arguments when no arguments are provided.
     * Prints README description if one of the options is -readme and exits.
     * Prints the appointment description if -print is chosen as an option along with valid command line arguments
     * Stores the appointments in a textFile if -textFile file(name of the file) is provided as an option.
     * @param args
     * @throws ParserException
     * @throws IOException
     */
    public static void main(String[] args) throws ParserException, IOException {
        ArrayList<String> list = new ArrayList();
        int numberOfOptions = 0;
        for(String arg: args) {
            list.add(arg.toLowerCase());
        }
        /**
         * Checks if there are no arguments and prints missing arguments as an error.
         */
        if(args.length == 0) {
            printErrorMessageAndExit(MISSING_ARGS);
        }
        /**
         * If the arguments contain -readme option, the code prints readme and exits.
         */
        else if(list.contains("-readme") && list.indexOf("-readme") <= 3) {
            printReadMe();
            System.exit(0);
        }
        /**
         * If there are more than the expected number of arguments code prints TOO_MANY_ARGUMENTS and exits
         */
        else if(args.length >= 11) {
            printErrorMessageAndExit(TOO_MANY_ARGS);
        } else {
            int len = 4;
            if(len > args.length) { len = args.length;}
            TreeSet<String> optionsList = new TreeSet<>();
            /**
             * loop for counting the number of options present in the command line.
             */
            for(int i = 0; i< len; i++) {
                if(args[i].equalsIgnoreCase("-print") || args[i].equalsIgnoreCase("-readme")
                || args[i].equalsIgnoreCase("-textFile")) {
                    optionsList.add(args[i]);
                    numberOfOptions++;
                }
            }
            if(numberOfOptions != optionsList.size()) {
                printErrorMessageAndExit("There are options which have been repeated more than once");
            }
            /**
             * Conditions for checking if there are no options.
             */
            if(numberOfOptions == 0) {
                /**
                 * There are no options and length of arguments is 6, check if arguments are valid or else print error messages.
                 */
                if(args.length == 6) {
                    ValidateArguments vargs = new ValidateArguments();
                    if(vargs.checkIfArgsAreValid(args[0],args[1],args[2],args[3],args[4],args[5])) {
                        Appointment appointment = new Appointment(args,-1);
                        AppointmentBook apptbook = new AppointmentBook(appointment, args[0]);
                        System.err.println("Successfully added appointment to Appointment Book");
                        System.exit(0);
                    } else {System.err.println(USAGE_MSG);System.exit(1);}
                } else {if(args.length < 6) {printErrorMessageAndExit(NO_OPTIONS_TOO_FEW_ARGS);} else {printErrorMessageAndExit(NO_OPTIONS_TOO_MANY_ARGS);}}
            }
            /**
             * Checking if the number of options is 1.
             * If the option is -print along with valid arguments print the latest appointment
             * If the option is -textFile file along with valid arguments store the appointments in file
             * Else print the respective error messages.
             */
            else if(numberOfOptions == 1) {
                if(list.contains("-print")) {
                    if(list.indexOf("-print") == 0) {
                        if(args.length == 7) {
                            ValidateArguments vargs = new ValidateArguments();
                            if(vargs.checkIfArgsAreValid(args[1], args[2], args[3], args[4], args[5], args[6])) {
                                Appointment appointment = new Appointment(args,0);
                                AppointmentBook apptbook = new AppointmentBook(appointment, args[1]);
                                System.out.println(apptbook.toString());
                                System.out.println(appointment.toString());
                                System.exit(0);
                            } else {System.err.println(USAGE_MSG);}
                        } else {
                            if(args.length < 7) {
                                if(args.length == 1) {printErrorMessageAndExit(MISSING_ARGS);}
                                else {printErrorMessageAndExit(PRINT_TOO_FEW_ARGS);}}
                            else {printErrorMessageAndExit(PRINT_TOO_MANY_ARGS);}
                        }
                    } else {
                        printErrorMessageAndExit("Expected options before arguments but found it differently");
                    }
                } else if(list.contains("-textfile")) {
                    if(list.indexOf("-textfile") == 0) {
                        if(args.length == 8) {
                            ValidateArguments vargs = new ValidateArguments();
                            if(vargs.checkIfArgsAreValid(args[2], args[3], args[4], args[5], args[6], args[7])) {
                                String textFile = args[1];
                                String ownerName = args[2];
                                Appointment appt = new Appointment(args,1);
                                createParseAndDump(textFile, ownerName, args, appt);
                                System.exit(0);
                            } else {System.err.println(USAGE_MSG);}
                        } else {
                            if(args.length < 8) {
                                if(args.length == 1) {printErrorMessageAndExit("Missing text file name and command line arguments"); }
                                else if(args.length == 2) {printErrorMessageAndExit(MISSING_ARGS);}
                                else {printErrorMessageAndExit(TOO_FEW_ARGS);}
                            }
                            else { printErrorMessageAndExit(TOO_MANY_ARGS); }
                        }
                    } else {
                        printErrorMessageAndExit("Expected options to be before arguments but found it differently");
                    }
                }
            }
            /**
             * Check if the number of options is two.
             * Check if -print and -textFile file with valid args are provided
             * Else print respective error messages.
             */
            else if(numberOfOptions == 2) {
                if(list.contains("-print") && list.contains("-textfile")) {
                    if(list.indexOf("-print") == 0 && list.indexOf("-textfile") == 1) {
                        if(args.length == 9) {
                            ValidateArguments vargs = new ValidateArguments();
                            if(vargs.checkIfArgsAreValid(args[3], args[4], args[5], args[6], args[7], args[8])) {
                                Appointment commandLineAppointment = new Appointment(args,2);
                                System.out.println("The latest appointment information is:\n"+commandLineAppointment);
                                createParseAndDump(args[2],args[3],args,commandLineAppointment);
                                System.exit(0);
                            } else {System.err.println(USAGE_MSG);}
                        } else {
                            if(args.length < 9) {
                                if(args.length == 2) {printErrorMessageAndExit("Missing text file name and command line arguments");}
                                else if(args.length == 3) {printErrorMessageAndExit(MISSING_ARGS);}
                                else {printErrorMessageAndExit(TOO_FEW_ARGS_WITH_TWO_OPTNS);}}
                            else {printErrorMessageAndExit(TOO_MANY_ARGS_WITH_TWO_OPTNS);}
                        }
                    } else if(list.indexOf("-textfile") == 0) {
                        if(list.indexOf("-print") == 1) {
                            printErrorMessageAndExit("Missing file name");
                        } else {
                            if(list.indexOf("-print") == 2) {
                                if(args.length == 9) {
                                    ValidateArguments vargs = new ValidateArguments();
                                    if (vargs.checkIfArgsAreValid(args[3], args[4], args[5], args[6], args[7], args[8])) {
                                        Appointment commandLineAppointment = new Appointment(args, 2);
                                        System.out.println("The latest appointment information is:\n" + commandLineAppointment);
                                        createParseAndDump(args[1], args[3], args, commandLineAppointment);
                                        System.exit(0);
                                    } else {System.err.println(USAGE_MSG);}
                                } else {
                                    if(args.length < 9) {
                                        if(args.length == 3) {printErrorMessageAndExit(MISSING_ARGS);}
                                        else {printErrorMessageAndExit(TOO_FEW_ARGS_WITH_TWO_OPTNS);}
                                    }
                                    else {printErrorMessageAndExit(TOO_MANY_ARGS_WITH_TWO_OPTNS);}
                                }
                            } else {
                                System.err.println("Option print is not at correct position");
                                System.err.println(USAGE_MSG);
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * Method to parse the Appointments present in textfile, and then dump all the Appointments
     * along with those in command line to the textfile by creating an AppointmentBook.
     * @param textFile
     * @param ownerName
     * @param args
     * @param cmdLineAppt
     * @throws ParserException
     * @throws IOException
     */
    public static void createParseAndDump(String textFile, String ownerName, String[] args,Appointment cmdLineAppt) throws ParserException, IOException {
        AppointmentBook apptbook;
        TextParser tp = new TextParser(textFile,ownerName);
        apptbook = (AppointmentBook) tp.parse();
        apptbook.addAppointment(cmdLineAppt);
        TextDumper td = new TextDumper(textFile);
        td.dump(apptbook);
        System.out.println("Successfully added all the appointments to Appointment Book");
    }
    /**
     * This method prints the Readme for the project when -readme option is invoked.
     */
    public static void printReadMe() {
        try {
            InputStream is = Project2.class.getResourceAsStream("README.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = null;
            while (true) {
                str = br.readLine();
                if (str != null) {
                    System.err.println(str);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Method for printing the provided error message, usage message and exit.
     * @param message
     */
    public static void printErrorMessageAndExit(String message) {
        System.err.println(message);
        System.err.println(USAGE_MSG);
        System.exit(1);
    }
}
