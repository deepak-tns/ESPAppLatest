package com.tns.espapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;

/**
 * Created by GARIMA on 6/23/2017.
 */

public class AccountStatementFragment extends Fragment {
    private View view;
    private WebView webView;
    private ProgressDialog pd;
    private SharedPreferenceUtils sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pernsonal_info, container, false);
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
        sharedPreferences = SharedPreferenceUtils.getInstance();
        sharedPreferences.setContext(getContext());
        String empId = sharedPreferences.getString(AppConstraint.EMPID);
        webView.loadUrl("http://182.71.51.35/ESP/Info/AccountStatementWebView/"+empId);
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
