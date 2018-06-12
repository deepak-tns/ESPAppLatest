package com.tns.espapp.activity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.tns.espapp.AppConstraint;
import com.tns.espapp.DataModel;
import com.tns.espapp.ListviewHelper;
import com.tns.espapp.R;
import com.tns.espapp.TaxiTest;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.fragment.BlankFragment;
import com.tns.espapp.fragment.CheckListFragment;
import com.tns.espapp.fragment.CheckListSavedFragment;
import com.tns.espapp.fragment.SurveyFormDynamicRowFragment;
import com.tns.espapp.fragment.GetCheckListSavedFragment;
import com.tns.espapp.fragment.HomeFragment;
import com.tns.espapp.fragment_new.AccountStatementFragment_new;
import com.tns.espapp.fragment_new.AttendenceFragment_new;
import com.tns.espapp.fragment_new.BillInfoFragment_new;
import com.tns.espapp.fragment_new.EntitlementInfoFragment_new;
import com.tns.espapp.fragment_new.LeaveApplyFragment_new;
import com.tns.espapp.fragment.LeaveApprovalFragment;
import com.tns.espapp.fragment_new.LeaveLadgerFragment_new;
import com.tns.espapp.fragment_new.LeaveSummaryFragment_new;
import com.tns.espapp.fragment_new.LeaveTransactionFragment_new;
import com.tns.espapp.fragment.LocationHistoryFragment;
import com.tns.espapp.fragment.OPApprovalFragment;
import com.tns.espapp.fragment_new.MiscellaneousFragment_new;
import com.tns.espapp.fragment_new.OPEntryFragment_new;
import com.tns.espapp.fragment.OPHistoryFragment;
import com.tns.espapp.fragment_new.OPEntryHistoryFragment_new;
import com.tns.espapp.fragment_new.PernsonalInfoFragment_new;
import com.tns.espapp.fragment.ReadNotificationFragment;
import com.tns.espapp.fragment.RouteMapFragment;
import com.tns.espapp.fragment.ScheduleFragment;
import com.tns.espapp.fragment.SettingFragment;
import com.tns.espapp.fragment.StoreInfoFragment;
import com.tns.espapp.fragment.TDSDeductionFragment;
import com.tns.espapp.fragment.TaxiFormFragment;
import com.tns.espapp.fragment.TaxiFormRecordFragment;
import com.tns.espapp.fragment.TicketFragment;
import com.tns.espapp.fragment.WelcomeNewJoineeFragment;
import com.tns.espapp.fragment_new.SalaryInfoFragment_new;
import com.tns.espapp.service.SendLatiLongiServerIntentService;
import com.tns.espapp.service.SendOPEntryLatiLongiServerIntentService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.tns.espapp.R.dimen.largeTextSize;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    final int CROP_PIC = 2;
    private Uri picUri;
    private BroadcastReceiver mReceiver;
    public static final String MyPREFERENCES = "MyPre";
    public static final String key = "nameKey";
    SharedPreferenceUtils sharedPreferencesUtlis;
    Bitmap btMap;
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView imageView;
    boolean checklist_flag = true;
    private ArrayList<DataModel> list;
    private ListView lst_check_list;
    private int notificationCounter;
    private SharedPreferences sharedPreferences;
    private ListView personalListView;
    private BroadcastReceiver mMyBroadcastReceiver;
    String[] subreports = new String[]{"Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};

    String[] permissions = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECEIVE_BOOT_COMPLETED"
    };

    public static final int MULTIPLE_PERMISSIONS = 10;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private RelativeLayout notificationLayout;
    private TextView tv_taxiform;
    private Button notificationButton;
    private TextView tv_userhomeid;
    private TextView tv_location_history;
    private TextView getTv_taxiform_record;
    private TextView tv_toolbar;
    private TextView tv_feedback;
    private TextView tv_locationmap;
    private TextView tv_notification;

    private TextView infoBullteinTV;
    private TextView ticketTv;
    private TextView leaveApplyTV;
    private TextView leaveSummaryTv;
    private TextView leaveTransactionTv;
    private TextView leaveLedgerTv;
    private TextView leaveTv;
    private TextView tv_checklist;

    private TextView tv_dynamicfeedback;
    private TextView tv_setting;
    private TextView approveTv;
    private TextView leaveApprove;
    private TextView opApprove;
    private TextView storeAccountTv;
    private TextView storeInfoTv;
    private TextView accountStatementsTv;
    private TextView billsInfoTv;
    private TextView entitlementInfoTv;
    private TextView attendanceTv;
    private TextView OPTV;
    private TextView OPEntryTv;
    private TextView OPHistoryTV;
    private TextView TDSDeductionTv;
    private TextView salaryTv;
    private TextView salaryInfoTv;
    private TextView feedBackInfoTv;
    private TextView taxiInfoTv;
    private TextView locationInfoTv;
    private TextView scheduleTv;
    private TextView VechicleTrakerTextView;
    private TextView locationTextView;
    private TextView OPTextView;
    private TextView taxiformrecord_live_taxi;
    private static TextView badgeNotification;
    private ImageView leaveImageView;
    private ImageView approveImageView;
    private ImageView OPImageView;
    private ImageView storeImageView;
    private ImageView salaryImageView;
    private ImageView feedBackImageView;
    private ImageView taxiImageView;
    private ImageView locationImageView;
    private LinearLayout linear_checklist;
    private LinearLayout mDrawerPane;
    private LinearLayout leaveLinearLayout;
    private LinearLayout approvalInfoLinearLayout;
    private LinearLayout approvalLinearlayout;
    private LinearLayout leaveInfoLinearLayout;
    private LinearLayout storeInfoLinearLayout;
    private LinearLayout storeAndAccountLinearLayout ;
    private LinearLayout OPInfoLinearLayout ;
    private LinearLayout OPLinearLayout;
    private LinearLayout salaryInfoLinearLayout ;
    private LinearLayout salaryLinearLayout ;
    private LinearLayout feedBackLinearLayout ;
    private LinearLayout feedBackInfoLinearLayout ;
    private LinearLayout taxiLinearLayout ;
    private LinearLayout locationLinearLayout ;
    private LinearLayout linear_checklist_header;

    private  RelativeLayout  re_home,re_welcomejoinee,re_personaklinfo,re_infobulletin,re_attendance,re_opentry,re_opentry_history,re_setting;
    private Toast toast;
    private long lastBackPressTime = 0;
    private Boolean personalFlag = true;
    private Boolean leaveFlag = true;
    private Boolean approvalFlag = true;
    private Boolean storeFlag = true;
    private Boolean OPFlag = true;
    private Boolean salaryFlag = true;
    private Boolean feedBackFlag = true;
    private Boolean taxiFlag = true;
    private Boolean locationFlag = true;
    private ScrollView scrollView;
    private Typeface face;
    private  DatabaseHandler db;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //selectColorChangeLayout();
        face  = Typeface.createFromAsset(getAssets(), "arial.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        scrollView = (ScrollView)findViewById(R.id.home_scroll);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        tv_toolbar = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        tv_toolbar.setTypeface(face);
        db = new DatabaseHandler(this);
        tv_toolbar.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(largeTextSize));
        //  db.updatedetails(incri_id, edt_settaxiform_date.getText().toString(), form_no, edtproject_type.getText().toString(), edt_vehicle_no.getText().toString(), edtstartkmtext.getText().toString(), startkmImageEncodeString, edtendkmtext.getText().toString(), endkmImageEncodeString, flag, edt_siteno.getText().toString(), edt_remark.getText().toString());
        // db.updatedetails(20, "08-09-17", "16865/080917/003", "test app", "testing", "0", "16865start12464.jpg", "0", "16865end12464.jpg", 1, "0", "test");

        // db.deleteSingleRowTaxiformData_ByID(6);
        // db.deleteAllRowLatLongData("16865/060118/003");
        navigationdrawer();
        findIDS();
        startService(new Intent(getApplication(), SendLatiLongiServerIntentService.class));
        startService(new Intent(getApplication(), SendOPEntryLatiLongiServerIntentService.class));
        if (savedInstanceState == null) {
            if (checkPermissions()) {
            }
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag, HomeFragment.newInstance(1)).commit();
        }
        SharedPreferences preferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        tv_userhomeid.setText(preferences.getString("empid", ""));
        setProfileImg();
        ImageView icon_logout = (ImageView) findViewById(R.id.icon_logout);
        icon_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

    }


    private void findIDS() {
        notificationLayout = (RelativeLayout)findViewById(R.id.badge_layout1);
        tv_taxiform = (TextView) findViewById(R.id.tv_taxiform_homeactivity);
        tv_userhomeid = (TextView) findViewById(R.id.tv_userhome_id);
        tv_location_history = (TextView) findViewById(R.id.location_history);
        getTv_taxiform_record = (TextView) findViewById(R.id.taxiformrecord_history_home);
        tv_feedback = (TextView) findViewById(R.id.tv_feedback);
      //  tv_feedback_history = (TextView) findViewById(R.id.tv_feedback_history);
        tv_locationmap = (TextView) findViewById(R.id.tv_currentlocation);
        notificationButton = (Button) findViewById(R.id.notificationButton);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        tv_checklist = (TextView) findViewById(R.id.tv_checklist);
        linear_checklist_header = (LinearLayout) findViewById(R.id.linear_checklist_header);
        tv_dynamicfeedback = (TextView) findViewById(R.id.tv_dynamicfeedback);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        leaveApprove = (TextView) findViewById(R.id.leaveApproveTV);
        opApprove = (TextView) findViewById(R.id.opApproveTV);
        taxiInfoTv = (TextView) findViewById(R.id.taxiInfoTv);
        lst_check_list = (ListView) findViewById(R.id.listviewreports);
        leaveImageView = (ImageView)findViewById(R.id.leaveImageView);
        storeImageView = (ImageView)findViewById(R.id.storeImageView);
        salaryImageView = (ImageView)findViewById(R.id.salaryImageView);
        approveImageView = (ImageView)findViewById(R.id.approveImageView);
        OPImageView = (ImageView)findViewById(R.id.OPImageView);
        taxiImageView = (ImageView)findViewById(R.id.taxiImageView);
        feedBackImageView = (ImageView)findViewById(R.id.feedBackImageView);
        linear_checklist = (LinearLayout) findViewById(R.id.linear_checklist);
        leaveApplyTV= (TextView) findViewById(R.id.leaveApplyTV);
        OPEntryTv = (TextView)findViewById(R.id.OPEntryTv);
        OPHistoryTV = (TextView)findViewById(R.id.OPHistoryTV);
        leaveSummaryTv= (TextView) findViewById(R.id.leaveSummaryTv);
        approveTv= (TextView) findViewById(R.id.approvalTv);
        leaveTransactionTv= (TextView) findViewById(R.id.leaveTransactionTv);
        leaveLedgerTv= (TextView) findViewById(R.id.leaveLedgerTv);
        leaveTv = (TextView) findViewById(R.id.leaveTv);
        ticketTv = (TextView) findViewById(R.id.ticketTv);
        storeInfoTv = (TextView)findViewById(R.id.storeInfoTv);
        accountStatementsTv = (TextView)findViewById(R.id.accountStatementsTv);
        billsInfoTv = (TextView)findViewById(R.id.billsInfoTv);
        entitlementInfoTv = (TextView)findViewById(R.id.entitlementInfoTv);
       // infoBullteinTV= (TextView) findViewById(R.id.infoBullteinTV);
       // personalInfoTV= (TextView) findViewById(R.id.personalInfoTV);
        attendanceTv= (TextView) findViewById(R.id.attendanceTv);
        VechicleTrakerTextView= (TextView) findViewById(R.id.VechicleTrakerTextView);
        OPTV= (TextView) findViewById(R.id.OPTV);
        salaryInfoTv= (TextView) findViewById(R.id.salaryInfoTv);
        salaryTv= (TextView) findViewById(R.id.salaryTv);
        feedBackInfoTv= (TextView) findViewById(R.id.feedBackInfoTv);
        TDSDeductionTv= (TextView) findViewById(R.id.TDSDeductionTv);
        leaveLinearLayout = (LinearLayout)findViewById(R.id.leaveLinearLayout);
        salaryInfoLinearLayout = (LinearLayout)findViewById(R.id.salaryInfoLinearLayout);
        leaveInfoLinearLayout = (LinearLayout)findViewById(R.id.leaveInfoLinearLayout);
        approvalInfoLinearLayout = (LinearLayout)findViewById(R.id.approvalInfoLinearLayout);
        OPLinearLayout = (LinearLayout)findViewById(R.id.OPLinearlayout);
        approvalLinearlayout = (LinearLayout)findViewById(R.id.approvalLinearlayout);
        storeInfoLinearLayout = (LinearLayout)findViewById(R.id.storeInfoLinearLayout);
        storeAccountTv = (TextView)findViewById(R.id.storeAccountTv);
        storeAndAccountLinearLayout = (LinearLayout)findViewById(R.id.storeAndAccountLinearlayout);
        OPInfoLinearLayout = (LinearLayout)findViewById(R.id.OPInfoLinearLayout);
        salaryLinearLayout = (LinearLayout)findViewById(R.id.salaryLinearLayout);
        feedBackLinearLayout = (LinearLayout)findViewById(R.id.feedBackLinearLayout);
        feedBackInfoLinearLayout = (LinearLayout)findViewById(R.id.feedBackInfoLinearLayout);
        taxiLinearLayout = (LinearLayout)findViewById(R.id.taxiLinearLayout);
        locationLinearLayout = (LinearLayout)findViewById(R.id.locationLinearLayout);
        locationInfoTv = (TextView)findViewById(R.id.locationInfoTv);
        scheduleTv = (TextView)findViewById(R.id.tv_schedule);
        locationImageView = (ImageView)findViewById(R.id.locationImageView);
        locationTextView = (TextView) findViewById(R.id.LocationTextView);
        OPTextView = (TextView) findViewById(R.id.OPTextView);
        badgeNotification = (TextView) findViewById(R.id.badge_notification_1);
        taxiformrecord_live_taxi = (TextView) findViewById(R.id.taxiformrecord_live_taxi);
        taxiformrecord_live_taxi.setOnClickListener(this);

       // feedBackInfoTv.setOnClickListener(this);
        locationTextView.setOnClickListener(this);
        locationInfoTv.setOnClickListener(this);
        locationImageView.setOnClickListener(this);
        taxiInfoTv.setOnClickListener(this);
        taxiImageView.setOnClickListener(this);
       // feedBackImageView.setOnClickListener(this);
        tv_taxiform.setOnClickListener(this);
        tv_location_history.setOnClickListener(this);
      //  tvpersomalinfo.setOnClickListener(this);
        getTv_taxiform_record.setOnClickListener(this);
        tv_feedback.setOnClickListener(this);
       // tv_feedback_history.setOnClickListener(this);
        tv_locationmap.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
      //  tv_taxiform_home_fragment.setOnClickListener(this);
        tv_checklist.setOnClickListener(this);
        linear_checklist_header.setOnClickListener(this);
        tv_dynamicfeedback.setOnClickListener(this);

       // tv_setting.setOnClickListener(this);
        linear_checklist.setOnClickListener(this);
        leaveApplyTV.setOnClickListener(this);

      //  infoBullteinTV.setOnClickListener(this);
        ticketTv.setOnClickListener(this);
       // leaveTv.setOnClickListener(this);
        leaveSummaryTv.setOnClickListener(this);
        leaveTransactionTv.setOnClickListener(this);
        leaveLedgerTv.setOnClickListener(this);
        leaveInfoLinearLayout.setOnClickListener(this);
        leaveImageView.setOnClickListener(this);

        approvalInfoLinearLayout.setOnClickListener(this);
        approveImageView.setOnClickListener(this);
        approveTv.setOnClickListener(this);
        storeInfoTv.setOnClickListener(this);
        accountStatementsTv.setOnClickListener(this);
        billsInfoTv.setOnClickListener(this);
        entitlementInfoTv.setOnClickListener(this);
        opApprove.setOnClickListener(this);
        leaveApprove.setOnClickListener(this);
        storeInfoLinearLayout.setOnClickListener(this);
       // storeAccountTv.setOnClickListener(this);
        storeImageView.setOnClickListener(this);
      //  OPEntryTv.setOnClickListener(this);
        OPHistoryTV.setOnClickListener(this);
       // attendanceTv.setOnClickListener(this);
        //OPTV.setOnClickListener(this);
        salaryInfoTv.setOnClickListener(this);
        salaryImageView.setOnClickListener(this);
        OPInfoLinearLayout.setOnClickListener(this);
        storeAndAccountLinearLayout.setOnClickListener(this);
        salaryInfoLinearLayout.setOnClickListener(this);
        OPLinearLayout.setOnClickListener(this);
        salaryLinearLayout.setOnClickListener(this);
        salaryTv.setOnClickListener(this);
        TDSDeductionTv.setOnClickListener(this);
        scheduleTv.setOnClickListener(this);
        VechicleTrakerTextView.setOnClickListener(this);
        OPTextView.setOnClickListener(this);
        notificationLayout.setOnClickListener(this);
       /* sharedPreferencesUtlis = SharedPreferenceUtils.getInstance();
        sharedPreferencesUtlis.setContext(getApplicationContext());
        badgeNotification.setText();*/

        re_home = (RelativeLayout) findViewById(R.id.re_home);
        re_welcomejoinee = (RelativeLayout) findViewById(R.id.re_welcomejoinee);
        re_personaklinfo = (RelativeLayout) findViewById(R.id.re_personalinfo);
        re_infobulletin = (RelativeLayout) findViewById(R.id.re_infobulletin);
        re_attendance = (RelativeLayout) findViewById(R.id.re_attendance);
        re_opentry=(RelativeLayout) findViewById(R.id.re_opentry);
        re_opentry_history=(RelativeLayout) findViewById(R.id.re_opentry_history);
        re_setting=(RelativeLayout) findViewById(R.id.re_setting);

        re_home.setOnClickListener(this);
        re_welcomejoinee.setOnClickListener(this);
        re_personaklinfo.setOnClickListener(this);
        re_infobulletin .setOnClickListener(this);
        re_attendance .setOnClickListener(this);
        re_opentry.setOnClickListener(this);
        re_opentry_history.setOnClickListener(this);
        re_setting.setOnClickListener(this);
        feedBackInfoLinearLayout.setOnClickListener(this);


    }


    private void navigationdrawer() {
        mDrawerPane = (LinearLayout) findViewById(R.id.drawerPane);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility


        );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public void onClick(View v) {

        if (v == tv_taxiform) {
            // tv_toolbar.setText("Taxi" + "Form");
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new TaxiFormFragment()).addToBackStack(null).commit();
            taxiLinearLayout.setVisibility(View.GONE);
            mDrawerLayout.closeDrawer(mDrawerPane);


        } else if (v == tv_location_history) {
            //tv_toolbar.setText("Location History");
            locationLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new LocationHistoryFragment()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        } else if (v == re_personaklinfo) {
            /*tv_toolbar.setText("Personal Info");*/
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new PernsonalInfoFragment_new()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        } else if (v == getTv_taxiform_record) {
           /* tv_toolbar.setText("TaxiForm History");*/
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new TaxiFormRecordFragment()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
            taxiLinearLayout.setVisibility(View.GONE);


        }  else if (v == taxiformrecord_live_taxi) {
           /* tv_toolbar.setText("TaxiForm History");*/
           /* getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new LiveTaxi()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
            taxiLinearLayout.setVisibility(View.GONE);*/
           startActivity(new Intent(this, TaxiTest.class));
        }

        else if (v == tv_feedback) {
           /* tv_toolbar.setText("FeedBack");*/
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new SurveyFormDynamicRowFragment()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
            feedBackLinearLayout.setVisibility(View.GONE);

        }/* else if (v == tv_feedback_history) {
          *//*  tv_toolbar.setText("FeedBack History");*//*
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new FeedbackFragmentHistory()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
            feedBackLinearLayout.setVisibility(View.GONE);

        }*/ else if (v == tv_locationmap) {
            /*tv_toolbar.setText("Current Location  ");*/
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new RouteMapFragment()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
            locationLinearLayout.setVisibility(View.GONE);
        }
        if (v == tv_notification) {
          /*  tv_toolbar.setText("Notification");*/
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new ReadNotificationFragment()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }

        if (v == notificationButton) {
            badgeNotification = (TextView) findViewById(R.id.badge_notification);
            sharedPreferencesUtlis = SharedPreferenceUtils.getInstance();
            sharedPreferencesUtlis.setContext(getApplicationContext());
            sharedPreferencesUtlis.putInteger(AppConstraint.NOTIFICATIONCOUNTER,0);
            badgeNotification.setVisibility(View.GONE);
            Intent intent = new Intent(HomeActivity.this,ReadNotificationActivity.class);
            startActivity(intent);
        }

        if(v==VechicleTrakerTextView)
        {
            Intent intent = new Intent(HomeActivity.this,VehicleTrakerHomeActivity.class);
            startActivity(intent);
            //  finish();
        }
        if(v==OPTextView)
        {
            Intent intent = new Intent(HomeActivity.this,OPActivity.class);
            startActivity(intent);
            //  finish();
       }
        if(v==locationTextView)
        {
            Intent intent = new Intent(HomeActivity.this,LocationActivity.class);
            startActivity(intent);
            // finish();
        }

        if (v == re_home) {
           /* tv_toolbar.setText("ESP");*/
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag,new  BlankFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);

        }
        if (v == tv_checklist || v== linear_checklist_header) {
           // tv_toolbar.setText("CheckList");
            if (checklist_flag) {
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 200);

                linear_checklist.setVisibility(View.VISIBLE);
                checklist_flag = false;
            } else {
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                }, 200);

                linear_checklist.setVisibility(View.GONE);
                checklist_flag = true;
            }

            List<ChecklistData> checklistDatas = db.getAllChecklist();
            ArrayList<String> stringArrayList = new ArrayList<>();
            stringArrayList.add("New Templete");

            if(checklistDatas.size() >0){
                for(ChecklistData c : checklistDatas){
                    stringArrayList.add(c.getFormno());
                }
            }
            Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(stringArrayList); // now let's clear the ArrayList so that we can copy all elements from LinkedHashSet primes.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);

            stringArrayList.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);
            stringArrayList.addAll(primesWithoutDuplicates);

            CheckListFormAdapterListview adapter = new CheckListFormAdapterListview(HomeActivity.this, R.layout.home_check_list_row_adapter,stringArrayList );

            lst_check_list.setAdapter(adapter);
            ListviewHelper.getListViewSize(lst_check_list);
            adapter.notifyDataSetChanged();

      /*
            lst_check_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = (String) parent.getItemAtPosition(position);

                    sublistdata(position, name);

                    linear_checklist.setVisibility(View.GONE);
                    checklist_flag = true;
                }

            });

     */
        }
      /*  if (v == tv_dynamicfeedback) {
            // tv_toolbar.setText("Setting");
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new SurveyFormDynamicRowFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);

        }*/

        if (v == re_setting) {
           // tv_toolbar.setText("Setting");
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new SettingFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);

        }
        if(v==re_welcomejoinee)
        {
            /*tv_toolbar.setText("Welcome");*/
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new WelcomeNewJoineeFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }


        if(v == re_infobulletin)
        {
            /*tv_toolbar.setText("Info Bulltein ");*/
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new MiscellaneousFragment_new()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == ticketTv)
        {
          /*  tv_toolbar.setText("Ticket/Grievance");*/
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new TicketFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == leaveApplyTV)
        {
            /*tv_toolbar.setText("Leave Apply");*/
            leaveLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new LeaveApplyFragment_new()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == leaveLedgerTv)
        {
            // tv_toolbar.setText("Leave Ledger");
            leaveLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new LeaveLadgerFragment_new()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == leaveSummaryTv)
        {
           // tv_toolbar.setText("Leave Summary");
            leaveLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new LeaveSummaryFragment_new()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == leaveTransactionTv)
        {
          // tv_toolbar.setText("Leave Transaction");
            leaveLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new LeaveTransactionFragment_new()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == leaveTv || v == leaveImageView || v == leaveInfoLinearLayout)
        {  if (leaveFlag) {

            leaveLinearLayout.setVisibility(View.VISIBLE);
            //showPersonalMenuList();
            leaveFlag = false;
        } else {
            leaveLinearLayout.setVisibility(View.GONE);
            leaveFlag = true;
        }
        }
        if(v == approveTv || v == approveImageView || v == approvalInfoLinearLayout)
        {  if (approvalFlag) {

            approvalLinearlayout.setVisibility(View.VISIBLE);
            //showPersonalMenuList();
            approvalFlag = false;
        } else {
            approvalLinearlayout.setVisibility(View.GONE);
            approvalFlag = true;
        }
        }
        if(v == leaveApprove)
        {
           // tv_toolbar.setText("Leave Approve");
            leaveLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new LeaveApprovalFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == opApprove)
        {
           // tv_toolbar.setText("OP Approve");
            leaveLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new OPApprovalFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }

        if(v ==storeAccountTv || v==storeImageView || v == storeInfoLinearLayout)
        {
            if (storeFlag) {
                storeAndAccountLinearLayout.setVisibility(View.VISIBLE);
                //showPersonalMenuList();
                storeFlag = false;
            } else {
                storeAndAccountLinearLayout.setVisibility(View.GONE);
                storeFlag = true;
            }
        }
        if(v == storeInfoTv)
        {
            // tv_toolbar.setText("Store Info");
            storeAndAccountLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new StoreInfoFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == accountStatementsTv)
        {
          // tv_toolbar.setText("Account Statement");
            storeAndAccountLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new AccountStatementFragment_new()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == billsInfoTv)
        {
            // tv_toolbar.setText("Bills Info");
            storeAndAccountLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new BillInfoFragment_new()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == entitlementInfoTv)
        {
           // tv_toolbar.setText("Entitlement Info");
            storeAndAccountLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new EntitlementInfoFragment_new()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v == re_attendance)
        {
            // tv_toolbar.setText("Attendance Info");
            storeAndAccountLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new AttendenceFragment_new()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v==OPInfoLinearLayout || v == OPTV || v ==OPImageView)
        {
            if (OPFlag) {

                OPLinearLayout.setVisibility(View.VISIBLE);
                //showPersonalMenuList();
                OPFlag = false;
            } else {
                OPLinearLayout.setVisibility(View.GONE);
                OPFlag = true;
            }
        }
        if(v ==re_opentry)
        {
           // tv_toolbar.setText("OP Entry");
            OPLinearLayout.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
          //  transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
            transaction.replace(R.id.frameLayout_home_frag, new OPEntryFragment_new());
            transaction.commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v ==re_opentry_history)
        {
           // tv_toolbar.setText("OP Entry");
            OPLinearLayout.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //  transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
            transaction.replace(R.id.frameLayout_home_frag, new OPEntryHistoryFragment_new());
            transaction.commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }

        if(v ==OPHistoryTV)
        {
          // tv_toolbar.setText("OP History");
            OPLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new OPHistoryFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v ==salaryInfoLinearLayout || v==salaryInfoTv ||v ==salaryImageView)
        {
            if (salaryFlag) {

                salaryLinearLayout.setVisibility(View.VISIBLE);
                //showPersonalMenuList();
                salaryFlag = false;
            } else {
                salaryLinearLayout.setVisibility(View.GONE);
                salaryFlag = true;
            }
        }
        if(v==salaryTv)
        {
            /*tv_toolbar.setText("Salary Info");*/
            OPLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new SalaryInfoFragment_new()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v==TDSDeductionTv)
        {
           // tv_toolbar.setText("TDS Declaration form");
            OPLinearLayout.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new TDSDeductionFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        if(v==feedBackInfoLinearLayout)
        {
            if (feedBackFlag) {
                feedBackLinearLayout.setVisibility(View.VISIBLE);
                //showPersonalMenuList();
                feedBackFlag = false;
            } else {
                feedBackLinearLayout.setVisibility(View.GONE);
                feedBackFlag = true;
            }
        }
        if(v==taxiInfoTv || v == taxiImageView)
        {
            if (taxiFlag) {
                taxiLinearLayout.setVisibility(View.VISIBLE);
                taxiFlag = false;
            } else {
                taxiLinearLayout.setVisibility(View.GONE);
                taxiFlag = true;
            }
        }
        if( v==locationInfoTv ||v ==locationImageView)
        {
            if (locationFlag) {
                locationLinearLayout.setVisibility(View.VISIBLE);
                //showPersonalMenuList();
                locationFlag = false;
            } else {
                locationLinearLayout.setVisibility(View.GONE);
                locationFlag = true;
            }
        }
        if(v==scheduleTv)
        {
           // tv_toolbar.setText("Scheduler");

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new ScheduleFragment()).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
    }


    @Override
    protected void onResume() {

        super.onResume();
        badgeNotification = (TextView)findViewById(R.id.badge_notification);
        sharedPreferencesUtlis = SharedPreferenceUtils.getInstance();
        sharedPreferencesUtlis.setContext(getApplicationContext());
        notificationCounter = sharedPreferencesUtlis.getInteger(AppConstraint.NOTIFICATIONCOUNTER);

        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");
        if(notificationCounter ==0)
        {
            badgeNotification.setVisibility(View.GONE);
        }
        else
        {
            badgeNotification.setVisibility(View.VISIBLE);
            badgeNotification.setText(String.valueOf(notificationCounter));

        }
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                int  notificationCounter = intent.getIntExtra(AppConstraint.NOTIFICATIONCOUNTER,0);

                if(notificationCounter ==0)
                {
                    badgeNotification.setVisibility(View.GONE);
                }
                else
                {
                    badgeNotification.setVisibility(View.VISIBLE);
                    badgeNotification.setText(String.valueOf(notificationCounter));

                }
                //log our message value


            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }





    private void sublistdata(int position, String frgname) {
        int id = position;

        if(frgname.equals("New Templete")) {
            mDrawerLayout.closeDrawer(mDrawerPane);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, CheckListFragment.newInstance_CheckListFragment(position, frgname)).commit();
        }else {

            Bundle bundle = new Bundle();

            bundle.putString("PARAM1", frgname );
            CheckListSavedFragment fragInfo = new CheckListSavedFragment();
            fragInfo.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, fragInfo).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
        }

    }


    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            alertdiaolog();
           // alertdiaologbackbutton();
        }
    }


    public void alertdiaologbackbutton() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_LONG);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
            mDrawerLayout.openDrawer(mDrawerPane);

        } else {
            if (toast != null) {
                toast.cancel();
               finish();
            }

        }
    }

    public  void alertdiaolog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Exit Application?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, close
                                // current activity
                               // startActivity(new Intent(CardLogin.this, MainActivity.class));

                                finishAffinity();

                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }




    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMyBroadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "permission not Granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }


    }



    private void setProfileImg() {
        imageView = (ImageView) findViewById(R.id.profile_image);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {

            String u = sharedPreferences.getString(key, "");
            btMap = decodeBase64(u);

            imageView.setImageBitmap(btMap);
        }

        ImageView btnLoadImage = (ImageView) findViewById(R.id.img_edt);
        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } catch (ActivityNotFoundException anfe) {
                    Toast toast = Toast.makeText(HomeActivity.this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            picUri = data.getData();
            performCrop();

        } else if (requestCode == CROP_PIC) {
            // get the returned data
            Bundle extras = data.getExtras();
            // get the cropped bitmap
            btMap = extras.getParcelable("data");
            imageView.setImageBitmap(btMap);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, encodeTobase64(btMap));
            editor.commit();
        }

    }


    public static String encodeTobase64(Bitmap image) {
        Bitmap image1 = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image1.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncode = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("image log", imageEncode);
        return imageEncode;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodeByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop

            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 250);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast.makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
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
                tv_history.setVisibility(View.GONE);

                tv_entry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String s2 = searchlist.get(position);

                        mDrawerLayout.closeDrawer(mDrawerPane);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, CheckListFragment.newInstance_CheckListFragment(position, s2)).commit();
                        linear_checklist.setVisibility(View.GONE);
                        checklist_flag = true;
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

                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, fragInfo).commit();
                        mDrawerLayout.closeDrawer(mDrawerPane);
                        linear_checklist.setVisibility(View.GONE);
                        checklist_flag = true;
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, fragInfo).commit();
                        mDrawerLayout.closeDrawer(mDrawerPane);
                        linear_checklist.setVisibility(View.GONE);
                        checklist_flag = true;
                    }
                });
            }
            return convertView;
        }



    }
 /*   private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallForwardingIndicatorChanged(boolean cfi) {

            super.onCallForwardingIndicatorChanged(cfi);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //checkInternetConnection();
            String callState = "UNKNOWN";
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    callState = "IDLE";
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    callState = "Ringing (" + incomingNumber + ")";
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    callState = "Offhook";
                    break;
            }

            Log.i("Phone Stats", "onCallStateChanged " + callState);

            super.onCallStateChanged(state, incomingNumber);
        }

        @Override
        public void onCellLocationChanged(CellLocation location) {
            String cellLocationString = location.toString();

            super.onCellLocationChanged(location);



        }

        @Override
        public void onDataActivity(int direction) {
            String directionString = "none";
            switch (direction) {
                case TelephonyManager.DATA_ACTIVITY_IN:
                    directionString = "IN";
                    break;
                case TelephonyManager.DATA_ACTIVITY_OUT:
                    directionString = "OUT";
                    break;
                case TelephonyManager.DATA_ACTIVITY_INOUT:
                    directionString = "INOUT";
                    break;
                case TelephonyManager.DATA_ACTIVITY_NONE:
                    directionString = "NONE";
                    break;
                default:
                    directionString = "UNKNOWN: " + direction;
                    break;
            }

            Log.i("Phone Stats", "onDataActivity " + directionString);

            super.onDataActivity(direction);
        }

        @Override
        public void onDataConnectionStateChanged(int state,int networktype) {
            String connectionState = "Unknown";

            switch (state ) {

                case TelephonyManager.DATA_CONNECTED :
                    connectionState = "Connected";
                    break;
                case TelephonyManager.DATA_CONNECTING:
                    connectionState = "Connecting";
                    break;
                case TelephonyManager.DATA_DISCONNECTED:
                    connectionState = "Disconnected";
                    break;
                case TelephonyManager.DATA_SUSPENDED:
                    connectionState = "Suspended";
                    break;
                default:
                    connectionState = "Unknown: " + state;
                    break;
            }

            super.onDataConnectionStateChanged(state);


            Log.i("Phone Stats", "onDataConnectionStateChanged "
                    + connectionState);
        }

        @Override
        public void onMessageWaitingIndicatorChanged(boolean mwi) {
            super.onMessageWaitingIndicatorChanged(mwi);
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            String serviceStateString = "UNKNOWN";
            switch (serviceState.getState()) {
                case ServiceState.STATE_IN_SERVICE:
                    serviceStateString = "IN SERVICE";
                    break;
                case ServiceState.STATE_EMERGENCY_ONLY:
                    serviceStateString = "EMERGENCY ONLY";
                    break;
                case ServiceState.STATE_OUT_OF_SERVICE:
                    serviceStateString = "OUT OF SERVICE";
                    break;
                case ServiceState.STATE_POWER_OFF:
                    serviceStateString = "POWER OFF";
                    break;

                default:
                    serviceStateString = "UNKNOWN";
                    break;
            }

            Log.i("Phone Stats", "onServiceStateChanged " + serviceStateString);

            super.onServiceStateChanged(serviceState);
        }

        @Override
        public void onSignalStrengthChanged(int asu) {

            Log.i("Phone Stats", "onSignalStrengthChanged " + asu);
            setSignalLevel( asu);
            super.onSignalStrengthChanged(asu);
        }
        private void setSignalLevel(int level) {
            int sLevel = (int) ((level / 31.0) * 100);


            Log.i("signalLevel ", "" + sLevel);
        }

    };*/

 private void selectColorChangeLayout(){

   final RelativeLayout  re = (RelativeLayout) findViewById(R.id.re_home);

     re.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View view, MotionEvent motionEvent) {
             re.setBackgroundColor(getResources().getColor(R.color.colorAccent));

             return true;
         }
     });

 }



}