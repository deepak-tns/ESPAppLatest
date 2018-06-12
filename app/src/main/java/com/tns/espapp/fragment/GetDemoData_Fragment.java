package com.tns.espapp.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tns.espapp.R;
import com.tns.espapp.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetDemoData_Fragment extends Fragment {

    private TableLayout tl;
    private TableRow tr;
    private CheckBox cb;
    private CheckBox cb_selectAll;


    String key;
    public static final String mypreference = "chk_test";
    List<String> valueFormlist = new ArrayList<>();


    String setFormname;
    DatabaseHandler db;
    List<String> keyFormlist = new ArrayList<>();
    SharedPreferences sharedpreferences;

    public GetDemoData_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_get_demo_data, container, false);

        TextView textview =(TextView)v.findViewById(R.id.set_formane);
        cb_selectAll =(CheckBox)v.findViewById(R.id.chk_selecl_all) ;
        cb.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox chk = (CheckBox) v;
                int itemCount = tr.getChildCount();

                for(int i=0 ; i < itemCount ; i++){
                  //  getListView().setItemChecked(i, chk.isChecked());
                }
            }
        });

        db = new DatabaseHandler(getActivity());

        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        setFormname = getArguments().getString("PARAM1");
       textview.setText(setFormname);
        // getFormlist = db.getAllChecklistwithFormno(setFormname);



        String gettext = getForm_pre(setFormname);

        if(gettext.equals("1")) {
            ArrayList<HashMap<String, String>> mapget = db.getForm(setFormname);

            int size = mapget.size();

            if (size > 0) {
                for (HashMap<String, String> map : mapget) {

                    for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                        key = mapEntry.getKey();
                        String value = mapEntry.getValue();
                        valueFormlist.add(value);
                        keyFormlist.add(key);


                        //  Toast.makeText(getActivity(), value, Toast.LENGTH_LONG).show();
                    }

                }



                Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(keyFormlist); // now let's clear the ArrayList so that we can copy all elements from LinkedHashSet primes.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);

                keyFormlist.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);
                keyFormlist.addAll(primesWithoutDuplicates);

                int keysize = keyFormlist.size();

           setView(v,keyFormlist,valueFormlist);



            } else {


            }

        }


        return v;
    }




    public void setView(View v ,List<String> key, List<String> value)
    {

        tl = (TableLayout)v. findViewById(R.id.main_table);
        TableRow tr_head = new TableRow(getActivity());
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        for(int i=0;i<key.size();i++) {

            TextView label_date = new TextView(getActivity());
            LinearLayout.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
            label_date.setLayoutParams(params);
            label_date.setGravity(Gravity.CENTER);
            label_date.setBackgroundResource(R.drawable.edit_text_design_table);;
            label_date.setId(20+i);
            label_date.setPadding(0,5,0,5);
            label_date.setText(key.get(i));
            label_date.setTextColor(Color.WHITE);
            tr_head.addView(label_date);

            // add the column to the table row here

        }

        tl.addView(tr_head, new TableLayout.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        Integer count = 0;
        TextView labelDATE =null;
        int getcount =value.size();
        int setCounter = 0;
        while(getcount >0)
        {
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
                labelDATE.setBackgroundResource(R.drawable.edit_text_design);;
                String getStr =value.get(setCounter);
                if(getStr.equalsIgnoreCase("0")){

                    labelDATE.setText("");
                      cb = new CheckBox(getActivity());
                    LinearLayout.LayoutParams cbparams = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

                    cb.setLayoutParams(cbparams);
                    cb.setBackgroundResource(R.drawable.edit_text_design);
                    cb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(((CheckBox)v).isChecked()){

                                ViewGroup parentView = (ViewGroup) v.getParent();
                                TableRow tr = (TableRow) parentView;


                                for (int i = 0; i < tr.getChildCount(); i++) {
                                    TextView tv_name;
                                    String st_name = null;
                                    final View view = tr.getChildAt(i);
                                    if (view instanceof TextView) {
                                        tv_name = (TextView) tr.getChildAt(i);
                                        st_name = tv_name.getText().toString();
                                        Toast.makeText(getActivity(), st_name, Toast.LENGTH_LONG).show();
                                    }
                                    if (view instanceof CheckBox) {

                                    }
                                }

//do something
                            }else{
//do something
                            }
                        }
                    });

                    tr.addView(cb);
                    setCounter++;

                }else
                    {

                    labelDATE.setText(value.get(setCounter));
                    labelDATE.setPadding(0, 15, 0, 15);
                    labelDATE.setTextColor(Color.BLACK);
                    tr.addView(labelDATE);
                    setCounter++;
                }
            }

          /* cb = new CheckBox(getActivity());
                 tr.addView(cb);*/

            tl.addView(tr, new TableLayout.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT));
                    getcount=getcount-key.size();

          /*  tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow tr = (TableRow) v;
                    for (int i = 0; i < tr.getChildCount(); i++) {
                        TextView companyTV, valueTV, yearTV;
                        String company = null, value, year;
                        final View view = tr.getChildAt(i);
                        if (view instanceof TextView) {
                            companyTV = (TextView) tr.getChildAt(i);
                            company = companyTV.getText().toString();

                        }
                        if (view instanceof CheckBox) {

                        }
                        Toast.makeText(getActivity(), company, Toast.LENGTH_LONG).show();


                    }
                }
            });*/
            count++;

        }


// finally add this to the table row


    }


    public String getForm_pre(String key ){

        String gets =  sharedpreferences.getString(key, "");
        return gets;
    }


}
