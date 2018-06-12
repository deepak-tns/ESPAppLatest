package com.tns.espapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.fragment.AccountStatementFragment;
import com.tns.espapp.fragment_new.AccountStatementFragment_new;
import com.tns.espapp.fragment_new.LeaveLadgerFragment_new;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AccountStatementsActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressDialog pd;
    private SharedPreferenceUtils sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_statements);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.li_account_statement,new AccountStatementFragment_new()).commit();
        }
      //  getLayoutsId();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
    private void getLayoutsId() {
        webView = (WebView)findViewById(R.id.webview);
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait Loading...");
        pd.show();
        pd.setCancelable(false);
        webView.setWebViewClient(new MyBrowser());


        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        sharedPreferences = SharedPreferenceUtils.getInstance();
        sharedPreferences.setContext(this);
        String empId = sharedPreferences.getString(AppConstraint.EMPID);
        webView.loadUrl("http://tnssofts.com/ESP/Info/AccountStatementWebView/"+empId);
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
