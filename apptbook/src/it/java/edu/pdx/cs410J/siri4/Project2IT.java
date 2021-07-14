package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project2} main class.
 */
class Project2IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project2} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain(Project2.class, args);
    }

    /**
     * Tests that invoking the main method with no arguments issues an error
     */
    @Test
    void invokingMainWithNoArgumentsHasExitCode1() {
        MainMethodResult result = invokeMain();
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithNoCommandLineArguments() {
        MainMethodResult result = invokeMain(Project2.class);
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MSG));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.MISSING_ARGS));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithMoreThanExpectedCommandLineArguments() {
        MainMethodResult result = invokeMain(Project2.class,"-print","textfile","file1.txt","abc","eadme","def","fgh","ghi","hij","klm","nop","qrs");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MSG));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.TOO_MANY_ARGS));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));

    }

    @Test
    void invokingMainWithReadMeAtOptionsPositionPrintsReadmeAndExits() {
        MainMethodResult result = invokeMain(Project2.class,"-readme");
        assertThat(result.getTextWrittenToStandardError(), containsString("This is a README file!"));
        assertThat(result.getExitCode(), IsEqual.equalTo(0));
    }

    @Test
    void invokingMainWithOnlyArgumentsNoOptions() {
        MainMethodResult result = invokeMain(Project2.class,"siri","description","5/7/2021","12:00","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("Successfully added appointment to Appointment Book"));
    }
    @Test
    void invokingMainWithCorrectNumberOfArgsButWrongDate() {
        MainMethodResult result = invokeMain(Project2.class,"siri","description","57/2021","12:00","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("Entered start date is invalid"));
    }
    @Test
    void invokingMainWithSameOptionPrintsErrorAndExits() {
        MainMethodResult result = invokeMain(Project2.class,"-print","-print","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("There are options which have been repeated more than once"));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));

    }

    @Test
    void invokingMainWithNoOptionsAndFewArgs() {
        MainMethodResult result = invokeMain(Project2.class,"siri","description","5/7/2021","16:00","5/7/2021");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.NO_OPTIONS_TOO_FEW_ARGS));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));

    }

    @Test
    void invokingMainWithNoOptionsAndManyArgs() {
        MainMethodResult result = invokeMain(Project2.class,"siri","chandana","meet me","7/15/2021","16:00","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.NO_OPTIONS_TOO_MANY_ARGS));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }
    @Test
    void invokingMainWithCorrectNumberOfArgsButInvalidInputs() {
        MainMethodResult result = invokeMain(Project2.class,"siri","chandana","17/15/2021","16:00","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }
    @Test
    void invokingMainWithOnlyPrintAndValidArgs() {
        MainMethodResult result = invokeMain(Project2.class,"-print","siri","description","5/7/2021","16:00","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardOut(), containsString("siri's appointment book with 1 appointments"));
    }

    @Test
    void invokingMainWithOnlyPrintAndFewArgs() {
        MainMethodResult result = invokeMain(Project2.class,"-print","siri","description","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.PRINT_TOO_FEW_ARGS));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithOnlyPrintAndManyArgs() {
        MainMethodResult result = invokeMain(Project2.class,"-print","siri","description","5/7/2021","16:00","5/7/2021","16:00","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.PRINT_TOO_MANY_ARGS));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithPrintOptionButNotInOptionsPosition() {
        MainMethodResult result = invokeMain(Project2.class,"siri","description","5/7/2021","-print","16:00","5/7/2021","16:00","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString("Expected options before arguments but found it differently"));
        assertThat(result.getTextWrittenToStandardError(),containsString(Project2.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithOnlyTextFileAndValidArgs() {
        String[] args = {"-textFile","src/file1.txt","siri","description","5/7/2021","16:00","5/7/2021","16:00"};
        Appointment cmd = new Appointment(args,1);
        MainMethodResult result = invokeMain(Project2.class,args);
        assertThat(result.getTextWrittenToStandardOut(),containsString(""));
    }

    @Test
    void invokingMainWithOnlyTextFileAndNameInvalid() {
        String[] args = {"-textFile","","siri","description","5/7/2021","16:00","5/7/2021","16:00"};
        MainMethodResult result = invokeMain(Project2.class,args);
        //assertThat(result.getTextWrittenToStandardOut(),containsString("siri"));
    }
    @Test
    void invokingMainWithOnlyTextFileAndLessArguments() {
        MainMethodResult result = invokeMain(Project2.class,"-textFile","src/file1.txt","description","5/7/2021","16:00","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.TOO_FEW_ARGS));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }
    @Test
    void invokingMainWithOnlyTextFileAndMoreArguments() {
        MainMethodResult result = invokeMain(Project2.class,"-textFile","src/file1.txt","siri","description","5/7/2021","16:00","5/7/2021","16:00","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.TOO_MANY_ARGS));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithOneOptionButInUnExpectedPosition() {
        MainMethodResult result = invokeMain(Project2.class,"src/file1.txt","siri","meet me","-textFile","5/7/2021","16:00","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(),containsString("Expected options to be before arguments but found it differently"));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithTwoOptionsAndLessArguments() {
        MainMethodResult result = invokeMain(Project2.class,"-print","-textfile","src/file1.txt","siri","meet me","5/7/2021","16:00","5/7/2021");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.TOO_FEW_ARGS_WITH_TWO_OPTNS));
    }
    @Test
    void invokingMainWithTwoOptionsAndMoreArguments() {
        MainMethodResult result = invokeMain(Project2.class,"-print","-textfile","src/file1.txt","siri","meet me","5/7/2021","16:00","5/7/2021","18:00","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.TOO_MANY_ARGS_WITH_TWO_OPTNS));
    }
    @Test
    void invokingMainWithTwoOptionsAndArgumentsCorrectly() {
        MainMethodResult result = invokeMain(Project2.class,"-print","-textfile","src/file1.txt","siri","meet me","5/7/2021","16:00","5/7/2021","18:00");
        assertThat(result.getTextWrittenToStandardOut(),containsString("The latest appointment information is"));
    }

    @Test
    void invokingMainWithTwoOptionsAndArgumentsInWrongOrder() {
        MainMethodResult result = invokeMain(Project2.class,"-textfile","-print","src/file1.txt","siri","meet me","5/7/2021","16:00","5/7/2021","18:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing file name"));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MSG));
    }
    @Test
    void invokingMainWithTwoOptionsAndPrintNotInCorrectPosition() {
        MainMethodResult result = invokeMain(Project2.class,"-textfile","src/file1.txt","siri","-print","meet me","5/7/2021","16:00","5/7/2021","18:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("Option print is not at correct position"));
    }
    @Test
    void invokingMainOnlyWithTwoOptionsAndNoArguments() {
        MainMethodResult result = invokeMain(Project2.class,"-textfile","src/file1.txt","-print");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.MISSING_ARGS));
    }

    @Test
    void invokingMainWithTwoOptionsButFewArgs() {
        MainMethodResult result = invokeMain(Project2.class,"-textfile","src/file1.txt","-print","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.TOO_FEW_ARGS_WITH_TWO_OPTNS));
    }

    @Test
    void invokingMainWithTwoOptionsButManyArgs() {
        MainMethodResult result = invokeMain(Project2.class,"-textfile","src/file1.txt","-print","siri","description","7/15/2021","12:00","7/15/2021","16:00","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project2.TOO_MANY_ARGS_WITH_TWO_OPTNS));
    }
    @Test
    void invokingMainWithTwoOptionsCorrectly() {
        MainMethodResult result = invokeMain(Project2.class,"-textfile","src/file1.txt","-print","siri","meet me","5/7/2021","16:00","5/7/2021","18:00");
        assertThat(result.getTextWrittenToStandardOut(), containsString("The latest appointment information is"));
    }
}