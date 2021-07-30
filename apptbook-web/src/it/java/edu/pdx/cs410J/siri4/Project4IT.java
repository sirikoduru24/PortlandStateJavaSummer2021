package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.UncaughtExceptionInMain;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.apache.tools.ant.Project;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * An integration test for {@link Project4} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    void test0RemoveAllMappings() throws IOException {
      AppointmentBookRestClient client = new AppointmentBookRestClient(HOSTNAME, Integer.parseInt(PORT));
      client.removeAllDictionaryEntries();
    }

    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project4.class );
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.MISSING_ARGS));
    }


    @Test
    void invokingMainWithOnlyReadme() {
        MainMethodResult result = invokeMain(Project4.class, "-README");
        assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    }
    @Test
    void test2EmptyServer() {
        MainMethodResult result = invokeMain( Project4.class, "-host",HOSTNAME, "-port",PORT );
        assertThat(result.getTextWrittenToStandardError(), containsString("There are less than the number of arguments required to perform some operation"));
    }

    @Test
    void invokingMainWithNoHost() {
        MainMethodResult result = invokeMain(Project4.class, "-port","123");
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing host option"));
    }

    @Test
    void invokingMainWithNoPort() {
        MainMethodResult result = invokeMain(Project4.class, "-host","localhost");
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing port option"));
    }
    @Test
    void invokingMainWithOnlyHostAndPortAndOwnerName() {
        MainMethodResult result = invokeMain(Project4.class, "-host",HOSTNAME,"-port",PORT,"siri");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Appointments"));

    }
    @Test
    void invokingMainWithPrintAndSearch(){
        MainMethodResult result = invokeMain(Project4.class, "-host",HOSTNAME,"-port",PORT,"-print","-search");
        assertThat(result.getTextWrittenToStandardError(),containsString(Project4.PRINT_AND_SEARCH));
    }

    @Test
    void invokingMainWithMissingHOSTName() {
        MainMethodResult result = invokeMain(Project4.class, "-host","-port",PORT,"siri");
        assertThat(result.getTextWrittenToStandardError(),containsString("There are less than the number of arguments required to perform some operation"));
    }

    @Test
    void invokingMainWithInvalidPort() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port","sru","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.MISSING_PORT_NAME));
    }

    @Test
    void invokingMainWithInvalidHostName() {
        MainMethodResult result = invokeMain(Project4.class, "-host","-port",PORT,"sruu","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid host name"));
    }

    @Test
    void invokingMainWithNoOtherOptionsButAddingAppointment() {
        MainMethodResult result = invokeMain(Project4.class, "-host",HOSTNAME,"-port",PORT,"siri","lunch","7/15/2021","12:30","am","7/15/2021","12:35","am");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Successfully added "));
    }

    @Test
    void invokingMainWithNoOtherOptionsButIncorrectArgumentsOfSameLength() {
        MainMethodResult result = invokeMain(Project4.class, "-host",HOSTNAME,"-port",PORT,"siri","lunch","7/15/2021","12:30","am","7/15/2021","12:35","m");
        assertThat(result.getTextWrittenToStandardError(),containsString("Invalid arguments"));
    }

    @Test
    void invokingMainWithLessNumberOfArgsNoOptions() {
        MainMethodResult result = invokeMain(Project4.class, "-host",HOSTNAME,"-port",PORT,"siri","lunch","7/15/2021","12:30","am","7/15/2021","12:35");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.TOO_FEW_ARGS));
    }
    @Test
    void invokingMainWithMoreNumberOfArgsNoOptions() {
        MainMethodResult result = invokeMain(Project4.class, "-host",HOSTNAME,"-port",PORT,"siri","lunch","7/15/2021","12:30","am","7/15/2021","12:35","am","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.TOO_MANY_ARGS));
    }

    @Test
    void invokingMainWithOptionsBeforeArgsOnlyHANDP() {
        MainMethodResult result = invokeMain(Project4.class,"siri","sdf","sd","-host","-port","122");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.OPTNS_BEFORE_ARGS));
    }

    @Test
    void invokingMainWithPrint() {
        MainMethodResult result = invokeMain(Project4.class, "-print","-host",HOSTNAME,"-port",PORT);
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.TOO_FEW_ARGS_PRINT));
        MainMethodResult result1 = invokeMain(Project4.class, "-print","-host",HOSTNAME,"-port",PORT,"siri","description","7/15/2021","12:30","am","7/15/2021","12:45","am","siri");
        assertThat(result1.getTextWrittenToStandardError(),containsString(Project4.TOO_MANY_ARGS_PRINT));
        //adding appointment
        MainMethodResult result2 = invokeMain(Project4.class, "-print","-host",HOSTNAME,"-port",PORT,"siri","description","7/15/2021","12:30","am","7/15/2021","12:45","am");
        assertThat(result2.getTextWrittenToStandardOut(),containsString("The latest appointment"));
        MainMethodResult result3 = invokeMain(Project4.class,"abc","def","-host",HOSTNAME,"-port",PORT,"-print");
        assertThat(result3.getTextWrittenToStandardError(), containsString(Project4.OPTNS_BEFORE_ARGS));
    }

    @Test
    void invokeMainWithSearchOptions() {
        //less number of args with search
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"-search");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.TOO_FEW_ARGS_SEARCH));
        //more number of args
        MainMethodResult result1 = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"-search","siri","7/15/2021","12:30","am","7/15/2021","12:45","am","siri");
        assertThat(result1.getTextWrittenToStandardError(), containsString(Project4.TOO_MANY_ARGS_SEARCH));
        //equal number of args but invalid args
        MainMethodResult result2 = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"-search","siri","7/15/2021","12:30","am","7/152021","12:45","am");
        assertThat(result2.getTextWrittenToStandardError(), containsString("Invalid arguments"));
        //Options after args
        //MainMethodResult result3 = invokeMain(Project4.class, "siri","abc","sjdkf","-host",HOSTNAME,"-port",PORT,"-search");
        //assertThat(result3.getTextWrittenToStandardError(), containsString(Project4.OPTNS_BEFORE_ARGS));
        //search correct
        MainMethodResult result4 = invokeMain(Project4.class, "-search","-host",HOSTNAME,"-port",PORT,"siri","7/15/2021","12:30","am","7/15/2021","12:45","am");
        assertThat(result4.getTextWrittenToStandardOut(), containsString("Search Results:"));
    }
//
//    @Test
//    void test3NoDefinitionsThrowsAppointmentBookRestException() {
//        String word = "WORD";
//        try {
//            invokeMain(Project4.class, HOSTNAME, PORT, word);
//            fail("Expected a RestException to be thrown");
//
//        } catch (UncaughtExceptionInMain ex) {
//            RestException cause = (RestException) ex.getCause();
//            assertThat(cause.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_NOT_FOUND));
//        }
//    }
//
//    @Test
//    void test4AddDefinition() {
//        String word = "WORD";
//        String definition = "DEFINITION";
//
//        MainMethodResult result = invokeMain( Project4.class, HOSTNAME, PORT, word, definition );
//        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(0));
//        String out = result.getTextWrittenToStandardOut();
//        assertThat(out, out, containsString(Messages.definedWordAs(word, definition)));
//
//        result = invokeMain( Project4.class, HOSTNAME, PORT, word );
//        out = result.getTextWrittenToStandardOut();
//        assertThat(out, out, containsString(Messages.formatDictionaryEntry(word, definition)));
//
//        result = invokeMain( Project4.class, HOSTNAME, PORT );
//        out = result.getTextWrittenToStandardOut();
//        assertThat(out, out, containsString(Messages.formatDictionaryEntry(word, definition)));
//    }
}