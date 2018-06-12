package com.tns.espapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tns.espapp.R;
import com.tns.espapp.fragment.BlankFragment;
import com.tns.espapp.fragment.HomeFragment;
import com.tns.espapp.fragment.LocationHistoryFragment;
import com.tns.espapp.fragment.TaxiFormFragment;
import com.tns.espapp.fragment.TaxiFormRecordFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.tns.espapp.R.dimen.largeTextSize;

public class VehicleTrakerHomeActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerPane;
    private DrawerLayout mDrawerLayout;
    private Toolbar vehicleToolbar;
    private TextView VehicleTrakerTextView;
    private TextView LocationTextView;
    private TextView OPTextView;
    private TextView espTextView;
    private TextView taxiFormTextView;
    private TextView texiRecordTextView;
    private TextView LocationHistoryTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_traker_home);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        navigationdrawer();
        getLayoutsId();
        setOnClickListener();
        getSupportFragmentManager().beginTransaction().add(R.id.vechicleTrakerFragment, BlankFragment.newInstance(1)).commit();

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    private void navigationdrawer() {
        mDrawerPane = (LinearLayout) findViewById(R.id.drawerPane);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        vehicleToolbar = (Toolbar) findViewById(R.id.toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, vehicleToolbar,
                //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility


        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

    }

    private void getLayoutsId() {
        VehicleTrakerTextView = (TextView) findViewById(R.id.VechicleTrakerTextView);
        OPTextView = (TextView) findViewById(R.id.OPTextView);
        LocationTextView = (TextView) findViewById(R.id.LocationTextView);
        espTextView = (TextView) findViewById(R.id.tv_toolbar);
        taxiFormTextView = (TextView) findViewById(R.id.taxiForm);
        texiRecordTextView = (TextView) findViewById(R.id.taxiFormRecord);
        LocationHistoryTextView = (TextView) findViewById(R.id.LocationHistoryTextView);
        espTextView = (TextView) findViewById(R.id.tv_toolbar);
        VehicleTrakerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.largeTextSize));
    }

    private void setOnClickListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.tv_toolbar: {
                        Intent intent = new Intent(VehicleTrakerHomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.VechicleTrakerTextView: {

                    }
                    break;

                    case R.id.OPTextView: {
                        Intent intent = new Intent(VehicleTrakerHomeActivity.this, OPActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.LocationTextView: {
                        Intent intent = new Intent(VehicleTrakerHomeActivity.this, LocationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.taxiForm: {

                        getSupportFragmentManager().beginTransaction().replace(R.id.vechicleTrakerFragment, new TaxiFormFragment()).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(mDrawerPane);
                    }
                    break;
                    case R.id.taxiFormRecord: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.vechicleTrakerFragment, new TaxiFormRecordFragment()).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(mDrawerPane);
                    }
                    break;
                    case R.id.LocationHistoryTextView: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.vechicleTrakerFragment, new LocationHistoryFragment()).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(mDrawerPane);
                    }
                    break;

                }
            }
        };
        espTextView.setOnClickListener(clickListener);
        VehicleTrakerTextView.setOnClickListener(clickListener);
        LocationTextView.setOnClickListener(clickListener);
        taxiFormTextView.setOnClickListener(clickListener);
        texiRecordTextView.setOnClickListener(clickListener);
        OPTextView.setOnClickListener(clickListener);
        LocationHistoryTextView.setOnClickListener(clickListener);
    }
}
