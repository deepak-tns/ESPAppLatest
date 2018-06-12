package com.tns.espapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.tns.espapp.R;

public class CreateTemplateActivity extends AppCompatActivity {

    private Spinner formListSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_template);
        getLayoutsId();
    }
    private void getLayoutsId()
    {

        formListSpinner = (Spinner)findViewById(R.id.formListSpinner);
    }
}
