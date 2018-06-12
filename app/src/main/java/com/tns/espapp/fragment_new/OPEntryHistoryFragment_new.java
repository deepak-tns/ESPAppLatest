package com.tns.espapp.fragment_new;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tns.espapp.AppConstraint;
import com.tns.espapp.HTTPPostRequestMethod;
import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.activity.RouteMapActivity;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.OPEntryData;
import com.tns.espapp.database.TaxiFormData;
import com.tns.espapp.fragment.TaxiFormRecordFragment;
import com.tns.espapp.service.SendLatiLongiServerIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class OPEntryHistoryFragment_new extends Fragment {

    private DatabaseHandler db;
    private OPHistoryAdapter adapter;
    private ArrayList<OPEntryData> OPEntryDataArrayList;
    private ListView listview_OP_history;
    private String  getstatus;
    private EditText editsearch;

    private Typeface face;


    private String year1,year2;
    int nextyear;
    Spinner spi_select_year;
    private String empid;
    private TextView tv_emid;
    private ArrayList<String>  selectyear_list = new ArrayList<>();
    int flag;
    public OPEntryHistoryFragment_new() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_opentry_history_fragment_new, container, false);

   final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);
       // findIDS(v);
      //  selectYear(v);
        db = new DatabaseHandler(getActivity());


        OPEntryDataArrayList = new ArrayList<>();
        listview_OP_history = (ListView) v.findViewById(R.id.listview_op_history);
        List<OPEntryData> opEntryDatalist = db.getAllOPEntry();
        int size = opEntryDatalist.size();

        face = Typeface.createFromAsset(getActivity().getAssets(),
                "arial.ttf");


        if (size > 0)
        {
           /* for (OPEntryData opEntryData : opEntryDatalist)
            {
                OPEntryDataArrayList.add(opEntryData);
            }*/
            OPEntryDataArrayList.addAll(opEntryDatalist);
        }


        adapter = new OPHistoryAdapter(getActivity(), R.layout.taxiform_record_history_adapter, OPEntryDataArrayList);
        listview_OP_history.setAdapter(adapter);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.header_listview_taxiform_record, null);
        listview_OP_history.addHeaderView(view);

/*        listview_OP_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            }
        });*/




        editsearch = (EditText) v.findViewById(R.id.search_ophristoryrecord);
        editsearch.setTypeface(face);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString();
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        return v;
    }

    private class OPHistoryAdapter extends ArrayAdapter {
        OPEntryData opEntryData;
        int deepColor = Color.parseColor("#FFFFFF");
        int deepColor2 = Color.parseColor("#DCDCDC");
        //  int deepColor3 = Color.parseColor("#B58EBF");
        private int[] colors = new int[]{deepColor, deepColor2};
        private List<OPEntryData> searchlist = null;
        List<OPEntryData> opentry_DataArrayList;
        private SparseBooleanArray mSelectedItemsIds;


        public OPHistoryAdapter(Context context, int resource, ArrayList<OPEntryData> opEntryDatalist) {
            super(context, resource, opEntryDatalist);
            this.searchlist = opEntryDatalist;
            this.opentry_DataArrayList = new ArrayList<>();
            opentry_DataArrayList.addAll(searchlist);
        }

        private class ViewHolder {

            TextView formno;
            TextView date;
            TextView id;
            TextView projecttype;
            TextView vihecleno;
            ImageView status;
            TextView startkm;
            TextView endkm;
            TextView delete;


        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            opEntryData = searchlist.get(position);
            ViewHolder viewHolder = new ViewHolder();

            if (convertView == null) {

                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.op_history_adapter, parent, false);
                //int colorPos = position % colors.length;
                // convertView.setBackgroundColor(colors[colorPos]);

                viewHolder.formno = (TextView) convertView.findViewById(R.id.tv_formno_taxiadapter);
                viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date_taxiadapter);
                viewHolder.id = (TextView) convertView.findViewById(R.id.tv_id_taxiadapter);
                viewHolder.projecttype = (TextView) convertView.findViewById(R.id.tv_project_taxiadapter);
                viewHolder.vihecleno = (TextView) convertView.findViewById(R.id.tv_vechicle_taxiadapter);
                viewHolder.status = (ImageView) convertView.findViewById(R.id.iv_status_taxiadapter);
                viewHolder.startkm = (TextView) convertView.findViewById(R.id.tv_startkm_taxiadapter);
                viewHolder.endkm = (TextView) convertView.findViewById(R.id.tv_endkm_taxiadapter);
                viewHolder.delete = (TextView) convertView.findViewById(R.id.tv_delete_history);

                viewHolder.formno.setTypeface(face);
                viewHolder.date.setTypeface(face);
                viewHolder.id.setTypeface(face);
                viewHolder.projecttype.setTypeface(face);
                viewHolder.vihecleno.setTypeface(face);
                viewHolder.endkm.setTypeface(face);

                convertView.setTag(viewHolder);

            } else {


                viewHolder = (ViewHolder) convertView.getTag();

            }



            getstatus = opEntryData.getFlag() + "";
            //  String fr = "<u>" + taxiFormData.getFormno() + "</u>";
            //  viewHolder.formno.setText(Html.fromHtml(fr));
            viewHolder.  formno.setText(opEntryData.getProjectNo());
            viewHolder.date.setText(opEntryData.getCurrentdate());
            viewHolder.id.setText(opEntryData.getId() + "");
            viewHolder.projecttype.setText(opEntryData.getOptype());
            viewHolder.vihecleno.setText(opEntryData.getMode());
            viewHolder.startkm.setText(opEntryData.getStartkm());
            viewHolder.endkm.setText(opEntryData.getEndkm());

            if (getstatus.equals("1")) {
                viewHolder.status.setBackgroundResource(R.drawable.success);
                //status.setText("Success");
            } else if (getstatus.equals("0")) {
                viewHolder.status.setBackgroundResource(R.drawable.pending);
                //status.setText("Pending");
            } else if (getstatus.equals("2")) {
                viewHolder.status.setBackgroundResource(R.drawable.upload);

                //status.setText("Retry");
            }


            viewHolder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    opEntryData = searchlist.get(position);
                    getstatus = opEntryData.getFlag() + "";

                    boolean b = getstatus.equals("2");

                    if (b) {
                        new sendDataAsnycTask(opEntryData).execute(AppConstraint.OPENTRY);
                     //   getActivity().startService(new Intent(getActivity(), SendLatiLongiServerIntentService.class));
                    }


                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteitemDialog(position);

                    // getActivity(). getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag, new TaxiFormRecordFragment()).commit();
              /*       FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(TaxiFormRecordFragment.this).attach(TaxiFormRecordFragment.this).commit();
               */
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



               /*     try {
                        if (position <= OPEntryDataArrayList.size()) {
                            Toast.makeText(getContext(),"Please Wait",Toast.LENGTH_LONG).show();
                            TaxiFormData taxiFormData = (TaxiFormData) adapter.getItem(position);
                            String fromNumber = taxiFormData.getFormno();
                            Intent intent = new Intent(getActivity(), RouteMapActivity.class);
                            intent.putExtra(AppConstraint.SELECTEDFORMNUMBER, fromNumber);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }*/
                }
            });


            return convertView;
        }

        private void deleteitemDialog(final int p){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            builder.setTitle("Are you sure delete this item?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // How to remove the selected item?
                    // adapter.remove(adapter.getItem(p));
                    dialog.dismiss();
                }


            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // How to remove the selected item?
                    // adapter.remove(adapter.getItem(p));
                    opEntryData = searchlist.get(p);
                    //  db.deleteSingleRowTaxiformData_ByID(opEntryData.getId());
                    db.deleteOPEntryDataFormno(opEntryData.getProjectNo());
                    db.deleteOPEntryLatLogDataFormno(opEntryData.getProjectNo());

                    searchlist.remove(p);
                    OPHistoryAdapter.this.notifyDataSetChanged();
                }


            });

            AlertDialog alert = builder.create();
            alert.show();
        }

        public void filter(String charText) {
            // charText = charText.toLowerCase(Locale.getDefault());
            searchlist.clear();
            if (charText.length() == 0) {
                searchlist.addAll(opentry_DataArrayList);
            } else {
                for (OPEntryData wp : opentry_DataArrayList) {
                    if (wp.getCurrentdate().contains(charText)) {

                        searchlist.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }




        public void remove(TaxiFormData object) {
            searchlist.remove(object);
            notifyDataSetChanged();
        }

        public List<OPEntryData> getTaxiForm_DataArrayList() {
            return searchlist;
        }

        public void toggleSelection(int position) {
            selectView(position, !mSelectedItemsIds.get(position));
        }

        public void removeSelection() {
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }

        public void selectView(int position, boolean value) {
            if (value)
                mSelectedItemsIds.put(position, value);
            else
                mSelectedItemsIds.delete(position);
            notifyDataSetChanged();
        }

        public int getSelectedCount() {
            return mSelectedItemsIds.size();
        }

        public SparseBooleanArray getSelectedIds() {
            return mSelectedItemsIds;
        }

    }

    private class sendDataAsnycTask extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(getActivity());
        OPEntryData opEntryData;

        sendDataAsnycTask(OPEntryData opEntryData) {
            this.opEntryData = opEntryData;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please Wait...");
            pd.setMessage("Uploaded Records");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.v("opentry json",JSonobjParameter(opEntryData).toString());
            String s = HTTPPostRequestMethod.postMethodforESP(params[0], JSonobjParameter(opEntryData));
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            //  iv_status.setVisibility(View.GONE);
            // GPSTracker.isRunning= false;


            String re = s;
            try {

                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String id = jsonObject.getString("id");

                Log.v("id",id);
                if (status.equals("true")) {
                    flag = 1;
                    int flag = 1;
                    db.updateOPEntryStatus(opEntryData.getId(),flag);

                    Toast.makeText(getActivity(), "Uploaded Successfully...", Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(OPEntryHistoryFragment_new.this).attach(OPEntryHistoryFragment_new.this).commit();
                } else {

                    Toast.makeText(getActivity(), "Internet is not working", Toast.LENGTH_LONG).show();
                    flag = 2;

                }


            } catch (JSONException e) {

                Toast.makeText(getActivity(), "internet is very slow please try again", Toast.LENGTH_LONG).show();
                flag = 2;


                e.printStackTrace();
            }


        }
    }

    private JSONObject JSonobjParameter(OPEntryData opEntryData) {
        String getDate = "";
        JSONObject jsonObject = new JSONObject();

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");

        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");

            Date dtt = df.parse(opEntryData.getCurrentdate());
            Date ds = new Date(dtt.getTime());
            getDate= dateFormat2.format(ds);
            System.out.println(getDate);
            Log.v("datess",getDate);

        } catch (ParseException e) {
            Log.v("datess",e.getMessage());
            e.printStackTrace();
        }

        try {

            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("EmpId", empid);
            jsonObject.put("ProjectNo", opEntryData.getProjectNo());
            jsonObject.put("Mode", opEntryData.getMode());
            jsonObject.put("StartKM", opEntryData.getStartkm());
            jsonObject.put("StartKmImg", opEntryData.getStartkmImage());
            jsonObject.put("EndKM", opEntryData.getEndkm());
            jsonObject.put("EndKMImg", opEntryData.getEndkmImage());
            jsonObject.put("vDate", getDate);
            jsonObject.put("CustomerName", opEntryData.getCustomerName());
            jsonObject.put("OPType", opEntryData.getOptype());
            jsonObject.put("ReasonForOP", opEntryData.getReasonforop());
            jsonObject.put("UserLocation",  opEntryData.getManuallocation());
            jsonObject.put("GPSLocation",  opEntryData.getCurrentLocation());
            jsonObject.put("Remarks", opEntryData.getRemark());
            jsonObject.put("Lat", "0.000");
            jsonObject.put("Log", "0.000");
            jsonObject.put("Status", "1");

      ;
            // Log.v("Taxi json",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void findIDS(View v)
    {
        tv_emid =(TextView)v.findViewById(R.id.tv_empid);
         spi_select_year = (Spinner) v.findViewById(R.id.spi_ophistory_year);
         tv_emid.setText(empid);

    }

    private void selectYear(View v)
    {
        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int curryear = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        year1 = String.valueOf(curryear);
        nextyear =1+Integer.parseInt(year1);

        selectyear_list.add("JAN-"+year1);
        selectyear_list.add("FEB-"+year1);
        selectyear_list.add("MAR-"+year1);
        selectyear_list.add("APR-"+year1);
        selectyear_list.add("MAY-"+year1);
        selectyear_list.add("JUN-"+year1);
        selectyear_list.add("JUL-"+year1);
        selectyear_list.add("AUG-"+year1);
        selectyear_list.add("SEP-"+year1);
        selectyear_list.add("OCT-"+year1);
        selectyear_list.add("NOV-"+year1);
        selectyear_list.add("DEC-"+year1);
        selectyear_list.add("JAN-"+nextyear);
        selectyear_list.add("FEB-"+nextyear);
        selectyear_list.add("MAR-"+nextyear);
        selectyear_list.add("APR-"+nextyear);
        selectyear_list.add("MAY-"+nextyear);
        selectyear_list.add("JUN-"+nextyear);


      ArrayAdapter  adapter_year = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectyear_list);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spi_select_year.setAdapter(adapter_year);
        spi_select_year.setSelection(month);

        spi_select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year2 = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
