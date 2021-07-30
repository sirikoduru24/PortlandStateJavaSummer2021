package edu.pdx.cs410J.siri4;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AppointmentBookServletTest {

  @Test
  void gettingAppointmentBookReturnsTextFormat() throws ServletException, IOException {
    String owner = "Dave";
    String description = "have lunch";
    String start = "7/15/2021 12:30 am";
    String end = "7/15/2021 12:45 am";

    AppointmentBookServlet servlet = new AppointmentBookServlet();
    AppointmentBook book = servlet.createAppointmentBook(owner);
    book.addAppointment(new Appointment(new String[]{owner,description,start,end}));

    Map<String, String> queryParams = Map.of("owner", owner);
    StringWriter sw = invokeServletMethod(queryParams, servlet::doGet);

    String text = sw.toString();
    assertThat(text, containsString(owner));
  }

  @Test
  void getAppointmentThatDonotExist() throws ServletException, IOException {
    String owner = "sai";
    AppointmentBookServlet servlet = new AppointmentBookServlet();
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("owner")).thenReturn(owner);
    HttpServletResponse response = mock(HttpServletResponse.class);
    servlet.doGet(request,response);
    verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, Messages.nonExistingAppointmentBook());
  }

  @Test
  void searchAppointmentThatDonotExist() throws ServletException, IOException {
    String owner = "sai";
    AppointmentBookServlet servlet = new AppointmentBookServlet();
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("owner")).thenReturn(owner);
    when(request.getParameter("start")).thenReturn("7/15/2021 12:30 am");
    when(request.getParameter("end")).thenReturn("7/15/2021 12:45 am");
    HttpServletResponse response = mock(HttpServletResponse.class);
    servlet.doGet(request,response);
    verify(response).sendError(HttpServletResponse.SC_NOT_FOUND,Messages.nonExistingAppointmentBook());
  }

  @Test
  void missingOwnerParameter() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    servlet.doGet(request,response);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, Messages.missingRequiredParameter("owner"));
  }
  @Test
  void searchingForAppointments() throws ServletException, IOException {
    String owner = "Dave";
    String description = "have lunch";
    String start = "7/15/2021 12:30 am";
    String end = "7/15/2021 12:45 am";

    AppointmentBookServlet servlet = new AppointmentBookServlet();
    AppointmentBook book = servlet.createAppointmentBook(owner);
    book.addAppointment(new Appointment(new String[]{owner,description,start,end}));

    Map<String, String> queryParams = Map.of("owner", owner,"start",start,"end",end);
    StringWriter sw = invokeServletMethod(queryParams, servlet::doGet);

    String text = sw.toString();
    assertThat(text, containsString(owner));
  }

  private StringWriter invokeServletMethod(Map<String, String> params, ServletMethodInvoker invoker) throws IOException, ServletException {
    HttpServletRequest request = mock(HttpServletRequest.class);
    params.forEach((key, value) -> when(request.getParameter(key)).thenReturn(value));

    HttpServletResponse response = mock(HttpServletResponse.class);

    StringWriter sw = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(sw));

    invoker.invoke(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);
    return sw;
  }

  private interface ServletMethodInvoker {
    void invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
  }
//
//  @Test
//  void addingMalformedContents() throws ServletException, IOException {
//    AppointmentBookServlet servlet = new AppointmentBookServlet();
//    String owner = "Dave";
//    String description = "Teach Java";
//    String start = "7/15/2021 12:30 am";
//    String end = "7/152021 12:45 am";
//    HttpServletRequest request = mock(HttpServletRequest.class);
//    when(request.getParameter("owner")).thenReturn(owner);
//    when(request.getParameter("description")).thenReturn(description);
//    when(request.getParameter("start")).thenReturn(start);
//    when(request.getParameter("end")).thenReturn(end);
//    HttpServletResponse response = mock(HttpServletResponse.class);
//    servlet.doPost(request,response);
//    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED,Messages.malformedContentsToPost());
//
//  }

  @Test
  void addAppointment() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    String owner = "Dave";
    String description = "Teach Java";
    String start = "7/15/2021 12:30 am";
    String end = "7/15/2021 12:45 am";

    invokeServletMethod(Map.of("owner", owner, "description", description,"start",start,"end",end), servlet::doPost);

    AppointmentBook book = servlet.getAppointmentBook(owner);
    assertThat(book, notNullValue());
    assertThat(book.getOwnerName(), equalTo(owner));

    Collection<Appointment> appointments = book.getAppointments();
    assertThat(appointments, hasSize(1));

    Appointment appointment = appointments.iterator().next();
    assertThat(appointment.getDescription(), equalTo(description));

  }
}
