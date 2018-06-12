package com.tns.espapp.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.fragment.TDSDeductionFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TDSActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressDialog pd;
    private SharedPreferenceUtils sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tds);
        getLayoutsId();

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    private void getLayoutsId() {
        webView = (WebView) findViewById(R.id.webview);
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait Loading...");
        pd.show();
        pd.setCancelable(false);
        webView.setWebViewClient(new MyBrowser());


        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);


            }
        });
        sharedPreferences = SharedPreferenceUtils.getInstance();
        sharedPreferences.setContext(this);
        String empId = sharedPreferences.getString(AppConstraint.EMPID);
        webView.loadUrl("http://tnserp.com/ESP/TDS/tdsDeclarationWebView/" + empId);
    }

    private class MyBrowser extends WebViewClient {
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
    }
}