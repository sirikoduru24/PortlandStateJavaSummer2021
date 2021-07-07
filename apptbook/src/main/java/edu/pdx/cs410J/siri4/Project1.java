package edu.pdx.cs410J.siri4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The main class for the CS410J appointment book Project
 */
public class Project1 {
  /**
   * Strings to print the error messages.
   */
  public static final String USAGE_MSG = "usage: java apptBook [-print -ReadMe] owner description startdate startime enddate endtime";
  public static final String MISSING_ARGS = "Missing command line arguments. Expected some options and arguments in the below order";
  public static final String TOO_MANY_ARGS = "Too many arguments provided expected only few: in the below format";
  public static final String INCORRECT_OPTIONS_FEW_ARGUMENTS = "Provided Options are incorrect expected -print or -readme, but found others and there are unexpected arguments";
  public static final String README_TEXT = "This is a README file!";
  public static final String TOO_MANY_ARGS_NO_OPTIONS = "There are more than expected number of arguments and there are no options";
  public static final String TOO_FEW_ARGUMENTS_OPTIONS_PRESENT = "There are less than the expected number of arguments. Expected arguments in the below format.";
  public static final String TOO_FEW_ARGUMENTS_NO_OPTIONS_PRESENT = "There are no options present and less than the expected number of arguments.\n " +
          "Expected arguments in the below format";

  /**
   * main method for the class Project-1
   * @author Siri Koduru
   * @param args
   */
  public static void main(String[] args) {
    /**
     * checks if there are no arguments and prints an error message.
     */
    if (args.length == 0) {
      printErrorMessageAndExit(MISSING_ARGS);
    }
    /**
     * checks if there is only one argument and if it is "-readme",
     * prints the readme file and exits.Else it prints the respective error message.
     */
    else if(args.length == 1) {
      if(args[0].equalsIgnoreCase("-readme")) {
        printReadMe();
        System.exit(1);
      } else {
        if(args[0].equalsIgnoreCase("-print")) {
          System.err.println("Missing arguments to perform print");
          System.err.println(USAGE_MSG);
          System.exit(1);
        } else {
          printErrorMessageAndExit(TOO_FEW_ARGUMENTS_NO_OPTIONS_PRESENT);
        }
      }
    }
    /**
     * If there is [-readme] option at either first or second position,
     * prints the project README and exits.
     */
    else if(args[0].equalsIgnoreCase("-readme") || args[1].equalsIgnoreCase("-readme")) {
      printReadMe();
      System.exit(1);
    }
    /**
     * Checks if there are more than the required number of arguments.
     * Checks if there are any options present, if there are options present, displays the error message of more arguments.
     * If there are no options displays the error message of many arguments and no options.
     */
    else if(args.length >= 8) {
      if((args[0].equalsIgnoreCase("-readme") || args[1].equalsIgnoreCase("-readme")) ||
              (args[0].equalsIgnoreCase("-print") || args[1].equalsIgnoreCase("-print"))) {
        printErrorMessageAndExit(TOO_MANY_ARGS);
      }
      if(!(args[0].equalsIgnoreCase("-readme") || args[1].equalsIgnoreCase("-readme")) ||
              !(args[0].equalsIgnoreCase("-print") || args[1].equalsIgnoreCase("-print"))){
        printErrorMessageAndExit(TOO_MANY_ARGS_NO_OPTIONS);
      }
    }
    /**
     * Checks if the args list length is less than the required length.
     * If there are expected number of arguments along with the options then it prints the description
     * validating the arguments.
     * Else displays the error message that there are less number of arguments.
     */
    else if(args.length < 8) {
      if(args.length == 7) {
        if (args[0].equalsIgnoreCase("-print")) {
          if(checkIfArgsAreValid(args)) {
            Appointment appointment = new Appointment(args);
            AppointmentBook apptbook = new AppointmentBook(appointment,args[1]);
            System.err.println(appointment.toString());
            System.exit(0);
          }
          } else {
          printErrorMessageAndExit(TOO_MANY_ARGS_NO_OPTIONS);
        }
        } else {
          if(args[0].equalsIgnoreCase("-readme") || args[1].equalsIgnoreCase("-readme")
          || args[0].equalsIgnoreCase("-print") || args[1].equalsIgnoreCase("-print")) {
            if(args[0].equalsIgnoreCase("-print")) {
              switch(args.length) {
                case 2:
                  System.err.println("Missing description");
                  printErrorMessageAndExit(TOO_FEW_ARGUMENTS_OPTIONS_PRESENT);
                  break;
                case 3:
                  System.err.println("Missing start date");
                  printErrorMessageAndExit(TOO_FEW_ARGUMENTS_OPTIONS_PRESENT);
                  break;
                case 4:
                  if(!checkIfDateIsValid(args[3])) {
                    System.err.println("Entered Invalid start date");
                  }
                  System.err.println("Missing start time");
                  printErrorMessageAndExit(TOO_FEW_ARGUMENTS_OPTIONS_PRESENT);
                  break;
                case 5:
                  if(!checkIfDateIsValid(args[3])) {
                    System.err.println("Entered Invalid start date");
                  }
                  if(!checkIfTimeIsValid(args[4])) {
                    System.err.println("Entered Invalid start time");
                  }
                  System.err.println("Missing end date");
                  printErrorMessageAndExit(TOO_FEW_ARGUMENTS_OPTIONS_PRESENT);
                  break;
                case 6:
                  if(!checkIfDateIsValid(args[3])) {
                    System.err.println("Entered Invalid start date");
                  }
                  if(!checkIfTimeIsValid(args[4])) {
                    System.err.println("Entered Invalid start time");
                  }
                  if(!checkIfDateIsValid(args[5])) {
                    System.err.println("Entered Invalid end date");
                  }
                  System.err.println("Missing end time");
                  printErrorMessageAndExit(TOO_FEW_ARGUMENTS_OPTIONS_PRESENT);
                  break;
              }
            }
            //printErrorMessageAndExit(TOO_FEW_ARGUMENTS_OPTIONS_PRESENT);
          }
          else if(!(args[0].equalsIgnoreCase("-readme") || args[1].equalsIgnoreCase("-readme")) &&
                !(args[0].equalsIgnoreCase("-print") || args[1].equalsIgnoreCase("-print"))){
          printErrorMessageAndExit(INCORRECT_OPTIONS_FEW_ARGUMENTS);
        }
      }
      }
    System.exit(0);
  }

  /**
   * @param message
   * This method takes the message as an argument and prints all error messages when this method is called with the required message parameter.
   */
  private static void printErrorMessageAndExit(String message) {
    System.err.println(message);
    System.err.println(USAGE_MSG);
    System.exit(1);
  }

  /**
   * This method prints the Readme for the project when -readme option is invoked.
   */
  private static void printReadMe() {
    try {
      InputStream is = Project1.class.getResourceAsStream("README.txt");
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
   * @param args
   * @return boolean
   * validates all the arguments.
   *
   */
  private static boolean checkIfArgsAreValid(String[] args) {
    if(!checkIfNameOrDescriptionIsValid(args[1])) {
      System.err.println("Entered owner name is empty. Expected an owner consisting of any characters including numbers");
    }
    if(!checkIfNameOrDescriptionIsValid(args[2])) {
      System.err.println("Entered description is empty. Expected a description consisting of any characters including numbers");
    }
    if(!checkIfDateIsValid(args[3])) {
      System.err.println("Entered start date is invalid. \n Expected start date in the format mm/dd/yyyy.");
    }
    if(!checkIfTimeIsValid(args[4])) {
      System.err.println("Entered start time is invalid. \n Expected start time in the format hh:mm");
    }
    if(!checkIfDateIsValid(args[5])) {
      System.err.println("Entered end date is invalid. \n Expected end date in the format mm/dd/yyyy");
    }
    if(!checkIfTimeIsValid(args[6])) {
      System.err.println("Entered end time is invalid. \n Expected end time in the format hh:mm");
    }
    if (checkIfNameOrDescriptionIsValid(args[1]) && checkIfNameOrDescriptionIsValid(args[2]) && checkIfDateIsValid(args[3]) && checkIfDateIsValid(args[5]) && checkIfTimeIsValid(args[4])
            && checkIfTimeIsValid(args[6])) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * @param arg
   * @return boolean
   * This method takes either name or description as an argument.
   * Returns true if the argument is valid else returns invalid.
   */
  private static boolean checkIfNameOrDescriptionIsValid(String arg) {
    if(arg.trim().isEmpty()) {
      return false;
    }
    return true;
  }
  /**
   * @param date
   * @return boolean
   * This method takes date as a parameter.
   * Validates the date in the format mm/dd/yyyy.
   */
  private static boolean checkIfDateIsValid(String date) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
      Date x = sdf.parse(date);
      if (date.equals(sdf.format(x))) {
        return true;
      }
      else {
        return false;
      }
    } catch (ParseException ex) {
      //ex.printStackTrace();
      return false;
    }
  }

  /**
   * @param time
   * @return boolean
   * This method validates the parameter time if it is in the format of hh:mm.
   */
  private static boolean checkIfTimeIsValid(String time) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
      Date x = sdf.parse(time);
      if (time.equals(sdf.format(x))) {
        return true;
      }
      else {
        return false;
      }
    } catch (ParseException ex) {
      //ex.printStackTrace();
      return false;
    }
  }
}