package edu.pdx.cs410j.siri4;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAppointments extends Fragment{
    public AddAppointments() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_appointments, container, false);

        v.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    getActivity().dispatchTouchEvent(event);
                }
                return true;
            }
        });

        EditText ownerName = v.findViewById(R.id.owner_name_edt);
        EditText description = v.findViewById(R.id.description_edt);
        EditText beginDate = v.findViewById(R.id.beginDate_edt);
        EditText endDate = v.findViewById(R.id.endDate_edt);
        CheckBox print = v.findViewById(R.id.print);
        TextView latestPrint = v.findViewById(R.id.print_latest_tv);
        Button btn = v.findViewById(R.id.add_appointments);
        btn.setOnClickListener(new View.OnClickListener() {
            String owner = null;
            String desc = null;
            String begin = null;
            String end = null;

            @Override
            public void onClick(View view) {
                owner = ownerName.getText().toString().trim();
                desc = description.getText().toString().trim();
                begin = beginDate.getText().toString().trim();
                end = endDate.getText().toString().trim();
                if(validateName(ownerName,"Owner Name") & validateName(description, "Description") & validateDate(beginDate,"Begin Date") & validateDate(endDate, "End Date")) {
                    Appointment appointment = new Appointment(owner, desc, begin, end);
                    if (checkIfDatesAreAfterEachOther(appointment, beginDate, endDate)) {
                        if(print.isChecked()) {
                            System.out.println(appointment.toString());
                            latestPrint.setText("The Latest Appointment Information is:\n" +
                                    appointment.toString());
                            latestPrint.postDelayed(new Runnable() {
                                public void run() {
                                    latestPrint.setVisibility(View.INVISIBLE);
                                }
                            }, 2000);

                        }
                        AppointmentBook appointmentBook = new AppointmentBook(owner);
                        appointmentBook.addAppointment(appointment);
                        //   System.out.println(appointmentBook);
                        //write to file
                        try {
                            String fileName = owner + ".txt";
                            File contextDirectory = getActivity().getApplicationContext().getDataDir();
                            File apptsFile = new File(contextDirectory, fileName);
                            PrintWriter pw = new PrintWriter(new FileWriter(apptsFile, true));
                            pw.println(appointment.getOwner()+","+ appointment.getDescription() + "," + appointment.getBeginTimeString() + "," + appointment.getEndTimeString());
                            pw.flush();
                        } catch (FileNotFoundException e) {
                            Toast.makeText(getActivity(), "Error occured while saving contents to Appointment Book", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Error occured while saving contents to Appointment Book", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity(), "Successfully added Appointment to Appointment Book", Toast.LENGTH_LONG).show();
                        ownerName.setText("");
                        description.setText("");
                        beginDate.setText("");
                        endDate.setText("");
                        print.setChecked(false);
                    }
                }
            }
        });
        return v;
    }

    public Boolean validateName(EditText name, String field) {
        String text = name.getText().toString();
        if(text.isEmpty()) {name.setError(field+"cannot be empty"); return false;}
        return true;
    }

    public Boolean validateDate(EditText date, String field) {
        String inputDate = date.getText().toString();
        String[] input = inputDate.split(" ");
        if(inputDate.isEmpty()) {date.setError(field+"cannot be empty");return false;}
        if(input.length != 3) {date.setError(field+"Should be of the format mm/dd/yyyy hh:mm am/pm");return false;}
        else {
            if(input.length == 3 & checkIfDateIsValid(input[0]) & checkIfTimeIsValid(input[1]) & checkIfTimeFormatIsValid(input[2])) {
                return true;
            }
            else {
                date.setError("Error in "+field+".\n Expected in the format mm/dd/yyyy hh:mm am/pm");
                return false;
            }
        }
    }

    public static boolean checkIfDateIsValid(String date) {
        try {
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("M/d/uuuu")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    public static boolean checkIfTimeIsValid(String time) {
        String regex = "^([0]?[1-9]|1[0-2]):[0-5][0-9]$";
        if(Pattern.matches(regex, time)) { return true; } else { return false; }
    }

    public static boolean checkIfTimeFormatIsValid(String format) {
        String regex = "^[AaPp][Mm]$";
        if(Pattern.matches(regex, format)) {return true;} else {return false;}
    }
    public static boolean checkIfDatesAreAfterEachOther(Appointment appointment, EditText begin, EditText end) {
        Date beginTime = appointment.getBeginTime();
        Date endTime = appointment.getEndTime();
        if(beginTime.after(endTime) || beginTime.equals(endTime)) {
            System.err.println("The inputted end time is equal to or before the start time.");
            begin.setError("The inputted end time is equal to or before the start time.");
            end.setError("The inputted end time is equal to or before the start time.");
            return false;
        }
        return true;
    }
}