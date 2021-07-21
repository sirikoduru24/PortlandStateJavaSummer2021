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
 * Integration tests for the {@link Project3} main class.
 */
class Project3IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project3} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain(Project3.class, args);
    }

    /**
     * Tests that invoking the main method with no arguments issues an error
     */

    @Test
    void invokingMainWithNoCommandLineArguments() {
        MainMethodResult result = invokeMain(Project3.class);
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.MISSING_ARGS));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithMoreThanExpectedCommandLineArguments() {
        MainMethodResult result = invokeMain(Project3.class,"-print","textfile","file1.txt","pretty","-","abc","eadme","def","fgh","ghi","hij","klm","nop","qrs");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.TOO_MANY_ARGS));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));

    }

    @Test
    void invokingMainWithReadMeAtOptionsPositionPrintsReadmeAndExits() {
        MainMethodResult result = invokeMain(Project3.class,"-readme");
        assertThat(result.getTextWrittenToStandardError(), containsString("This is a README file!"));
        assertThat(result.getExitCode(), IsEqual.equalTo(0));
    }

    @Test
    void invokingMainWithOnlyArgumentsNoOptions() {
        MainMethodResult result = invokeMain(Project3.class,"siri","description","5/7/2021","12:00","am","5/7/2021","1:00","am");
        assertThat(result.getTextWrittenToStandardError(), containsString("Successfully added appointment to Appointment Book"));
    }

    @Test
    void invokingMainWithCorrectNumberOfArgsButWrongDate() {
        MainMethodResult result = invokeMain(Project3.class,"siri","description","57/2021","12:00","am","5/7/2021","1:00","am");
        assertThat(result.getTextWrittenToStandardError(), containsString("Entered start date is invalid"));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithSameOptionPrintsErrorAndExits() {
        MainMethodResult result = invokeMain(Project3.class,"-print","-print","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("There are options which have been repeated more than once"));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));

    }

    @Test
    void invokingMainWithNoOptionsAndFewArgs() {
        MainMethodResult result = invokeMain(Project3.class,"siri","description","5/7/2021","1:00","5/7/2021");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.NO_OPTIONS_TOO_FEW_ARGS));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithNoOptionsAndManyArgs() {
        MainMethodResult result = invokeMain(Project3.class,"siri","chandana","meet me","7/15/2021","16:00","5/7/2021","16:00","am","pm","am");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.NO_OPTIONS_TOO_MANY_ARGS));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithOnlyPrintAndValidArgs() {
        MainMethodResult result = invokeMain(Project3.class,"-print","siri","description","5/7/2021","1:00","am","5/7/2021","6:00","am");
        assertThat(result.getTextWrittenToStandardOut(), containsString("siri's appointment book with 1 appointments"));
    }

    @Test
    void invokingMainWithOnlyPrintAndFewArgs() {
        MainMethodResult result = invokeMain(Project3.class,"-print","siri","description","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.PRINT_TOO_FEW_ARGS));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithOnlyPrintAndManyArgs() {
        MainMethodResult result = invokeMain(Project3.class,"-print","siri","description","5/7/2021","1:00","am","5/7/2021","6:00","am","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.PRINT_TOO_MANY_ARGS));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithPrintOptionButNotInOptionsPosition() {
        MainMethodResult result = invokeMain(Project3.class,"siri","description","5/7/2021","-print","16:00","5/7/2021","16:00","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString("Expected options before arguments but found it differently"));
        assertThat(result.getTextWrittenToStandardError(),containsString(Project3.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithPrintAndCorrectNumberOfArgsButInvalid() {
        MainMethodResult result = invokeMain(Project3.class,"-print","siri","description","5/7/2021","1:00","am","5/7/2021","6:00","a");
        assertThat(result.getTextWrittenToStandardError(),containsString("Entered format for end time is invalid. Expected am/pm"));
        assertThat(result.getTextWrittenToStandardError(),containsString(Project3.USAGE_MSG));
        assertThat(result.getExitCode(),IsEqual.equalTo(1));

    }
    @Test
    void invokingMainWithOnlyTextFileAndValidArgs() {
        String[] args = {"-textFile","src/file1.txt","siri","description","5/7/2021","1:00","am","5/7/2021","6:00","am"};
        Appointment cmd = new Appointment(args,1);
        MainMethodResult result = invokeMain(Project3.class,args);
        assertThat(result.getTextWrittenToStandardOut(),containsString(""));
    }

    @Test
    void invokingMainWithOnlyTextFileAndNameInvalid() {
        String[] args = {"-textFile","","siri","description","5/7/2021","1:00","am","5/7/2021","6:00","am"};
        MainMethodResult result = invokeMain(Project3.class,args);
        //assertThat(result.getTextWrittenToStandardOut(),containsString("siri"));
    }

    @Test
    void invokingMainWithOnlyTextFileAndLessArguments() {
        MainMethodResult result = invokeMain(Project3.class,"-textFile","src/file1.txt","description","5/7/2021","1:00","am","5/7/2021","6:00");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.TEXT_FILE_TOO_FEW_ARGS));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }
    @Test
    void invokingMainWithOnlyTextFileOption() {
        MainMethodResult result = invokeMain(Project3.class,"-textFile");
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing text file name and command line arguments"));
    }
    @Test
    void invokingMainOnlyWithTextFileOptionAndFileName() {
        MainMethodResult result = invokeMain(Project3.class,"-textFile","src/file1");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.MISSING_ARGS));
    }
    @Test
    void invokingMainWithOnlyTextFileAndMoreArguments() {
        MainMethodResult result = invokeMain(Project3.class,"-textFile","src/file1.txt","siri","description","5/7/2021","1:00","am","5/7/2021","6:00","am","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.TEXT_FILE_TOO_MANY_ARGS));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithOneOptionButInUnExpectedPosition() {
        MainMethodResult result = invokeMain(Project3.class,"src/file1.txt","siri","meet me","-textFile","5/7/2021","16:00","5/7/2021","16:00");
        assertThat(result.getTextWrittenToStandardError(),containsString("Expected options to be before arguments but found it differently"));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result.getExitCode(), IsEqual.equalTo(1));
    }

    @Test
    void invokingMainWithOnlyTextFileAndCorrectNumberOfArgsButArgsInvalid() {
        String[] args = {"-textFile","src/file1.txt","siri","description","5/7/2021","1:00","ap","5/7/2021","6:00","am"};
        MainMethodResult result = invokeMain(Project3.class,args);
        assertThat(result.getTextWrittenToStandardError(),containsString("Entered format for start time is invalid. Expected am/pm"));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result.getExitCode(),IsEqual.equalTo(1));
    }
    @Test
    void invokingMainWithOnlyPretty() {
        MainMethodResult result = invokeMain(Project3.class,"-pretty");
        assertThat(result.getTextWrittenToStandardError(), containsString("Expected either - or text file name and command line arguments"));
    }

    @Test
    void invokingMainWithPrettyAndArgumentForPretty() {
        MainMethodResult result = invokeMain(Project3.class,"-pretty","-");
        assertThat(result.getTextWrittenToStandardError(),containsString(Project3.MISSING_ARGS));
    }
    @Test
    void invokingMainWithPrettyAndLessNumberOfArguments() {
        MainMethodResult result = invokeMain(Project3.class,"-pretty","-","siri");
        assertThat(result.getTextWrittenToStandardError(),containsString(Project3.PRETTY_TOO_FEW_ARGS));
    }
    @Test
    void invokingMainWithPrettyAndManyArgs() {
        MainMethodResult result = invokeMain(Project3.class,"-pretty","-","siri","chandana","7/15/2021","12:30","am","7/15/2021","1:30","am","siri");
        assertThat(result.getTextWrittenToStandardError(),containsString(Project3.PRETTY_TOO_MANY_ARGS));
    }

    @Test
    void invokingMainWithPrettyAndCorrectNumberOfArgs() {
        MainMethodResult result = invokeMain(Project3.class,"-pretty","-","siri","description","7/15/2021","12:30","am","7/15/2021","1:30","am");
        assertThat(result.getExitCode(),IsEqual.equalTo(0));
    }

    @Test
    void invokingMainWithPrettyAndFileNameAndCorrectArgs() {
        MainMethodResult result = invokeMain(Project3.class,"-pretty","src/file2","siri","description","7/15/2021","12:30","am","7/15/2021","1:30","am");
        assertThat(result.getExitCode(),IsEqual.equalTo(0));
    }

    @Test
    void invokingMainWithPrettyAndCorrectNumberOfArgsButInvalid() {
        MainMethodResult result = invokeMain(Project3.class,"-pretty","src/file2","siri","description","7/15/2021","12:30","a","7/15/2021","1:30","am");
        assertThat(result.getTextWrittenToStandardError(),containsString(Project3.USAGE_MSG));
    }

    @Test
    void invokingMainWithPrettyAndIncorrectPositionForPretty() {
        MainMethodResult result = invokeMain(Project3.class,"abc","-pretty","siri","description","7/15/2021","12:30","a","7/15/2021","1:30","am");
        assertThat(result.getTextWrittenToStandardError(),containsString("Expected options to be before arguments but found it differently"));
    }

    @Test
    void invokingMainWithTxtFileAndPrintOptionsAndLessArguments() {
        MainMethodResult result = invokeMain(Project3.class,"-print","-textfile","src/file1.txt","siri","meet me","5/7/2021","16:00","5/7/2021");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.TOO_FEW_ARGS_WITH_PRINT_TXTFILE));
    }

    @Test
    void invokingMainWithPrintAndTextFileWithMoreArguments() {
        MainMethodResult result = invokeMain(Project3.class,"-print","-textfile","src/file1.txt","siri","meet me","5/7/2021","1:00","am","5/7/2021","8:00","am","siri");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.TOO_MANY_ARGS_WITH_PRINT_TXTFILE));
    }
    @Test
    void invokingMainWithPrintAndTextFileArgumentsCorrectly() {
        MainMethodResult result = invokeMain(Project3.class,"-print","-textfile","src/file1.txt","siri","meet me","5/7/2021","1:00","am","5/7/2021","8:00","am");
        assertThat(result.getTextWrittenToStandardOut(),containsString("The latest appointment information is"));
    }

    @Test
    void invokingMainWithPrintAndTextFileArgumentsInWrongOrder() {
        MainMethodResult result = invokeMain(Project3.class,"-textfile","-print","src/file1.txt","siri","meet me","5/7/2021","1:00","am","5/7/2021","8:00","am");
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing text file name"));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
    }

    @Test
    void invokingMainOnlyWithPrintAndTextFileAndNoArguments() {
        MainMethodResult result = invokeMain(Project3.class,"-textfile","src/file1.txt","-print");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project3.MISSING_ARGS));
    }

    @Test
    void invokingMainWithPrintAndPrettyOnly() {
        MainMethodResult result = invokeMain(Project3.class,"-print","-pretty");
        assertThat(result.getTextWrittenToStandardError(),containsString("Missing text file name or '-' input for pretty and command line arguments"));
        MainMethodResult result1 = invokeMain(Project3.class,"-pretty","-print");
        assertThat(result1.getTextWrittenToStandardError(),containsString("Expected either - or textfile, but found others"));
    }

    @Test
    void invokingMainWithOnlyPrettyAndPrintAndLessAndMoreNumberOfArgs() {
        MainMethodResult result = invokeMain(Project3.class,"-print","-pretty","-");
        assertThat(result.getTextWrittenToStandardError(),containsString(Project3.MISSING_ARGS));
        MainMethodResult result1 = invokeMain(Project3.class,"-print","-pretty","-","siri");
        assertThat(result1.getTextWrittenToStandardError(),containsString(Project3.TOO_FEW_ARGS_WITH_PRINT_PRETTY));
        MainMethodResult result2 = invokeMain(Project3.class,"-print","-pretty","-","siri","desc","7/15/2021","12:30","am","7/15/2021","1:30","am","fred");
        assertThat(result2.getTextWrittenToStandardError(),containsString(Project3.TOO_MANY_ARGS_WITH_PRINT_PRETTY));
        MainMethodResult result3 = invokeMain(Project3.class,"abc","-pretty","-print");
        assertThat(result3.getTextWrittenToStandardError(),containsString("Expected options in the first of arguments list but found differently"));
    }
    @Test
    void invokingMainWithPrintAndPrettyAndCorrectNumberOfArgs(){
        MainMethodResult result = invokeMain(Project3.class,"-print","-pretty","-","siri","hello","7/15/2021","12:30","am","7/15/2021","12:50","am");
        assertThat(result.getTextWrittenToStandardOut(),containsString("The latest appointment information is"));
        assertThat(result.getTextWrittenToStandardOut(),containsString("Pretty Printing to Console...."));
        //-pretty with file
        MainMethodResult result1 = invokeMain(Project3.class,"-print","-pretty","src/file2","siri","hello","7/15/2021","12:30","am","7/15/2021","12:50","am");
        assertThat(result1.getTextWrittenToStandardOut(),containsString("The latest appointment information is"));
        //-pretty with correct number of args but invalid arg
        MainMethodResult result2 = invokeMain(Project3.class,"-print","-pretty","-","siri","hello","715/2021","12:30","am","7/15/2021","12:50","am");
        assertThat(result2.getTextWrittenToStandardError(),containsString("Entered start date is invalid"));
        assertThat(result2.getTextWrittenToStandardError(),containsString(Project3.USAGE_MSG));
    }

    @Test
    void invokingMainWithOnlyTextFileAndPrettyWithLessOrMoreArgs() {
        MainMethodResult result = invokeMain(Project3.class,"-textFile","-pretty");
        assertThat(result.getTextWrittenToStandardError(),containsString("Missing text file for -textFile option"));
        MainMethodResult result1 = invokeMain(Project3.class,"-pretty","-textFile");
        assertThat(result1.getTextWrittenToStandardError(),containsString("Missing '-' or text file for -pretty option"));
        MainMethodResult result2 = invokeMain(Project3.class,"-textFile","src/file1","-pretty");
        assertThat(result2.getTextWrittenToStandardError(),containsString(Project3.MISSING_ARGS));
        MainMethodResult result3 = invokeMain(Project3.class,"abc","-pretty","-textFile");
        assertThat(result3.getTextWrittenToStandardError(),containsString("Expected options in the first of arguments list but found differently"));
        MainMethodResult result4 = invokeMain(Project3.class,"-pretty","-","-textFile","siri","desc");
        assertThat(result4.getTextWrittenToStandardError(),containsString(Project3.TOO_FEW_ARGS_WITH_PRETTY_TXTFILE));
        MainMethodResult result5 = invokeMain(Project3.class,"-pretty","-","-textFile","src/file1","siri","desc","7/15/2021","12:30","am","7/15/2021","1:00","am","siri");
        assertThat(result5.getTextWrittenToStandardError(),containsString(Project3.TOO_MANY_ARGS_WITH_PRETTY_TXTFILE));
    }

    @Test
    void invokeMainWithPrettyAndTextFileWithValidNumberOfArgs() {
        MainMethodResult result = invokeMain(Project3.class,"-pretty","-","-textFile","src/file1","siri","desc","7/15/2021","12:30","am","7/15/2021","1:00","am");
        assertThat(result.getExitCode(),IsEqual.equalTo(0));
        MainMethodResult result1 = invokeMain(Project3.class,"-pretty","src/file2","-textFile","src/file1","siri","desc","7/15/2021","12:30","am","7/15/2021","1:00","am");
        assertThat(result1.getExitCode(),IsEqual.equalTo(0));
        MainMethodResult result2 = invokeMain(Project3.class,"-pretty","-","-textFile","src/file1","","desc","7/15/2021","12:30","am","7/15/2021","1:00","am");
        assertThat(result2.getTextWrittenToStandardError(),containsString("Entered an invalid name"));
        assertThat(result2.getTextWrittenToStandardError(),containsString(Project3.USAGE_MSG));
        MainMethodResult result3 = invokeMain(Project3.class,"-pretty","src/file1","-textFile","src/file1","siri","desc","7/15/2021","12:30","am","7/15/2021","1:00","am");
        assertThat(result3.getTextWrittenToStandardError(),containsString("TextFiles for -textFile and -pretty cannot be the same"));
    }

    @Test
    void invokingMainWithThreeOptionsWithSomeErrorInOptionsPositionsAndLessArgs() {
        MainMethodResult result = invokeMain(Project3.class, "abc","-print","-textFile","-pretty","adf");
        assertThat(result.getTextWrittenToStandardError(), containsString("Expected options before arguments but found differently"));
        MainMethodResult result1 = invokeMain(Project3.class, "-print","-pretty","-textFile","adf");
        assertThat(result1.getTextWrittenToStandardError(), containsString("Missing textfile or - input for -pretty option"));
        MainMethodResult result2 = invokeMain(Project3.class, "-print","-textFile","-pretty","adf");
        assertThat(result2.getTextWrittenToStandardError(),containsString("Missing textfile for -textFile option"));
        MainMethodResult result3 = invokeMain(Project3.class, "-print","abc","-textFile","-pretty","adf");
        assertThat(result3.getTextWrittenToStandardError(),containsString("Some errors have been found in command line arguments in options format"));
        MainMethodResult result4 = invokeMain(Project3.class, "-print","-textFile","abc","-pretty");
        assertThat(result4.getTextWrittenToStandardError(), containsString("and missing command line arguments"));
        MainMethodResult result5 = invokeMain(Project3.class, "-print","-textFile","abc","-pretty","abc");
        assertThat(result5.getTextWrittenToStandardError(),containsString(Project3.MISSING_ARGS));
        MainMethodResult result6 = invokeMain(Project3.class, "-print","-textFile","abc","-pretty","abc","def");
        assertThat(result6.getTextWrittenToStandardError(), containsString(Project3.TOO_FEW_ARGS_WITH_THREE_OPTNS));
        MainMethodResult result7 = invokeMain(Project3.class,"abc","abc","asd","-print","-textFile","-pretty");
        assertThat(result7.getTextWrittenToStandardError(),containsString("Expected options before arguments but found it differently"));
    }

    @Test
    void invokingMainWithCorrectNumberOfArgumentsAndThreeOptionsValidAndInvalidCases() {
        MainMethodResult result = invokeMain(Project3.class,"-print","-textFile","src/file1","-pretty","src/file1","siri","desc","7/15/2021","12:30","am","7/15/2021","1:30","am");
        assertThat(result.getTextWrittenToStandardError(),containsString("File names for -textfile option and -pretty option cannot be the same"));
        MainMethodResult result1 = invokeMain(Project3.class,"-print","-textFile","src/file1","-pretty","src/file2","siri","desc","7/15/2021","12:30","am","7/15/2021","1:30","am");
        assertThat(result1.getExitCode(),IsEqual.equalTo(0));
        MainMethodResult result2 = invokeMain(Project3.class,"-print","-textFile","src/file1","-pretty","-","siri","desc","7/15/2021","12:30","am","7/15/2021","1:30","am");
        assertThat(result2.getExitCode(),IsEqual.equalTo(0));
        MainMethodResult result3 = invokeMain(Project3.class,"-print","-textFile","src/file1","-pretty","-","siri","","7/15/2021","12:30","am","7/15/2021","1:30","am");
        assertThat(result3.getTextWrittenToStandardError(),containsString("Entered an invalid description"));
        assertThat(result3.getTextWrittenToStandardError(), containsString(Project3.USAGE_MSG));
        assertThat(result3.getExitCode(),IsEqual.equalTo(1));
    }
}