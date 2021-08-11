package edu.pdx.cs410j.siri4;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        EditText ownerName = v.findViewById(R.id.search_owner_edt);
        EditText startDate = v.findViewById(R.id.beginDateedt);
        EditText endDate = v.findViewById(R.id.endDateedt);
        Button search = v.findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            String owner = null;
            String strtDate = null;
            String enDate = null;
            @Override
            public void onClick(View view) {
                owner = ownerName.getText().toString().trim();
                strtDate = startDate.getText().toString().trim();
                enDate = endDate.getText().toString().trim();
                if(validateName(ownerName,"Owner name")) {
                    if(strtDate.isEmpty() && enDate.isEmpty()) {
                        Intent intent = new Intent(getActivity(),PrettyPrint.class);
                        intent.putExtra("ownerName",owner);
                        startActivity(intent);
                        ownerName.setText("");
                    } else {
                        if(!strtDate.isEmpty() && enDate.isEmpty()) {
                            endDate.setError("End date cannot be empty when start date has been entered");
                        }
                        else if(!enDate.isEmpty() && strtDate.isEmpty()) {
                            startDate.setError("Start date cannot be empty when end date has been entered");
                        } else {
                            Intent intent = new Intent(getActivity(),PrettyPrint.class);
                            if(validateDate(startDate,"Start Date") && validateDate(endDate,"End Date")) {
                                Appointment appointment = new Appointment(owner,"desc",strtDate,enDate);
                                if(checkIfDatesAreAfterEachOther(appointment,startDate,endDate)) {
                                    intent.putExtra("ownerName",owner);
                                    intent.putExtra("startDate",strtDate);
                                    intent.putExtra("endDate",enDate);
                                    startActivity(intent);
                                    ownerName.setText("");
                                    startDate.setText("");
                                    endDate.setText("");
                                }
                            }

                        }
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