package com.tns.espapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.NotificationData;
import com.tns.espapp.push_notification.UpdateNotificationData;

import java.util.Comparator;
import java.util.List;

public class ReadNotificationActivity extends AppCompatActivity {

    DatabaseHandler db;
    MyCustomAdapter adapter;
    TextView tv_set_notifu;
    private TextView notificationTextView;
    SharedPreferenceUtils sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_notification);
        db = new DatabaseHandler(this);





        /*
        ImageView iv = (ImageView)findViewById(R.id.iv_notification_back) ;
        iv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                finish();
            }


        });

       */




        ListView listView = (ListView) findViewById(R.id.lst_notification);

        List<NotificationData> getdata = db.getAllNotification();


        adapter = new MyCustomAdapter(getApplicationContext(), R.layout.notificationmessageui, getdata);
     /*   adapter.sort(new Comparator<NotificationData>() {
            public int compare(NotificationData object1, NotificationData object2) {
                return object1.getNoti_tittle().compareToIgnoreCase(object2.getNoti_tittle());
            }


        });*/
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (getdata.size() > 0) {
            for (NotificationData data : getdata) {

                // Toast.makeText(getApplicationContext(),data.getNoti_message(),Toast.LENGTH_LONG).show();
                // tv_set_notifu.append(data.getNoti_message()+"\n");
            }


        }


    }

    private void setFontFamily(TextView Tv) {
        Typeface face = Typeface.createFromAsset(getAssets(),
                "arial.ttf");

        Tv.setTypeface(face);


    }




    private class MyCustomAdapter extends ArrayAdapter {
        Animation scaleUp;
        List<NotificationData> notificationDataArraylist;

        public MyCustomAdapter(@NonNull Context context, @LayoutRes int resource, List<NotificationData> datas) {
            super(context, resource, datas);
            this.notificationDataArraylist = datas;
            scaleUp = AnimationUtils.loadAnimation(context, R.anim.zoon_in);
        }


        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ViewHolder viewHolder = new ViewHolder();

            if (convertView == null) {

                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationmessageui, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_notification_message);
                viewHolder.time = (TextView) convertView.findViewById(R.id.tv_notification_time);
                viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_notification_delete);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }
            setFontFamily(viewHolder.textView);
            setFontFamily(viewHolder.time);

            final NotificationData data = notificationDataArraylist.get(position);

            //  viewHolder.textView.setAnimation(scaleUp);

            viewHolder.textView.setText(data.getNoti_message());
            viewHolder.time.setText(data.getNoti_tittle());
            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.deleteSingleRow(data.getNoti_message());

                    notificationDataArraylist.remove(data.getNoti_message());

                    adapter.remove(adapter.getItem(position));
                    adapter.notifyDataSetChanged();
                }
            });


            return convertView;

        }

        private class ViewHolder {
            TextView textView;
            TextView time;
            ImageView iv_delete;


        }

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            tv_set_notifu.setText(message);

            //do other stuff here
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter("get"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }
}
