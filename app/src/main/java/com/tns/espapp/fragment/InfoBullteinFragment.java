package com.tns.espapp.fragment;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by GARIMA on 6/23/2017.
 */

public class InfoBullteinFragment extends Fragment {
    private View view;
    private WebView mWebView;
    private Long mOnGoingDownload;
    private Button mCancel;
    private DownloadManager mDownloadManger;
    private ProgressDialog pd;
    private SharedPreferenceUtils sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.infobulltein_fragment, container, false);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait Loading...");
        pd.show();
        pd.setCancelable(false);
        mDownloadManger = (DownloadManager)getContext().getSystemService(DOWNLOAD_SERVICE);

        mCancel = (Button)view.findViewById(R.id.cancelDownload);
        mCancel.setVisibility(View.GONE);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDownload();
            }
        });

        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient(){
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
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                startDownload(url, userAgent, mimetype);
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://182.71.51.35/ESP/info/BulletinInfoWebView");

        return view;
    }

    protected void cancelDownload() {
        if (mOnGoingDownload != null) {
            mDownloadManger.remove(mOnGoingDownload);
            clearDownloadingState();
            Toast.makeText(getContext(), "Download canceled", Toast.LENGTH_SHORT).show();
        }
    }
    protected void clearDownloadingState() {
        getContext().unregisterReceiver(mDownloadCompleteListener);
        mCancel.setVisibility(View.GONE);
        mOnGoingDownload = null;
    }

    BroadcastReceiver mDownloadCompleteListener = new BroadcastReceiver() {
        public void onReceive(Context ctx, Intent intent) {
            clearDownloadingState();
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
        mCancel.setVisibility(View.VISIBLE);
        mOnGoingDownload = mDownloadManger.enqueue(request);
        Toast.makeText(getContext(), "68|MainActivity::startDownload: Download of " + fileName + " started",Toast.LENGTH_LONG).show();
    }




}
