package com.tns.espapp.fragment_new;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tns.espapp.R;

public class AttendanceRemoteFixHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_remote_fix_history);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

       String getname = getIntent().getStringExtra("value");

        if(getname.equalsIgnoreCase("Remote")){
            getSupportFragmentManager().beginTransaction().add(R.id.li_attendancehistory_frag,new AttendanceRemoteHistoryFragment()).commit();

        }
        if(getname.equalsIgnoreCase("Fixed")){
            getSupportFragmentManager().beginTransaction().add(R.id.li_attendancehistory_frag,new AttendanceFixHistoryFragment()).commit();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
