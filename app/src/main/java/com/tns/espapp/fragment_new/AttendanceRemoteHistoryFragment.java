package com.tns.espapp.fragment_new;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.AttendenceRemoteData;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.TaxiFormData;
import com.tns.espapp.fragment.TaxiFormRecordFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRemoteHistoryFragment extends Fragment {
    private DatabaseHandler db;
    private String empid;
    private ArrayList<AttendenceRemoteData> dataArrayList;
    private ListView listView;
    TaxiFormRecordHistoryAdapter adapter;

    public AttendanceRemoteHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_attendance_remote_history, container, false);
        db = new DatabaseHandler(getActivity());

        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);

        dataArrayList = new ArrayList<>();
        listView = (ListView) v.findViewById(R.id.listview_attandence_remote_history);
        List<AttendenceRemoteData> recordDataList = db.getAllAttendanceRemote();
        int size = recordDataList.size();
        if(size >0) {
            dataArrayList.addAll(recordDataList);
             adapter = new TaxiFormRecordHistoryAdapter(getActivity(), R.layout.taxiform_record_history_adapter, dataArrayList);
            listView.setAdapter(adapter);
        }

        return v;
    }




    private class TaxiFormRecordHistoryAdapter extends ArrayAdapter {
        AttendenceRemoteData taxiFormData;
        int deepColor = Color.parseColor("#FFFFFF");
        int deepColor2 = Color.parseColor("#DCDCDC");
        //  int deepColor3 = Color.parseColor("#B58EBF");
        private int[] colors = new int[]{deepColor, deepColor2};
        private List<AttendenceRemoteData> searchlist = null;
        List<AttendenceRemoteData> taxiForm_DataArrayList;
        private SparseBooleanArray mSelectedItemsIds;
        String  getstatus;

        public TaxiFormRecordHistoryAdapter(Context context, int resource, ArrayList<AttendenceRemoteData> latLongDatas) {
            super(context, resource, latLongDatas);
            this.searchlist = latLongDatas;
            this.taxiForm_DataArrayList = new ArrayList<>();
            taxiForm_DataArrayList.addAll(searchlist);
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
            taxiFormData = searchlist.get(position);
           ViewHolder viewHolder = new ViewHolder();

            if (convertView == null) {

                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.taxiform_record_history_adapter, parent, false);
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



                convertView.setTag(viewHolder);

            } else {


                viewHolder = (ViewHolder) convertView.getTag();

            }



            getstatus = taxiFormData.getFlag() + "";
            //  String fr = "<u>" + taxiFormData.getFormno() + "</u>";
            //  viewHolder.formno.setText(Html.fromHtml(fr));
            viewHolder.  formno.setText(position+1 +"");
            viewHolder.date.setText(taxiFormData.getCurrentDate());
           // viewHolder.id.setText(position+"");
            viewHolder.projecttype.setText(taxiFormData.getInTime());
            viewHolder.vihecleno.setText(taxiFormData.getOutTime());
            viewHolder.startkm.setText(taxiFormData.getInlat());
            viewHolder.endkm.setText(taxiFormData.getInlongi());


            if (getstatus.equals("1")) {
                viewHolder.status.setBackgroundResource(R.drawable.success);
                //status.setText("Success");
            } else if (getstatus.equals("0")) {
                viewHolder.status.setBackgroundResource(R.drawable.upload);
                //status.setText("Pending");
            }


            viewHolder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taxiFormData = searchlist.get(position);
                    getstatus = taxiFormData.getFlag() + "";
                    boolean b = getstatus.equals("0");

                    if (b) {

                        sendAllDataServer(taxiFormData);

                     //   new TaxiFormRecordFragment.getDataAsnycTask(taxiFormData).execute(AppConstraint.TAXIFORMURL);
                      //  getActivity().startService(new Intent(getActivity(), SendLatiLongiServerIntentService.class));
                    }


                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteitemDialog(position);

                    // getActivity(). getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag, new TaxiFormRecordFragment()).commit();

                    /*
                       FragmentTransaction ft = getFragmentManager().beginTransaction();
                       ft.detach(TaxiFormRecordFragment.this).attach(TaxiFormRecordFragment.this).commit();
                   */
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
                    taxiFormData = searchlist.get(p);
                    //  db.deleteSingleRowTaxiformData_ByID(taxiFormData.getId());
                    db.deleteAttendanceRemoteListData(taxiFormData.getId());

                    searchlist.remove(p);
                    TaxiFormRecordHistoryAdapter.this.notifyDataSetChanged();
                }


            });

            AlertDialog alert = builder.create();
            alert.show();
        }


        public void remove(TaxiFormData object) {
            searchlist.remove(object);
            notifyDataSetChanged();
        }




    }

    private void sendAllDataServer(final AttendenceRemoteData ad) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstraint.ATTENDANCEREMOTE, JsonParameterSendServer(ad),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.e("!_@@_SUCESS", response + "");

                        Toast.makeText(getActivity(), "Uploaded Successfully...", Toast.LENGTH_LONG).show();
                        db.updateAttandenceRemoteStatus(ad.getId(),ad.getOutTime(),ad.getOuttimeImage(),ad.getOutlat(),ad.getOutlongi(),ad.getIntimeRemark(),ad.getOuttimeNear(),1);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(AttendanceRemoteHistoryFragment.this).attach(AttendanceRemoteHistoryFragment.this).commit();

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Uploaded Failed...", Toast.LENGTH_LONG).show();
                        db.updateAttandenceRemoteStatus(ad.getId(),ad.getOutTime(),ad.getOuttimeImage(),ad.getOutlat(),ad.getOutlongi(),ad.getIntimeRemark(),ad.getOuttimeNear(),0);
                    }
                }) {

        /*
          @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }  */
        };
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest, null);

    }

    private JSONObject JsonParameterSendServer(AttendenceRemoteData ad) {

        JSONObject jsonObject = new JSONObject();

   /*     SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");

        try {

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");

            Date dtt = df.parse(current_date);
            Date ds = new Date(dtt.getTime());
            getDate= dateFormat2.format(ds);
            System.out.println(getDate);
            Log.v("datess",getDate);

        } catch (ParseException e) {
            Log.v("datess",e.getMessage());
            e.printStackTrace();
        }
        */
        try {

            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            //   jsonObject.put("spName", "AndroidattendanceInsert");
            jsonObject.put("EmpId", empid);
            jsonObject.put("InTime", ad.getInTime());
            jsonObject.put("InTimePic", ad.getIntimeImage());
            jsonObject.put("OutTime", ad.getOutTime());
            jsonObject.put("OutTimePic", ad.getOuttimeImage());
            jsonObject.put("InLat", ad.getInlat());
            jsonObject.put("InLog", ad.getOutlat());
            jsonObject.put("InRemark",ad.getIntimeRemark());
            jsonObject.put("OutLat", ad.getOutlat());
            jsonObject.put("OutLog", ad.getOutlongi());
            jsonObject.put("OutRemark", ad.getOuttimeRemark());
            jsonObject.put("LocationType", "Anywhere");
            jsonObject.put("RadialDistance", "0");
            jsonObject.put("Date", ad.getCurrentDate());
            //  jsonObject.put("ParameterList", jsonArrayParameter);

            Log.i("Json Param",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
