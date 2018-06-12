package com.tns.espapp.fragment_new;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.adapter.MiscellaneousAdapter;
import com.tns.espapp.database.MiscellaneousAllData;

import com.tns.espapp.database.MiscellaneousData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiscellaneousFragment_new extends Fragment {
    private List<MiscellaneousData> miscellaneousDatasList = new ArrayList<>();
    
  private RecyclerView  recyclerView ;
    private String empid;
    private MiscellaneousAdapter mAdapter;
    private Spinner spi_select_categary;
    private ArrayAdapter adapter;
   private ArrayList<String> categaryList;
    private String selectCategary;
    public MiscellaneousFragment_new() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_miscellaneous_new, container, false);
        final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        spi_select_categary =(Spinner)v.findViewById(R.id.spi_select_categary);

        selectCategary = "General";

        categaryList = new ArrayList<>();
        categaryList. add("General");
        categaryList. add("Departmental");
        categaryList. add("Health & sefty");
        categaryList. add("HR Pollicy");

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categaryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_select_categary.setAdapter(adapter);

        spi_select_categary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCategary = (String) parent.getItemAtPosition(position);

                createJsonRequestMiscellaneousData(empid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findIDS(v);

        return v;
    }

    private void findIDS(final View v){

        createJsonRequestMiscellaneousData(empid);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_miscellaneous);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity()) ;
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(divider);
        mAdapter = new MiscellaneousAdapter( miscellaneousDatasList,R.layout.miscellaneous_data_adapter,getActivity(), new MiscellaneousAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MiscellaneousData item, int pos) {

           //String downloads = AppConstraint.MISCELLANOUSDETAILSDOWNLOADS+item.getMiscellaneousDatas().get(pos).getFilePath();

            //    Toast.makeText(getActivity(),item.getMiscellaneousDatas().get(pos).getDate(),Toast.LENGTH_LONG).show();

            }
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


    }


    private void createJsonRequestMiscellaneousData(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = AppConstraint.MISCELLANOUSDETAILS;


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamMiscellaneousData(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseMiscellaneousData(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // handleResponseMiscellaneousData(empId);
                pDialog.hide();
            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParamMiscellaneousData(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        // jsonArray.put("");

        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");


            jsonArray.put(empId);
            jsonArray.put(selectCategary);
            jsonObject.put("spName", "GetMiscellaniousDetails");

            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponseMiscellaneousData(String results) {
        miscellaneousDatasList.clear();
        JSONArray jsonArrayOuter = null;
        JSONArray jsonArray1 =null;
        Log.v("miscellaneousresponse", results.toString());
        if (results.equals("[]")) {
            mAdapter.notifyDataSetChanged();
            return;
        }



        try {
            Gson gson = new Gson();
            jsonArrayOuter = new JSONArray(results);

            Log.v ("size",jsonArrayOuter.length()+"");
           // jsonArray1 = jsonArrayOuter.getJSONArray(0);

         /*

         for(int i=0 ; i<jsonArrayOuter.length()-1;i++){
                miscellaneousalldatamodel = new MiscellaneousAllData();
                miscellaneousalldatamodel.setPath(jsonArrayOuter.getString(i));
                miscellaneousallDatasList.add(miscellaneousalldatamodel);
                Log.v("miscleddata",miscellaneousalldatamodel.getPath());
            }

            */
          List<MiscellaneousData>  miscellaneousDatasListsssss = Arrays.asList(gson.fromJson(jsonArrayOuter.toString(), MiscellaneousData[].class));
          //  miscellaneousalldatamodel.setMiscellaneousDatas(miscellaneousDatasList);
            miscellaneousDatasList.addAll(miscellaneousDatasListsssss);

            mAdapter.notifyDataSetChanged();
            // String json = "{"client":"127.0.0.1","servers":["8.8.8.8","8.8.4.4","156.154.70.1","156.154.71.1"]}";

        } catch (JSONException e) {
            Log.v("miserror",e.getMessage());
            e.printStackTrace();
        }

    }


}
