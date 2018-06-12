package com.tns.espapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tns.espapp.AppConstraint;
import com.tns.espapp.BusinessIntelligence.BusinessIntelligenceFragment;
import com.tns.espapp.DataModel;
import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.adapter.ViewPagerAdapter;
import com.tns.espapp.fragment_new.LocatorFragment;
import com.tns.espapp.fragment_new.OPEntryFragment_new;
import com.tns.espapp.fragment_new.PresenceFragment;
import com.tns.espapp.fragment_new.SiteInfoFragment;
import com.tns.espapp.service.GPSTracker;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private TextView welcomeTextView;
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean fragmentVisible;
    private TextView textView;
    private BroadcastReceiver mReceiver;
    private SharedPreferenceUtils sharedPreferences;

    private int notificationCounter;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(int index) {
        HomeFragment f = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new ReadNotificationFragment(), "Notification");
        adapter.addFrag(new BlankFragment(), "ESP");
        adapter.addFrag(new LocatorFragment(), "Locator");
        adapter.addFrag(new PresenceFragment(), "Presence");
        adapter.addFrag(new SiteInfoFragment(), "SiteInfo");
        adapter.addFrag(new BusinessIntelligenceFragment(), "BI");
        // adapter.addFrag(new FeedBackFragment(), "FeedBack");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of HomeFragment");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of HomeFragment");
        super.onPause();
        fragmentVisible = false;
    }
}
