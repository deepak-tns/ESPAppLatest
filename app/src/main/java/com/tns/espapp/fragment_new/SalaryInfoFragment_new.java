package com.tns.espapp.fragment_new;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.database.FinanceYearData;
import com.tns.espapp.database.SalaryDetailData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.icu.lang.UCharacter.LineBreak.GLUE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalaryInfoFragment_new extends Fragment {
    private WebView mWebView;
    private ProgressDialog pd;
    private Long mOnGoingDownload;
    private DownloadManager mDownloadManger;
    private String year1;
    private String year2, selectMonth;
    private Spinner spi_select_year, spiMonth, spi_salaryinfo_reporttype;
    private ArrayAdapter adapter, adaptermonth, adaptersalaryType;

    ArrayList<String> selectyear_list = new ArrayList<>();
    ArrayList<String> selectMonthList = new ArrayList<>();
    ArrayList<String> selectReportTypeList = new ArrayList<>();


    private TextView tv_salaryinfo_salaryfor,
            tv_salaryinfo_workingdays,
            tv_salaryinfo_fworkingdays,
            tv_salaryinfo_basicda,
            tv_salaryinfo_hra,
            tv_salaryinfo_tptallow,
            tv_salaryinfo_splallow,
            tv_salaryinfo_pli,
            tv_salaryinfo_conveyallow,
            tv_salaryinfo_commallow,
            tv_salaryinfo_fieldallow,
            tv_salaryinfo_grossalary,
            tv_salaryinfo_emplrepf,
            tv_salaryinfo_empepf,
            tv_salaryinfo_emplresic,
            tv_salaryinfo_empesic,
            tv_salaryinfo_communication,
            tv_salaryinfo_imprest,
            tv_salaryinfo_loanadv,
            tv_salaryinfo_tds,
            tv_salaryinfo_deduction,
            tv_salaryinfo_netsalary;

    private LinearLayout linear_salarydetail;
    private TextView tv_salaryinfo_norecord;
    private String empid;

    public SalaryInfoFragment_new() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_salary_info_fragment_new, container, false);

        // generatePdf();
        final SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstraint.EMPID);

        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int curryear = now.get(Calendar.YEAR);
        year2 = String.valueOf(curryear);
        //  createJsonRequestYear(empid);
        selectyear_list.add(curryear-1+"");
        selectyear_list.add(year2);
        findIDS(v);
        return v;
    }

    private void findIDS(final View v) {
        selectMonthList.add("Jan");
        selectMonthList.add("Feb");
        selectMonthList.add("Mar");
        selectMonthList.add("Apr");
        selectMonthList.add("May");
        selectMonthList.add("Jun");
        selectMonthList.add("Jul");
        selectMonthList.add("Aug");
        selectMonthList.add("Sep");
        selectMonthList.add("Oct");
        selectMonthList.add("Nov");
        selectMonthList.add("Dec");


        spi_select_year = (Spinner) v.findViewById(R.id.spi_salaryinfo_selectyear);
        spi_salaryinfo_reporttype = (Spinner) v.findViewById(R.id.spi_salaryinfo_reporttype);
        spiMonth = (Spinner) v.findViewById(R.id.spi_salaryinfo_selectmonth);

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectyear_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_select_year.setAdapter(adapter);
        spi_select_year.setSelection(1);
        spi_select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                year2 = (String) parent.getItemAtPosition(position);
                //   createJsonRequestLeaveInfoData(empid);
                // createJsonRequestSalaryDetailData(empid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adaptermonth = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectMonthList);
        adaptermonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiMonth.setAdapter(adaptermonth);

        spiMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectMonth = (String) parent.getItemAtPosition(position);

                // createJsonRequestLeaveInfoData(empid);
                // createJsonRequestSalaryDetailData(empid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       selectReportTypeList.add("Salary Detail");
      // selectReportTypeList.add("Salary Slip");

        adaptersalaryType = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectReportTypeList);
        adaptersalaryType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_salaryinfo_reporttype.setAdapter(adaptersalaryType);

        spi_salaryinfo_reporttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String data = (String) parent.getItemAtPosition(position);

                if(data.equalsIgnoreCase("Salary Slip"))
                {
                  downloadSalarySlip(v);
                    mWebView.setDownloadListener(new DownloadListener() {
                        @Override
                        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                            startDownload(url, userAgent, mimetype);
                        }
                    });


                    mWebView.loadUrl("http://tnserp.com/ESP/Info/SalaryInfoWebView/"+empid);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button btn_salaryifoSearch = (Button) v.findViewById(R.id.btn_salaryifoSearch);
        btn_salaryifoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createJsonRequestSalaryDetailData(empid);
            }
        });


        tv_salaryinfo_salaryfor = (TextView) v.findViewById(R.id.tv_salaryinfo_salaryfor);
        tv_salaryinfo_workingdays = (TextView) v.findViewById(R.id.tv_salaryinfo_workingdays);
        tv_salaryinfo_fworkingdays = (TextView) v.findViewById(R.id.tv_salaryinfo_fworkingdays);
        tv_salaryinfo_basicda = (TextView) v.findViewById(R.id.tv_salaryinfo_basicda);
        tv_salaryinfo_hra = (TextView) v.findViewById(R.id.tv_salaryinfo_hra);
        tv_salaryinfo_tptallow = (TextView) v.findViewById(R.id.tv_salaryinfo_tptallow);
        tv_salaryinfo_splallow = (TextView) v.findViewById(R.id.tv_salaryinfo_splallow);
        tv_salaryinfo_pli = (TextView) v.findViewById(R.id.tv_salaryinfo_pli);
        tv_salaryinfo_conveyallow = (TextView) v.findViewById(R.id.tv_salaryinfo_conveyallow);
        tv_salaryinfo_commallow = (TextView) v.findViewById(R.id.tv_salaryinfo_commallow);
        tv_salaryinfo_fieldallow = (TextView) v.findViewById(R.id.tv_salaryinfo_fieldallow);
        tv_salaryinfo_grossalary = (TextView) v.findViewById(R.id.tv_salaryinfo_grossalary);
        tv_salaryinfo_emplrepf = (TextView) v.findViewById(R.id.tv_salaryinfo_emplrepf);
        tv_salaryinfo_empepf = (TextView) v.findViewById(R.id.tv_salaryinfo_empepf);
        tv_salaryinfo_emplresic = (TextView) v.findViewById(R.id.tv_salaryinfo_emplresic);
        tv_salaryinfo_empesic = (TextView) v.findViewById(R.id.tv_salaryinfo_empesic);
        tv_salaryinfo_communication = (TextView) v.findViewById(R.id.tv_salaryinfo_communication);
        tv_salaryinfo_imprest = (TextView) v.findViewById(R.id.tv_salaryinfo_imprest);
        tv_salaryinfo_loanadv = (TextView) v.findViewById(R.id.tv_salaryinfo_loanadv);
        tv_salaryinfo_tds = (TextView) v.findViewById(R.id.tv_salaryinfo_tds);
        tv_salaryinfo_deduction = (TextView) v.findViewById(R.id.tv_salaryinfo_deduction);
        tv_salaryinfo_netsalary = (TextView) v.findViewById(R.id.tv_salaryinfo_netsalary);

        linear_salarydetail = (LinearLayout) v.findViewById(R.id.linear_salarydetail);
        tv_salaryinfo_norecord = (TextView) v.findViewById(R.id.tv_salaryinfo_norecord);
    }


    private void createJsonRequestYear(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = AppConstraint.FINANCIALYEARFORDRP;


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamYear(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseYear(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }
//............................JSON  Parameter Get Year
    private JSONObject jsonParamYear(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // jsonArray.put("");

        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "GetYearForDrp");
            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponseYear(String results) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        if (results.equals("[]")) {
            return;
        }


        FinanceYearData billdata[] = gson.fromJson(results, FinanceYearData[].class);
        for (FinanceYearData data : billdata) {

            String[] separated = data.getFinyr().split("-");
            year1 = separated[0];
            year2 = separated[1];
            selectyear_list.add(year2);
            adapter.notifyDataSetChanged();


            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }
    }


    private void createJsonRequestSalaryDetailData(String empId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String URL = AppConstraint.SALARYDETAIL;


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                URL, jsonParamSalaryDetailData(empId),
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        handleResponseSalaryDeatailData(response.toString());
                        Log.v("responce", response.toString());
                        pDialog.hide();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                createJsonRequestSalaryDetailData(empid);
                Log.v("responceerr_salary", error.toString());
                pDialog.hide();
            }

        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonParamSalaryDetailData(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // jsonArray.put("");

        try {
            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");


            jsonArray.put(empId);
            jsonArray.put(selectMonth);
            jsonArray.put(year2);
            jsonObject.put("spName", "GetEmployeeSalaryDetails");

            jsonObject.put("ParameterList", jsonArray);

            Log.v("jsonparam", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void handleResponseSalaryDeatailData(String results) {
        Log.v("jsonresponse", results.toString());
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        if (results.equals("[]")) {

            tv_salaryinfo_norecord.setVisibility(View.VISIBLE);
            linear_salarydetail.setVisibility(View.GONE);

            return;
        }

        linear_salarydetail.setVisibility(View.VISIBLE);
        tv_salaryinfo_norecord.setVisibility(View.GONE);
        SalaryDetailData salaryDetailDatasdata[] = gson.fromJson(results, SalaryDetailData[].class);
        for (SalaryDetailData data : salaryDetailDatasdata) {

            tv_salaryinfo_salaryfor.setText(data.getSalRegister());
            tv_salaryinfo_workingdays.setText(data.getWDays());
            tv_salaryinfo_fworkingdays.setText(data.getFWDays());
            tv_salaryinfo_basicda.setText(data.getP_BAsic());
            tv_salaryinfo_hra.setText(data.getP_HRA());
            tv_salaryinfo_tptallow.setText(data.getP_TPTAll());
            tv_salaryinfo_splallow.setText(data.getP_SplAll());
            tv_salaryinfo_pli.setText(data.getP_PLI());
            tv_salaryinfo_conveyallow.setText(data.getP_ConvAllow());
            tv_salaryinfo_commallow.setText(data.getP_CommAllow());
            tv_salaryinfo_fieldallow.setText(data.getP_FieldAllow());
            tv_salaryinfo_grossalary.setText(data.getTot_Payable());
            tv_salaryinfo_emplrepf.setText(data.getEmplrEPF());
            tv_salaryinfo_empepf.setText(data.getEmpEPF());
            tv_salaryinfo_emplresic.setText(data.getEmplrESIC());
            tv_salaryinfo_empesic.setText(data.getEmpESIC());
            tv_salaryinfo_communication.setText(data.getComm());
            tv_salaryinfo_imprest.setText(data.getImperest());
            tv_salaryinfo_loanadv.setText(data.getLoan());
            tv_salaryinfo_tds.setText(data.getTDS());
            tv_salaryinfo_deduction.setText(data.getDeduction());
            tv_salaryinfo_netsalary.setText(data.getNetSalary());


            //Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show(); ;
        }

    }

    private void downloadSalarySlip(View view) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait Loading...");
        pd.show();
        pd.setCancelable(false);
        mDownloadManger = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);


        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                if (!pd.isShowing()) {
                    pd.show();
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("on finish");
                if (pd.isShowing()) {
                    pd.dismiss();
                }

            }
        });
    }


    BroadcastReceiver mDownloadCompleteListener = new BroadcastReceiver() {
        public void onReceive(Context ctx, Intent intent) {

            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            Uri fileUri = mDownloadManger.getUriForDownloadedFile(id);
            Toast.makeText(ctx, fileUri.getLastPathSegment() + " downloaded", Toast.LENGTH_SHORT).show();
        }
    };

    protected void startDownload(String url, String userAgent, String mimetype) {


        Uri fileUri = Uri.parse(url);
        String fileName = fileUri.getLastPathSegment();
        String cookies = CookieManager.getInstance().getCookie(url);

        DownloadManager.Request request = new DownloadManager.Request(fileUri);
        request.setMimeType(mimetype)
                .addRequestHeader("cookie", cookies)
                .addRequestHeader("User-Agent", userAgent)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        getContext().registerReceiver(mDownloadCompleteListener, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mOnGoingDownload = mDownloadManger.enqueue(request);
        Toast.makeText(getContext(), "68|MainActivity::startDownload: Download of " + fileName + " started", Toast.LENGTH_LONG).show();
    }


























    private  void generatePdf() {



        try {
            String name ="Telecom Network Solutions";
            String phone = "9455693525";

            File docDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!docDir.exists()) {
                if (!docDir.mkdirs()) {
                    return;
                }
            }
            String fileName = "tns" + System.currentTimeMillis() + ".pdf";
            String pdfFile = docDir + "/" + fileName;
            OutputStream file = new FileOutputStream(pdfFile);

            Document document = new Document(new Rectangle(PageSize.A4), 20, 20, 20, 20);
            PdfWriter.getInstance(document, file);

            document.open();
            setBackground(document);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c.getTime());

            String header = "";
            String message = "";


                message = "You have successfully sold " + "100000" + " bit on " + formattedDate + ".";
                header = "Transfer Bitcoin";


            Paragraph paragraph = new Paragraph(" ");

          //  InputStream ims = getActivity().getAssets().open("tns_icon.jpg");

            InputStream ims = getResources().openRawResource(+ R.drawable.tns_icon);

            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
            document.add(paragraph);

            Font tableColStyle = new Font();
            tableColStyle.setSize(20);
            tableColStyle.setColor(new BaseColor(31,174,255));

            BaseFont bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            float charWidth = bf.getWidth(" ");
            int charactersPerLine = 101;
            float pageWidth = document.right() - document.left();
            float fontSize = (1000 * (pageWidth / (charWidth * charactersPerLine)));
            fontSize = ((int)(fontSize * 100)) / 100f;
            Font font = new Font(bf, fontSize+10);


            addParagraphWithTwoParts1(document, font,  "TELECOM NETWORK SOLUTIONS PVT. LTD.H 34, Sector - 63, , Noida,UP","For the month of Jul17");
      /*      document.add(new Paragraph("Test 1",tableColStyle));
            addParagraphWithTwoParts2(document, font, "0123456789012345", string2);*/
            document.add(new Paragraph("Test 3",tableColStyle));
            addParagraphWithTwoParts1(document, font, "012", "0123456789");
            document.add(new Paragraph("Test 4"));
            addParagraphWithTwoParts2(document, font, "012", "0123456789");
            document.add(new Paragraph("Test 4"));
            addParagraphWithTwoParts1(document, font, "012345", "01234567890123456789");
            document.add(new Paragraph("Test 5",tableColStyle));
            addParagraphWithTwoParts2(document, font, "012345", "01234567890123456789");
            document.add(new Paragraph("Test 5"));
            addParagraphWithTwoParts1(document, font, "0", "012345678901234567890123");
            document.add(new Paragraph("Test 6"));
            addParagraphWithTwoParts2(document, font, "0", "01234567890123456789012345678901234567890123456789");

           /* Font headerStyle = new Font();
            headerStyle.setStyle("bold");
            headerStyle.setSize(25);
            Paragraph paragraph1 = new Paragraph(header, headerStyle);
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph(paragraph1));
            document.add(paragraph);
            document.add(paragraph);

            Font tableColStyle = new Font();
            tableColStyle.setSize(20);
            tableColStyle.setColor(new BaseColor(31,174,255));

            String toText = "To\n\n";
            if (TextUtils.isEmpty(name)) {
                toText = toText + "" + phone;
            } else {
                toText = toText + "" + name;
            }

            String fromText = "From\n\n";
            fromText += "Bitpay IT Services PVT Ltd\nwww.bitpay.co.in";

            Font tableHeaderStyle = new Font();
            tableHeaderStyle.setStyle("bold");
            tableHeaderStyle.setSize(20);

            PdfPTable table = new PdfPTable(2);
            table.setPaddingTop(30);

            PdfPCell cell1 = new PdfPCell(new Phrase(toText, tableColStyle));
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setPadding(10);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase(fromText, tableColStyle));
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell2.setPadding(10);
            table.addCell(cell2);

            document.add(table);

            Font textStyle = new Font();
            textStyle.setSize(22);

            document.add(paragraph);
            document.add(paragraph);
            Paragraph paragraph2 = new Paragraph(message, textStyle);
            paragraph2.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph2);
            document.add(paragraph);
            textStyle.setSize(24);
            String tnxId = "Your Transaction Id is #" + "tuuuuiihih" + ".";
            Paragraph paragraph3 = new Paragraph(tnxId, textStyle);
            paragraph3.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph3);


            document.add(paragraph);
            document.add(paragraph);
            document.add(paragraph);
            textStyle.setSize(30);
            Paragraph paragraph4 = new Paragraph("Thank You for using Bitpay", textStyle);
            paragraph4.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph4);
*/
            document.close();
            file.close();

           // toast("Your transaction has been saved in your document folder", true);

            File generatedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + fileName);
            if (generatedFile.exists()) {
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(generatedFile), "application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e) {
                   // toast("Please install pdf reader to open it", true);
                }
            }
        } catch
                (Exception e) {
            e.printStackTrace();
        }
    }



    private void setBackground(Document document) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.bg_app);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image img;
        try {
            img = Image.getInstance(stream.toByteArray());
            img.setAbsolutePosition(0, 0);

            document.add(img);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void addParagraphWithTwoParts1(Document document, Font font, String string1, String string2)
            throws DocumentException {
    Chunk GLUE = new Chunk(new VerticalPositionMark());
        String string3 ="";
        String cut1 ="";
        String cut2 ="";
        String cut3 ="";
        String cut4 ="";
        String cut5 ="";

        Font tableColStyle = new Font();
        tableColStyle.setSize(20);
        tableColStyle.setColor(new BaseColor(255,0,0));
        if (string1 == null) string1 = "";
        if (string1.length() > 10)
            cut1 = string1.substring(0, 25);
        if (string1.length() > 25)
            cut2 = string1.substring(25, 50);
        if (string1.length() > 49)
            cut3 = string1.substring(50, string1.length());

        string1 = cut1+"\n"+cut2+"\n"+cut3;
        Chunk chunk1 = new Chunk(string1, font);
        if (string2 == null) string2 = "";
        if (string1.length() + string2.length() > 100)
            string2 = string2.substring(0, 100 - string1.length());


        Chunk chunk2 = new Chunk(string2, tableColStyle);

        Paragraph p = new Paragraph();
        p.add(chunk1);
        p.add(GLUE);
        p.add(chunk2);
    /*    LineSeparator line = new LineSeparator();
        line.setOffset(-2);
        p.add(line);*/
        document.add(p);
    }

    public void addParagraphWithTwoParts2(Document document, Font font, String string1, String string2)
            throws DocumentException {
        if (string1 == null) string1 = "";
        if (string1.length() > 10)
            string1 = string1.substring(0, 10);
        if (string2 == null) string2 = "";
        if (string1.length() + string2.length() > 100)
            string2 = string2.substring(0, 100 - string1.length());
        Paragraph p = new Paragraph(string1 + " " + string2, font);
        LineSeparator line = new LineSeparator();
        line.setOffset(-2);
        p.add(line);
        document.add(p);
    }

}



