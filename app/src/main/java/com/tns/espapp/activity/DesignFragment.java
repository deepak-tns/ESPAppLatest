package com.tns.espapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tns.espapp.R;

public class DesignFragment extends Fragment {


    public DesignFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static DesignFragment newInstance(int index) {
        DesignFragment fragment = new DesignFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_design, container, false);
    }


}
