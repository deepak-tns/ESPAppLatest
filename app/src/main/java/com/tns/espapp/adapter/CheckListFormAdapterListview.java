package com.tns.espapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tns.espapp.R;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.fragment.CheckListFragment;
import com.tns.espapp.fragment.CheckListSavedFragment;
import com.tns.espapp.fragment.GetCheckListSavedFragment;

import java.util.List;

/**
 * Created by TNS on 07-Mar-18.
 */

public class CheckListFormAdapterListview extends ArrayAdapter {
    String s;
    Context context;
    int deepColor = Color.parseColor("#FFFFFF");
    int deepColor2 = Color.parseColor("#DCDCDC");
    //  int deepColor3 = Color.parseColor("#B58EBF");
    private int[] colors = new int[]{deepColor, deepColor2};
    private List<String> searchlist = null;
    DatabaseHandler db;

    //   private CustomOnItemClickListener customOnItemClickListener;
    public CheckListFormAdapterListview(Context context, int resource, List<String> lst) {
        super(context, resource, lst);
        this.context = context;
        this.searchlist = lst;
        db = new DatabaseHandler(context);

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView tv_entry, tv_history;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.home_check_list_row_adapter, parent, false);
        tv_entry =(TextView)convertView.findViewById(R.id.tv_chk_entry) ;
        tv_history =(TextView)convertView.findViewById(R.id.tv_chk_history) ;
        //int colorPos = position % colors.length;
        // convertView.setBackgroundColor(colors[colorPos]);
        s = searchlist.get(position);
        if(s.equalsIgnoreCase("New Templete")) {
            tv_entry.setText(s );
            Log.v("Listvalue",s);
            tv_history.setVisibility(View.GONE);

            tv_entry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s2 = searchlist.get(position);
                    ((AppCompatActivity)context). getSupportFragmentManager().beginTransaction().replace(R.id.linear_checklist_siteinfo, CheckListFragment.newInstance_CheckListFragment(position, s2)).commit();


                }
            });


        }else{

            tv_entry.setText(s + " Entry");
            tv_entry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s2 = searchlist.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("PARAM1", s2 );
                    CheckListSavedFragment fragInfo = new CheckListSavedFragment();
                    fragInfo.setArguments(bundle);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.linear_checklist_siteinfo, fragInfo).commit();



                }
            });

            tv_history.setText(s +" History");
            tv_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s2 = searchlist.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("PARAM1", s2 );
                    GetCheckListSavedFragment fragInfo = new GetCheckListSavedFragment();
                    fragInfo.setArguments(bundle);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.linear_checklist_siteinfo, fragInfo).commit();
                }
            });
        }
        return convertView;
    }



}