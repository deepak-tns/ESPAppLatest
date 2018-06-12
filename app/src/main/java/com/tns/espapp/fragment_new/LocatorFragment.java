package com.tns.espapp.fragment_new;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tns.espapp.R;
import com.tns.espapp.TaxiTest;
import com.tns.espapp.activity.CurrentLocationActivity;
import com.tns.espapp.activity.LocationHistoryActivity;
import com.tns.espapp.activity.OPHistoryActivity;
import com.tns.espapp.activity.TaxiFormActivity;
import com.tns.espapp.activity.TaxiHistoryActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocatorFragment extends Fragment implements View.OnClickListener {

private RelativeLayout re_loctorfrag_taxiform,loctorfrag_taxihistory,loctorfrag_locationhistory,loctorfrag_livetaxi,loctorfrag_currentlocation;

    public LocatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_locator, container, false);
        findIDs(v);


        return v;
    }

    private void findIDs(View v) {
        re_loctorfrag_taxiform =(RelativeLayout)v.findViewById(R.id.loctorfrag_taxiform) ;
        re_loctorfrag_taxiform.setOnClickListener(this);
        loctorfrag_taxihistory =(RelativeLayout)v.findViewById(R.id.loctorfrag_taxihistory) ;
        loctorfrag_taxihistory.setOnClickListener(this);
        loctorfrag_locationhistory =(RelativeLayout)v.findViewById(R.id.loctorfrag_locationhistory) ;
        loctorfrag_locationhistory.setOnClickListener(this);
        loctorfrag_livetaxi =(RelativeLayout)v.findViewById(R.id.loctorfrag_livetaxi) ;
        loctorfrag_livetaxi.setOnClickListener(this);
        loctorfrag_currentlocation =(RelativeLayout)v.findViewById(R.id.loctorfrag_currentlocation) ;
        loctorfrag_currentlocation.setOnClickListener(this);
      ;
    }


    @Override
    public void onClick(View view) {
        if(view == re_loctorfrag_taxiform){
           // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag,new TaxiFormFragment()).addToBackStack(null).commit();
            startActivity(new Intent(getActivity(), TaxiFormActivity.class));

        }
        if(view == loctorfrag_taxihistory){
           // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag,new TaxiFormRecordFragment()).addToBackStack(null).commit();
            startActivity(new Intent(getActivity(), TaxiHistoryActivity.class));
        }
        if(view == loctorfrag_locationhistory){
          //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag,new LocationHistoryFragment()).addToBackStack(null).commit();
            startActivity(new Intent(getActivity(), LocationHistoryActivity.class));

        }
        if(view == loctorfrag_livetaxi){
           // getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag,new TaxiFormFragment()).addToBackStack(null).commit();
          getActivity().startActivity(new Intent(getActivity(), TaxiTest.class));
        }
        if(view == loctorfrag_currentlocation){
           // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag,new RouteMapFragment()).addToBackStack(null).commit();
            startActivity(new Intent(getActivity(), CurrentLocationActivity.class));
        }

    }
}
