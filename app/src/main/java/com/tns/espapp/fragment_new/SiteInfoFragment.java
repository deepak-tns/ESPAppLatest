package com.tns.espapp.fragment_new;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tns.espapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiteInfoFragment extends Fragment implements View.OnClickListener{
    
    private RelativeLayout re_siteinfo ,re_checklist,re_siteinfo_history,re_checklisthistory,re_siteinfo_fixform;

    public SiteInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_site_info, container, false);
         findIDs(v);

        return v;
    }

    private void findIDs(View v) {
        re_siteinfo =(RelativeLayout)v.findViewById(R.id.re_siteinfo);
        re_checklist =(RelativeLayout)v.findViewById(R.id.re_checklist);
        re_siteinfo_history =(RelativeLayout)v.findViewById(R.id.re_siteinfo_history);
        re_checklisthistory =(RelativeLayout)v.findViewById(R.id.re_checklisthistory);
        re_siteinfo_fixform =(RelativeLayout)v.findViewById(R.id.re_siteinfo_fixform);

        re_siteinfo.setOnClickListener(this);
        re_checklist.setOnClickListener(this);
        re_siteinfo_history.setOnClickListener(this);
        re_checklisthistory.setOnClickListener(this);
        re_siteinfo_fixform.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        if(view == re_siteinfo){
            startActivity(new Intent(getActivity(),SurveryFormDynamicRowActivity.class));
        }
        if(view == re_checklist){
            startActivity(new Intent(getActivity(),CheckListActivity.class));
        }
        if(view == re_siteinfo_history){
        }
        if(view == re_checklisthistory){
        }

        if(view == re_siteinfo_fixform){
            startActivity(new Intent(getActivity(),SurveyFormFixRowActivity.class));
        }

    }
}
