package edu.pdx.cs410j.siri4;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrettyPrint extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        String owner = getIntent().getStringExtra("ownerName");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date beginDate = null;
        Date endingDate = null;
        if(startDate!= null && endDate != null) {
            try {
                beginDate = sdf.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                endingDate = sdf.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        TextView searchResults = findViewById(R.id.search_op_tv);
        try {
            String allResults = "";
            String filteredResults = "";
            String fileName = owner+".txt";
            File contextDirectory = getApplicationContext().getDataDir();
            File searchRes = new File(contextDirectory,fileName);
            if(!searchRes.exists()) {
                searchResults.setText("There exists no Appointments for the given owner");
            } else {
                BufferedReader br = new BufferedReader(new FileReader(searchRes));
                String line = br.readLine();
                int count = 1;
                if(startDate == null && endDate == null) {
                    allResults = allResults + "All Appointments for a owner:\n\n";
                    while(line!=null) {
                        String[] contents = line.split(",");
                        Appointment appointment = new Appointment(contents[0],contents[1],contents[2],contents[3]);
                        String res = printInFormat(appointment,count);
                        count++;
                        allResults = allResults+res+"\n";
                        line = br.readLine();
                    }
                    searchResults.setText(allResults);
                } else {
                    while(line != null) {
                        String[] contents = line.split(",");
                        Appointment appointment = new Appointment(contents[0],contents[1],contents[2],contents[3]);
                        Date date = appointment.getBeginTime();
                        if((date.equals(beginDate) || date.after(beginDate)) && (date.equals(endingDate) || date.before(endingDate))){
                            String res = printInFormat(appointment,count);
                            count++;
                            filteredResults = filteredResults + res;
                        }
                        line = br.readLine();
                    }
                    if(filteredResults == "") {
                        searchResults.setText("There are no appointments between the given times.\n" +
                                "Please search using another date and time");
                    }
                    searchResults.setText(filteredResults);
                }
                searchResults.setMovementMethod(new ScrollingMovementMethod());
            }
        } catch (FileNotFoundException e) {
            searchResults.setText("Error while reading contents of Appointment Book");
            e.printStackTrace();
        } catch (IOException e) {
            searchResults.setText("Error while reading contents of Appointment Book");
        }
    }

    private String printInFormat(Appointment appointment, int count) {
        String res = "";
        res = res+"Appointment - "+count+"\n";
        res = res + "--description: " +appointment.getDescription()+"\n"
                + "--duration:    " +appointment.duration()+" minutes"+"\n"
                + "--Start Date:  " + appointment.getBeginTime()+"\n"
                + "--End Date  :  " +appointment.getEndTime()+"."+"\n";

        return res;
    }
}
