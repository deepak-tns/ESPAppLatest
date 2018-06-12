package com.tns.espapp.fragment_new;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.FinanceYearData;
import com.tns.espapp.database.LeaveLadgerData;
import com.tns.espapp.database.LeaveTransactionData;
import com.tns.espapp.database.Picture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveLadgerFragment_new extends Fragment {
    private String year1;
    private String year2;
    private Spinner spi_select_year;
    private ArrayAdapter adapter;
    ArrayList<String> selectyear_list = new ArrayList<>();
    private String empid;


    private List<LeaveLadgerData> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    public LeaveLadgerFragment_new() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_leaveladger_fragment_new, container, false);
        final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int curryear = now.get(Calendar.YEAR);
        year2 = String.valueOf(curryear);
        findIDS(v);
        createJsonRequestLeaveInfoData(empid);
        return v;
    }

    private void findIDS(View v) {

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        spi_select_year = (Spinner) v.findViewById(R.id.spi_leaveladger_selectyear);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        /*LayoutInflater inflaters = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerPlaceHolder = inflaters.inflate(R.layout.leave_ladger_adapter_view_header, null, false);
        mLayoutManager.addView(headerPlaceHolder, 0);*/
        mAdapter = new MyAdapter(getHeader(), movieList);
        recyclerView.setAdapter(mAdapter);
       // prepareMovieData();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectyear_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_select_year.setAdapter(adapter);
        createJsonRequestYear(empid);

        spi_select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year2 = (String) parent.getItemAtPosition(position);
             /*   String[] separated = sItem.split("-");
                year1=  separated[0];
                year2=  separated[1]*/
                ;


                 createJsonRequestLeaveInfoData(empid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public LeaveLadgerData getHeader() {
        return new LeaveLadgerData("JAN", "FEB", "deepak", "sachan", "deepak", "sachan", "deepak", "sachan", "deepak", "sachan", "deepak", "sachan");
    }


    private void createJsonRequestYear(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = AppConstraint.FINANCIALYEARFORDRP;


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamYear(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseYear(response.toString());
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

    private JSONObject jsonParamYear(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // jsonArray.put("");

        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");


            jsonObject.put("spName", "GetYearForDrp");

            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }



    private void handleResponseYear(String results) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        if (results.equals("[]")) {
            return;
        }


        FinanceYearData billdata[] = gson.fromJson(results, FinanceYearData[].class);
        for (FinanceYearData data : billdata) {

         /*   String[] separated = data.getFinyr().split("-");
            year1 = separated[0];
            year2 = separated[1];*/
            year2 = data.getFinyr();
            selectyear_list.add(year2);
            adapter.notifyDataSetChanged();


            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }

    }

    private void createJsonRequestLeaveInfoData(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = AppConstraint.LEAVELADGER;


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamLeaveData(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseLeaveData(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  createJsonRequestLeaveInfoData(empid);
                Log.v("error_res", error.toString());
                pDialog.hide();
            }

        });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParamLeaveData(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // jsonArray.put("");

        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");


            jsonArray.put(empId);
            jsonArray.put(year2);
            jsonObject.put("spName", "Get_LeaveSummery");

            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponseLeaveData(String results) {
        movieList.clear();
        Log.v("jsonresponse", results.toString());
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        if (results.equals("[]")) {
            movieList.clear();
            return;
        }
        LeaveLadgerData leaveDetailDatasdata[] = gson.fromJson(results, LeaveLadgerData[].class);
        movieList.add(new LeaveLadgerData());
        for (LeaveLadgerData data : leaveDetailDatasdata) {

            movieList.add(data);
            mAdapter.notifyDataSetChanged();
            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }

    }

 public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        LeaveLadgerData headername;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2;

        private List<LeaveLadgerData> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView mon, opel, opcl, creel, crecl, takenel, takencl, balanceel, balancecl, perbyhr, totalcredit, totaltaken, dedbyhr, totalbalance;

            public MyViewHolder(View view) {
                super(view);
                mon = (TextView) view.findViewById(R.id.tv_leaveladger_month);
                opel = (TextView) view.findViewById(R.id.tv_leaveladger_op_el);
                opcl = (TextView) view.findViewById(R.id.tv_leaveladger_op_cl);
                creel = (TextView) view.findViewById(R.id.tv_leaveladger_cre_el);
                crecl = (TextView) view.findViewById(R.id.tv_leaveladger_cre_cl);
                takenel = (TextView) view.findViewById(R.id.tv_leaveladger_taken_el);
                takencl = (TextView) view.findViewById(R.id.tv_leaveladger_taken_cl);
                balanceel = (TextView) view.findViewById(R.id.tv_leaveladger_balance_el);
                balancecl = (TextView) view.findViewById(R.id.tv_leaveladger_balance_cl);
                perbyhr = (TextView) view.findViewById(R.id.tv_leaveladger_per_by_hr);
                totalcredit = (TextView) view.findViewById(R.id.tv_leaveladger_total_cre);
                totaltaken = (TextView) view.findViewById(R.id.tv_leaveladger_total_taken);
                dedbyhr = (TextView) view.findViewById(R.id.tv_leaveladger_ded_by_hr);
                totalbalance = (TextView) view.findViewById(R.id.tv_leaveladger_total_balance);
            }
        }


        public MyAdapter(LeaveLadgerData headername, List<LeaveLadgerData> moviesList) {
            this.headername = headername;
            this.moviesList = moviesList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.leave_ladger_adapter_view, parent, false);
            // return new MyViewHolder(itemView);

            if (viewType == TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_ladger_adapter_view_header, parent, false);
                return new VHHeader(v);
            } else if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_ladger_adapter_view, parent, false);
                return new MyViewHolder(v);
            }/* else if (viewType == TYPE_FOOTER) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_ladger_adapter_view_footer, parent, false);
                return new VHHeader(v);
            }*/
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
          /*    if(position > 1) {


                  if (position % 2 == 1) {
                      // Set a background color for ListView regular row/item
                      holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                      //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                  } else {
                      // Set the background color for alternate row/item
                      holder.itemView.setBackgroundColor(Color.parseColor("#78B266"));
                      //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
                  }*/
            if (holder instanceof VHHeader) {
                VHHeader VHheader = (VHHeader) holder;
                //    VHheader.txtTitle.setText(headername.getmonth());
                //    VHheader.txtfeb.setText(headername.getopeningEL());

            } else if (holder instanceof MyViewHolder) {



                MyViewHolder VHitem = (MyViewHolder) holder;
                LeaveLadgerData movie = moviesList.get(position);
                VHitem.mon.setText(movie.getmonth());
                VHitem.opel.setText(movie.getopeningEL());
                VHitem.opcl.setText(movie.getopeningCL());
                VHitem.creel.setText(movie.getcreditEL());
                VHitem.crecl.setText(movie.getcreditCL());
                VHitem.takenel.setText(movie.gettakenEL());
                VHitem.takencl.setText(movie.gettakenCL());

                VHitem.balanceel.setText(movie.getbalanceEL());
                VHitem.balancecl.setText(movie.getbalanceCL());
                VHitem.perbyhr.setText(movie.getpermittedbyHR());
                VHitem.totalcredit.setText(movie.gettotalcredit());
                VHitem.totaltaken.setText(movie.gettotaltaken());
                VHitem.dedbyhr.setText(movie.getDeductedByHR());
                VHitem.totalbalance.setText(movie.getTotalBalance());

                if(position == moviesList.size()-1){
                    holder.itemView.setBackgroundColor(Color.parseColor("#00FF00"));
                }else{
                    holder.itemView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }

            }

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;
            /*if (isPositionFooter(position))
                return TYPE_FOOTER;*/
            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        private boolean isPositionFooter(int position) {
            return position == moviesList.size() - 1;
        }


        class VHHeader extends RecyclerView.ViewHolder {
            TextView txtTitle, txtfeb;

            public VHHeader(View itemView) {
                super(itemView);
                // txtTitle = (TextView)itemView.findViewById(R.id.tv_leaveladger_month);
                //  txtfeb = (TextView)itemView.findViewById(R.id.tv_leaveladger_op_el);

            }
        }

    }

    public void prepareMovieData() {
        LeaveLadgerData movie = new LeaveLadgerData();
        movieList.add(movie);
        movie = new LeaveLadgerData("Jan", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("Feb", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("March", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("Apr", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("May", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("Jun", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("July", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("Aug", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("Sep", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("Oct", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("Nov", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData("Dec", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        movieList.add(movie);

        movie = new LeaveLadgerData();
        movieList.add(movie);

        mAdapter.notifyDataSetChanged();
    }
}