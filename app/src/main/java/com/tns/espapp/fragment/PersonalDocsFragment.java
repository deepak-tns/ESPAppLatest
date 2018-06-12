package com.tns.espapp.fragment;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by GARIMA on 6/23/2017.
 */

public class PersonalDocsFragment extends Fragment {
    private View view;
    private WebView webView;
    private ProgressDialog pd;
    private SharedPreferenceUtils sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.personal_docs_fragment, container, false);
        getLayoutsId();

        return view;
    }

    private void getLayoutsId() {
        webView = (WebView) view.findViewById(R.id.webview);
        pd = new ProgressDialog(getContext());
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
                DownloadManager dm = (DownloadManager)getContext().getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);


            }
        });
        sharedPreferences = SharedPreferenceUtils.getInstance();
        sharedPreferences.setContext(getContext());
        String empId = sharedPreferences.getString(AppConstraint.EMPID);
        webView.loadUrl("http://182.71.51.35/esp/info/PersonalDocsWebView/"+empId);
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
