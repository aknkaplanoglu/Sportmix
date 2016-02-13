package tech.ozak.sportmix.webView;

import android.app.ProgressDialog;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ako on 22-Dec-15.
 */
public class CustomWebViewClient extends WebViewClient {

    ProgressDialog progressDialog;
    Context mcontext;

    public CustomWebViewClient(ProgressDialog pd,Context c) {
        progressDialog=pd;
        mcontext=c;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        try{
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }

    }
}
