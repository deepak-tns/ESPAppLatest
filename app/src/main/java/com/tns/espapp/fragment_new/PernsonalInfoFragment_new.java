package com.tns.espapp.fragment_new;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.UnitModel;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.database.PersonalInfoData;
import com.tns.espapp.rest.ApiClient;
import com.tns.espapp.rest.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TNS on 12-Sep-17.
 */

public class PernsonalInfoFragment_new extends Fragment {

    private TextView tv_name, tv_fathername, tv_mothername, tv_date_of_join, tv_category, tv_grade, tv_businessunit, tv_department, tv_designation,
            tv_reportinghead, tv_bankname, tv_bankacno, tv_pfno, tv_pf_joindate, tv_esicno, tv_esic_joindate, tv_esic_branch, tv_esic_despency,
            tv_idproof, tv_bloodgroup, tv_paddress, tv_caddress, tv_email1, tv_email2, tv_mobile1, tv_mobile2, tv_emegency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pernsonal_info_new, container, false);
        findIds(view);
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        String empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        createJsonRequest(empid);

        return view;

    }

    private void findIds(View v) {
        tv_name = (TextView) v.findViewById(R.id.tv_personal_info_name);
        tv_fathername = (TextView) v.findViewById(R.id.tv_personal_info_fathername);
        tv_mothername = (TextView) v.findViewById(R.id.tv_personal_info_mothername);
        tv_date_of_join = (TextView) v.findViewById(R.id.tv_personal_info_doj);
        tv_category = (TextView) v.findViewById(R.id.tv_personal_info_category);
        tv_grade = (TextView) v.findViewById(R.id.tv_personal_info_grade);
        tv_businessunit = (TextView) v.findViewById(R.id.tv_personal_info_businessunit);
        tv_department = (TextView) v.findViewById(R.id.tv_personal_info_department);
        tv_designation = (TextView) v.findViewById(R.id.tv_personal_info_designation);
        tv_reportinghead = (TextView) v.findViewById(R.id.tv_personal_info_reportinghead);
        tv_bankname = (TextView) v.findViewById(R.id.tv_personal_info_bankname);
        tv_bankacno = (TextView) v.findViewById(R.id.tv_personal_info_bankaccno);
        tv_pfno = (TextView) v.findViewById(R.id.tv_personal_info_pfno);
        tv_pf_joindate = (TextView) v.findViewById(R.id.tv_personal_info_pf_joining);
        tv_esicno = (TextView) v.findViewById(R.id.tv_personal_info_esicno);
        tv_esic_joindate = (TextView) v.findViewById(R.id.tv_personal_info_esicjoiningdate);
        tv_esic_branch = (TextView) v.findViewById(R.id.tv_personal_info_esic_branch);
        tv_esic_despency = (TextView) v.findViewById(R.id.tv_personal_info_despencery);
        tv_idproof = (TextView) v.findViewById(R.id.tv_personal_info_idproof);
        tv_bloodgroup = (TextView) v.findViewById(R.id.tv_personal_info_blood);
        tv_paddress = (TextView) v.findViewById(R.id.tv_personal_info_permanentaddress);
        tv_caddress = (TextView) v.findViewById(R.id.tv_personal_info_curr_address);
        tv_email1 = (TextView) v.findViewById(R.id.tv_personal_info_email1);
        tv_email2 = (TextView) v.findViewById(R.id.tv_personal_info_email2);
        tv_mobile1 = (TextView) v.findViewById(R.id.tv_personal_info_mobile1);
        tv_mobile2 = (TextView) v.findViewById(R.id.tv_personal_info_mobile2);
        tv_emegency = (TextView) v.findViewById(R.id.tv_personal_info_emergency);
    }


    private void createJsonRequest(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                AppConstraint.PERSONALDETAIL, jsonParam(empId),
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
            jsonObject.put("spName", "GetPersonalDetail");
            jsonObject.put("ParameterList", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponse(String results) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        PersonalInfoData perdata[] = gson.fromJson(results, PersonalInfoData[].class);

        for (PersonalInfoData data : perdata) {
            tv_name.setText(data.getName());
            tv_fathername.setText(data.getFname());
            tv_mothername.setText(data.getMname());
            tv_date_of_join.setText(data.getDoj());
            tv_category.setText(data.getCatType());
            tv_grade.setText(data.getGrdType());
            tv_businessunit.setText(data.getBUnitName());
            tv_department.setText(data.getDeptCode());
            tv_designation.setText(data.getDesgType());
            tv_reportinghead.setText(data.getReportingHead());
            tv_bankname.setText(data.getSalaryBankName());
            tv_bankacno.setText(data.getSalaryBankAccNo());
            tv_pfno.setText(data.getPFAccountNo());
            tv_pf_joindate.setText(data.getPFjoinDate());
            tv_esicno.setText(data.getESICInsuranceNo());
            tv_esic_joindate.setText(data.getESICJoinDate());
            tv_esic_branch.setText(data.getESICBranch());
            tv_esic_despency.setText(data.getESICDespencery());
            tv_idproof.setText(data.getID_No());
            tv_bloodgroup.setText(data.getBloodGroup());
            tv_paddress.setText((data.getP_Address()+""+data.getP_City()+"-"+data.getP_Zip()));
            tv_caddress.setText(data.getC_Address());
            tv_email1.setText(data.getEmail1());
            tv_email2.setText(data.getEmail2());
            tv_mobile1.setText(data.getMobile1());
            tv_mobile2.setText(data.getMobile2());
            tv_emegency.setText(data.getEmergencyContactNo());
            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }

    }

}

