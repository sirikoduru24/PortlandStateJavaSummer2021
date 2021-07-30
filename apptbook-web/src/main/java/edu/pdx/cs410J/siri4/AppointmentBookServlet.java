package edu.pdx.cs410J.siri4;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.
 */
public class AppointmentBookServlet extends HttpServlet
{
    static final String OWNER_PARAMETER = "owner";
    static final String DESCRIPTION_PARAMETER = "description";
    static final String START_PARAMETER = "start";
    static final String END_PARAMETER = "end";

    private final Map<String, AppointmentBook> book = new HashMap<>();

    /**
     * Handles HTTP GET requests. If the request contains owner, the code returns All Appointments for that owner.
     * If it has owner,start and end times returns all the appointments between the times.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String owner = getParameter( OWNER_PARAMETER, request );
        String start = getParameter(START_PARAMETER, request);
        String end = getParameter(END_PARAMETER, request);
        if (owner == null) {
            missingRequiredParameter(response,OWNER_PARAMETER);
        } else if(start == null & end == null) {
            getAppointmentsForOwner(owner,response);
        } else if(start == null) {
            missingRequiredParameter(response,START_PARAMETER);
        } else if (end == null) {
            missingRequiredParameter(response,END_PARAMETER);
        } else {
            getAppointmentsBetweenStartAndEnd(owner,start,end,response);
        }
    }

    /**
     * Handles a HTTP Post by adding Appointment to Appointment Book
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );
        String owner = getParameter(OWNER_PARAMETER, request);
        if(owner == null) {missingRequiredParameter(response, OWNER_PARAMETER);return;}
        String description = getParameter(DESCRIPTION_PARAMETER, request );
        if (description == null) { missingRequiredParameter(response, DESCRIPTION_PARAMETER);return; }
        String start = getParameter(START_PARAMETER, request );
        if ( start == null) { missingRequiredParameter( response, START_PARAMETER );return; }
        String end = getParameter(END_PARAMETER, request );
        if ( end == null) { missingRequiredParameter( response, END_PARAMETER );return; }

        AppointmentBook apptBook = this.book.get(owner);
        if(apptBook == null) {
             apptBook = new AppointmentBook(owner);
        }
        Appointment appt = new Appointment(new String[]{owner,description,start,end});
        PrintWriter pw = response.getWriter();

        if(appt.checkIfDatesAreCorrect()) {
            apptBook.addAppointment(appt);
            this.book.put(owner,apptBook);
            response.setStatus( HttpServletResponse.SC_OK);
            pw.println(Messages.addedContentsToAppointmentBook());
        } else {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            pw.println(Messages.malformedContentsToPost());
        }
        pw.flush();
    }

    /**
     * Handles an HTTP DELETE request by removing all Appointments.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        this.book.clear();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allDictionaryEntriesDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * method to get Appointments for a particular owner
     * @param owner
     * @param response
     * @throws IOException
     */
    private void getAppointmentsForOwner(String owner, HttpServletResponse response) throws IOException {
        AppointmentBook apptBook = getAppointmentBook(owner);
        if(apptBook == null) {
            String message = Messages.nonExistingAppointmentBook();
            response.sendError(HttpServletResponse.SC_NOT_FOUND,message);
        } else {
            PrintWriter writer = response.getWriter();
            TextDumper td = new TextDumper(writer);
            td.dump(apptBook);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * Method to get Appointments between start and end time for a owner.
     * @param owner
     * @param start
     * @param end
     * @param response
     * @throws IOException
     */
    private void getAppointmentsBetweenStartAndEnd(String owner, String start, String end, HttpServletResponse response) throws IOException {
        AppointmentBook apptBook = getAppointmentBook(owner);
        if(apptBook == null) {
            String message = Messages.nonExistingAppointmentBook();
            response.sendError(HttpServletResponse.SC_NOT_FOUND, message);
        } else {
            PrintWriter writer = response.getWriter();
            ValidateArguments val = new ValidateArguments();
            String[] startTime = start.split(" ");
            String[] endTime = end.split(" ");
            if(startTime.length > 3 || startTime.length < 3) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed start time entered for search");
                //writer.println("Malformed start time entered for search");
            } else if(endTime.length > 3 || endTime.length < 3) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed end time entered for search");
            } else {
                Date startDate = val.parseDate(start);
                Date endDate = val.parseDate(end);
                if(startDate == null) {response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed start time entered for search");
                }
                else if(endDate == null) {response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed end time entered for search"); }
                else {
                    AppointmentBook searchedAppointments = apptBook.searchBetween(startDate,endDate);
                    if(searchedAppointments == null) {
                        //writer.println("No Appointments found between the specified time");
                        response.sendError(HttpServletResponse.SC_ACCEPTED,"No Appointments found between specified times");
                    } else {
                        TextDumper td = new TextDumper(writer);
                        td.dump(searchedAppointments);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }

        }
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

    @VisibleForTesting
    AppointmentBook getAppointmentBook(String owner) {
        return this.book.get(owner);
    }

    /**
     * Method to create an Appointment Book
     * @param owner
     * @return
     */
    public AppointmentBook createAppointmentBook(String owner) {
        AppointmentBook apptBook = new AppointmentBook(owner);
        this.book.put(owner, apptBook);
        return apptBook;
    }
}
