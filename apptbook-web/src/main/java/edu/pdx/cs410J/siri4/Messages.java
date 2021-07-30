package edu.pdx.cs410J.siri4;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{

    public static String nonExistingAppointmentBook() {
        return String.format("Queried for a non-existing Appointment");
    }
    public static String malformedContentsToPost() {
        return String.format("Entered malformed contents to post");
    }
    public static String addedContentsToAppointmentBook() {
        return String.format("Added contents to Appointment Book");
    }

    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public static String allDictionaryEntriesDeleted() {
        return "All dictionary entries have been deleted";
    }
}
