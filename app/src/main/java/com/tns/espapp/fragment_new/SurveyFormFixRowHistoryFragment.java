package com.tns.espapp.fragment_new;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.ListviewHelper;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.activity.CameraSurfaceViewActivity;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.SurveyFormData;

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
public class SurveyFormFixRowHistoryFragment extends Fragment {
    private Spinner spin;
    String setFormname;
    private ArrayList<String> formlist;
    private ArrayAdapter aa;
    DatabaseHandler db;
    ArrayList<String> listvalue = new ArrayList<>();
    ArrayList<String> listkey = new ArrayList<>();

    public SurveyFormFixRowHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_survey_form_fix_row_history, container, false);
        db = new DatabaseHandler(getActivity());
        getFormname(view);


        return view;
    }

    private void getFormname(final View v) {
        spin = (Spinner) v.findViewById(R.id.spi_getsurveyform);
        final LinearLayout table_main2 = (LinearLayout) v.findViewById(R.id.table_main2);
        final View list_header = (View) v.findViewById(R.id.lay_header);

        //final TextView tvone = (TextView) v.findViewById(R.id.one);
       // final TextView tvtwo = (TextView) v.findViewById(R.id.two);
       // final TextView tvthree = (TextView) v.findViewById(R.id.three);
        final ListView lstview=(ListView)v.findViewById(R.id.lstview);

        getVOLLY_FORMNAME();
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectFormSpinner = parent.getItemAtPosition(position).toString();
                setFormname = selectFormSpinner;
                List<SurveyFormData> Table2 = db.getAllFinalChecklist2_Save(setFormname);
                ArrayList<HashMap<String, String>> Table1 = db.printTable1Data(setFormname);
                int coloumcount = db.numberOfColumns(setFormname);

                for (HashMap<String, String> hash : Table1) {
                    listvalue.add(hash.get("column_value"));
                    listkey.add(hash.get("column_name"));

                  /*  for (Map.Entry<String, String> entry : hash.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        //  listkey.add(key);
                        // listvalue.add(value);
                        Log.v("Table1", key + ":" + value);
                    }*/
                }

                for (SurveyFormData print : Table2) {
                    Log.v("Table1list", print.getSts() + "  "+print.getRemark() );
                }
              //  tvone.setText(listkey.get(0));
              //  tvtwo.setText((listkey.get(1)).substring(1));
              //  tvthree.setText(listkey.get(2));
                table_main2.setVisibility(View.VISIBLE);
                RecyclerView rvlist = (RecyclerView) v.findViewById(R.id.rvlist);
                RecyclerView id_rv_table1header = (RecyclerView) v.findViewById(R.id.id_rv_table1header);

                Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(listkey); // now let's clear the ArrayList so that we can copy all elements from LinkedHashSet primes.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);
                listkey.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);
               listkey.addAll(primesWithoutDuplicates);
                id_rv_table1header.setLayoutManager(new GridLayoutManager(getActivity(), coloumcount));
                MyAdapter adapter1 = new MyAdapter(getActivity(), listkey, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int pos) {
                        Toast.makeText(getActivity(), ((TextView) o).getText().toString() + pos, Toast.LENGTH_LONG).show();
                    }
                });
                id_rv_table1header.setAdapter(adapter1);

                rvlist.setLayoutManager(new GridLayoutManager(getActivity(), coloumcount));
                MyAdapter adapter = new MyAdapter(getActivity(), listvalue, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int pos) {
                        Toast.makeText(getActivity(), ((TextView) o).getText().toString() + pos, Toast.LENGTH_LONG).show();
                    }
                });

                rvlist.setAdapter(adapter);


                SurveyFormFixRowAdapterListview_Save  Table2Adapter= new SurveyFormFixRowAdapterListview_Save(getActivity(),0,Table2);
                lstview.setAdapter(Table2Adapter);
                ListviewHelper.setListViewHeightBasedOnItems(lstview);
                list_header.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getVOLLY_FORMNAME() {
        // String mStringUrl = "http://www.yoururl.com";
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DatabaseName", "TNS_NT_PMS");
            //jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "GetQuestionFormName");
            //  jsonObject.put("spName", "USP_GetForm ");
            //  jsonObject.put("ParameterList","");
            Log.d("JsonParam", jsonObject + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.DYNAMICSURVEYFIXFORMNAME, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseFormNAMEJSON(response.toString());
                        pDialog.dismiss();
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                        pDialog.dismiss();
                    }
                }) {
        /*    @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }*/
        };
        jsObjRequest.setRetryPolicy(new RetryPolicy() {
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
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest, null);
    }

    public void parseFormNAMEJSON(String jsonObject) {
        String formNameno = "";
        formlist = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(jsonObject);
            //   {"FormName":[{"FormName":"Form3"}]}
            JSONArray jsonArray = obj.getJSONArray("FormName");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj2 = jsonArray.getJSONObject(i);
                formNameno = jObj2.getString("FormName");
                formlist.add(formNameno);
            }
            aa = new ArrayAdapter(getActivity(), R.layout.spinner_layout, formlist);
            aa.setDropDownViewResource(R.layout.spinner_layout);
            spin.setAdapter(aa);
            spin.setSelection(aa.getCount() - 1);
            //  getVOLLY_FORMVARYFY(formNameno);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<String> list;
        private Context context;
        private final OnItemClickListener listener;

        // data is passed into the constructor
        MyAdapter(Context context, ArrayList<String> list, OnItemClickListener listener) {
            this.context = context;
            this.list = list;
            this.listener = listener;
        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.itemview, parent, false);
//            View view = LayoutInflater.inflate(R.layout.itemview, parent, false);
            return new ViewHolder(itemView);
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String text = list.get(position);
            holder.tvtext.setText(text);
            holder.bind(listener, position);
        }


        // total number of cells
        @Override
        public int getItemCount() {
            return list.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvtext;

            ViewHolder(final View itemView) {
                super(itemView);
                tvtext = (TextView) itemView.findViewById(R.id.tvtext);

                //itemView.setOnClickListener(this);
              /*  itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // open another activity on item click
                        Toast.makeText(context,tvtext.getText().toString()+ getAdapterPosition(),Toast.LENGTH_LONG).show();
                    }
                });*/
            }


            public void bind(final OnItemClickListener listener, final int pos) {

                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        listener.onItemClick((Object) tvtext, pos);

                    }

                });

            }


//            @Override
//            public void onClick(View view) {
//                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
//            }
            //   }

            // convenience method for getting data at click position
//        String getItem(int id) {
//            return list[id];
//        }

            // allows clicks events to be caught
//
        }


    }

    public interface OnItemClickListener {
        void onItemClick(Object o, int pos);
    }

    public class SurveyFormFixRowAdapterListview_Save extends ArrayAdapter {
        Context context;
        List<SurveyFormData> getListArray;

        public SurveyFormFixRowAdapterListview_Save(Context context, int resource, List<SurveyFormData> list) {
            super(context, resource, list);
            this.context = context;
            this.getListArray = list;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.surveyformdynamicrow_data_adapter, parent, false);
            }
            //  lst_attachment =(ListView)convertView.findViewById(R.id.lstcapture_adapter);
            TextView tvSno = (TextView) convertView.findViewById(R.id.tv_sno);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.tv_description);
            final TextView tvSts = (TextView) convertView.findViewById(R.id.tv_status);
            final EditText tvRemark = (EditText) convertView.findViewById(R.id.tv_remark);
            TextView ivPhotos = (TextView) convertView.findViewById(R.id.tv_photos);


            final SurveyFormData data = getListArray.get(position);
            tvSno.setText(data.getsNo() + "");
            tvDesc.setText(data.getDesc());
            tvSts.setText(data.getSts());
            tvRemark.setText(data.getRemark());
            ivPhotos.setText(data.getCount()+"");


            return convertView;

        }

    }





}