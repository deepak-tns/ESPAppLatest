package com.tns.espapp.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.tns.espapp.R;
import com.tns.espapp.adapter.ViewPagerAdapter;
import com.tns.espapp.fragment.BlankFragment;
import com.tns.espapp.fragment.FeedBackFragment;
import com.tns.espapp.fragment.OPEntryFragment;
import com.tns.espapp.fragment.PersonalInfoFragment;
import com.tns.espapp.fragment.ReadNotificationFragment;
import com.tns.espapp.fragment.RouteMapFragment;
import com.tns.espapp.fragment.TaxiFormFragment;
import com.tns.espapp.fragment.TaxiFragment;

public class NewHomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new BlankFragment(), "Esp");
        adapter.addFrag(new TaxiFormFragment(), "Taxi");
        adapter.addFrag(new FeedBackFragment(), "OP");
        viewPager.setAdapter(adapter);
    }
}
