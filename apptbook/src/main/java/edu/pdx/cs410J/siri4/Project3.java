package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Main Class Project-3
 */
public class Project3 {
    /**
     * Strings for storing some error messages.
     */
    public static final String USAGE_MSG = "usage: java apptBook \n" +
            "OPTIONS: (can be in any order) \n" +
            "[-print -ReadMe -textFile filename -pretty [-/filename]] \n" +
            "ARGUMENTS:\n" +
            "owner \n" +
            "description\n" +
            "begin (format : mm/dd/yyyy hh:mm am/pm) \n" +
            "enddate (format : mm/dd/yyyy hh:mm am/pm";
    public static final String MISSING_ARGS = "Missing command line arguments. Expected some options and arguments in the below order";
    public static final String TOO_MANY_ARGS = "Too many arguments found. Expected only a few in the below order";
    public static final String TOO_FEW_ARGS = "Found less arguments. Expected more arguments in the below order.";
    public static final String NO_OPTIONS_TOO_FEW_ARGS = "There are no options entered and the arguments are very few to add to an Appointment Book.";
    public static final String NO_OPTIONS_TOO_MANY_ARGS = "There are no options entered and the arguments are more than expected to be added to an Appointment Book.";
    public static final String PRINT_TOO_FEW_ARGS = "Option -print has been chosen but there are less than expected number of arguments to perform -print";
    public static final String PRINT_TOO_MANY_ARGS = "Option -print has been chosen but there are more than the expected number of arguments to perform -print";
    public static final String TEXT_FILE_TOO_FEW_ARGS = "Option -textFile has been chosen but there are less than the expected number of arguments to store to -textFile";
    public static final String TEXT_FILE_TOO_MANY_ARGS = "Option -textFile has been chosen but there are more than the expected number of arguments to store to -textFile";
    public static final String PRETTY_TOO_FEW_ARGS = "Option -pretty has been chosen but there are less than the expected number of arguments to perform -pretty";
    public static final String PRETTY_TOO_MANY_ARGS = "Option -pretty has been chosen but there are more than the expected number of arguments to perform -pretty";
    public static final String TOO_FEW_ARGS_WITH_PRINT_TXTFILE = "Options -print and -textfile have been chosen but there are less than the expected number of arguments";
    public static final String TOO_MANY_ARGS_WITH_PRINT_TXTFILE = "Options -print and -textfile have been chosen but there are more than the expected number of arguments";
    public static final String TOO_FEW_ARGS_WITH_PRINT_PRETTY = "Options -print and -pretty have been chosen but there are less than the expected number of arguments";
    public static final String TOO_MANY_ARGS_WITH_PRINT_PRETTY = "Options -print and -pretty have been chosen but there are more than the expected number of arguments";
    public static final String TOO_FEW_ARGS_WITH_PRETTY_TXTFILE = "Options -pretty and -textfile have been chosen but there are less than the expected number of arguments";
    public static final String TOO_MANY_ARGS_WITH_PRETTY_TXTFILE = "Options -pretty and -textfile have been chosen but there are more than the expected number of arguments";
    public static final String TOO_FEW_ARGS_WITH_THREE_OPTNS = "Options -pretty,-print and -textfile have been chosen but there are less than the expected number of arguments";
    public static final String TOO_MANY_ARGS_WITH_THREE_OPTNS = "Options -print,-pretty and -textfile have been chosen but there are more than the expected number of arguments";
    /**
     * Main method for the class Project-3
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
        else if(list.contains("-readme")) {
            printReadMe();
            System.exit(0);
        }
        /**
         * If there are more than the expected number of arguments code prints TOO_MANY_ARGUMENTS and exits
         */
        else if(args.length >= 14) {
            printErrorMessageAndExit(TOO_MANY_ARGS);
        } else {
            int len = 6;
            if(len > args.length) { len = args.length;}
            TreeSet<String> optionsList = new TreeSet<>();
            /**
             * loop for counting the number of options present in the command line.
             */
            for(int i = 0; i< len; i++) {
                if(args[i].equalsIgnoreCase("-print") || args[i].equalsIgnoreCase("-pretty")
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
                 * There are no options and length of arguments is 8, check if arguments are valid or else print error messages.
                 */
                if(args.length == 8) {
                    ValidateArguments vargs = new ValidateArguments();
                    if(vargs.checkIfArgsAreValid(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7])) {
                        Appointment appointment = new Appointment(args,-1);
                        AppointmentBook apptbook = new AppointmentBook(appointment, args[0]);
                        System.err.println("Successfully added appointment to Appointment Book");
                        System.exit(0);
                    } else
                    {
                        System.err.println(USAGE_MSG);
                        System.exit(1);
                    }
                } else {
                    if(args.length < 8) {
                        printErrorMessageAndExit(NO_OPTIONS_TOO_FEW_ARGS);
                    } else {
                        printErrorMessageAndExit(NO_OPTIONS_TOO_MANY_ARGS);}}
            }
            /**
             * Checking if the number of options is 1.
             * If the option is -print along with valid arguments print the latest appointment
             * If the option is -textFile file along with valid arguments store the appointments in file
             * If the option is -pretty check for the next argument if it is "-" or a file name and pretty print the Appointments respectively
             * Else print the respective error messages.
             */
            else if(numberOfOptions == 1) {
                if(list.contains("-print")) {
                    if(list.indexOf("-print") == 0) {
                        if(args.length == 9) {
                            ValidateArguments vargs = new ValidateArguments();
                            if(vargs.checkIfArgsAreValid(args[1], args[2], args[3], args[4], args[5], args[6],args[7],args[8])) {
                                Appointment appointment = new Appointment(args,0);
                                AppointmentBook apptbook = new AppointmentBook(appointment, args[1]);
                                System.out.println(apptbook.toString());
                                System.out.println(appointment.toString());
                                System.exit(0);
                            } else {System.err.println(USAGE_MSG);System.exit(1);}
                        } else {
                            if(args.length < 9) { if(args.length == 1) {printErrorMessageAndExit(MISSING_ARGS);} else {printErrorMessageAndExit(PRINT_TOO_FEW_ARGS);}}
                            else {printErrorMessageAndExit(PRINT_TOO_MANY_ARGS);}
                        }
                    } else {
                        printErrorMessageAndExit("Expected options before arguments but found it differently");
                    }
                } else if(list.contains("-textfile")) {
                    if(list.indexOf("-textfile") == 0) {
                        if(args.length == 10) {
                            ValidateArguments vargs = new ValidateArguments();
                            if(vargs.checkIfArgsAreValid(args[2], args[3], args[4], args[5], args[6], args[7],args[8],args[9])) {
                                Appointment appt = new Appointment(args,1);
                                createParseAndDump(args[1], args[2],"false", args, appt);
                                System.exit(0);
                            } else {System.err.println(USAGE_MSG);System.exit(1);}
                        } else {
                            if(args.length < 10) {
                                if(args.length == 1) {printErrorMessageAndExit("Missing text file name and command line arguments"); }
                                else if(args.length == 2) {printErrorMessageAndExit(MISSING_ARGS);}
                                else {printErrorMessageAndExit(TEXT_FILE_TOO_FEW_ARGS);}
                            }
                            else { printErrorMessageAndExit(TEXT_FILE_TOO_MANY_ARGS); }
                        }
                    } else {
                        printErrorMessageAndExit("Expected options to be before arguments but found it differently");
                    }
                } else if(list.contains("-pretty")){
                    if(list.indexOf("-pretty") == 0) {
                        if(args.length == 10) {
                            ValidateArguments vargs = new ValidateArguments();
                            if(vargs.checkIfArgsAreValid(args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])) {
                                String textFile = args[1];
                                Appointment appt = new Appointment(args,1);
                                AppointmentBook apptBook = new AppointmentBook(appt, args[2]);
                                if(textFile.equalsIgnoreCase("-")) {
                                    prettyPrintToConsole(apptBook);
                                } else {
                                    PrettyPrint pp = new PrettyPrint(textFile);
                                    pp.dump(apptBook);
                                }
                                System.exit(0);
                            } else {System.err.println(USAGE_MSG);}
                        } else {
                            if(args.length < 10) {
                                if(args.length == 1) {printErrorMessageAndExit("Expected either - or text file name and command line arguments"); }
                                else if(args.length == 2) {printErrorMessageAndExit(MISSING_ARGS);}
                                else {printErrorMessageAndExit(PRETTY_TOO_FEW_ARGS);}
                            }
                            else { printErrorMessageAndExit(PRETTY_TOO_MANY_ARGS); }
                        }
                    } else {
                        printErrorMessageAndExit("Expected options to be before arguments but found it differently");
                    }
                }
            }
            /**
             * Check if the number of options is two.
             * If -print and -textFile file with valid args are provided
             * print the latest appointment to console and store them into the textFile provided.
             * If -print and -pretty -/file with valid args
             * print the latest appointment and pretty print it to the console/file based on the option provided.
             * If -textFile file and -pretty -/file with valid args
             * Take if there are any inputs in textFile, check with command line args, add them to file and AppointmentBook,
             * then pretty print all the appointments to either file or console.
             * Else print respective error messages.
             */
            else if(numberOfOptions == 2) {
                if(list.contains("-print") && list.contains("-textfile")) {
                    if(list.indexOf("-print") <=2 && list.indexOf("-textfile") <= 2) {
                        if((!args[0].equalsIgnoreCase("-print") && !args[0].equalsIgnoreCase("-textFile"))
                        || (args[0].equalsIgnoreCase("-print") && !args[1].equalsIgnoreCase("-textfile"))) {
                            printErrorMessageAndExit("Expected options in the first of arguments list but found differently");
                        }
                        if((list.indexOf("-print") == (list.indexOf("-textfile") + 1))) {
                            printErrorMessageAndExit("Missing text file name");
                        } else {
                            if(args.length == 11) {
                                ValidateArguments vargs = new ValidateArguments();
                                int index = list.indexOf("-textfile");
                                String textFile = args[index+1];
                                if(vargs.checkIfArgsAreValid(args[3], args[4], args[5], args[6], args[7], args[8],args[9],args[10])) {
                                    Appointment commandLineAppointment = new Appointment(args,2);
                                    System.out.println("The latest appointment information is:\n"+commandLineAppointment);
                                    createParseAndDump(textFile,args[3],"false",args,commandLineAppointment);
                                    System.exit(0);
                                } else {System.err.println(USAGE_MSG);}
                            } else {
                                if(args.length < 11) {
                                    if(args.length == 2) {printErrorMessageAndExit("Missing text file name and command line arguments");}
                                    else if(args.length == 3) {printErrorMessageAndExit(MISSING_ARGS);}
                                    else {printErrorMessageAndExit(TOO_FEW_ARGS_WITH_PRINT_TXTFILE);}}
                                else {printErrorMessageAndExit(TOO_MANY_ARGS_WITH_PRINT_TXTFILE);}
                            }
                        }
                    }
                } else if(list.contains("-print") && list.contains("-pretty")) {
                    if(list.indexOf("-print") <=2 && list.indexOf("-pretty") <= 2) {
                        if((!args[0].equalsIgnoreCase("-print") && !args[0].equalsIgnoreCase("-pretty")) ||
                                (args[0].equalsIgnoreCase("-print") && !args[1].equalsIgnoreCase("-pretty"))) {
                            printErrorMessageAndExit("Expected options in the first of arguments list but found differently");
                        }
                        if((list.indexOf("-print") == (list.indexOf("-pretty") + 1))) {
                            printErrorMessageAndExit("Expected either - or textfile, but found others");
                        } else {
                            if(args.length == 11) {
                                ValidateArguments vargs = new ValidateArguments();
                                int index = list.indexOf("-pretty");
                                String textFile = args[index+1];
                                if(vargs.checkIfArgsAreValid(args[3], args[4], args[5], args[6], args[7], args[8],args[9],args[10])) {
                                    Appointment commandLineAppointment = new Appointment(args,2);
                                    System.out.println("The latest appointment information is:\n"+commandLineAppointment);
                                    AppointmentBook apptBook = new AppointmentBook(commandLineAppointment,args[3]);
                                    if(textFile.equalsIgnoreCase("-")) {
                                        System.out.println("Pretty Printing to Console....");
                                        prettyPrintToConsole(apptBook);
                                    } else {
                                        PrettyPrint pp = new PrettyPrint(textFile);
                                        pp.dump(apptBook);
                                    }
                                    System.exit(0);
                                } else {System.err.println(USAGE_MSG);}
                            } else {
                                if(args.length < 11) {
                                    if(args.length == 2) {printErrorMessageAndExit("Missing text file name or '-' input for pretty and command line arguments");}
                                    else if(args.length == 3) {printErrorMessageAndExit(MISSING_ARGS);}
                                    else {printErrorMessageAndExit(TOO_FEW_ARGS_WITH_PRINT_PRETTY);}}
                                else {printErrorMessageAndExit(TOO_MANY_ARGS_WITH_PRINT_PRETTY);}
                            }
                        }
                    }
                } else if(list.contains("-pretty") && list.contains("-textfile")) {
                    if(list.indexOf("-textfile") <= 3 && list.indexOf("-pretty") <= 3) {
                        if(!args[0].equalsIgnoreCase("-textfile") && !args[0].equalsIgnoreCase("-pretty")) {
                            printErrorMessageAndExit("Expected options in the first of arguments list but found differently");
                        }
                        if(list.indexOf("-textfile") < list.indexOf("-pretty") && list.indexOf("-pretty") == (list.indexOf("-textfile") + 1)) {
                            printErrorMessageAndExit("Missing text file for -textFile option");
                        }
                        if(list.indexOf("-textfile") > list.indexOf("-pretty") && list.indexOf("-textfile") == (list.indexOf("-pretty") + 1)) {
                            printErrorMessageAndExit("Missing '-' or text file for -pretty option");
                        }
                        else {
                            if(args.length == 12) {
                                ValidateArguments vargs = new ValidateArguments();
                                int indexPrint = list.indexOf("-pretty");
                                int indexText = list.indexOf("-textfile");
                                String textFile = args[indexText+1];
                                String pretty = args[indexPrint + 1];
                                if(vargs.checkIfArgsAreValid(args[4], args[5], args[6], args[7], args[8], args[9],args[10],args[11])) {
                                    Appointment commandLineAppointment = new Appointment(args,3);
                                    if(textFile.equals(pretty)) {
                                        printErrorMessageAndExit("TextFiles for -textFile and -pretty cannot be the same");
                                    } else {
                                        createParseAndDump(textFile,args[4],pretty,args,commandLineAppointment);
                                    }
                                    System.exit(0);
                                } else {System.err.println(USAGE_MSG);}
                            } else {
                                if(args.length < 12) {
                                    if(args.length == 3) {printErrorMessageAndExit(MISSING_ARGS);}
                                        else {printErrorMessageAndExit(TOO_FEW_ARGS_WITH_PRETTY_TXTFILE);}
                                    }
                                else {printErrorMessageAndExit(TOO_MANY_ARGS_WITH_PRETTY_TXTFILE);}
                            }
                        }
                    } else {
                        printErrorMessageAndExit("Expected options before arguments but found differently");
                    }
                }
            }
            /**
             * If all three options are present.
             * First print to console the latest Appointment.
             * Add to textFile the latest one, along with ones in textFile
             * Pretty print to either console or file based on provide option.
             */
            else if(numberOfOptions == 3) {
                if(list.contains("-print") && list.contains("-textfile") && list.contains("-pretty")) {
                    if(list.indexOf("-print") <= 4 && list.indexOf("-textfile") <= 4 &&  list.indexOf("-pretty") <= 4) {
                        if(!args[0].equalsIgnoreCase("-print") && !args[0].equalsIgnoreCase("-textfile") && !args[0].equalsIgnoreCase("-pretty")) {
                            printErrorMessageAndExit("Expected options before arguments but found differently");
                        }
                        int printIndex = list.indexOf("-print");
                        int prettyIndex = list.indexOf("-pretty");
                        int textFileIndex = list.indexOf("-textfile");
                        Boolean flag = true;
                        if((printIndex == (prettyIndex+1)) || (textFileIndex == (prettyIndex+1)) ) {
                            System.err.println("Missing textfile or - input for -pretty option");
                            flag = false;
                        }
                        if((printIndex == (textFileIndex+1)) || (prettyIndex == (textFileIndex+1))) {
                            System.err.println("Missing textfile for -textFile option");
                            flag = false;
                        }
                        if(printIndex <= 2 && !(args[printIndex+1].equalsIgnoreCase("-textfile") || args[printIndex+1].equalsIgnoreCase("-pretty"))) {
                            printErrorMessageAndExit("Some errors have been found in command line arguments in options format");
                        }
                        if(flag) {
                            if(args.length == 13) {
                                ValidateArguments vargs = new ValidateArguments();
                                if(vargs.checkIfArgsAreValid(args[5], args[6], args[7], args[8],args[9], args[10],args[11],args[12])){
                                    Appointment commandLineAppointment = new Appointment(args,4);
                                    System.out.println("The latest appointment information is:\n"+commandLineAppointment.toString());
                                    if(args[prettyIndex+1].equals(args[textFileIndex+1])) {
                                        printErrorMessageAndExit("File names for -textfile option and -pretty option cannot be the same");
                                    }
                                    createParseAndDump(args[textFileIndex+1], args[5],args[prettyIndex+1],args,commandLineAppointment);
                                    System.exit(0);
                                } else {System.err.println(USAGE_MSG);System.exit(1);}
                            } else {
                                if(args.length < 13) {
                                    if(args.length == 4) {printErrorMessageAndExit("and missing command line arguments");}
                                    else if(args.length == 5) {printErrorMessageAndExit(MISSING_ARGS);}
                                    else {printErrorMessageAndExit(TOO_FEW_ARGS_WITH_THREE_OPTNS);}} else {printErrorMessageAndExit(TOO_MANY_ARGS_WITH_THREE_OPTNS);}
                            }
                        } else { System.err.println(USAGE_MSG);System.exit(1); }
                    } else { printErrorMessageAndExit("Expected options before arguments but found it differently"); }
                }
            }
        }
    }

    /**
     * This method parses a textFile given with -textFile, checks for owner name
     * Then adds all Appointments to AppointmentBook and to textFile.
     * Based on the -pretty option it either dumps to console or file.
     * @param textFile
     * @param ownerName
     * @param pretty
     * @param args
     * @param cmdLineAppt
     * @throws ParserException
     * @throws IOException
     */
    public static void createParseAndDump(String textFile, String ownerName, String pretty, String[] args,Appointment cmdLineAppt) throws ParserException, IOException {
        AppointmentBook apptbook;
        TextParser tp = new TextParser(textFile,ownerName);
        apptbook = (AppointmentBook) tp.parse();
        apptbook.addAppointment(cmdLineAppt);
        TextDumper td = new TextDumper(textFile);
        td.dump(apptbook);
        System.out.println("Successfully added all the appointments to Appointment Book");
        if(pretty.equalsIgnoreCase("-")) {
            prettyPrintToConsole(apptbook);
        } else if(pretty.equalsIgnoreCase("false")) {

        } else {
            PrettyPrint pp = new PrettyPrint(pretty);
            pp.dump(apptbook);
            //System.out.println("");
        }
    }
    /**
     * This method prints the Readme for the project when -readme option is invoked.
     */
    public static void printReadMe() {
        try {
            InputStream is = Project3.class.getResourceAsStream("README.txt");
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

    /**
     * Method to pretty print to console the Appointments stored in Appointment Book.
     * @param apptBook
     */
    public static void prettyPrintToConsole(AppointmentBook apptBook) {
        Collection<Appointment> appointments = apptBook.getAppointments();
        System.out.println("Owner:"+apptBook.getOwnerName());
        System.out.println(apptBook.toString());
        int count = 1;
        for (Appointment appointment : appointments) {
            System.out.println("Appointment: "+count);
            System.out.println("---Description:"+appointment.getDescription());
            System.out.println("---Begin Date: "+appointment.getBeginTime());
            System.out.println("---End Date:   "+appointment.getEndTime());
            System.out.println("---Duration:   "+appointment.duration()+" minutes");
            count++;
        }
    }
}
