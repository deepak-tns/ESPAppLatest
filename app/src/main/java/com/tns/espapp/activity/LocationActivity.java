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
import com.tns.espapp.fragment.LocationHistoryFragment;
import com.tns.espapp.fragment.RouteMapFragment;
import com.tns.espapp.fragment.TaxiFormFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.tns.espapp.R.dimen.largeTextSize;

public class LocationActivity extends AppCompatActivity {

    private LinearLayout mDrawerPane;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private TextView VehicleTrakerTextView;
    private TextView OPTextView;
    private TextView LocationTextView;
    private TextView espTextView;
    private TextView currentLocationTextView;
    private TextView locationHistoryTextView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        navigationdrawer();
        getLayoutsId();
        setOnClickListener();
        getSupportFragmentManager().beginTransaction().add(R.id.LocationFragment, BlankFragment.newInstance(1)).commit();
    }
    private void navigationdrawer() {
        mDrawerPane = (LinearLayout) findViewById(R.id.drawerPane);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar)findViewById(R.id.toolbar);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
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
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
    private void getLayoutsId()
    {
        VehicleTrakerTextView = (TextView)findViewById(R.id.VechicleTrakerTextView);
        OPTextView = (TextView)findViewById(R.id.OPTextView);
        LocationTextView = (TextView)findViewById(R.id.LocationTextView);
        espTextView = (TextView)findViewById(R.id.tv_toolbar);
        currentLocationTextView = (TextView)findViewById(R.id.currentLocationTextView);
        LocationTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(largeTextSize));

    }
    private void setOnClickListener()
    {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id)
                {
                    case R.id.tv_toolbar:
                    {
                        Intent intent = new Intent(LocationActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.VechicleTrakerTextView:
                    {
                        Intent intent = new Intent(LocationActivity.this,VehicleTrakerHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case R.id.currentLocationTextView:
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.LocationFragment, new RouteMapFragment()).addToBackStack(null).commit();
                        mDrawerLayout.closeDrawer(mDrawerPane);
                    }
                    break;

                    case R.id.LocationTextView:
                    {


                    }
                    break;
                    case R.id.OPTextView:
                    {
                        Intent intent = new Intent(LocationActivity.this,OPActivity.class);
                        startActivity(intent);
                        finish();
                    }


                    break;

                }
            }
        };
        espTextView.setOnClickListener(clickListener);
        VehicleTrakerTextView.setOnClickListener(clickListener);
        LocationTextView.setOnClickListener(clickListener);
        currentLocationTextView.setOnClickListener(clickListener);
        OPTextView.setOnClickListener(clickListener);
    }
}
