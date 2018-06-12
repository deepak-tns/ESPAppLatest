package com.tns.espapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tns.espapp.R;
import com.tns.espapp.fragment_new.OPEntryFragment_new;
import com.tns.espapp.fragment_new.OPEntryHistoryFragment_new;
import com.tns.espapp.fragment_new.OPEntryLocationHistoryFragment_new;

public class OPEntryLocationHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opentry_location_history);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.li_oplochistory,new OPEntryLocationHistoryFragment_new()).commit();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
