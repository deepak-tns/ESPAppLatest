package com.tns.espapp.fragment_new;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.BillInfoData;
import com.tns.espapp.database.FinanceYearData;
import com.tns.espapp.database.FinanceYearData1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillInfoFragment_new extends Fragment implements View.OnClickListener {
    private String empid;
    private String type;
    private String year1;
    private String year2;
    private List<String> financeYearDataList = new ArrayList<>();

    private Spinner spi_billinfo_selectyear;
    private Button btn_billinfoSearch;
    private TextView tv_empname, tvbillinfo_month, tvbillinfo_cycle, tvbillinfo_billamount, tvbillinfo_passingamount,tv_norecord_found;
    private ArrayAdapter adapter;
    private LinearLayout linear_billinfo_data,linear_billinfo_header;


    public BillInfoFragment_new() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bill_info_new, container, false);

        final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());

        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        type = "type";
        createJsonRequest(empid, type);
        findIDS(v);

          tv_empname.setText("#"+empid);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, financeYearDataList);
        spi_billinfo_selectyear.setAdapter(adapter);
        spi_billinfo_selectyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sItem =(String) parent.getItemAtPosition(position);
                String[] separated = sItem.split("-");
              year1=  separated[0];
              year2=  separated[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }


    private void findIDS(View v) {
        spi_billinfo_selectyear = (Spinner) v.findViewById(R.id.spi_billinfo_selectyear);
        tv_empname = (TextView) v.findViewById(R.id.tv_billifo_empname);
        btn_billinfoSearch = (Button) v.findViewById(R.id.btn_billinfoSearch);
        tvbillinfo_month = (TextView) v.findViewById(R.id.tvbillinfo_month);
        tvbillinfo_cycle = (TextView) v.findViewById(R.id.tvbillinfo_cycle);
        tvbillinfo_billamount = (TextView) v.findViewById(R.id.tvbillinfo_billamount);
        tvbillinfo_passingamount = (TextView) v.findViewById(R.id.tvbillinfo_passingamount);
        tv_norecord_found = (TextView) v.findViewById(R.id.tv_norecord_found);

        linear_billinfo_data  = (LinearLayout) v.findViewById(R.id.linear_billinfo_data);
        linear_billinfo_header  = (LinearLayout) v.findViewById(R.id.linear_billinfo_header);

        btn_billinfoSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_billinfoSearch) {
            createJsonRequest(empid, "no");
        }
    }

    private void createJsonRequest(String empId, final String type) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = "";
        if (type.equalsIgnoreCase("TYPE")) {
            URL = AppConstraint.FINANCIALYEARFORDRP1;
        } else {
            URL = AppConstraint.BILLDETAILS;
        }

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParam(empId, type),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponse(response.toString(), type);
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

    private JSONObject jsonParam(String empId, String type) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // jsonArray.put("");

        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");

            if (type.equalsIgnoreCase("TYPE")) {
                jsonObject.put("spName", "GetFinancialYearForDrp");
            } else {
                jsonArray.put(empId);
                jsonArray.put(year1);
                jsonArray.put(year2);
                jsonObject.put("spName", "GetBillInfo");
            }

            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponse(String results, String type) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        if(results.equals("[]")){
            tv_norecord_found.setVisibility(View.VISIBLE);
            linear_billinfo_data.setVisibility(View.GONE);
            linear_billinfo_header.setVisibility(View.GONE);
            return;
        }

        if (type.equalsIgnoreCase("TYPE")) {

            FinanceYearData1 billdata[] = gson.fromJson(results, FinanceYearData1[].class);
            for (FinanceYearData1 data : billdata) {
                financeYearDataList.add(data.getFinyr());
                adapter.notifyDataSetChanged();

            }

            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        } else {
            BillInfoData billdata[] = gson.fromJson(results, BillInfoData[].class);
            for (BillInfoData data : billdata) {
                tvbillinfo_month.setText(data.getMonth());
                tvbillinfo_cycle.setText("("+data.getCycle()+")");
                tvbillinfo_billamount.setText(data.getCurAmount());
                tvbillinfo_passingamount.setText(data.getCurPassingAmount());
                Toast.makeText(getActivity(), data.toString(), Toast.LENGTH_LONG).show();
                tv_norecord_found.setVisibility(View.GONE);
                linear_billinfo_data.setVisibility(View.VISIBLE);
                linear_billinfo_header.setVisibility(View.VISIBLE);


                Log.v("responcedata", data.toString());

            }


        }

    }


}
