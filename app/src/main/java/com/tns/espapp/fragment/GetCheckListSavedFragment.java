package com.tns.espapp.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.LinearLayout;

import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.SalaryDetailData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */

public class GetCheckListSavedFragment extends Fragment {


    private TableLayout tl;
    private TableRow tr;
    private CheckBox cb;
    private CheckBox chk_selectall;
    private Button sendButton;
    private ArrayList<String> checkedValueArrayList = new ArrayList<>();

    private ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();


    String key;
    public static final String mypreference = "chk_test";
    List<String> valueFormlist = new ArrayList<>();


    String setFormname;
    DatabaseHandler db;
    List<String> keyFormlist = new ArrayList<>();

    private String empid;
    SharedPreferences sharedpreferences;
    private List<ChecklistData> getFormlist;

    public GetCheckListSavedFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_get_check_list_saved, container, false);
        TextView textview = (TextView) v.findViewById(R.id.set_formane);
        db = new DatabaseHandler(getActivity());
        chk_selectall =(CheckBox)v.findViewById(R.id.chk_selectall);
        sendButton = (Button)v.findViewById(R.id.sendButton);
        final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);

        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        setFormname = getArguments().getString("PARAM1");
        getFormlist = db.getAllChecklistwithFormno(setFormname);

        textview.setText(setFormname);
        String gettext = getForm_pre(setFormname);


        if (gettext.equals("1")) {
            ArrayList<HashMap<String, String>> mapget = db.getForm(setFormname);

            int size = mapget.size();

            if (size > 0) {
                for (HashMap<String, String> map : mapget) {

                    for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                        key = mapEntry.getKey();
                        String value = mapEntry.getValue();
                        valueFormlist.add(value);
                        keyFormlist.add(key);
                    }
                    sendButton.setVisibility(View.VISIBLE);
                    chk_selectall.setVisibility(View.VISIBLE);
                }


                Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(keyFormlist); // now let's clear the ArrayList so that we can copy all elements from LinkedHashSet primes.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);

                keyFormlist.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);
                keyFormlist.addAll(primesWithoutDuplicates);
                int keysize = keyFormlist.size();
                Log.v("keysize",keysize+"");

                setView(v, keyFormlist, valueFormlist);

            } else {


            }

        }
        chk_selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectAllCheckBox(isChecked);
            }
        });

        sendButtonClickListener();

        return v;
    }

    @SuppressLint("ResourceType")
    public void setView(View v, List<String> key, List<String> value) {

        tl = (TableLayout) v.findViewById(R.id.main_table);
        TableRow tr_head = new TableRow(getActivity());
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < key.size(); i++) {
            TextView label_date = new TextView(getActivity());
            LinearLayout.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
            label_date.setLayoutParams(params);
            label_date.setGravity(Gravity.CENTER);
            label_date.setBackgroundResource(R.drawable.edit_text_design_table);
            label_date.setId(20 + i);
            label_date.setPadding(0, 5, 0, 5);
            label_date.setText(key.get(i));
            label_date.setTextColor(Color.WHITE);

            tr_head.addView(label_date);
            // add the column to the table row here
        }

        tl.addView(tr_head, new TableLayout.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        Integer count = 0;
        TextView labelDATE = null;
        int getcount = value.size();
        int setCounter = 0;
        while (getcount > 0) {
            tr = new TableRow(getActivity());
            tr.setId(100 + getcount);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT));
            for (int k = 0; k < key.size(); k++) {

                labelDATE = new TextView(getActivity());

                LinearLayout.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                labelDATE.setLayoutParams(params);
                labelDATE.setGravity(Gravity.CENTER);
                labelDATE.setId(200 + setCounter);
                labelDATE.setBackgroundResource(R.drawable.edit_text_design);
                String getStr = value.get(setCounter);

                if (getStr.equalsIgnoreCase("No")) {
                    cb = new CheckBox(getActivity());
                    LinearLayout.LayoutParams cb_params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    cb.setLayoutParams(cb_params);
                    cb.setBackgroundResource(R.drawable.edit_text_design);
                    cb.setId(500 + getcount);
                    checkBoxArrayList.add(cb);
                    tr.addView(cb);
                } else
                    {
                    labelDATE.setText(value.get(setCounter));
                    labelDATE.setPadding(0, 15, 0, 15);
                    labelDATE.setTextColor(Color.BLACK);
                    tr.addView(labelDATE);
                }
                setCounter++;
            }

            tl.addView(tr, new TableLayout.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT));
            getcount = getcount - key.size();

            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow tr = (TableRow) v;
                    for (int i = 0; i < tr.getChildCount(); i++) {
                        TextView companyTV, valueTV, yearTV;
                        String company = null, value, year;
                        final View view = tr.getChildAt(i);
                        if (view instanceof TextView) {
                            companyTV = (TextView) tr.getChildAt(i);
                        }
                        if (view instanceof CheckBox) {
                        }
                    }
                }
            });
            count++;
        }
     //   Utility.displayMessage(getContext(),""+checkBoxArrayList.size());
     // finally add this to the table row
    }

    public String getForm_pre(String key) {
        String gets = sharedpreferences.getString(key, "");
        return gets;
    }

    private void selectAllCheckBox(boolean status)
    {
        for(int i=0;i<checkBoxArrayList.size();i++)
        {
           CheckBox checkBox =checkBoxArrayList.get(i);
            if(status){
                checkBox.setChecked(true);
            }
           else {
                checkBox.setChecked(false);
            }
        }
    }

    private void sendButtonClickListener()
    {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = null;
                checkedValueArrayList.clear();
                for (int i = 1; i < tl.getChildCount(); i++) {
                    View child = tl.getChildAt(i);
                    if (child instanceof TableRow) {
                        TableRow row = (TableRow) child;
                        for (int x = 0; x < row.getChildCount(); x++) {
                            View view = row.getChildAt(x);
                            if(view instanceof  CheckBox)
                             {
                               CheckBox checkBox= (CheckBox) row.getChildAt(x);
                                if(checkBox.isChecked())
                                {
                                    ViewGroup viewGroup=(ViewGroup)checkBox.getParent();
                                    TableRow tableRow =(TableRow) viewGroup;
                                    for (int j =0;j<tableRow.getChildCount();j++)
                                    {
                                        View view1 = tableRow.getChildAt(j);
                                        if((view1.getClass().getName().equalsIgnoreCase("android.widget.TextView")))
                                        {
                                            TextView textView = (TextView)row.getChildAt(j);
                                            data =textView .getText().toString();
                                            checkedValueArrayList.add(data);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }

                printCheckedValue();

            }

        });

    }
    private void createFromJson()
    {

    }

    private void printCheckedValue()
    {
        for(int i =0;i<checkedValueArrayList.size();i++)
        {
            Log.v("printData",checkedValueArrayList.get(i));
            Utility.displayMessage(getContext(),checkedValueArrayList.get(i));
        }

       // createJsonRequestchecklistData(empid);
    }

    private void createJsonRequestchecklistData(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        String URL = AppConstraint.SALARYDETAIL;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST, URL, jsonParamchecklistData(empId),
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseChecklistData(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                createJsonRequestchecklistData(empid);
                pDialog.hide();
            }
        });
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }


    private JSONObject jsonParamchecklistData(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Gson gson = new Gson();
        String jsonString = gson.toJson(checkedValueArrayList);
        try {

            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonArray.put(empId);
            jsonArray.put(jsonString);
            jsonObject.put("spName", "GetEmployeeSalaryDetails");
            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void handleResponseChecklistData(String results) {
        Log.v("jsonresponse", results.toString());
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        if (results.equals("[]")) {
            return;
        }
        //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }


  }
