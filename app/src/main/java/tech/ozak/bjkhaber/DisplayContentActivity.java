package tech.ozak.bjkhaber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by ako on 21-Dec-15.
 */
public class DisplayContentActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_news);
        Intent i = getIntent();
        String feed_link = i.getStringExtra("feed_link");
        webView = (WebView) findViewById(R.id.webViewFeed);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(feed_link);

    }
}
