package edu.pdx.cs410J.siri4;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.
 */
public class AppointmentBookRestClient extends HttpRequestHelper {
  private static final String WEB_APP = "apptbook";
  private static final String SERVLET = "appointments";

  private final String url;


  /**
   * Creates a client to the appointment book REST service running on the given host and port
   *
   * @param hostName The name of the host
   * @param port     The port
   */
  public AppointmentBookRestClient(String hostName, int port) {
    this.url = String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET);
  }

  /**
   * This method helps to add an Appointment to Appointment Book invoking doGET method.
   * @param appointment
   * @throws IOException
   */
  public void addAppointment(Appointment appointment) throws IOException {
    Response response = postToMyURL(Map.of("owner",appointment.getOwner(),
            "description",appointment.getDescription(),
            "start",appointment.getBeginTimeString(),
            "end",appointment.getEndTimeString()));
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   *
   * This method gets all the Appointments from the Appointment Book invoking doGET method.
   * @param owner
   * @return
   * @throws IOException
   * @throws ParserException
   */
  public String getAllAppointmentBookEntries(String owner) throws IOException, ParserException {
    Response response = get(this.url, Map.of("owner",owner));
    throwExceptionIfNotOkayHttpStatus(response);
    String book = response.getContent();
    TextParser parser = new TextParser(new StringReader(book));
    AppointmentBook apptBook = parser.parse();
    PrettyPrint pretty = new PrettyPrint();
    String result = pretty.getPrettyAppointments(apptBook);
    return result;
  }

  /**
   * This method searches for All Appointments between the provided start and end times.
   * @param owner
   * @param start
   * @param end
   * @return
   * @throws IOException
   * @throws ParserException
   */
  public String getAllAppointmentsBetweenGivenTimes(String owner, String start, String end) throws IOException, ParserException {
    Response response = get(this.url,Map.of("owner",owner,
            "start",start,
            "end",end));
    //System.out.println("Response:"+response.getContent());
    if(response.getContent().equalsIgnoreCase("Accepted")) {
      return "There are no appointments between given time";
    }
    throwExceptionIfNotOkayHttpStatus(response);
    String book = response.getContent();
    TextParser parser = new TextParser(new StringReader(book));
    AppointmentBook apptBook = parser.parse();
    PrettyPrint pretty = new PrettyPrint();
    String result = pretty.getPrettyAppointments(apptBook);
    return result;
  }

  @VisibleForTesting
  Response postToMyURL(Map<String, String> dictionaryEntries) throws IOException {
    return post(this.url, dictionaryEntries);
  }

  public void removeAllDictionaryEntries() throws IOException {
    Response response = delete(this.url, Map.of());
    throwExceptionIfNotOkayHttpStatus(response);
  }

  private Response throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
    return response;
  }

}
