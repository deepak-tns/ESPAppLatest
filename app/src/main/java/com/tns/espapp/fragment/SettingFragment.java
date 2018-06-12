package com.tns.espapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tns.espapp.R;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.SettingData;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

  EditText edt_gpsintervel,edt_gpsspeed;
    TextView edt_gpsenable;
     DatabaseHandler db;

    int gpsenable,gpsintervel,gpsspeed;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        db = new DatabaseHandler(getActivity());
        findsIds(v);

        List<SettingData> settingDatas = db.getGPS_settingData();
        if(settingDatas.size() >0){

             gpsenable = settingDatas.get(0).getSett_Gpsenabled();
             gpsintervel = settingDatas.get(0).getSett_Gpsinterval();
             gpsspeed = settingDatas.get(0).getSett_Gpsspeed();
        }

        if(gpsenable == 1){
            edt_gpsenable.setText("Enabled");
        }else{
            edt_gpsenable.setText("Disabled");
        }

        edt_gpsintervel.setText(gpsintervel +"");
        edt_gpsspeed.setText(gpsspeed +"");

        return v;
    }

    private  void findsIds(View v){
       edt_gpsenable =(TextView) v.findViewById(R.id.setting_gps_enabled);
        edt_gpsintervel =(EditText)v.findViewById(R.id.setting_gps_intervel);
        edt_gpsspeed  =(EditText)v.findViewById(R.id.setting_gps_speed);

    }

}
