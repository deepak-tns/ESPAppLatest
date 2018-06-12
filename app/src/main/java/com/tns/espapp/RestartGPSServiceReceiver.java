package com.tns.espapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.activity.LockScreenActivity;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.TaxiFormData;
import com.tns.espapp.service.GPSTracker;

import java.util.List;

public class RestartGPSServiceReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        autoRestartService(context);
    }

    private void autoRestartService(Context context){
         SharedPreferences sharedPreferencessss;
         SharedPreferences.Editor editor;
        DatabaseHandler   db = new DatabaseHandler(context);
        sharedPreferencessss = context.getSharedPreferences("SERVICE", Context.MODE_PRIVATE);
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(context);
        String  empIDS = sharedPreferenceUtils.getString(AppConstraint.EMPID);

        List<TaxiFormData> datas = db.getLastTaxiformData();
        if (datas.size() > 0) {
            int flag = datas.get(0).getFlag();
            if (flag == 0) {

                Intent   intent2 = new Intent(context, GPSTracker.class);
                if (!GPSTracker.isRunning) {
                    editor = sharedPreferencessss.edit();
                    editor.putString("formno", datas.get(0).getFormno());
                    editor.putString("getdate", datas.get(0).getSelectdate());
                    editor.putString("empid", empIDS);
                    editor.commit();
                    context.startService(intent2);

                }else{
                    Utility.displayMessage(context,"running service");
                }
            }
        }

    }
}
