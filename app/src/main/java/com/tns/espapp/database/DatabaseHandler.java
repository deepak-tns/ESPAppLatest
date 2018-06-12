package com.tns.espapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tns.espapp.AttachmentData;
import com.tns.espapp.CaptureData;
import com.tns.espapp.activity.DynamicForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by TNS on 12/22/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();
    // Database Version
    private static final int DATABASE_VERSION = 13;

    // Database Name
    public static final String DATABASE_NAME = "my";
    private static final String TABLE_TAXIFOM_DATA = "add_texiformaata";
    private static final String TABLE_LATLONG = "latlong";
    private static final String TABLE_FEEDBACK_RECORD = "feedback_record";
    private static final String TABLE_FEEDBACK_ATTACHMENT = "feedback_attachment";
    private static final String TABLE_FEEDBACK_CAPTURE = "feedback_capture";
    private static final String TABLE_NOTIFICATION = "notification";
    private static final String TABLE_SETTING = "setting";
    private static final String TABLE_DYNAMICFORM = "dynamicForm";

    private static final String TABLE_CHECKLIST = "checklist";
    //private static final String TABLE_CHECKLIST2_SAVE = "checklist2";
    private static final String TABLE_FINALCHECKLIST2SAVE = "finalchecklist";

    private static final String TABLE_ATTENDANCE_REMOTE = "attendanceRemote";
    private static final String TABLE_ATTENDANCE_FIX = "attendanceFix";
    private static final String TABLE_OPENTRY = "OPENTRY";
    private static final String TABLE_OPENTRY_LATLOG = "OPENTRYLatLog";
    private static final String TABLE_OPENTRY_MILESTONE = "OPEntryMilestoneLatLog";
    private static final String TABLE_ATTENDANCESETTING = "AttendanceSetting";
    private static final String TABLE_GPSCHECKSTATUSDATA = "GPSStatus";

    // Contacts Table Columns names


    // Contacts Table Columns names for add post data student


    private static final String KEY_INCRI_ID = "incri_id";
    private static final String KEY_ID = "key_id";
    private static final String KEY_SELECTDATE = "selectdate";
    private static final String KEY_SETFORMNO = "formno";
    private static final String KEY_PROJECTTYPE = "projecttype";
    private static final String KEY_VEHICLENO = "vehicleno";
    private static final String KEY_STARTKM = "startkm";
    private static final String KEY_STARTKM_IMAGE = "startkm_image";
    private static final String KEY_ENDKM = "endkm";
    private static final String KEY_ENDKM_IMAGE = "endkmimage";
    private static final String KEY_FLAG = "flag";
    private static final String KEY_SITENO = "siteno";
    private static final String KEY_REMARK = "remark";


// Table LatLong Columns name

    private static final String KEY_LATLONG_INCRIID = "incri_latlongid";
    private static final String KEY_LATLONG_SETFORMNO = "formno";
    private static final String KEY_LATLONG_DATE = "lat_longdate";
    private static final String KEY_LATLONG_LAT = "lat";
    private static final String KEY_LATLONG_LONG = "long";
    private static final String KEY_LATLONG_FLAG = "latlong_flag";
    private static final String KEY_LATLONG_TOTALDIST = "dist_sum";
    private static final String KEY_LATLONG_TIME = "latlong_time";
    private static final String KEY_LATLONG_SPEED = "latlong_speed";
    private static final String KEY_LATLONG_COUNT_FlAG = "count_flag";
    private static final String KEY_LATLONG_BEARING_DEGREE = "dirdegree";
    private static final String KEY_LATLONG_BEARING_NAME = "dirname";


    // Table FEEDBACK_RECORD Columns name

    private static final String KEY_FEEDBACK_RECORD_INCRIID = "incri_feedback_record_id";
    private static final String KEY_FEEDBACK_RECORD_UNITNAME = "unitname";
    private static final String KEY_FEEDBACK_RECORD_REFERENCENO = "referenceno";
    private static final String KEY_FEEDBACK_RECORD_DATE = "date";
    private static final String KEY_FEEDBACK_RECORD_BREIF = "brief";
    private static final String KEY_FEEDBACK_RECORD_FLAG = "feedbackrecord_flag";
    private static final String KEY_FEEDBACK_RECORD_LAT = "feedbackrecord_lat";
    private static final String KEY_FEEDBACK_RECORD_LOG = "feedbackrecord_log";


    // Table FEEDBACK_ATTACHMENTDATA Columns name
    private static final String KEY_FEEDBACK_ATT_INCRIID = "incri_feedback_att_id";
    // private static final String KEY_FEEDBACK_ATT_FNID="fnId";
    private static final String KEY_FEEDBACK_ATT_REFNO = "att_refno";
    private static final String KEY_FEEDBACK_ATT_IMAGENAME = "imagename";
    private static final String KEY_FEEDBACK_ATT_IMAGE_FILE = "imagename_file";
    private static final String KEY_FEEDBACK_ATT_FLAG = "feedback_att_flag";

    // Table FEEDBACK_CAPTUREDATA Columns name
    private static final String KEY_FEEDBACK_CAPTURE_INCRIID = "incri_feedback_capture_id";
    //  private static final String KEY_FEEDBACK_CAPTURE_FNID="fnId";
    private static final String KEY_FEEDBACK_CAPTURE_REFNO = "image_refno";
    private static final String KEY_FEEDBACK_CAPTURE_IMAGENAME = "imagename";
    private static final String KEY_FEEDBACK_CAPTURE_IMAGE_FILE = "imagename_file";
    private static final String KEY_FEEDBACK_CAPTURE_FLAG = "feedback_capture_flag";


    // Table Notification Columns name
    private static final String KEY_NOTIFICATION_INCRIID = "incri_notification";
    private static final String KEY_NOTIFICTION_TITTLE = "notification_tittle";
    private static final String KEY_NOTIFICATION_MESSAGE = "notification_message";
    private static final String KEY_NOTIFICATION_IMAGE = "notification_image";
    private static final String KEY_NOTIFICATION_FLAG = "notification_flag";

    // Table Setting Columns name
    private static final String KEY_SETT_INCRIID = "sett_incri_id";
    private static final String KEY_SETT_GPS_ENABLED = "sett_gps_enabled";
    private static final String KEY_SETT_GPS_INTERVEL = "sett_gps_intervel";
    private static final String KEY_SETT_GPS_SPEED = "sett_gps_speed";
    private static final String KEY_SETT_STATUS = "sett_gps_status";

    // Table Checklist Columns name
    private static final String KEY_CHECKLIST_INCRI = "chk_incri_id";
    private static final String KEY_CHECKLIST_FORMNO = "chk_formno";
    private static final String KEY_CHECKLIST_NAME = "chk_name";
    private static final String KEY_CHECKLIST_NAMEVALUE = "chk_namevalue";
    private static final String KEY_CHECKLIST_DATATYPE = "chk_datatype";
    private static final String KEY_CHECKLIST_SIZE = "chk_size";
    private static final String KEY_CHECKLIST_DECIMAL = "chk_decimal";
    private static final String KEY_CHECKLIST_FLAG = "chk_flag";
    // Table DynamicForm
    private static final String KEY_FORMNAME = "formName";
    private static final String KEY_FIELDNAME = "fieldName";
    private static final String KEY_FIELDVALUE = "fieldValue";
    private static final String KEY_DATATYPE = "dataType";
    private static final String KEY_SIZE = "size";
    private static final String KEY_DECIMAL = "decimal";


    // Table Checklist2 Columns name
    private static final String KEY_CHECKLIST_2_INCRI = "chk2_incri_id";
    private static final String KEY_CHECKLIST_2_VFORMNAME = "chk2_formname";
    private static final String KEY_CHECKLIST_2_VNAMEKEY = "chk2_namekey";
    private static final String KEY_CHECKLIST_2_VNAMEVALUE = "chk2_namevalue";
    private static final String KEY_CHECKLIST_2_FLAG = "chk2_flag";

    //Table FinalChecklist columns name
    private static final String KEYFINALCHECKLISTSAVE_INCRI = "id";
    private static final String KEYFINALCHECKLISTSAVE_SETFORMNO = "formNo";
    private static final String KEYFINALCHECKLISTSAVE_SNO = "sNo";
    private static final String KEYFINALCHECKLISTSAVE_DESC = "desc";
    private static final String KEYFINALCHECKLISTSAVE_STS = "sts";
    private static final String KEYFINALCHECKLISTSAVE_REMARK = "remark";
    private static final String KEYFINALCHECKLISTSAVE_PHOTOS = "photos";
    private static final String KEYFINALCHECKLISTSAVE_PATH = "path";
    private static final String KEYFINALCHECKLISTSAVE_COUNT = "count";
    private static final String KEYFINALCHECKLISTSAVE_PHOTO1 = "photo1";
    private static final String KEYFINALCHECKLISTSAVE_PHOTO2 = "photo2";
    private static final String KEYFINALCHECKLISTSAVE_PHOTO3 = "photo3";
    private static final String KEYFINALCHECKLISTSAVE_PHOTO4 = "photo4";
    private static final String KEYFINALCHECKLISTSAVE_PHOTO5 = "photo5";
    private static final String KEYFINALCHECKLISTSAVE_PHOTOTEXT1 = "Textphoto1";
    private static final String KEYFINALCHECKLISTSAVE_PHOTOTEXT2 = "Textphoto2";
    private static final String KEYFINALCHECKLISTSAVE_PHOTOTEXT3 = "Textphoto3";
    private static final String KEYFINALCHECKLISTSAVE_PHOTOTEXT4 = "Textphoto4";
    private static final String KEYFINALCHECKLISTSAVE_PHOTOTEXT5 = "Textphoto5";

    private static final String KEYFINALCHECKLISTSAVE_FLAG = "flag";

    //Table Attendance Remote columns name
    private static final String ATT_REMOTE_INCRI = "id";
    private static final String ATT_REMOTE_EMPID = "empId";
    private static final String ATT_REMOTE_CURRENTDATE= "Date";
    private static final String ATT_REMOTE_INTIME = "inTime";
    private static final String ATT_REMOTE_OUTTIME = "outTime";
    private static final String ATT_REMOTE_INTIME_PICS = "inTimePics";
    private static final String ATT_REMOTE_OUTTIME_PICS = "outTimePics";
    private static final String ATT_REMOTE_INLAT = "inlat";
    private static final String ATT_REMOTE_INLONG = "inlongi";
    private static final String ATT_REMOTE_OUTLAT = "outlat";
    private static final String ATT_REMOTE_OUTLONG = "outlongi";
    private static final String ATT_REMOTE_INTIMEREMARK = "IntimeRemark";
    private static final String ATT_REMOTE_OUTTIMEREMARK = "OuttimeRemark";
    private static final String ATT_REMOTE_NEARESTINTIME = "Intimenear";
    private static final String ATT_REMOTE_NEARESTOUTTIME = "Outtimenear";
    private static final String ATT_REMOTE_FLAG = "flag";


    //Table Attendance Fix columns name
    private static final String ATT_FIX_INCRI = "id";
    private static final String ATT_FIX_EMPID = "empId";
    private static final String ATT_FIX_CURRENTDATE= "Date";
    private static final String ATT_FIX_INTIME = "inTime";
    private static final String ATT_FIX_OUTTIME = "outTime";
    private static final String ATT_FIX_INTIME_PICS = "inTimePics";
    private static final String ATT_FIX_OUTTIME_PICS = "outTimePics";
    private static final String ATT_FIX_INLAT = "inlat";
    private static final String ATT_FIX_INLONG = "inlongi";
    private static final String ATT_FIX_OUTLAT = "outlat";
    private static final String ATT_FIX_OUTLONG = "outlongi";
    private static final String ATT_FIX_INTIMEREMARK = "IntimeRemark";
    private static final String ATT_FIX_OUTTIMEREMARK = "OuttimeRemark";
    private static final String ATT_FIX_NEARESTINTIME = "Intimenear";
    private static final String ATT_FIX_NEARESTOUTTIME = "Outtimenear";

    private static final String ATT_FIX_FLAG = "flag";

    //Table OPEntry columns name
    private static final String OPENTRY_INCRI = "id";
    private static final String OPENTRY_KEY_ID = "key_id";
    private static final String OPENTRY_PROJECTNO = "ProjectNo";
    private static final String OPENTRY_MODE= "Mode";
    private static final String OPENTRY_STARTKM = "StartKM";
    private static final String OPENTRY_ENDKM = "EndKM";
    private static final String OPENTRY_STARTKMIMAGE = "StartKMImage";
    private static final String OPENTRY_ENDKMIMAGE = "EndKMImage";
    private static final String OPENTRY_CURRENTDATE = "Date";
    private static final String OPENTRY_CUSTOMERNAME = "CustomerName";
    private static final String OPENTRY_OPTYPE= "OPType";
    private static final String OPENTRY_REASONOP= "REASONOP";
    private static final String OPENTRY_MANUALLOCATION = "ManualLocation";
    private static final String OPENTRY_CURRENTLOCATION = "CurrentLocation";
    private static final String OPENTRY_REMARK = "Remark";
    private static final String OPENTRY_FLAG= "Status";

    // Table OPEntryLatLong Columns name

    private static final String KEY_OPENTRYLATLONG_INCRIID = "incri_id";
    private static final String KEY_OPENTRYLATLONG_SETFORMNO = "formno";
    private static final String KEY_OPENTRYLATLONG_DATE = "lat_longdate";
    private static final String KEY_OPENTRYLATLONG_LAT = "lat";
    private static final String KEY_OPENTRYLATLONG_LONG = "long";
    private static final String KEY_OPENTRYLATLONG_TOTALDIST = "dist_sum";
    private static final String KEY_OPENTRYLATLONG_TIME = "latlong_time";
    private static final String KEY_OPENTRYLATLONG_SPEED = "latlong_speed";
    private static final String KEY_OPENTRYLATLONG_FLAG = "latlong_flag";

    // Table OPEntryMilestoneLatLong Columns name

    private static final String KEY_OPENTRY_MILESTONE_INCRIID = "incri_id";
    private static final String KEY_OPENTRY_MILESTONE_SETFORMNO = "formno";
    private static final String KEY_OPENTRY_MILESTONE_DATE = "date";
    private static final String KEY_OPENTRY_MILESTONE_LAT = "lat";
    private static final String KEY_OPENTRY_MILESTONE_LONG = "long";
    private static final String KEY_OPENTRY_MILESTONE_LOCATION = "location";
    private static final String KEY_OPENTRY_MILESTONE_NEAREST = "NearLocation";
    private static final String KEY_OPENTRY_MILESTONEL_FLAG = "status";
    // Table Setting Columns name
    private static final String KEY_ATTENDANCE_SETT_INCRIID = "Id";
    private static final String  KEY_ATTENDANCE_SETT_LAT = "Lat";
    private static final String  KEY_ATTENDANCE_SETT_LOG = "Log";
    private static final String  KEY_ATTENDANCE_SETT_RADILDIS = "RadialDistance";
    private static final String  KEY_ATTENDANCE_SETT_TYPE = "Type";

    // Table GPS on off Columns name
    private static final String KEY_GPSCHECK_INCRIID = "Id";
    private static final String KEY_GPSCHECK_TIME = "Time";
    private static final String KEY_GPSCHECK_STATUS = "Status";


    public DatabaseHandler(Context context) {
       // super(context, "/mnt/sdcard/my.db", null, DATABASE_VERSION);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


        Log.v(TAG, "Databaser object created");
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_TABLE_TAXIFORM_DATA = "CREATE TABLE " + TABLE_TAXIFOM_DATA + "(" + KEY_INCRI_ID + " integer primary key autoincrement," + KEY_ID + " integer,"
                + KEY_SELECTDATE + " TEXT," + KEY_SETFORMNO + " TEXT," + KEY_PROJECTTYPE + " TEXT," + KEY_VEHICLENO + " TEXT,"
                + KEY_STARTKM + " TEXT," + KEY_STARTKM_IMAGE + " TEXT," + KEY_ENDKM + " TEXT," + KEY_ENDKM_IMAGE + " TEXT," + KEY_FLAG + " integer," + KEY_SITENO + " text," + KEY_REMARK + " text" + ")";


        String CREATE_TABLE_LATlONGDATA = "create table "
                + TABLE_LATLONG + " (" + KEY_LATLONG_INCRIID
                + " integer primary key autoincrement," + KEY_LATLONG_SETFORMNO
                + " text," + KEY_LATLONG_DATE
                + " text," + KEY_LATLONG_LAT + " text,"
                + KEY_LATLONG_LONG + " text," + KEY_LATLONG_FLAG + " integer," + KEY_LATLONG_TOTALDIST + " text," + KEY_LATLONG_TIME + " text," + KEY_LATLONG_SPEED + " text," + KEY_LATLONG_COUNT_FlAG + " integer," + KEY_LATLONG_BEARING_DEGREE + " text," + KEY_LATLONG_BEARING_NAME + " text" + ");";

        String CREATE_TABLE_FEEDBACK_RECORD = "create table "
                + TABLE_FEEDBACK_RECORD + " (" + KEY_FEEDBACK_RECORD_INCRIID
                + " integer primary key autoincrement," + KEY_FEEDBACK_RECORD_UNITNAME
                + " text," + KEY_FEEDBACK_RECORD_REFERENCENO
                + " text," + KEY_FEEDBACK_RECORD_DATE + " text,"
                + KEY_FEEDBACK_RECORD_BREIF + " text," + KEY_FEEDBACK_RECORD_FLAG + " integer," + KEY_FEEDBACK_RECORD_LAT + " text," + KEY_FEEDBACK_RECORD_LOG + " text" + ");";

        String CREATE_TABLE_FEEDBACK_ATTACHMENT = "create table "
                + TABLE_FEEDBACK_ATTACHMENT + " (" + KEY_FEEDBACK_ATT_INCRIID + " integer primary key autoincrement," + KEY_FEEDBACK_ATT_REFNO + " text,"
                + KEY_FEEDBACK_ATT_IMAGENAME + " text," + KEY_FEEDBACK_ATT_IMAGE_FILE + " text," + KEY_FEEDBACK_ATT_FLAG + " integer" + ");";


        String CREATE_TABLE_FEEDBACK_CAPTURE = "create table "
                + TABLE_FEEDBACK_CAPTURE + " (" + KEY_FEEDBACK_CAPTURE_INCRIID + " integer primary key autoincrement," + KEY_FEEDBACK_CAPTURE_REFNO + " text,"
                + KEY_FEEDBACK_CAPTURE_IMAGENAME + " text," + KEY_FEEDBACK_CAPTURE_IMAGE_FILE + " text," + KEY_FEEDBACK_CAPTURE_FLAG + " integer" + ");";

        String CREATE_TABLE_NOTIFICATION = "create table "
                + TABLE_NOTIFICATION + " (" + KEY_NOTIFICATION_INCRIID + " integer primary key autoincrement," + KEY_NOTIFICTION_TITTLE + " text,"
                + KEY_NOTIFICATION_MESSAGE + " text," + KEY_NOTIFICATION_IMAGE + " text," + KEY_NOTIFICATION_FLAG + " integer" + ");";


        String CREATE_TABLE_SETTING = "create table "
                + TABLE_SETTING + " (" + KEY_SETT_INCRIID + " integer primary key autoincrement," + KEY_SETT_GPS_ENABLED + " integer,"
                + KEY_SETT_GPS_INTERVEL + " integer," + KEY_SETT_GPS_SPEED + " integer," + KEY_SETT_STATUS + " text" + ");";

        String CREATE_TABLE_DYNAMICFORM = "create table "
                + TABLE_DYNAMICFORM + " (" + KEY_FORMNAME + " text," + KEY_FIELDNAME + " text,"
                + KEY_FIELDVALUE + " text," + KEY_DATATYPE + " text," + KEY_SIZE + " integer," + KEY_DECIMAL + " real" + ");";


        String CREATE_TABLE_CHECKLIST = "create table "
                + TABLE_CHECKLIST + " (" + KEY_CHECKLIST_INCRI + " integer primary key autoincrement," + KEY_CHECKLIST_FORMNO + " text,"
                + KEY_CHECKLIST_NAME + " text," + KEY_CHECKLIST_NAMEVALUE + " text," + KEY_CHECKLIST_DATATYPE + " text," + KEY_CHECKLIST_SIZE + " text," + KEY_CHECKLIST_DECIMAL + " real," + KEY_CHECKLIST_FLAG + " integer" + ");";

    /*    String CREATE_TABLE_CHECKLIST2_SAVE = "create table "
                + TABLE_CHECKLIST2_SAVE + " (" + KEY_CHECKLIST_2_INCRI + " integer primary key autoincrement," + KEY_CHECKLIST_2_VFORMNAME + " text," + KEY_CHECKLIST_2_VNAMEKEY + " text,"
                + KEY_CHECKLIST_2_VNAMEVALUE + " text," + KEY_CHECKLIST_2_FLAG + " integer" + ");";
*/
        String CREATE_TABLE_FINALCHECKLIST2_SAVE = "create table "
                + TABLE_FINALCHECKLIST2SAVE + " (" + KEYFINALCHECKLISTSAVE_INCRI + " integer primary key autoincrement," + KEYFINALCHECKLISTSAVE_SETFORMNO + " text," + KEYFINALCHECKLISTSAVE_SNO + " integer," + KEYFINALCHECKLISTSAVE_DESC + " text,"
                + KEYFINALCHECKLISTSAVE_STS + " text," + KEYFINALCHECKLISTSAVE_REMARK + " text," + KEYFINALCHECKLISTSAVE_PHOTOS + " text," + KEYFINALCHECKLISTSAVE_PATH + " text," + KEYFINALCHECKLISTSAVE_COUNT + " integer,"+ KEYFINALCHECKLISTSAVE_PHOTO1 + " text,"+ KEYFINALCHECKLISTSAVE_PHOTO2 + " text,"+ KEYFINALCHECKLISTSAVE_PHOTO3 + " text,"+ KEYFINALCHECKLISTSAVE_PHOTO4 + " text,"+ KEYFINALCHECKLISTSAVE_PHOTO5 + " text,"+ KEYFINALCHECKLISTSAVE_PHOTOTEXT1 + " text,"+ KEYFINALCHECKLISTSAVE_PHOTOTEXT2 + " text,"+ KEYFINALCHECKLISTSAVE_PHOTOTEXT3 + " text,"+ KEYFINALCHECKLISTSAVE_PHOTOTEXT4 + " text,"+ KEYFINALCHECKLISTSAVE_PHOTOTEXT5 + " text," + KEYFINALCHECKLISTSAVE_FLAG + " integer" + ");";

        String CREATE_TABLE_ATTENDANCEREMOTE = "create table "
                + TABLE_ATTENDANCE_REMOTE + " (" + ATT_REMOTE_INCRI + " integer primary key autoincrement," + ATT_REMOTE_EMPID + " text,"+ ATT_REMOTE_CURRENTDATE+ " text," + ATT_REMOTE_INTIME + " text," + ATT_REMOTE_OUTTIME + " text,"
                + ATT_REMOTE_INTIME_PICS + " text," + ATT_REMOTE_OUTTIME_PICS + " text," + ATT_REMOTE_INLAT + " text," + ATT_REMOTE_INLONG + " text," + ATT_REMOTE_OUTLAT + " text," + ATT_REMOTE_OUTLONG + " text," + ATT_REMOTE_INTIMEREMARK + " text," + ATT_REMOTE_OUTTIMEREMARK + " text," +ATT_REMOTE_NEARESTINTIME + " text,"+ ATT_REMOTE_NEARESTOUTTIME + " text,"+  ATT_REMOTE_FLAG + " integer" + ");";


        String CREATE_TABLE_ATTENDANCEFIX = "create table "
                + TABLE_ATTENDANCE_FIX + " (" + ATT_FIX_INCRI + " integer primary key autoincrement," + ATT_FIX_EMPID + " text,"+ ATT_FIX_CURRENTDATE+ " text," + ATT_FIX_INTIME + " text," + ATT_FIX_OUTTIME + " text,"
                + ATT_FIX_INTIME_PICS + " text," + ATT_FIX_OUTTIME_PICS + " text," + ATT_FIX_INLAT + " text," + ATT_FIX_INLONG + " text," + ATT_FIX_OUTLAT + " text," + ATT_FIX_OUTLONG + " text,"+ ATT_FIX_INTIMEREMARK + " text," + ATT_FIX_OUTTIMEREMARK + " text," + ATT_FIX_NEARESTINTIME + " text,"+ ATT_FIX_NEARESTOUTTIME + " text,"  +  ATT_FIX_FLAG + " integer" + ");";

        String CREATE_TABLE_OPENTRY = "create table "
                + TABLE_OPENTRY+ " (" + OPENTRY_INCRI + " integer primary key autoincrement," + OPENTRY_KEY_ID + " integer," + OPENTRY_PROJECTNO + " text,"+ OPENTRY_MODE+ " text," + OPENTRY_STARTKM + " text," + OPENTRY_ENDKM + " text,"
                + OPENTRY_STARTKMIMAGE + " text," + OPENTRY_ENDKMIMAGE + " text," + OPENTRY_CURRENTDATE+ " text," + OPENTRY_CUSTOMERNAME + " text," +  OPENTRY_OPTYPE + " text,"+ OPENTRY_REASONOP+ " text," + OPENTRY_MANUALLOCATION + " text," +  OPENTRY_CURRENTLOCATION + " text," + OPENTRY_REMARK + " text," +  OPENTRY_FLAG+ " integer"+ ");";


        String CREATE_TABLE_OPENTRY_LATLOG = "create table "
                + TABLE_OPENTRY_LATLOG + " (" + KEY_OPENTRYLATLONG_INCRIID
                + " integer primary key autoincrement," + KEY_OPENTRYLATLONG_SETFORMNO
                + " text," + KEY_OPENTRYLATLONG_DATE
                + " text," + KEY_OPENTRYLATLONG_LAT + " text,"
                + KEY_OPENTRYLATLONG_LONG + " text," + KEY_OPENTRYLATLONG_FLAG + " integer," + KEY_OPENTRYLATLONG_TOTALDIST + " text," + KEY_OPENTRYLATLONG_TIME + " text," + KEY_OPENTRYLATLONG_SPEED + " text" + ");";

        String CREATE_TABLE_OPENTRY_MILESTONE = "create table "
                + TABLE_OPENTRY_MILESTONE + " (" + KEY_OPENTRY_MILESTONE_INCRIID
                + " integer primary key autoincrement," + KEY_OPENTRY_MILESTONE_SETFORMNO
                + " text," + KEY_OPENTRY_MILESTONE_DATE
                + " text," + KEY_OPENTRY_MILESTONE_LAT + " text,"
                + KEY_OPENTRY_MILESTONE_LONG + " text," + KEY_OPENTRY_MILESTONE_LOCATION + " text,"+KEY_OPENTRY_MILESTONE_NEAREST+ " text,"+ KEY_OPENTRY_MILESTONEL_FLAG + " integer"  + ");";

        String CREATE_TABLE_ATTENDANCE_SETTING = "create table "
                + TABLE_ATTENDANCESETTING+ " (" + KEY_ATTENDANCE_SETT_INCRIID + " integer primary key autoincrement," + KEY_ATTENDANCE_SETT_LAT + " text,"
                + KEY_ATTENDANCE_SETT_LOG + " text," + KEY_ATTENDANCE_SETT_RADILDIS + " text," + KEY_ATTENDANCE_SETT_TYPE + " text" + ");";

        String CREATE_TABLE_GPSCHECK_STATUS = "create table "
                + TABLE_GPSCHECKSTATUSDATA + " (" + KEY_GPSCHECK_INCRIID
                + " integer primary key autoincrement," + KEY_GPSCHECK_TIME
                + " text," + KEY_GPSCHECK_STATUS
                + " text " +")";



// TABLE_OPENTRY_MILESTONE_LATLOG
        db.execSQL(CREATE_TABLE_TAXIFORM_DATA);
        db.execSQL(CREATE_TABLE_LATlONGDATA);
        db.execSQL(CREATE_TABLE_FEEDBACK_RECORD);
        db.execSQL(CREATE_TABLE_FEEDBACK_ATTACHMENT);
        db.execSQL(CREATE_TABLE_FEEDBACK_CAPTURE);
        db.execSQL(CREATE_TABLE_NOTIFICATION);
        db.execSQL(CREATE_TABLE_SETTING);
        db.execSQL(CREATE_TABLE_CHECKLIST);
      //  db.execSQL(CREATE_TABLE_CHECKLIST2_SAVE);
        db.execSQL(CREATE_TABLE_DYNAMICFORM);
        db.execSQL(CREATE_TABLE_FINALCHECKLIST2_SAVE);
        db.execSQL(CREATE_TABLE_ATTENDANCEREMOTE);
        db.execSQL(CREATE_TABLE_OPENTRY);
        db.execSQL(CREATE_TABLE_OPENTRY_LATLOG);
        db.execSQL(CREATE_TABLE_OPENTRY_MILESTONE);
        db.execSQL(CREATE_TABLE_ATTENDANCEFIX);
        db.execSQL(CREATE_TABLE_ATTENDANCE_SETTING);
        db.execSQL(CREATE_TABLE_GPSCHECK_STATUS);


        Log.v(TAG, "Database table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAXIFOM_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LATLONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK_RECORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK_ATTACHMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK_CAPTURE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKLIST);
     //   db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKLIST2_SAVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DYNAMICFORM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINALCHECKLIST2SAVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_REMOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPENTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPENTRY_LATLOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPENTRY_MILESTONE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_FIX);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCESETTING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GPSCHECKSTATUSDATA);

        // Create tables again
        onCreate(db);
    }


    // code to add the new addBus


    public void addTaxiformData(TaxiFormData taxiFormData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, taxiFormData.getKeyid());
        values.put(KEY_SELECTDATE, taxiFormData.getSelectdate());
        values.put(KEY_SETFORMNO, taxiFormData.getFormno());
        values.put(KEY_PROJECTTYPE, taxiFormData.getProjecttype());
        values.put(KEY_VEHICLENO, taxiFormData.getVechicleno());

        values.put(KEY_STARTKM, taxiFormData.getStartkm());
        values.put(KEY_STARTKM_IMAGE, taxiFormData.getStartkm_image());
        values.put(KEY_ENDKM, taxiFormData.getEndkm());
        values.put(KEY_ENDKM_IMAGE, taxiFormData.getEndkmimage());
        values.put(KEY_FLAG, taxiFormData.getFlag());

        values.put(KEY_SITENO, taxiFormData.getSiteno());
        values.put(KEY_REMARK, taxiFormData.getRemark());


        // Inserting Row
        db.insert(TABLE_TAXIFOM_DATA, null, values);
        Log.v(TAG, "Databaser insert taxidata table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addTaxiformLatLong(LatLongData latLongData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATLONG_SETFORMNO, latLongData.getFormno());
        values.put(KEY_LATLONG_DATE, latLongData.getDate());
        values.put(KEY_LATLONG_LAT, latLongData.getLat());
        values.put(KEY_LATLONG_LONG, latLongData.getLongi());
        values.put(KEY_LATLONG_TOTALDIST, latLongData.getTotaldis());
        values.put(KEY_LATLONG_FLAG, latLongData.getLatlong_flag());
        values.put(KEY_LATLONG_TIME, latLongData.getCurrent_time_str());
        values.put(KEY_LATLONG_SPEED, latLongData.getSpeed());
        values.put(KEY_LATLONG_COUNT_FlAG, latLongData.getCountFlag());
        values.put(KEY_LATLONG_BEARING_DEGREE, latLongData.getBearing_degree());
        values.put(KEY_LATLONG_BEARING_NAME, latLongData.getBearing_name());
        // values.put(KEY_LATLONG_TOTALDIST, latLongData.getTotaldis());


        // Inserting Row
        db.insert(TABLE_LATLONG, null, values);
        Log.v(TAG, "Databaser insert taxi Lat Long data table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    // code to get the single AddBus

    public void deleteSingleRow_LatLong() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_LATLONG + " ;");
        db.close();
    }

    public void deleteSingleRowTaxiformData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TAXIFOM_DATA + " WHERE " + KEY_SETFORMNO + "='" + value + "'");
        db.close();
    }

    public void deleteSingleRowTaxiformData_ByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TAXIFOM_DATA + " WHERE " + KEY_INCRI_ID + "='" + id + "'");
        db.close();
    }

    public void delete_TaxiFormRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        //  db.execSQL("DELETE FROM " + TABLE_DASHBOARD + " WHERE " + KEY_EMAIL + "='" + value + "'");
        db.execSQL("DELETE FROM " + TABLE_TAXIFOM_DATA + " ;");
        db.close();
    }


    public List<String> showData(String u, String p) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<String> list = new ArrayList<String>();

        String str = "No Data Found";
        String query = "SELECT * FROM " + TABLE_TAXIFOM_DATA + " WHERE selectdate=? and projecttype=?";
        Cursor cursor = db.rawQuery(query, new String[]{u, p});

        int c = cursor.getCount();

        if (c > 0) {
            while (cursor.moveToNext()) {
                str = "" + cursor.getString(0) + ":" + cursor.getString(1) + ":" + cursor.getString(2) + "";
                list.add(str);
            }
        }
        return list;
    }


    public List<TaxiFormData> getAllTaxiformData() {
        List<TaxiFormData> List = new ArrayList<TaxiFormData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TAXIFOM_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TaxiFormData data = new TaxiFormData();
                //contact.setID(Integer.parseInt(cursor.getString(0)));
                data.setId(cursor.getInt(0));
                data.setKeyid(cursor.getInt(1));
                data.setSelectdate(cursor.getString(2));
                data.setFormno(cursor.getString(3));
                data.setProjecttype(cursor.getString(4));
                data.setVechicleno(cursor.getString(5));
                data.setStartkm(cursor.getString(6));
                data.setStartkm_image(cursor.getString(7));
                data.setEndkm(cursor.getString(8));
                data.setEndkmimage(cursor.getString(9));
                data.setFlag(cursor.getInt(10));
                data.setSiteno(cursor.getString(11));
                data.setRemark(cursor.getString(12));
                // Adding contact to list
                List.add(data);
            } while (cursor.moveToNext());
        }
        // return contact list
        return List;
    }

    public List<TaxiFormData> getLastTaxiformData() {
        ArrayList<TaxiFormData> list = new ArrayList<TaxiFormData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        //String selectQuery = "SELECT  * FROM " + TABLE_LATLONG +" ORDER BY "+KEY_LATLONG_INCRIID+" DESC LIMIT 1;";

        String selectQuery = "SELECT  * FROM " + TABLE_TAXIFOM_DATA  + " ORDER BY " + KEY_INCRI_ID+ " DESC LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        TaxiFormData data = new TaxiFormData();
                        //contact.setID(Integer.parseInt(cursor.getString(0)));
                        data.setId(cursor.getInt(0));
                        data.setKeyid(cursor.getInt(1));
                        data.setSelectdate(cursor.getString(2));
                        data.setFormno(cursor.getString(3));
                        data.setProjecttype(cursor.getString(4));
                        data.setVechicleno(cursor.getString(5));
                        data.setStartkm(cursor.getString(6));
                        data.setStartkm_image(cursor.getString(7));
                        data.setEndkm(cursor.getString(8));
                        data.setEndkmimage(cursor.getString(9));
                        data.setFlag(cursor.getInt(10));
                        data.setSiteno(cursor.getString(11));
                        data.setRemark(cursor.getString(12));
                        // Adding contact to list
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;

    }


    public int getLastInsertId() {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_TAXIFOM_DATA, null);

        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        return index;
    }


    public List<LatLongData> getAllLatLong() {

        ArrayList<LatLongData> list = new ArrayList<LatLongData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LATLONG;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        LatLongData data = new LatLongData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setFormno(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setLat(cursor.getString(3));
                        data.setLongi(cursor.getString(4));
                        data.setLatlong_flag(cursor.getInt(5));
                        data.setTotaldis(cursor.getString(6));
                        data.setCurrent_time_str(cursor.getString(7));
                        data.setSpeed(cursor.getString(8));


                        //you could add additional columns here..

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<LatLongData> getAllLatLongORDerBy() {

        ArrayList<LatLongData> list = new ArrayList<LatLongData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_LATLONG + " ORDER BY " + KEY_LATLONG_INCRIID + " ASC;";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        LatLongData data = new LatLongData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setFormno(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setLat(cursor.getString(3));
                        data.setLongi(cursor.getString(4));
                        data.setLatlong_flag(cursor.getInt(5));
                        data.setTotaldis(cursor.getString(6));
                        data.setCurrent_time_str(cursor.getString(7));
                        data.setSpeed(cursor.getString(8));

                        //you could add additional columns here..

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<LatLongData> getAllLatLongStatus() {

        ArrayList<LatLongData> list = new ArrayList<LatLongData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_LATLONG + " WHERE " + KEY_LATLONG_FLAG + " = 0" + " ORDER BY " + KEY_LATLONG_INCRIID + " ASC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        LatLongData data = new LatLongData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setFormno(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setLat(cursor.getString(3));
                        data.setLongi(cursor.getString(4));
                        data.setLatlong_flag(cursor.getInt(5));
                        data.setTotaldis(cursor.getString(6));
                        data.setCurrent_time_str(cursor.getString(7));
                        data.setSpeed(cursor.getString(8));

                        //you could add additional columns here..

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }


    public List<LatLongData> getLatLongbyFormNo(String formno) {
        ArrayList<LatLongData> list = new ArrayList<LatLongData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        // String selectQuery = "SELECT  * FROM " + TABLE_LATLONG +" ORDER BY "+KEY_LATLONG_INCRIID+" ASC LIMIT 1;";
        //  String selectQuery = "SELECT  * FROM " + TABLE_LATLONG + " WHERE " +KEY_LATLONG_SETFORMNO +" = ?" + " ORDER BY "+KEY_LATLONG_INCRIID+  " ASC LIMIT 1" ;

        String selectQuery = "SELECT  * FROM " + TABLE_LATLONG + " WHERE " + KEY_LATLONG_SETFORMNO + " = ?";


        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{formno});
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        LatLongData data = new LatLongData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setFormno(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setLat(cursor.getString(3));
                        data.setLongi(cursor.getString(4));
                        data.setLatlong_flag(cursor.getInt(5));
                        data.setTotaldis(cursor.getString(6));
                        data.setCurrent_time_str(cursor.getString(7));
                        data.setSpeed(cursor.getString(8));

                        //you could add additional columns here..

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;

    }

    public List<LatLongData> getLastLatLong(String formno) {
        ArrayList<LatLongData> list = new ArrayList<LatLongData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        //String selectQuery = "SELECT  * FROM " + TABLE_LATLONG +" ORDER BY "+KEY_LATLONG_INCRIID+" DESC LIMIT 1;";

        String selectQuery = "SELECT  * FROM " + TABLE_LATLONG + " WHERE " + KEY_LATLONG_SETFORMNO + " = ?" + " ORDER BY " + KEY_LATLONG_INCRIID + " DESC LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{formno});
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        LatLongData data = new LatLongData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setFormno(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setLat(cursor.getString(3));
                        data.setLongi(cursor.getString(4));
                        data.setLatlong_flag(cursor.getInt(5));
                        data.setTotaldis(cursor.getString(6));
                        data.setCurrent_time_str(cursor.getString(7));
                        data.setSpeed(cursor.getString(8));
                        //you could add additional columns here..

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;

    }


    // Getting AddBusData Count
    public int getCountTaxiform() {
        String countQuery = "SELECT  * FROM " + TABLE_TAXIFOM_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    public boolean updatedetails(int rowId, String date, String formno, String projectType, String vehicleno, String stkm, String stkm_image, String endkm, String endkmImage, int flag, String siteno, String remark) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();

        args.put(KEY_SELECTDATE, date);
        args.put(KEY_SETFORMNO, formno);
        args.put(KEY_PROJECTTYPE, projectType);
        args.put(KEY_VEHICLENO, vehicleno);
        args.put(KEY_STARTKM, stkm);
        args.put(KEY_STARTKM_IMAGE, stkm_image);
        args.put(KEY_ENDKM, endkm);
        args.put(KEY_ENDKM_IMAGE, endkmImage);
        args.put(KEY_FLAG, flag);
        args.put(KEY_SITENO, siteno);
        args.put(KEY_REMARK, remark);


        int i = db.update(TABLE_TAXIFOM_DATA, args, KEY_INCRI_ID + "=" + rowId, null);
        return i > 0;
    }


    public boolean updateLatLong(int incriid, String form_no, String date, String lat, String longi, int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_LATLONG_SETFORMNO, form_no);
        args.put(KEY_LATLONG_DATE, date);
        args.put(KEY_LATLONG_LAT, lat);
        args.put(KEY_LATLONG_LONG, longi);
        args.put(KEY_LATLONG_FLAG, flag);
        //  args.put(KEY_LATLONG_TOTALDIST,dis);


        int i = db.update(TABLE_LATLONG, args, KEY_LATLONG_INCRIID + "=" + incriid, null);
        return i > 0;
    }


    public void deleteSomeRow_Taxiform() {
        SQLiteDatabase db = this.getWritableDatabase();
        // db.execSQL("delete from "+ TABLE_TAXIFOM_DATA+" where " +KEY_INCRI_ID+ " not in ( select " +KEY_INCRI_ID+" from "+ TABLE_TAXIFOM_DATA+" order by "+KEY_SELECTDATE +" desc limit 100)");
        db.execSQL("DELETE FROM " + TABLE_TAXIFOM_DATA + " ;");

        db.close();
    }

    public void deleteSomeRow_LatLong() {
        SQLiteDatabase db = this.getWritableDatabase();
        //  db.execSQL("delete from "+ TABLE_LATLONG+" where " +KEY_LATLONG_INCRIID+ " not in ( select " +KEY_LATLONG_INCRIID+" from "+ TABLE_LATLONG+" order by "+KEY_LATLONG_INCRIID +" desc limit 1000)");
        // db.execSQL("DELETE FROM " + TABLE_LATLONG + " ;");

        db.execSQL("delete from " + TABLE_LATLONG + " where " + KEY_LATLONG_FLAG + " = 1");

        db.close();
    }
    public void deleteAllRowLatLongData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_LATLONG + " WHERE " + KEY_LATLONG_SETFORMNO + "='" + value + "'");
        db.close();
    }


    public void insert_feedbackREcordData(FeedbackRecordData feedbackRecordData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put(KEY_ID, feedbackRecordData.getKeyid());
        values.put(KEY_FEEDBACK_RECORD_UNITNAME, feedbackRecordData.getfEEDBACK_RECORD_UNITNAME());
        values.put(KEY_FEEDBACK_RECORD_REFERENCENO, feedbackRecordData.getfEEDBACK_RECORD_REFERENCENO());
        values.put(KEY_FEEDBACK_RECORD_DATE, feedbackRecordData.getfEEDBACK_RECORD_DATE());
        values.put(KEY_FEEDBACK_RECORD_BREIF, feedbackRecordData.getfEEDBACK_RECORD_BREIF());
        values.put(KEY_FEEDBACK_RECORD_FLAG, feedbackRecordData.getfEEDBACK_RECORD_FLAG());
        values.put(KEY_FEEDBACK_RECORD_LAT, feedbackRecordData.getfEED_LAT());
        values.put(KEY_FEEDBACK_RECORD_LOG, feedbackRecordData.getfEED_LONG());


        // Inserting Row
        db.insert(TABLE_FEEDBACK_RECORD, null, values);
        Log.v(TAG, "Databaser insert feedbackRecord table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<FeedbackRecordData> getAllFeedbackRecord() {

        ArrayList<FeedbackRecordData> list = new ArrayList<FeedbackRecordData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK_RECORD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FeedbackRecordData data = new FeedbackRecordData();
                //only one column
                data.setfEEDBACK_RECORD_INCRIID(cursor.getInt(0));
                data.setfEEDBACK_RECORD_UNITNAME(cursor.getString(1));
                data.setfEEDBACK_RECORD_REFERENCENO(cursor.getString(2));
                data.setfEEDBACK_RECORD_DATE(cursor.getString(3));
                data.setfEEDBACK_RECORD_BREIF(cursor.getString(4));
                data.setfEEDBACK_RECORD_FLAG(cursor.getInt(5));
                data.setfEED_LAT(cursor.getString(6));
                data.setfEED_LONG(cursor.getString(7));

                //you could add additional columns here..

                list.add(data);
            } while (cursor.moveToNext());
        }


        return list;
    }


    public boolean updateFeedbackRecord(int incriid, int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_FEEDBACK_RECORD_FLAG, flag);

        int i = db.update(TABLE_FEEDBACK_RECORD, args, KEY_FEEDBACK_RECORD_INCRIID + "=" + incriid, null);
        return i > 0;
    }

    public void deleteFeedbackRecordTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FEEDBACK_RECORD + " ;");
        db.close();
    }


    public void insertfeedbackAttachment(AttachmentData attachmentData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put(KEY_ID, feedbackRecordData.getKeyid());
        values.put(KEY_FEEDBACK_ATT_REFNO, attachmentData.getRefNo());
        values.put(KEY_FEEDBACK_ATT_IMAGENAME, attachmentData.getAttachshow());
        values.put(KEY_FEEDBACK_ATT_IMAGE_FILE, attachmentData.getAttachsend());
        values.put(KEY_FEEDBACK_ATT_FLAG, attachmentData.getFlag());
        // Inserting Row
        db.insert(TABLE_FEEDBACK_ATTACHMENT, null, values);
        Log.v(TAG, "Databaser insert feedbackAttachmenttable");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<AttachmentData> getAllFeedbackAttachment() {

        ArrayList<AttachmentData> list = new ArrayList<AttachmentData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK_ATTACHMENT;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AttachmentData data = new AttachmentData();
                //only one column
                data.setIncriID(cursor.getInt(0));
                data.setRefNo(cursor.getString(1));
                data.setAttachshow(cursor.getString(2));
                data.setAttachsend(cursor.getString(3));
                data.setFlag(cursor.getInt(4));

                //you could add additional columns here..

                list.add(data);
            } while (cursor.moveToNext());
        }


        return list;
    }


    public List<AttachmentData> getAllFeedbackAttachment_BY_Refno(String refno) {

        ArrayList<AttachmentData> list = new ArrayList<AttachmentData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK_ATTACHMENT + " WHERE " + KEY_FEEDBACK_ATT_REFNO + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{refno});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AttachmentData data = new AttachmentData();
                //only one column
                data.setIncriID(cursor.getInt(0));
                data.setRefNo(cursor.getString(1));
                data.setAttachshow(cursor.getString(2));
                data.setAttachsend(cursor.getString(3));
                data.setFlag(cursor.getInt(4));

                //you could add additional columns here..

                list.add(data);
            } while (cursor.moveToNext());
        }


        return list;
    }


    public boolean updateFeedbackAttachment(int incriid, int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_FEEDBACK_ATT_FLAG, flag);
        int i = db.update(TABLE_FEEDBACK_ATTACHMENT, args, KEY_FEEDBACK_ATT_INCRIID + "=" + incriid, null);
        return i > 0;
    }

    public void deleteFeedbackAttachmentTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FEEDBACK_ATTACHMENT + " ;");
        db.close();
    }

    public void insertfeedbackCapture(CaptureData captureData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put(KEY_ID, feedbackRecordData.getKeyid());
        //values.put(KEY_FEEDBACK_CAPTURE_FNID, captureData.getFnID());

        values.put(KEY_FEEDBACK_CAPTURE_REFNO, captureData.getRefNo());
        values.put(KEY_FEEDBACK_CAPTURE_IMAGENAME, captureData.getCaptureImageshow());
        values.put(KEY_FEEDBACK_CAPTURE_IMAGE_FILE, captureData.getCaptureFilesend());
        values.put(KEY_FEEDBACK_CAPTURE_FLAG, captureData.getFlag());


        // Inserting Row
        db.insert(TABLE_FEEDBACK_CAPTURE, null, values);
        Log.v(TAG, "Databaser insert feedbackCapturetable");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void insertDynamicFormData(ArrayList<DynamicForm> arrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrayList.size(); i++) {
            DynamicForm dynamicForm = arrayList.get(i);
            if (dynamicForm == null)
                return;

            ContentValues values = new ContentValues();


            values.put(KEY_FORMNAME, dynamicForm.getFormName());
            values.put(KEY_FIELDNAME, dynamicForm.getFieldName());
            values.put(KEY_FIELDVALUE, dynamicForm.getFieldValue());
            values.put(KEY_DATATYPE, dynamicForm.getDatatype());
            values.put(KEY_SIZE, dynamicForm.getSize());
            values.put(KEY_DECIMAL, dynamicForm.getDecimal());


            // Inserting Row
            db.insert(TABLE_DYNAMICFORM, null, values);
        }
        Log.v(TAG, "Database insert Dynamic Form");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public ArrayList<DynamicForm> getDynamicFormData(String formName) {


        ArrayList<DynamicForm> list = new ArrayList<>();

        // Select All Query

        String query = "SELECT * FROM " + TABLE_DYNAMICFORM + " WHERE " + KEY_FORMNAME + " LIKE '%" + formName + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(query, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        DynamicForm data = new DynamicForm();
                        //only one column
                        data.setFormName(cursor.getString(0));
                        data.setFieldName(cursor.getString(1));
                        data.setFieldValue(cursor.getString(2));
                        data.setDatatype(cursor.getString(3));
                        data.setSize(cursor.getInt(4));
                        data.setDecimal(cursor.getDouble(5));

                        //you could add additional columns here..

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }


    public ArrayList<LatLongData> getLatLong(String formNumber) {

        ArrayList<LatLongData> list = new ArrayList<>();
        // Select All Query

        String query = "SELECT * FROM " + TABLE_LATLONG + " WHERE " + KEY_LATLONG_SETFORMNO + " LIKE '%" + formNumber + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(query, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        LatLongData data = new LatLongData();
                        //only one column
                        data.setFormno(cursor.getString(cursor.getColumnIndex(KEY_SETFORMNO)));
                        data.setDate(cursor.getString(cursor.getColumnIndex(KEY_LATLONG_DATE)));
                        data.setLat(cursor.getString(cursor.getColumnIndex(KEY_LATLONG_LAT)));
                        data.setLongi(cursor.getString(cursor.getColumnIndex(KEY_LATLONG_LONG)));
                        data.setLatlong_flag(cursor.getInt(cursor.getColumnIndex(KEY_LATLONG_FLAG)));
                        data.setTotaldis(cursor.getString(cursor.getColumnIndex(KEY_LATLONG_TOTALDIST)));
                        data.setCurrent_time_str(cursor.getString(cursor.getColumnIndex(KEY_LATLONG_TIME)));
                        data.setSpeed(cursor.getString(cursor.getColumnIndex(KEY_LATLONG_SPEED)));
                        data.setCountFlag(cursor.getInt(cursor.getColumnIndex(KEY_LATLONG_COUNT_FlAG)));
                        data.setBearing_degree(cursor.getString(cursor.getColumnIndex(KEY_LATLONG_BEARING_DEGREE)));
                        data.setBearing_name(cursor.getString(cursor.getColumnIndex(KEY_LATLONG_BEARING_NAME)));

                        //you could add additional columns here..

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<CaptureData> getAllFeedbackCaputre() {

        ArrayList<CaptureData> list = new ArrayList<CaptureData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK_CAPTURE;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        CaptureData data = new CaptureData();
                        //only one column
                        data.setIncri_id(cursor.getInt(0));
                        // data.setFnID(cursor.getInt(1));
                        data.setRefNo(cursor.getString(1));
                        data.setCaptureImageshow(cursor.getString(2));
                        data.setCaptureFilesend(cursor.getString(3));
                        data.setFlag(cursor.getInt(4));

                        //you could add additional columns here..

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<CaptureData> getAllFeedbackCapture_BY_Refno(String refno) {

        ArrayList<CaptureData> list = new ArrayList<CaptureData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK_CAPTURE + " WHERE " + KEY_FEEDBACK_CAPTURE_REFNO + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{refno});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CaptureData data = new CaptureData();
                //only one column
                data.setIncri_id(cursor.getInt(0));
                // data.setFnID(cursor.getInt(1));
                data.setRefNo(cursor.getString(1));
                data.setCaptureImageshow(cursor.getString(2));
                data.setCaptureFilesend(cursor.getString(3));
                data.setFlag(cursor.getInt(4));

                //you could add additional columns here..

                list.add(data);
            } while (cursor.moveToNext());
        }

        return list;
    }


    public boolean updateFeedbackCapture(int incriid, int flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_FEEDBACK_CAPTURE_FLAG, flag);

        int i = db.update(TABLE_FEEDBACK_CAPTURE, args, KEY_FEEDBACK_CAPTURE_INCRIID + "=" + incriid, null);

        return i > 0;

    }

    public void deleteFeedbackCaptureTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FEEDBACK_CAPTURE + " ;");
        db.close();
    }

/*
    public boolean insertcontacts(String name, String status, String from, String image) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("status", status);
        contentValues.put("`from`", from);
        contentValues.put("image", image);
        db.insert("contacts", null, contentValues);
        return true;
    }*/

/*
    public void insertAllContacts() {
        for (Picture contact : Contact.getContacts()) {
            insertcontacts(contact.getName(), contact.getStatus(), contact.getFrom(), contact.getImage());
        }
    }
*/

    //....................................Notification...............................................

    public void add_DB_Notification(NotificationData notificationData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTIFICTION_TITTLE, notificationData.getNoti_tittle());
        values.put(KEY_NOTIFICATION_MESSAGE, notificationData.getNoti_message());
        values.put(KEY_NOTIFICATION_IMAGE, notificationData.getNoti_image());


        // Inserting Row
        db.insert(TABLE_NOTIFICATION, null, values);
        Log.v(TAG, "Databaser insert notification table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<NotificationData> getAllNotification() {

        ArrayList<NotificationData> list = new ArrayList<NotificationData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NotificationData data = new NotificationData();
                //only one column
                data.setNoti_id(cursor.getInt(0));
                data.setNoti_tittle(cursor.getString(1));
                data.setNoti_message(cursor.getString(2));
                data.setNoti_tittle(cursor.getString(3));
                data.setFlag(cursor.getInt(4));

                //you could add additional columns here..

                list.add(data);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public void deleteSingleRow(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTIFICATION + " WHERE " + KEY_NOTIFICATION_MESSAGE + "='" + value + "'");
        db.close();
    }
//...............................................GPS Setting..........................

    public void insertGPSSettingData(SettingData settingData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SETT_GPS_ENABLED, settingData.getSett_Gpsenabled());
        values.put(KEY_SETT_GPS_INTERVEL, settingData.getSett_Gpsinterval());
        values.put(KEY_SETT_GPS_SPEED, settingData.getSett_Gpsspeed());
        values.put(KEY_SETT_STATUS, settingData.getSett_status());


        // Inserting Row
        db.insert(TABLE_SETTING, null, values);
        Log.v(TAG, "Databaser insert setting table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<SettingData> getGPS_settingData() {
        List<SettingData> List = new ArrayList<SettingData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SETTING;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SettingData data = new SettingData();
                //contact.setID(Integer.parseInt(cursor.getString(0)));
                data.setSett_id(cursor.getInt(0));
                data.setSett_Gpsenabled(cursor.getInt(1));
                data.setSett_Gpsinterval(cursor.getInt(2));
                data.setSett_Gpsspeed(cursor.getInt(3));
                data.setSett_status(cursor.getString(4));

                // Adding contact to list
                List.add(data);
            } while (cursor.moveToNext());
        }
        // return contact list
        return List;
    }


    public boolean updateGPSsettingData(int incriid, SettingData settingData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_SETT_GPS_ENABLED, settingData.getSett_Gpsenabled());
        args.put(KEY_SETT_GPS_INTERVEL, settingData.getSett_Gpsinterval());
        args.put(KEY_SETT_GPS_SPEED, settingData.getSett_Gpsspeed());
        args.put(KEY_SETT_STATUS, settingData.getSett_status());

        int i = db.update(TABLE_SETTING, args, KEY_SETT_INCRIID + "=" + incriid, null);

        return i > 0;

    }

    public void deleteGPSsettingData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SETTING + " ;");
        db.close();
    }

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End GPS Setting Data>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public void insertCheckListData(ChecklistData checklistData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CHECKLIST_FORMNO, checklistData.getFormno());
        values.put(KEY_CHECKLIST_NAME, checklistData.getName());
        values.put(KEY_CHECKLIST_NAMEVALUE, checklistData.getName_value());
        values.put(KEY_CHECKLIST_DATATYPE, checklistData.getDataType());
        values.put(KEY_CHECKLIST_DECIMAL, checklistData.getDecimal());
        values.put(KEY_CHECKLIST_FLAG, checklistData.getFlag());

        // Inserting Row
        db.insert(TABLE_CHECKLIST, null, values);
        Log.v(TAG, "Databaser insert checklist table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<ChecklistData> getAllChecklist() {
        ArrayList<ChecklistData> list = new ArrayList<ChecklistData>();

        try {

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_CHECKLIST;
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {


                    ChecklistData data = new ChecklistData();
                    //only one column
                    data.setId(cursor.getInt(0));
                    data.setFormno(cursor.getString(1));
                    data.setName(cursor.getString(2));
                    data.setName_value(cursor.getString(3));
                    data.setDataType(cursor.getString(4));
                    data.setDecimal(cursor.getString(5));
                    data.setFlag(cursor.getInt(6));

                    //you could add additional columns here..

                    list.add(data);
                } while (cursor.moveToNext());
            }

        } catch (Exception ex) {
            Log.v("SQLException", ex.getMessage());
        }
        return list;
    }


    public void deletecheckListData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CHECKLIST + " ;");
        db.close();
    }

    public List<ChecklistData> getAllChecklistwithFormno(String formno) {
        ArrayList<ChecklistData> list = new ArrayList<ChecklistData>();
        try {

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_CHECKLIST + " WHERE " + KEY_CHECKLIST_FORMNO + " = ?";
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, new String[]{formno});

            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    ChecklistData data = new ChecklistData();
                    //only one column
                    data.setId(cursor.getInt(0));
                    // data.setFnID(cursor.getInt(1));
                    data.setFormno(cursor.getString(1));
                    data.setName(cursor.getString(2));
                    data.setName_value(cursor.getString(3));
                    data.setDataType(cursor.getString(4));
                    data.setDecimal(cursor.getString(5));
                    data.setSize(cursor.getString(6));
                    data.setFlag(cursor.getInt(7));

                    //you could add additional columns here..


                    list.add(data);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.v("SQLException", ex.getMessage());
        }

        return list;
    }

    public boolean update_CheckList_input(String va, String flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_CHECKLIST_NAMEVALUE, flag);
        int i = db.update(TABLE_CHECKLIST, args, KEY_CHECKLIST_NAMEVALUE + "=" + va, null);
        return i > 0;
    }

  /*  public void insertCheckListData2_Save(ChecklistData2Save checklistData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CHECKLIST_2_VFORMNAME, checklistData.getV_form_name());
        values.put(KEY_CHECKLIST_2_VNAMEKEY, checklistData.getV_control_name());
        values.put(KEY_CHECKLIST_2_VNAMEVALUE, checklistData.getV_control_value());
        values.put(KEY_CHECKLIST_2_FLAG, checklistData.getFlag());


        // Inserting Row
        db.insert(TABLE_CHECKLIST2_SAVE, null, values);
        Log.v(TAG, "Databaser insert checklist table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<ChecklistData2Save> getAllChecklist2_Save() {

        ArrayList<ChecklistData2Save> list = new ArrayList<ChecklistData2Save>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHECKLIST2_SAVE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChecklistData2Save data = new ChecklistData2Save();
                //only one column
                data.setId(cursor.getInt(0));
                data.setV_form_name(cursor.getString(1));
                data.setV_control_name(cursor.getString(2));
                data.setV_control_value(cursor.getString(3));
                data.setFlag(cursor.getInt(4));
                //you could add additional columns here..

                list.add(data);
            } while (cursor.moveToNext());
        }

        return list;
    }*/

    public void deleteFormcheckListData(String formname) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + formname + " ;");
        db.close();
    }


    public ArrayList<HashMap<String, String>> getForm(String formname) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from " + formname;
        // Cursor cursor = db.rawQuery("select * from " + formname, null);
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new LinkedHashMap<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                maplist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return maplist;

    }
//................................ Survey Form...............................................

    public void insertFinalCheckListData_Save(SurveyFormData finalFeedBackData) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(KEYFINALCHECKLISTSAVE_SETFORMNO, finalFeedBackData.getFormNo());
            values.put(KEYFINALCHECKLISTSAVE_SNO, finalFeedBackData.getsNo());
            values.put(KEYFINALCHECKLISTSAVE_DESC, finalFeedBackData.getDesc());
            values.put(KEYFINALCHECKLISTSAVE_STS, finalFeedBackData.getSts());
            values.put(KEYFINALCHECKLISTSAVE_REMARK, finalFeedBackData.getRemark());
            values.put(KEYFINALCHECKLISTSAVE_PHOTOS, finalFeedBackData.getPhotos());
            values.put(KEYFINALCHECKLISTSAVE_PATH, finalFeedBackData.getPath());
            values.put(KEYFINALCHECKLISTSAVE_COUNT, finalFeedBackData.getCount());
            values.put(KEYFINALCHECKLISTSAVE_PHOTO1, finalFeedBackData.getImg1());
            values.put(KEYFINALCHECKLISTSAVE_PHOTO2, finalFeedBackData.getImg2());
            values.put(KEYFINALCHECKLISTSAVE_PHOTO3, finalFeedBackData.getImg3());
            values.put(KEYFINALCHECKLISTSAVE_PHOTO4, finalFeedBackData.getImg4());
            values.put(KEYFINALCHECKLISTSAVE_PHOTO5, finalFeedBackData.getImg5());
            values.put(KEYFINALCHECKLISTSAVE_PHOTOTEXT1, finalFeedBackData.getImageText1());
            values.put(KEYFINALCHECKLISTSAVE_PHOTOTEXT2, finalFeedBackData.getImageText2());
            values.put(KEYFINALCHECKLISTSAVE_PHOTOTEXT3, finalFeedBackData.getImageText3());
            values.put(KEYFINALCHECKLISTSAVE_PHOTOTEXT4, finalFeedBackData.getImageText5());
            values.put(KEYFINALCHECKLISTSAVE_PHOTOTEXT5, finalFeedBackData.getImageText5());
            values.put(KEYFINALCHECKLISTSAVE_FLAG, finalFeedBackData.getFlag());


            // Inserting Row
            db.insert(TABLE_FINALCHECKLIST2SAVE, null, values);
            Log.v(TAG, "Databaser insert finalchecklist table");
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (Exception ex) {
            Log.v("SQLException", ex.getMessage());
        }
    }


    public List<SurveyFormData> getAllFinalChecklist2_Save(String formno) {

        ArrayList<SurveyFormData> list = new ArrayList<SurveyFormData>();
        // Select All Query
       // String selectQuery = "SELECT  * FROM " + TABLE_FINALCHECKLIST2SAVE;
        String selectQuery = "SELECT  * FROM " + TABLE_FINALCHECKLIST2SAVE + " WHERE " + KEYFINALCHECKLISTSAVE_SETFORMNO + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{formno});
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {

                SurveyFormData data = new SurveyFormData();
                //only one column
                data.setFormNo(cursor.getString(1));
                data.setsNo(cursor.getString(2));
                data.setDesc(cursor.getString(3));
                data.setSts(cursor.getString(4));
                data.setRemark(cursor.getString(5));
                data.setPhotos(cursor.getString(6));
                data.setPath(cursor.getString(7));
                data.setCount(cursor.getInt(8));
                data.setImg1(cursor.getString(9));
                data.setImg2(cursor.getString(10));
                data.setImg3(cursor.getString(11));
                data.setImg4(cursor.getString(12));
                data.setImg5(cursor.getString(13));
                data.setImageText1(cursor.getString(14));
                data.setImageText2(cursor.getString(15));
                data.setImageText3(cursor.getString(16));
                data.setImageText4(cursor.getString(17));
                data.setImageText5(cursor.getString(18));
                data.setFlag(cursor.getInt(19));

                //you could add additional columns here..
                list.add(data);

            } while (cursor.moveToNext());
        }
        Log.v("Datalist",list.toString());

        return list;
    }

    public int getLastInsertIdFinalcheklist() {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_FINALCHECKLIST2SAVE, null);

        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        Log.v(TAG, index+"");
        return index;
    }


    public boolean updateFinalcheklistFlag(int incriid,  int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();

        args.put(KEYFINALCHECKLISTSAVE_FLAG, flag);
        //  args.put(KEY_LATLONG_TOTALDIST,dis);


        int i = db.update(TABLE_FINALCHECKLIST2SAVE, args, KEYFINALCHECKLISTSAVE_INCRI + "=" + incriid, null);
        return i > 0;
    }


    public int getCountFinalChecklist2_Save() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_FINALCHECKLIST2SAVE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    //................................ Survey Form...............................................

    //......................  Attendance Remote.............................
    public void insertAttandanceRemote(AttendenceRemoteData attendenceRemoteData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ATT_REMOTE_EMPID, attendenceRemoteData.getEmpId());
        values.put(ATT_REMOTE_CURRENTDATE, attendenceRemoteData.getCurrentDate());
        values.put(ATT_REMOTE_INTIME, attendenceRemoteData.getInTime());
        values.put(ATT_REMOTE_OUTTIME, attendenceRemoteData.getOutTime());
        values.put(ATT_REMOTE_INTIME_PICS, attendenceRemoteData.getIntimeImage());
        values.put(ATT_REMOTE_OUTTIME_PICS, attendenceRemoteData.getOuttimeImage());
        values.put(ATT_REMOTE_INLAT, attendenceRemoteData.getInlat());
        values.put(ATT_REMOTE_INLONG, attendenceRemoteData.getInlongi());
        values.put(ATT_REMOTE_OUTLAT, attendenceRemoteData.getOutlat());
        values.put(ATT_REMOTE_OUTLONG, attendenceRemoteData.getOutlongi());
        values.put(ATT_REMOTE_INTIMEREMARK, attendenceRemoteData.getIntimeRemark());
        values.put(ATT_REMOTE_OUTTIMEREMARK, attendenceRemoteData.getOuttimeRemark());
        values.put(ATT_REMOTE_NEARESTINTIME, attendenceRemoteData.getIntimetimeNear());
        values.put(ATT_REMOTE_NEARESTOUTTIME, attendenceRemoteData.getOuttimeNear());
        values.put(ATT_REMOTE_FLAG, attendenceRemoteData.getFlag());

        // Inserting Row
        db.insert(TABLE_ATTENDANCE_REMOTE, null, values);
        Log.v(TAG, "Databaser insert Attendance Remote table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    public List<AttendenceRemoteData> getAllAttendanceRemote() {

        ArrayList<AttendenceRemoteData> list = new ArrayList<AttendenceRemoteData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_REMOTE ;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendenceRemoteData data = new AttendenceRemoteData();
                        //only one column
                        data.setId(cursor.getInt(0));  //REMOTE ATT_REMOTE_INCRI

                        data.setEmpId(cursor.getString(1));// ATT_REMOTE_EMPID
                        data.setCurrentDate(cursor.getString(2));// ATT_REMOTE_CURRENTDATE
                        data.setInTime(cursor.getString(3));//ATT_REMOTE_INTIME
                        data.setOutTime(cursor.getString(4));//ATT_REMOTE_OUTTIME
                        data.setIntimeImage(cursor.getString(5));//ATT_REMOTE_INTIME_PICS
                        data.setOuttimeImage(cursor.getString(6));//ATT_REMOTE_OUTTIME_PICS
                        data.setInlat(cursor.getString(7));//ATT_REMOTE_LAT
                        data.setInlongi(cursor.getString(8));//ATT_REMOTE_LONG
                        data.setOutlat(cursor.getString(9));//ATT_REMOTE_LAT
                        data.setOutlongi(cursor.getString(10));
                        data.setIntimeRemark(cursor.getString(11));
                        data.setOuttimeRemark(cursor.getString(12));
                        data.setIntimetimeNear(cursor.getString(13));
                        data.setOuttimeNear(cursor.getString(14));
                        data.setFlag(cursor.getInt(15));//ATT_REMOTE_FLAG
                        //you could add additional columns here..
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<AttendenceRemoteData> getLastRowAttendanceRemote() {
        ArrayList<AttendenceRemoteData> list = new ArrayList<AttendenceRemoteData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        //String selectQuery = "SELECT  * FROM " + TABLE_LATLONG +" ORDER BY "+KEY_LATLONG_INCRIID+" DESC LIMIT 1;";

        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_REMOTE +  " ORDER BY " + ATT_REMOTE_INCRI + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendenceRemoteData data = new AttendenceRemoteData();
                        //only one column
                        data.setId(cursor.getInt(0));  //REMOTE ATT_REMOTE_INCRI

                        data.setEmpId(cursor.getString(1));// ATT_REMOTE_EMPID
                        data.setCurrentDate(cursor.getString(2));// ATT_REMOTE_CURRENTDATE
                        data.setInTime(cursor.getString(3));//ATT_REMOTE_INTIME
                        data.setOutTime(cursor.getString(4));//ATT_REMOTE_OUTTIME
                        data.setIntimeImage(cursor.getString(5));//ATT_REMOTE_INTIME_PICS
                        data.setOuttimeImage(cursor.getString(6));//ATT_REMOTE_OUTTIME_PICS
                        data.setInlat(cursor.getString(7));//ATT_REMOTE_LAT
                        data.setInlongi(cursor.getString(8));//ATT_REMOTE_LONG
                        data.setOutlat(cursor.getString(9));//ATT_REMOTE_LAT
                        data.setOutlongi(cursor.getString(10));
                        data.setIntimeRemark(cursor.getString(11));
                        data.setOuttimeRemark(cursor.getString(12));
                        data.setIntimetimeNear(cursor.getString(13));
                        data.setOuttimeNear(cursor.getString(14));

                        data.setFlag(cursor.getInt(15));//ATT_REMOTE_FLAG
                        //you could add additional columns here..

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;

    }

    public int getLastInsertIdAttendanceRemote() {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_ATTENDANCE_REMOTE, null);

        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        return index;
    }

    public void deleteAttendanceRemoteListData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ATTENDANCE_REMOTE + " WHERE " + ATT_REMOTE_INCRI + "='" + id + "'");
        db.close();
    }

//new AttendenceRemoteData(empid,current_date,tv_intime_get.getText().toString(),tv_outtime_get.getText().toString(),intimeIvEncodeString,outtime_IvEncodeString,getIntimeLat(),getIntimeLog(),lat,longi,edt_remote_intimeremark.getText().toString(),"",1));

    public boolean updateAttandenceRemoteStatus(int incriid,String outtime,String outtimePic,String outtimelat,String outtimelong, String intimeremark,String outimenear, int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();

        args.put(ATT_REMOTE_OUTTIME, outtime);
        args.put(ATT_REMOTE_OUTTIME_PICS, outtimePic);
        args.put(ATT_REMOTE_OUTLAT,outtimelat);
        args.put(ATT_REMOTE_OUTLONG, outtimelong);
        args.put(ATT_REMOTE_INTIMEREMARK, intimeremark);
        args.put(ATT_REMOTE_OUTTIMEREMARK,intimeremark);
        args.put(ATT_REMOTE_NEARESTOUTTIME,outimenear);
        args.put(ATT_REMOTE_FLAG, flag);
        int i = db.update(TABLE_ATTENDANCE_REMOTE, args, ATT_REMOTE_INCRI + "=" + incriid, null);
        return i > 0;
    }

//......................  Attendance Remote.............................
//.....................AttendanceFIx................

public void insertAttandanceFix(AttendenceFixData attendenceFixData) {
    SQLiteDatabase db = this.getWritableDatabase();
try{
    ContentValues values = new ContentValues();
    values.put(ATT_FIX_EMPID, attendenceFixData.getEmpId());
    values.put(ATT_FIX_CURRENTDATE, attendenceFixData.getCurrentDate());
    values.put(ATT_FIX_INTIME, attendenceFixData.getInTime());
    values.put(ATT_FIX_OUTTIME, attendenceFixData.getOutTime());
    values.put(ATT_FIX_INTIME_PICS, attendenceFixData.getIntimeImage());
    values.put(ATT_FIX_OUTTIME_PICS, attendenceFixData.getOuttimeImage());
    values.put(ATT_FIX_INLAT, attendenceFixData.getInlat());
    values.put(ATT_FIX_INLONG, attendenceFixData.getInlongi());
    values.put(ATT_FIX_OUTLAT, attendenceFixData.getOutlat());
    values.put(ATT_FIX_OUTLONG, attendenceFixData.getOutlongi());
    values.put(ATT_FIX_INTIMEREMARK, attendenceFixData.getIntimeRemark());
    values.put(ATT_FIX_OUTTIMEREMARK, attendenceFixData.getOuttimeRemark());
    values.put(ATT_FIX_NEARESTINTIME, attendenceFixData.getIntimetimeNear());
    values.put(ATT_FIX_NEARESTOUTTIME, attendenceFixData.getOuttimeNear());
    values.put(ATT_FIX_FLAG, attendenceFixData.getFlag());

    // Inserting Row
    db.insert(TABLE_ATTENDANCE_FIX, null, values);
    Log.v(TAG, "Databaser insert Attendance Fix table");
    //2nd argument is String containing nullColumnHack
    db.close(); // Closing database connection
} finally {
        try {
            db.close();

        } catch (Exception ignore) {
            Log.v(TAG, "insertException");
        }
    }
}


    public List<AttendenceFixData> getAllAttendanceFix() {

        ArrayList<AttendenceFixData> list = new ArrayList<AttendenceFixData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_FIX ;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendenceFixData data = new AttendenceFixData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setEmpId(cursor.getString(1));
                        data.setCurrentDate(cursor.getString(2));
                        data.setInTime(cursor.getString(3));
                        data.setOutTime(cursor.getString(4));
                        data.setIntimeImage(cursor.getString(5));
                        data.setOuttimeImage(cursor.getString(6));
                        data.setInlat(cursor.getString(7));
                        data.setInlongi(cursor.getString(8));
                        data.setOutlat(cursor.getString(9));
                        data.setOutlongi(cursor.getString(10));
                        data.setIntimeRemark(cursor.getString(11));
                        data.setOuttimeRemark(cursor.getString(12));
                        data.setIntimetimeNear(cursor.getString(13));
                        data.setOuttimeNear(cursor.getString(14));
                        data.setFlag(cursor.getInt(15));
                        //you could add additional columns here..
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<AttendenceFixData> getLastRowAttendanceFix() {

        ArrayList<AttendenceFixData> list = new ArrayList<AttendenceFixData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_FIX +  " ORDER BY " + ATT_FIX_INCRI + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendenceFixData data = new AttendenceFixData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setEmpId(cursor.getString(1));
                        data.setCurrentDate(cursor.getString(2));
                        data.setInTime(cursor.getString(3));
                        data.setOutTime(cursor.getString(4));
                        data.setIntimeImage(cursor.getString(5));
                        data.setOuttimeImage(cursor.getString(6));
                        data.setInlat(cursor.getString(7));
                        data.setInlongi(cursor.getString(8));
                        data.setOutlat(cursor.getString(9));
                        data.setOutlongi(cursor.getString(10));
                        data.setIntimeRemark(cursor.getString(11));
                        data.setOuttimeRemark(cursor.getString(12));
                        data.setIntimetimeNear(cursor.getString(13));
                        data.setOuttimeNear(cursor.getString(14));
                        data.setFlag(cursor.getInt(15));
                        //you could add additional columns here..
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public int getLastInsertIdAttendanceFix() {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_ATTENDANCE_FIX, null);

        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        return index;
    }

    public void deleteAttendanceFixListData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ATTENDANCE_FIX + " WHERE " + ATT_FIX_INCRI + "='" + id + "'");
        db.close();
    }

    public boolean updateAttandenceFixStatus(int incriid,String outtime,String outtimePic,String outtimelat,String outtimelong, String intimeremark,String outimenear, int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(ATT_FIX_OUTTIME, outtime);
        args.put(ATT_REMOTE_OUTTIME_PICS, outtimePic);
        args.put(ATT_REMOTE_OUTLAT,outtimelat);
        args.put(ATT_REMOTE_OUTLONG, outtimelong);
        args.put(ATT_REMOTE_INTIMEREMARK, intimeremark);
        args.put(ATT_REMOTE_OUTTIMEREMARK,intimeremark);
        args.put(ATT_REMOTE_NEARESTOUTTIME,outimenear);
        args.put(ATT_FIX_FLAG, flag);
        int i = db.update(TABLE_ATTENDANCE_FIX, args, ATT_FIX_INCRI + "=" + incriid, null);
        return i > 0;
    }

    //......................  OPEntry.............................

    public void insertOPEntry(OPEntryData opEntryData) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OPENTRY_KEY_ID, opEntryData.getKeyid());
        values.put(OPENTRY_PROJECTNO, opEntryData.getProjectNo());
        values.put(OPENTRY_MODE, opEntryData.getMode());
        values.put(OPENTRY_STARTKM, opEntryData.getStartkm());
        values.put(OPENTRY_ENDKM, opEntryData.getEndkm());
        values.put(OPENTRY_STARTKMIMAGE, opEntryData.getStartkmImage());
        values.put(OPENTRY_ENDKMIMAGE, opEntryData.getEndkmImage());
        values.put(OPENTRY_CURRENTDATE, opEntryData.getCurrentdate());
        values.put(OPENTRY_CUSTOMERNAME, opEntryData.getCustomerName());
        values.put(OPENTRY_OPTYPE, opEntryData.getOptype());
        values.put(OPENTRY_REASONOP, opEntryData.getReasonforop());
        values.put(OPENTRY_MANUALLOCATION, opEntryData.getManuallocation());
        values.put(OPENTRY_CURRENTLOCATION, opEntryData.getCurrentLocation());
        values.put(OPENTRY_REMARK, opEntryData.getRemark());
        values.put(OPENTRY_FLAG, opEntryData.getFlag());
        // Inserting Row
        db.insert(TABLE_OPENTRY, null, values);
        Log.v(TAG, "Database insert OPEntry table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<OPEntryData> getAllOPEntry() {
        ArrayList<OPEntryData> list = new ArrayList<OPEntryData>();
        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_OPENTRY;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    OPEntryData data = new OPEntryData();
                    //only one column
                    data.setId(cursor.getInt(0));
                    data.setKeyid(cursor.getInt(1));
                    data.setProjectNo(cursor.getString(2));
                    data.setMode(cursor.getString(3));
                    data.setStartkm(cursor.getString(4));
                    data.setEndkm(cursor.getString(5));
                    data.setStartkmImage(cursor.getString(6));
                    data.setEndkmImage(cursor.getString(7));
                    data.setCurrentdate(cursor.getString(8));
                    data.setCustomerName(cursor.getString(9));
                    data.setOptype(cursor.getString(10));
                    data.setReasonforop(cursor.getString(11));
                    data.setManuallocation(cursor.getString(12));
                    data.setCurrentLocation(cursor.getString(13));
                    data.setRemark(cursor.getString(14));
                    data.setFlag(cursor.getInt(15));
                    //you could add additional columns here..
                    list.add(data);
                } while (cursor.moveToNext());
            }

        } catch (Exception ex) {
            Log.v("SQLException", ex.getMessage());
        }
        return list;
    }

    public List<OPEntryData> getLastOPEntryData() {
        ArrayList<OPEntryData> list = new ArrayList<OPEntryData>();
        String selectQuery = "SELECT  * FROM " + TABLE_OPENTRY  + " ORDER BY " + OPENTRY_INCRI+ " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        OPEntryData data = new OPEntryData();
                        //contact.setID(Integer.parseInt(cursor.getString(0)));
                        data.setId(cursor.getInt(0));//OPENTRY_INCRI
                        data.setKeyid(cursor.getInt(1));  //OPENTRY_KEY_ID
                        data.setProjectNo(cursor.getString(2));  //OPENTRY_PROJECTNO
                        data.setMode(cursor.getString(3)); //OPENTRY_MODE
                        data.setStartkm(cursor.getString(4));//OPENTRY_STARTKM
                        data.setEndkm(cursor.getString(5));//OPENTRY_ENDKM
                        data.setStartkmImage(cursor.getString(6));//OPENTRY_STARTKMIMAGE
                        data.setEndkmImage(cursor.getString(7));//OPENTRY_ENDKMIMAGE
                        data.setCurrentdate(cursor.getString(8));//OPENTRY_CURRENTDATE
                        data.setCustomerName(cursor.getString(9));//OPENTRY_CUSTOMERNAME
                        data.setOptype(cursor.getString(10));//OPENTRY_OPTYPE
                        data.setReasonforop(cursor.getString(11));//OPENTRY_REASONOP
                        data.setManuallocation(cursor.getString(12));//OPENTRY_MANUALLOCATION
                        data.setCurrentLocation(cursor.getString(13));//OPENTRY_CURRENTLOCATION
                        data.setRemark(cursor.getString(14));//OPENTRY_REMARK
                        data.setFlag(cursor.getInt(15));//OPENTRY_FLAG
                        // Adding contact to list
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;

    }
    public int getLastOPEntryInsertId() {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_OPENTRY, null);

        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        return index;
    }


    public boolean updateOPEntryDetail(int rowId,String projectNo,String mode,String startkm_text,
                    String endkm_text, String startkm_image, String endkm_image,String current_date,
                    String custoname, String stoptype,String streasonop, String mLocation,String getAddress,String opentry_remark,int flag){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();

        args.put(OPENTRY_PROJECTNO, projectNo);
        args.put(OPENTRY_MODE, mode);
        args.put(OPENTRY_STARTKM, startkm_text);
        args.put(OPENTRY_ENDKM, endkm_text);
        args.put(OPENTRY_STARTKMIMAGE, startkm_image);
        args.put(OPENTRY_ENDKMIMAGE, endkm_image);
        args.put(OPENTRY_CURRENTDATE,current_date);
        args.put(OPENTRY_CUSTOMERNAME, custoname);
        args.put(OPENTRY_OPTYPE, stoptype);
        args.put(OPENTRY_REASONOP, streasonop);
        args.put(OPENTRY_MANUALLOCATION, mLocation);
        args.put(OPENTRY_CURRENTLOCATION, getAddress);
        args.put(OPENTRY_REMARK, opentry_remark);
        args.put(OPENTRY_FLAG, flag);

        int i = db.update(TABLE_OPENTRY, args, OPENTRY_INCRI + "=" + rowId, null);
        return i > 0;
    }


    public boolean updateOPEntryStatus(int incriid, int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(OPENTRY_FLAG, flag);
        int i = db.update(TABLE_OPENTRY, args, OPENTRY_INCRI + "=" + incriid, null);
        return i > 0;
    }

    public void deleteOPEntryDataFormno(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_OPENTRY + " WHERE " + OPENTRY_PROJECTNO + "='" + value + "'");
        db.close();
    }


    public void insertOPEntryLatLong(OPEntryLatLog latLongData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OPENTRYLATLONG_SETFORMNO, latLongData.getFormno());
        values.put(KEY_OPENTRYLATLONG_DATE, latLongData.getDate());
        values.put(KEY_OPENTRYLATLONG_LAT, latLongData.getLat());
        values.put(KEY_OPENTRYLATLONG_LONG, latLongData.getLongi());
        values.put(KEY_OPENTRYLATLONG_TOTALDIST, latLongData.getTotaldis());
        values.put(KEY_OPENTRYLATLONG_FLAG, latLongData.getLatlong_flag());
        values.put(KEY_OPENTRYLATLONG_TIME, latLongData.getCurrent_time_str());
        values.put(KEY_OPENTRYLATLONG_SPEED, latLongData.getSpeed());
        // values.put(KEY_LATLONG_TOTALDIST, latLongData.getTotaldis());
        // Inserting Row
        db.insert(TABLE_OPENTRY_LATLOG, null, values);
        Log.v(TAG, "Databaser insert OP Entry Lat Long data table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<OPEntryLatLog> getAllOPEntryLatLongStatus() {
        ArrayList<OPEntryLatLog> list = new ArrayList<OPEntryLatLog>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_OPENTRY_LATLOG + " WHERE " + KEY_OPENTRYLATLONG_FLAG + " = 0" + " ORDER BY " + KEY_OPENTRYLATLONG_INCRIID + " ASC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        OPEntryLatLog data = new OPEntryLatLog();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setFormno(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setLat(cursor.getString(3));
                        data.setLongi(cursor.getString(4));
                        data.setLatlong_flag(cursor.getInt(5));
                        data.setTotaldis(cursor.getString(6));
                        data.setCurrent_time_str(cursor.getString(7));
                        data.setSpeed(cursor.getString(8));
                        //you could add additional columns here..
                        list.add(data);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }
    public List<OPEntryLatLog> getAllOPEntryLatLongORDerBy() {

        ArrayList<OPEntryLatLog> list = new ArrayList<OPEntryLatLog>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_OPENTRY_LATLOG + " ORDER BY " + KEY_OPENTRYLATLONG_INCRIID + " ASC;";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        OPEntryLatLog data = new OPEntryLatLog();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setFormno(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setLat(cursor.getString(3));
                        data.setLongi(cursor.getString(4));
                        data.setLatlong_flag(cursor.getInt(5));
                        data.setTotaldis(cursor.getString(6));
                        data.setCurrent_time_str(cursor.getString(7));
                        data.setSpeed(cursor.getString(8));
                        //you could add additional columns here..
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<OPEntryLatLog> getLastOPEntryLatLong(String formno) {
        ArrayList<OPEntryLatLog> list = new ArrayList<OPEntryLatLog>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        //String selectQuery = "SELECT  * FROM " + TABLE_LATLONG +" ORDER BY "+KEY_LATLONG_INCRIID+" DESC LIMIT 1;";
        String selectQuery = "SELECT  * FROM " + TABLE_OPENTRY_LATLOG + " WHERE " + KEY_OPENTRYLATLONG_SETFORMNO + " = ?" + " ORDER BY " + KEY_OPENTRYLATLONG_INCRIID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{formno});
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        OPEntryLatLog data = new OPEntryLatLog();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setFormno(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setLat(cursor.getString(3));
                        data.setLongi(cursor.getString(4));
                        data.setLatlong_flag(cursor.getInt(5));
                        data.setTotaldis(cursor.getString(6));
                        data.setCurrent_time_str(cursor.getString(7));
                        data.setSpeed(cursor.getString(8));
                        //you could add additional columns here..
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                }
                catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }
        return list;
    }


    public boolean updateOPEntryLatLong(int incriid,  int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();

        args.put(KEY_OPENTRYLATLONG_FLAG, flag);
        //  args.put(KEY_LATLONG_TOTALDIST,dis);
        int i = db.update(TABLE_OPENTRY_LATLOG, args, KEY_OPENTRYLATLONG_INCRIID + "=" + incriid, null);
        return i > 0;
    }

    public void deleteOPEntryLatLogDataFormno(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_OPENTRY_LATLOG + " WHERE " + KEY_OPENTRYLATLONG_SETFORMNO + "='" + value + "'");
        db.close();
    }
    public void insertOPEntryMilestone(OPEntryMilestone opEntryMilestone) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OPENTRY_MILESTONE_SETFORMNO, opEntryMilestone.getFormno());
        values.put(KEY_OPENTRY_MILESTONE_DATE, opEntryMilestone.getDate());
        values.put(KEY_OPENTRY_MILESTONE_LAT, opEntryMilestone.getLat());
        values.put(KEY_OPENTRY_MILESTONE_LONG, opEntryMilestone.getLongi());
        values.put(KEY_OPENTRY_MILESTONE_LOCATION, opEntryMilestone.getLocation());
        values.put(KEY_OPENTRY_MILESTONE_NEAREST, opEntryMilestone.getNearestLocation());
        values.put(KEY_OPENTRY_MILESTONEL_FLAG, opEntryMilestone.getMilestone_flag());

        // values.put(KEY_LATLONG_TOTALDIST, latLongData.getTotaldis());


        // Inserting Row
        db.insert(TABLE_OPENTRY_MILESTONE, null, values);
        Log.v(TAG, "Databaser insert OP Entry Milestone data table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<OPEntryMilestone> getOPEntryMilestone(String formno) {
        ArrayList<OPEntryMilestone> list = new ArrayList<OPEntryMilestone>();

        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        //String selectQuery = "SELECT  * FROM " + TABLE_LATLONG +" ORDER BY "+KEY_LATLONG_INCRIID+" DESC LIMIT 1;";

        String selectQuery = "SELECT  * FROM " + TABLE_OPENTRY_MILESTONE + " WHERE " + KEY_OPENTRY_MILESTONE_SETFORMNO + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, new String[]{formno});
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        OPEntryMilestone data = new OPEntryMilestone();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setFormno(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setLat(cursor.getString(3));
                        data.setLongi(cursor.getString(4));
                        data.setLocation(cursor.getString(5));
                        data.setNearestLocation(cursor.getString(6));
                        data.setMilestone_flag(cursor.getInt(7));
                        //you could add additional columns here..
                        list.add(data);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }
        return list;

    }
    public List<OPEntryMilestone> getAllOPEntryMilestoneStatus() {
        ArrayList<OPEntryMilestone> list = new ArrayList<OPEntryMilestone>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_OPENTRY_MILESTONE + " WHERE " + KEY_OPENTRY_MILESTONEL_FLAG + " = 0" + " ORDER BY " + KEY_OPENTRY_MILESTONE_INCRIID + " ASC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        OPEntryMilestone data = new OPEntryMilestone();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setFormno(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setLat(cursor.getString(3));
                        data.setLongi(cursor.getString(4));
                        data.setLocation(cursor.getString(5));
                        data.setNearestLocation(cursor.getString(6));
                        data.setMilestone_flag(cursor.getInt(7));

                        //you could add additional columns here..
                        list.add(data);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }
    public int getLastInsertIdMilestone() {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_OPENTRY_MILESTONE, null);

        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        Log.v(TAG, index+"");
        return index;
    }


    public boolean updateMilestone(int incriid,  int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();

        args.put(KEY_OPENTRY_MILESTONEL_FLAG, flag);
        //  args.put(KEY_LATLONG_TOTALDIST,dis);


        int i = db.update(TABLE_OPENTRY_MILESTONE, args, KEY_OPENTRY_MILESTONE_INCRIID + "=" + incriid, null);
        return i > 0;
    }
//...............................................Attendance Setting..........................

    public void insertAttendanceSettingData(AttendanceSettingData settingData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ATTENDANCE_SETT_LAT, settingData.getLat());
        values.put(KEY_ATTENDANCE_SETT_LOG, settingData.getLog());
        values.put(KEY_ATTENDANCE_SETT_RADILDIS, settingData.getRadialdistance());
        values.put(KEY_ATTENDANCE_SETT_TYPE, settingData.getType());


        // Inserting Row
        db.insert(TABLE_ATTENDANCESETTING, null, values);
        Log.v(TAG, "Databaser insert Attendance setting table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<AttendanceSettingData> getAttendance_settingData() {
        List<AttendanceSettingData> List = new ArrayList<AttendanceSettingData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCESETTING;

        SQLiteDatabase db = this.getWritableDatabase();


        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendanceSettingData data = new AttendanceSettingData();
                        //contact.setID(Integer.parseInt(cursor.getString(0)));
                        data.setId(cursor.getInt(0));
                        data.setLat(cursor.getString(1));
                        data.setLog(cursor.getString(2));
                        data.setRadialdistance(cursor.getString(3));
                        data.setType(cursor.getString(4));

                        // Adding contact to list
                        List.add(data);
                    } while (cursor.moveToNext());
                }
            }finally {
                    try {
                        cursor.close();

                    } catch (Exception ignore) {
                    }
                }

            } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }
        // return contact list
        return List;
    }


    public void deleteAttendancesettingData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ATTENDANCESETTING + " ;");
        db.close();
    }

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End GPS Setting Data>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public boolean updateFinalcheklistTable1Flag(int incriid,  int flag, String fromno) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put("Status", flag);
        //  args.put(KEY_LATLONG_TOTALDIST,dis);
        int i = db.update(fromno, args, "id" + "=" + incriid, null);
        return i > 0;
    }

    public int getLastInsertIdFinalcheklistTable1(String fromno) {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + fromno, null);
        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        return index;
    }

    public  ArrayList<HashMap<String, String>> printTable1Data(String table_name){
        ArrayList<HashMap<String, String>> prodArrayList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        String row_values = "";
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name, null);

        if(cur.getCount() != 0){
            cur.moveToFirst();

            do{


                for(int i = 0 ; i < cur.getColumnCount(); i++){
                    String data = cur.getString(i);
                    String column_name = cur.getColumnName(i);

                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("column_value",data);
                    map.put("column_name",column_name);

                    prodArrayList.add(map);
                }

                Log.d("LOG_TAG_HERE", row_values);

            }while (cur.moveToNext());
        }

        return prodArrayList;
    }

    public int numberOfColumns(String tableName) {
        int result = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(tableName, null, null, null, null, null,
                    null);
            result = cursor.getColumnCount();
            if (result < 0) {
                result = 0;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return result;
    }

    public void insertGPSCheckStatusData(GPSCheckStatusData gpsCheckStatusData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GPSCHECK_TIME, gpsCheckStatusData.getTime());
        values.put(KEY_GPSCHECK_STATUS, gpsCheckStatusData.getStatus());



        // Inserting Row
        db.insert(TABLE_GPSCHECKSTATUSDATA, null, values);
        Log.v(TAG, "Databaser insert GPS Status table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<GPSCheckStatusData> getGPSCheckStatusData() {
        List<GPSCheckStatusData> List = new ArrayList<GPSCheckStatusData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GPSCHECKSTATUSDATA;
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        GPSCheckStatusData data = new GPSCheckStatusData();
                        //contact.setID(Integer.parseInt(cursor.getString(0)));
                        data.setId(cursor.getInt(0));
                        data.setTime(cursor.getString(1));
                        data.setStatus(cursor.getString(2));

                        // Adding contact to list
                        List.add(data);
                    } while (cursor.moveToNext());
                }
            }finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }
        // return contact list
        return List;
    }


}