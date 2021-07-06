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
  public static final String USAGE_MSG = "usage: java apptBook [-print -ReadMe] owner description startdate startime enddate endtime";
  public static final String MISSING_ARGS = "Missing command line arguments. Expected some options and arguments in the below order";
  public static final String TOO_MANY_ARGS = "Too many arguments provided expected only few: in the below format";
  public static final String INCORRECT_OPTIONS_FEW_ARGUMENTS = "Provided Options are incorrect expected -print or -readme, but found others and there are unexpected arguments";
  public static final String README_TEXT = "This is a README file!";
  public static final String TOO_MANY_ARGS_NO_OPTIONS = "There are more than expected number of arguments and there are no options";
  public static final String TOO_FEW_ARGUMENTS_OPTIONS_PRESENT = "There are less than the expected number of arguments. Expected arguments in the below format.";
  public static final String TOO_FEW_ARGUMENTS_NO_OPTIONS_PRESENT = "There are no options present and less than the expected number of arguments.\n " +
          "Expected argumenents in the below format";

  public static void main(String[] args) {
    int i = 0;
    if (args.length == 0) {
      printErrorMessageAndExit(MISSING_ARGS);
    }
    else if(args.length == 1) {
      if(args[0].equalsIgnoreCase("-readme")) {
        printReadMe();
        System.exit(1);
      } else {
       printErrorMessageAndExit(TOO_FEW_ARGUMENTS_NO_OPTIONS_PRESENT);
      }
    }
    else if(args[0].equalsIgnoreCase("-readme") || args[1].equalsIgnoreCase("-readme")) {
      printReadMe();
      System.exit(1);
    }
    else if(args.length >= 8) {
      if((args[0].equalsIgnoreCase("-readme") || args[1].equalsIgnoreCase("-readme")) ||
              (args[0].equalsIgnoreCase("-print") || args[1].equalsIgnoreCase("-print"))) {
        printErrorMessageAndExit(TOO_MANY_ARGS);
      }
      if(!(args[0].equalsIgnoreCase("-readme") || args[1].equalsIgnoreCase("-readme")) ||
              !(args[0].equalsIgnoreCase("-print") || args[1].equalsIgnoreCase("-print"))){
        printErrorMessageAndExit(TOO_MANY_ARGS_NO_OPTIONS);
      }
    } else if(args.length < 8) {
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
            printErrorMessageAndExit(TOO_FEW_ARGUMENTS_OPTIONS_PRESENT);
          }
          else if(!(args[0].equalsIgnoreCase("-readme") || args[1].equalsIgnoreCase("-readme")) &&
                !(args[0].equalsIgnoreCase("-print") || args[1].equalsIgnoreCase("-print"))){
          printErrorMessageAndExit(INCORRECT_OPTIONS_FEW_ARGUMENTS);
        }
      }
      }
    System.exit(0);
  }

  private static void printErrorMessageAndExit(String message) {
    System.err.println(message);
    System.err.println(USAGE_MSG);
    System.exit(1);
  }

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

  private static boolean checkIfArgsAreValid(String[] args) {
    if(!checkIfDateIsValid(args[3])) {
      System.err.println("Entered start date is invalid");
    }
    if(!checkIfTimeIsValid(args[4])) {
      System.err.println("Entered start time is invalid");
    }
    if(!checkIfDateIsValid(args[5])) {
      System.err.println("Entered end date is invalid");
    }
    if(!checkIfTimeIsValid(args[6])) {
      System.err.println("Entered end time is invalid");
    }
    if (checkIfDateIsValid(args[3]) && checkIfDateIsValid(args[5]) && checkIfTimeIsValid(args[4])
            && checkIfTimeIsValid(args[6])) {
      return true;
    } else {
      return false;
    }
  }

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
