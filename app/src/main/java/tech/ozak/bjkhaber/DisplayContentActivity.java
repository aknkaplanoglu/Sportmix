package tech.ozak.bjkhaber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import dmax.dialog.SpotsDialog;

/**
 * Created by ako on 21-Dec-15.
 */
public class DisplayContentActivity extends Activity {

    private WebView webView;
    AlertDialog alertDialog;
    private final int WAITING_TIME = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_news);
        alertDialog=new SpotsDialog(DisplayContentActivity.this,R.style.Custom_Progress_Dialog);
        alertDialog.show();
        setCustomAlertDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                webView = (WebView) findViewById(R.id.webViewFeed);

                webView.clearHistory();
                webView.clearCache(true);
                webView.setClickable(false);
                webView.setFocusable(false);
                webView.setFocusableInTouchMode(false);

                webView.setWebViewClient(new myWebClient());
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setLightTouchEnabled(true);
                webSettings.setBuiltInZoomControls(false);
                webSettings.setSupportZoom(false);
                Intent i = getIntent();
                String feed_link = i.getStringExtra("feed_link");
                webView.loadUrl(feed_link);

            }
        }, WAITING_TIME);



    }

    private void setCustomAlertDialog() {
        Window window = this.alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        this.alertDialog.setCancelable(true);
        alertDialog.setInverseBackgroundForced(false);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
           // progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            webView.setVisibility(View.VISIBLE);
            alertDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


}
