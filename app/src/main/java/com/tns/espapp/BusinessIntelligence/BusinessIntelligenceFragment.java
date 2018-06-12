package com.tns.espapp.BusinessIntelligence;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tns.espapp.R;


public class BusinessIntelligenceFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RelativeLayout bi_approval,bi_salary,bi_leave,bi_attendance;
    public BusinessIntelligenceFragment() {
        // Required empty public constructor
    }


    public static BusinessIntelligenceFragment newInstance(String param1, String param2) {
        BusinessIntelligenceFragment fragment = new BusinessIntelligenceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  v= inflater.inflate(R.layout.fragment_business_intelligence, container, false);
        findIds(v);
        return v;
    }

    private void findIds(View v){

        bi_approval =(RelativeLayout)v.findViewById(R.id.bi_approval) ;
        bi_salary=(RelativeLayout)v.findViewById(R.id.bi_salary) ;
        bi_leave=(RelativeLayout)v.findViewById(R.id.bi_leave) ;
        bi_attendance=(RelativeLayout)v.findViewById(R.id.bi_attendance) ;
        bi_approval.setOnClickListener(this);
        bi_salary.setOnClickListener(this);
        bi_leave.setOnClickListener(this);
        bi_attendance.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == bi_approval){

         startActivity(new Intent(getActivity(),AttendanceReportActivity.class));

        }
        if(view == bi_salary){

        }
        if(view == bi_leave){

        }
        if(view == bi_attendance){

        }

    }
}
