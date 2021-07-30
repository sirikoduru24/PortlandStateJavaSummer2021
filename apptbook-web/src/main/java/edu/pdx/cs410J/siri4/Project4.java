package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {
    /**
     * Strings to print error messages.
     */
    public static final String MISSING_ARGS = "Missing command line arguments";
    public static final String MISSING_HOST_AND_PORT = "Missing host and port.\n" + "Required host and port options";
    public static final String PRINT_AND_SEARCH = "Found options print and search but they cannot be together";
    public static final String OPTNS_BEFORE_ARGS = "Expected options before arguments but found differently";
    public static final String MISSING_HOST_NAME = "Missing host name";
    public static final String MISSING_PORT_NAME = "Expected the port name to be an Integer port number";
    public static final String TOO_FEW_ARGS = "There are less than the number of arguments to add Appointment to Book";
    public static final String TOO_MANY_ARGS = "There are more than the number of arguments to add Appointment to Book";
    public static final String TOO_FEW_ARGS_PRINT = "There are less than the number of arguments to add Appointment to Book and perform -print";
    public static final String TOO_MANY_ARGS_PRINT = "There are more than the number of arguments to add Appointment to Book and perform -print";
    public static final String TOO_FEW_ARGS_SEARCH = "There are less than the number of arguments to search records from Appointment Book.";
    public static final String TOO_MANY_ARGS_SEARCH = "There are more than the number of arguments to search records from Appointment Book.";

    /**
     * This is the main method of the Project.
     * Reads the command line arguments and performs respective operations.
     * @param args
     * @throws IOException
     */
    public static void main(String... args) throws IOException {
        ArrayList list = new ArrayList();
        list.addAll(Arrays.asList(args));
        //Prints missing args if there are nothing provided.
        if(args.length == 0) {
            error(MISSING_ARGS);
        }
        /**
         * If arguments contain README,prints and exits.
         */
        else if(list.contains("-README")) {
            printReadMeAndExit();
        } else {
            /**
             * Program stops if there are no -host and -port options provided.
             */
            if(!list.contains("-host") && !list.contains("-port")) {
                error(MISSING_HOST_AND_PORT);
            } else if(!list.contains("-host")) {
                error("Missing host option");
            } else if(!list.contains("-port")) {
                error("Missing port option");
            } else {
                /**
                 * counts the options
                 */
                int numberOfOptns = 0;
                for(Object options:list) {
                    if(options.equals("-print") || options.equals("-search")) {numberOfOptns++;}
                }
                if(numberOfOptns >= 3) {
                    error("Expected one of either -print or -search along with port but they were repeated multiple times");
                }
                if(numberOfOptns == 2) {
                    error(PRINT_AND_SEARCH);
                }
                if(args.length > 4) {
                    String hostName = args[list.indexOf("-host") + 1];
                    int port = 0;
                    try {
                        port = Integer.parseInt(args[list.indexOf("-port") + 1]);
                    } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                        error(MISSING_PORT_NAME);
                    }
                    AppointmentBookRestClient client = new AppointmentBookRestClient(hostName,port);
                    try {
                        /**
                         * No options and only owner name gets All Appointments
                         * No options and all arguments correct adds Appointment to Appointment Book.
                         */
                        if(numberOfOptns == 0) {
                            if(list.indexOf("-host") <= 2 && list.indexOf("-port") <= 2) {
                                if(hostName.equals("-port")) {
                                    error("Invalid host name provided");
                                }
                                //get all appointments related to a particular owner in pretty format.
                                if(args.length == 5) {
                                       System.out.println(client.getAllAppointmentBookEntries(args[4]));
                                }

                                //add appointment to appointment book
                                else if (args.length == 12) {
                                    ValidateArguments valargs = new ValidateArguments();
                                    if(valargs.checkIfArgsAreValid(args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11])) {
                                        Appointment appt = new Appointment(args,3);
                                        client.addAppointment(appt);
                                        System.out.println("Successfully added Appointment to Appointment Book.");
                                    } else {
                                        error("Invalid arguments provided to add appointment to Appointment Book");
                                    }
                                } else {
                                    if(args.length <= 11) {error(TOO_FEW_ARGS);}
                                    else {error(TOO_MANY_ARGS);}
                                }
                            } else { error(OPTNS_BEFORE_ARGS); }
                        } else if(numberOfOptns == 1) {
                            /**
                             * If the chosen option is print the program adds the Appointment to Appointment Book
                             * and prints the latest Appointment.
                             */
                            if(list.contains("-print")) {
                                if(list.indexOf("-host") <= 4 & list.indexOf("-port") <= 4 & list.indexOf("-print") <= 4) {
                                    if(hostName.equals("-port") || hostName.equals("-print")) { error(MISSING_HOST_NAME);}
                                    if(args.length == 13) {
                                        ValidateArguments valargs = new ValidateArguments();
                                        if(valargs.checkIfArgsAreValid(args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12])) {
                                                Appointment appointment = new Appointment(args,4);
                                                System.out.println("The latest appointment information is :"+ appointment.toString());
                                                client.addAppointment(appointment);
                                        } else {error("Invalid arguments provided to perform -print");}
                                    } else {
                                        if(args.length >= 14) {error(TOO_MANY_ARGS_PRINT);}
                                        else { error(TOO_FEW_ARGS_PRINT);}
                                    }
                                } else {error(OPTNS_BEFORE_ARGS);}
                            } else if(list.contains("-search")) {
                                /**
                                 * Searches for appointments between given times.
                                 */
                                if(list.indexOf("-host") <= 4 & list.indexOf("-port") <= 4 & list.indexOf("-search") <= 4) {
                                    if(hostName.equals("-port") || hostName.equals("-print")) { error(MISSING_HOST_NAME);}
                                    if(args.length == 12) {
                                        ValidateArguments valargs = new ValidateArguments();
                                        if(valargs.checkIfArgsAreValid(args[5],"searching",args[6],args[7],args[8],args[9],args[10],args[11])) {
                                            System.out.println("Search Results:");
                                            System.out.println(client.getAllAppointmentsBetweenGivenTimes(args[5],args[6]+" "+args[7]+" "+args[8], args[9]+" "+args[10]+" "+args[11]));

                                        } else {error("Invalid arguments provided to perform -search");}
                                    } else {
                                        if(args.length <= 11) {error(TOO_FEW_ARGS_SEARCH);}
                                        else {error(TOO_MANY_ARGS_SEARCH);}
                                    }

                                }
                            }
                        }

                    } catch(ConnectException | ParserException cex) {
                        System.err.println("Refused for connection");
                        System.exit(1);
                    } catch(RuntimeException ex) {
                        System.err.println("There exists no Appointment Book with the given owner name");
                    }

                } else {
                    error("There are less than the number of arguments required to perform some operation");
                }

            }
        }
    }

    /**
     * Method to print README
     */
    private static void printReadMeAndExit() {
    System.out.println("This is a README file!");
    usage();
    }

    /**
     * Method to print Error.
     * @param message
     */
    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println(message);
        usage();
    }

    private static void usage()
    {
        PrintStream err = System.err;
        err.println();
        err.println("usage: java Project4 [options]\n" +
                "[options](can be in any order)-[-host hostname -port portnumber -print -search]\n" +
                "[arguments] owner, description, start date and time, end date and time");
        err.println("-search needs only ownername, start and end times to search from the Appointment Book.");
        err.println("This simple program posts appointment information");
        err.println("to the server.");
        err.println("The -print option and -search option cannot be given together");
        err.println("All other options can be in any order");
        System.exit(1);
    }
}