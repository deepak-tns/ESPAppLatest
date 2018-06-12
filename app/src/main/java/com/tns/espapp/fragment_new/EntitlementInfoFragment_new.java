package com.tns.espapp.fragment_new;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.EntitlementData;
import com.tns.espapp.database.PersonalInfoData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EntitlementInfoFragment_new extends Fragment {

    private TextView vHotel;
    private TextView vHotDA;
    private TextView vDA;
    private TextView nCommunication;
    private TextView nAdvance;
    private TextView vRemark;
    private TextView nImprest;
    private TextView nInternet;
    private TextView Project;
    private TextView nConveyance;
    private TextView vRemark1;

    public EntitlementInfoFragment_new() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_entitlement_info_fragment_new, container, false);
        findIds(v);
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        String empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        createJsonRequest(empid);
        return v;
    }

    private void findIds(View v) {
        vHotel = (TextView) v.findViewById(R.id.tv_entitlementinfo_hotel);
        vHotDA = (TextView) v.findViewById(R.id.tv_entitlementinfo_hotel_da);
        vDA = (TextView) v.findViewById(R.id.tv_entitlementinfo_da);
        nCommunication = (TextView) v.findViewById(R.id.tv_entitlementinfo_communication);
        nAdvance = (TextView) v.findViewById(R.id.tv_entitlementinfo_advance);
        vRemark = (TextView) v.findViewById(R.id.tv_entitlementinfo_remark);
        nImprest = (TextView) v.findViewById(R.id.tv_entitlementinfo_imprest);
        nInternet = (TextView) v.findViewById(R.id.tv_entitlementinfo_internet);
        Project = (TextView) v.findViewById(R.id.tv_entitlementinfo_project);
        nConveyance = (TextView) v.findViewById(R.id.tv_entitlementinfo_conveyance);
        vRemark1 = (TextView) v.findViewById(R.id.tv_entitlementinfo_remark1);

    }
    private void createJsonRequest(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                AppConstraint.ENTITLEMENTDETAIL, jsonParam(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponse(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParam(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(empId);

        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "GetEntitlementInfo");
            jsonObject.put("ParameterList", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponse(String results) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        EntitlementData entitlementdata[] = gson.fromJson(results, EntitlementData[].class);

        for (EntitlementData data : entitlementdata) {
            vHotel.setText(data.getvHotel());
            vDA.setText(data.getvDA());
            vHotDA.setText(data.getvHotDA());
            nCommunication.setText(data.getnCommunication());
            nAdvance.setText(data.getnAdvance());
            vRemark.setText(data.getvRemark());
            nImprest.setText(data.getnImprest());
            Project.setText(data.getProject());
            nInternet.setText(data.getnInternet());
            nConveyance.setText(data.getnConveyance());
            vRemark1.setText(data.getvRemark1());

            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }

    }




}