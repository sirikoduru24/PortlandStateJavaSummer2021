package edu.pdx.cs410J.siri4;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project1} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project1.class, args );
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
    MainMethodResult result = invokeMain(Project1.class);
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.USAGE_MSG));
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.MISSING_ARGS));
  }

  @Test
  void invokingMainWithOnlyReadMe() {
    MainMethodResult result = invokeMain(Project1.class, "-readme");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.README_TEXT));
    assertThat(result.getExitCode(), IsEqual.equalTo(1));
  }

  @Test
  void invokingMainWithOnlyPrint() {
    MainMethodResult result = invokeMain(Project1.class, "-print");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing arguments to perform print"));
    assertThat(result.getExitCode(), IsEqual.equalTo(1));
  }
  @Test
  void invokingMainOnlyWithOneArgument() {
    MainMethodResult result = invokeMain(Project1.class, "siri");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.USAGE_MSG));
  }
  @Test
  void invokingMainWithReadmeAtOptionsPositionPrintsReadMEAndExits() {
    MainMethodResult result = invokeMain(Project1.class, "-print","-ReadMe","siri","meet Lisa", "Have dinner","7/15/2021", "14:39","06/2/2021","1:03");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.README_TEXT));
    assertThat(result.getExitCode(), IsEqual.equalTo(1));
  }

  @Test
  void invokingMainWithTooManyArgumentsAndNoOptions() {
    MainMethodResult result = invokeMain(Project1.class, "siri","siri","meet Lisa", "lunch","Have dinner","7/15/2021", "14:39","06/2/2021","1:03");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.TOO_MANY_ARGS_NO_OPTIONS));
    assertThat(result.getTextWrittenToStandardError(),containsString(Project1.USAGE_MSG));
  }

  @Test
  void invokingMainWithTooManyArgumentsAndOptions() {
    MainMethodResult result = invokeMain(Project1.class, "-print","eadme","Lisa","lunch","Have dinner","7/15/2021", "14:39","06/2/2021","1:03");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.TOO_MANY_ARGS));
    assertThat(result.getTextWrittenToStandardError(),containsString(Project1.USAGE_MSG));
  }

  @Test
  void invokingMainWithLessArgumentsAndNoOptions() {
    MainMethodResult result = invokeMain(Project1.class, "-prt", "siri", "Lisa", "lunch", "Have dinner", "7/15/2021", "14:39");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.TOO_MANY_ARGS_NO_OPTIONS));
  }

  @Test
  void invokingMainWithLessNumberOfArgumentsThanExpected() {
    MainMethodResult result = invokeMain(Project1.class, "siri", "Lisa", "lunch", "Have dinner");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.INCORRECT_OPTIONS_FEW_ARGUMENTS));
  }

  @Test
  void invokingMainWithTwoArguments() {
    MainMethodResult result = invokeMain(Project1.class, "-print","siri");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing description"));
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.TOO_FEW_ARGUMENTS_OPTIONS_PRESENT));
  }

  @Test
  void invokingMainWithThreeArguments() {
    MainMethodResult result = invokeMain(Project1.class, "-print","siri","meet me");
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing start date"));
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.TOO_FEW_ARGUMENTS_OPTIONS_PRESENT));
  }

  @Test
  void invokingMainWithFourArguments() {
    MainMethodResult result = invokeMain(Project1.class, "-print","siri", "meet me", "7/15/2021");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.TOO_FEW_ARGUMENTS_OPTIONS_PRESENT));
  }

  @Test
  void invokingMainWithFiveArguments() {
    MainMethodResult result = invokeMain(Project1.class, "-print","siri","meet me", "7/15/2021", "1:39");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.TOO_FEW_ARGUMENTS_OPTIONS_PRESENT));
  }

  @Test
  void invokingMainWithSixArguments() {
    MainMethodResult result = invokeMain(Project1.class, "-print","siri","meet me", "7/15/2021", "1:39", "7/15/2021");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.TOO_FEW_ARGUMENTS_OPTIONS_PRESENT));
  }

  @Test
  void invokingMainWithCorrectNumberOfArgumentsButNoOptions() {
    MainMethodResult result = invokeMain(Project1.class, "-prt", "siri", "Have dinner", "7/15/2021", "14:39", "7/15/2021", "14:39");
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.TOO_MANY_ARGS_NO_OPTIONS));
    assertThat(result.getTextWrittenToStandardError(),containsString(Project1.USAGE_MSG));
  }

  @Test
  void invokingMainWithCorrectArguments() {
    MainMethodResult result = invokeMain(Project1.class, "-print", "siri", "Have dinner", "7/15/2021", "8:39", "7/15/2021", "14:39");
    assertThat(result.getTextWrittenToStandardError(), containsString("Have dinner from 7/15/2021 8:39 until 7/15/2021 14:39"));
  }

  @Test
  void invokingMainWithInvalidOwnerName() {
    MainMethodResult result = invokeMain(Project1.class, "-print", "       ", "Have dinner", "7/15/2021", "8:39", "7/15/2021", "14:39");
    assertThat(result.getTextWrittenToStandardError(), containsString("Entered owner name is empty. Expected an owner consisting of any characters including numbers"));
  }

  @Test
  void invokingMainWithInvalidDescription() {
    MainMethodResult result = invokeMain(Project1.class, "-print", "siri", "", "7/15/2021", "8:39", "7/15/2021", "14:39");
    assertThat(result.getTextWrittenToStandardError(), containsString("Entered description is empty. Expected a description consisting of any characters including numbers"));
  }

  @Test
  void invokingMainWithInvalidStartDate() {
    MainMethodResult result = invokeMain(Project1.class, "-print", "siri", "Having dinner with Lisa", "17/15/2021", "8:39", "7/15/2021", "14:39");
    assertThat(result.getTextWrittenToStandardError(), containsString("Entered start date is invalid"));
  }

  @Test
  void invokingMainWithInvalidStartDate1() {
    MainMethodResult result = invokeMain(Project1.class, "-print", "siri", "Having dinner with Lisa", "17152021", "8:39", "7/15/2021", "14:39");
    assertThat(result.getTextWrittenToStandardError(), containsString("Entered start date is invalid"));
  }

  @Test
  void invokingMainWithInvalidStartTime() {
    MainMethodResult result = invokeMain(Project1.class, "-print", "siri", "Having dinner with Lisa", "7/15/2021", "28:39", "7/15/2021", "14:39");
    assertThat(result.getTextWrittenToStandardError(), containsString("Entered start time is invalid"));
  }

  @Test
  void invokingMainWithInvalidEndDate() {
    MainMethodResult result = invokeMain(Project1.class, "-print", "siri", "Having dinner with Lisa", "7/15/2021", "8:39", "17/15/2021", "14:39");
    assertThat(result.getTextWrittenToStandardError(), containsString("Entered end date is invalid"));
  }

  @Test
  void invokingMainWithInvalidEndTime() {
    MainMethodResult result = invokeMain(Project1.class, "-print", "siri", "Having dinner with Lisa", "7/15/2021", "8:39", "7/15/2021", "2439");
    assertThat(result.getTextWrittenToStandardError(), containsString("Entered end time is invalid"));
  }
}