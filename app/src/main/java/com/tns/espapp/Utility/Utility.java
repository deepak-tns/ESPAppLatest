package com.tns.espapp.Utility;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by eninovuser on 27-04-2016.
 */
public class Utility {
    private static int idCounter = 0;
    private static String objectType = "";
 public static   boolean isFragmentVisible = false;

    public static String getTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    public static String getDeviceId(Context ctx) {
        String deviceId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    public static String getDeviceLanguage() {
        String deviceLanguage = Locale.getDefault().getDisplayLanguage();
        return deviceLanguage;
    }

    public static String getDeviceLanguageCode() {
        String deviceLanguage = Locale.getDefault().getLanguage();
        return deviceLanguage;
    }

    public static String getDeviceName() {
        String deviceName = Build.BRAND;
        return deviceName;
    }

    public static int getDeviceOs() {
        int deviceOs = Build.VERSION.PREVIEW_SDK_INT;
        return deviceOs;
    }

    public static String getDeviceVersion() {
        String deviceVersion = Build.VERSION.RELEASE;
        return deviceVersion;
    }

    public static String getModelNumber() {
        String modelNumber = Build.MODEL;
        return modelNumber;
    }
    public static String getIMEINumber(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDeviceId = tm.getDeviceId();
        return tmDeviceId;
    }
    public static void displayMessage(Context context,String message) {

        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isNet = false;
        if (context == null) {
            //TODO: put logs here
            return isNet;
        }
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            if (connManager.getActiveNetworkInfo() != null) {
                if (connManager.getActiveNetworkInfo().isConnected())
                    isNet = true;
                else
                    isNet = false;
            }

        }

        return isNet;
    }


    public static String generateLocalId(String objType) {
        String localId = "";
        if (!objectType.equalsIgnoreCase(objType)) {
            objectType = objType;
            idCounter = 0;
        }
        ++idCounter;
        localId = localId + objType + "." + Long.toString(System.currentTimeMillis()) + "." + idCounter;
        //snprintf(localId, OBJECTIDSIZE, "%s.%llu.%d", objectType, getCurrLongDateTime(), ++idCounter);
        return localId;
    }



}