package com.tns.espapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tns.espapp.DataModel;
import com.tns.espapp.R;

import java.util.ArrayList;

/**
 * Created by GARIMA on 7/14/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DataModel> list;

    // Constructor
    public ImageAdapter(Context c, ArrayList<DataModel> list) {
        mContext = c;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
       DataModel dataModel= (DataModel)list.get(position);
        return dataModel;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView =null;
        TextView textView = null;
        DataModel dataModels = list.get(position);


            LayoutInflater layoutInflater=((Activity) mContext).getLayoutInflater();;
            convertView = layoutInflater.inflate(R.layout.dashboard_row_item, parent, false);

            imageView = (ImageView)convertView.findViewById(R.id.SingleView);
            textView = (TextView)convertView.findViewById(R.id.textView) ;




        imageView.setImageResource(dataModels.getImage());
            textView.setText(dataModels.getText());



        return convertView;
    }




}
