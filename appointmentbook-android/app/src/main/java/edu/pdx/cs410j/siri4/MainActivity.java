package edu.pdx.cs410j.siri4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottom_navigatin_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home2, R.id.addAppointments, R.id.search)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.readme_popup,null);
                View layoutView = findViewById(R.id.main_layout);
                Button closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);

                PopupWindow popupWindow = new PopupWindow(customView,RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextView popupText = customView.findViewById(R.id.popup_tv);
                String helpContents = "Hi, Welcome to Help Menu of Appointment Book Application.\n" +
                        "This Application has Three tabs namely Home, Add Appointments and Search.\n" +
                        "Home tab describes what each tab has.\n" +
                        "If you click on the Add Appointment tab, it facilitates with a form\n" +
                        "Here in this Add appointments section inorder to add an Appointment:\n" +
                        "Enter ownername(Any name), description(Should not be empty), start and end times in the formats specified(mm/dd/yyyy hh:mm am/pm).\n" +
                        "In the next tab i.e Search, if you want to get all the Appointments of a owner just enter owner name.\n" +
                        "If you want to get the Appointments between specific times enter the times specified in the format(mm/dd/yyyy hh:mm am/pm).";
                popupText.setText(helpContents);

                popupWindow.showAtLocation(layoutView, Gravity.CENTER, 0, 0);

                closePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                return true;
        }
        return true;
    }
}