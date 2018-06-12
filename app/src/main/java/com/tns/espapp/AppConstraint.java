package com.tns.espapp;

import com.tns.espapp.service.SendLatiLongiServerIntentService;

import java.util.HashMap;

/**
 * Created by TNS on 12/21/2016.
 */

public interface AppConstraint {
    public static Double GEOFENCELAT = 28.628997;
    public static Double GEOFENCELONG = 77.379377;
    public static final float GEOFENCE_RADIUS_IN_METERS = 25;

    public static String form[] ={"Form1","Form2","Form3","Form4","Form5"};
    //  static final String APP_SERVER_URL = "http://192.168.2.4:9000/gcm/gcm.php?shareRegId=true";
    static final String APPNAME = "ESP";
    static final String ESP_LOGIN = "http://www.tnserp.com/apiesp/api/Taxi/Login";
    // static final String ESP_LOGIN ="http://bkp-server";


    static final String FTP_HOST = "119.82.74.82";
    static final String FTP_USER = "tnssoft";
    static final String FTP_PASS = "@soft4321";

    String REGISTERFLAG = "REGISTERED";
    String APPROVEDFLAG = "APPROVEDFLAG";
    String EMPID = "EMPID";
    String PASSWORD = "PASSWORD";

    String NOTIFICATIONCOUNTER ="NotificationCounter";
    String SELECTEDFORMNUMBER ="selectedFromNumber";


    int REGISTERED = 1;
    int NOTREGISTERED = 0;
    int APPROVED = 1;
    int NOTAPPROVED = 0;



    static final String TAXIFORMURL = "http://www.tnserp.com/apiesp/api/Taxi/Taxi";
    static final String TAXITRACKROOT = "http://www.tnserp.com/apiesp/api/Taxi/ExecuteJsonsp";
    static final String FEEDBACK_UNIT = "http://www.tnserp.com/apiesp/api/Taxi/ExecuteJson";
    static final String PERSONALDETAIL = "http://www.tnserp.com/apiesp/api/info/personaldetails";
    static final String ENTITLEMENTDETAIL = "http://www.tnserp.com/ApiEsp/Api/info/EntitlementDetails";
    static final String FINANCIALYEARFORDRP = "http://www.tnserp.com/ApiEsp/Api/info/YearForDrp";
    String FINANCIALYEARFORDRP1 = "http://www.tnserp.com/ApiEsp/Api/info/FinancialYearForDrp";
    static final String BILLDETAILS = "http://www.tnserp.com/ApiEsp/Api/info/BillDetails";
    static final String LEAVEINFO = "http://www.tnserp.com/ApiEsp/Api/info/EmployeeLeaveInfo";
    static final String SALARYDETAIL = "http://www.tnserp.com/ApiEsp/Api/info/ShowSalaryDetails";
    static final String ACCOUNTSTATEMENTINFO = "http://www.tnserp.com/ApiEsp/Api/info/AccountStatementInfo";
    static final String LEAVETRANSACTION = "http://www.tnserp.com/ApiEsp/Api/info/EmployeeLeaveTransection";
    static final String ATTENDANCEDETAILS = "http://www.tnserp.com/ApiEsp/Api/info/AttendanceDetails";
    static final String MISCELLANOUSDETAILS = "http://www.tnserp.com/ApiEsp/Api/info/MiscellaniousDetails";
    static final String MISCELLANOUSDETAILSDOWNLOADS = "http://www.tnserp.com/ESP/";
    static final String LEAVELADGER = "http://www.tnserp.com/ApiEsp/Api/info/LeaveSummery";
    static final String LEAVEFROMDATA = "http://www.tnserp.com/ApiEsp/Api/info/LeaveFormData";
    static final String LEAVEFROM = "http://www.tnserp.com/ApiEsp/Api/info/LeaveForm";

    public static final String MESSAGE_PROGRESS = "message_progress";
    static final String COMMONURL = "http://www.tnssofts.com/apiesp/api/Taxi/ExecuteJsonsp";

    String VERIFYLOGINURL = "http://www.tnserp.com/apiesp/api/Taxi/LoginVarify";
    String FORMVERIFY  ="http:///www.tnssofts.com/apiesp/api/Form/FormVarify";

    String FORMNAME ="http://www.tnssofts.com/apiesp/api/Form/FormName";
    String DYNAMICFORMNAME ="http://www.tnserp.com/apiesp/api/Form/FormNameData";

    String DYNAMICGETJSONDATA ="http://www.tnserp.com/apiesp/api/Form/JsonData";
    String DYNAMICINSERTJSONDATA1 ="http://www.tnserp.com/apiesp/api/Form/DynamicDataInsert1";
    String DYNAMICINSERTJSONDATA2 ="http://tnserp.com/ApiEsp/api/Form/DynamicDataInsert2";


    String DYNAMICSURVEYFIXFORMNAME ="http://www.tnserp.com/apiesp/api/Form/QuestionFormNameData";
    String DYNAMICSURVEYFIXGETJSONDATA ="http://www.tnserp.com/apiesp/api/Form/FeedBackQuestionJsonData";
    String DYNAMICSURVEYFIXINSERTJSONDATA ="http://www.tnserp.com/apiesp/api/Form/FeedBackQuestionDynamicDataInsert";

    String DYNAMICSURVEYFIXINSERTJSONDATA1 ="http://www.tnserp.com/apiesp/api/Form/FeedBackQuestionDynamicDataInsert1";
    String DYNAMICSURVEYFIXINSERTJSONDATA2 ="http://www.tnserp.com/apiesp/api/Form/FeedBackQuestionDynamicDataInsert2";


    String ATTENDANCEREMOTE ="http://www.tnserp.com/ApiEsp/Api/info/AndroidAttendance";
    String ATTENDANCEREMOTE_SETTING ="http://tnserp.com/ApiEsp/api/info/AttendanceSettings";

    String OPENTRY = "http://www.tnserp.com/apiesp/api/OP/OPEntry";
    String OPENTRYMILESTONE = "http://tnserp.com/ApiEsp/Api/OP/OPEntryMileStone";
    String OPENTRYLATLOG = "http://tnserp.com/ApiEsp/api/Op/OPEntryLatLog";


    String ATTENDANCEREPORTMONTH = "http://tnserp.com/apiesp/api/Info/AttendanceReportMonthWise";


    // Google Project Number
    static final String GOOGLE_PROJ_ID = "837715578074";
    // Message Key
    static final String MSG_KEY = "m";
    public static String FCM_PUSH_URL ="https://fcm.googleapis.com/fcm/send";


}





/*

        S/No. 	FTP Folder	User Name	Password 	Permission
        1	TNSSOFT Folder	tnssoft	@soft4321	R/W

        1. FTP USE FOR OUT SIDE USER  Address is:- ftp://119.82.74.82
        2. FTP USE FOR OUT SIDE USER  Address is:- ftp://182.71.51.34
        3. FTP USE FOR INTERNAL USER  Address is:- ftp://192.168.1.6
*/
