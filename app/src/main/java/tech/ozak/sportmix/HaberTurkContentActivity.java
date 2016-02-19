package tech.ozak.sportmix;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by ako on 31-Dec-15.
 */
public class HaberTurkContentActivity extends AppCompatActivity {

    private ImageView imageView;
    private WebView webView;
    private TextView textView;
    ProgressBar progressBar = null;
    ShareActionProvider provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_show_content);

        Intent i = getIntent();
        String feed_link = i.getStringExtra("feed_link");
        String img_link = i.getStringExtra("img_link");
        String header = i.getStringExtra("header");
        textView= (TextView) findViewById(R.id.header);
        textView.setText(header);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.real_action_bar);
        ab.setBackgroundDrawable(drawable);
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        imageView= (ImageView) findViewById(R.id.imagevw);
        webView= (WebView) findViewById(R.id.webViewFeed);
        webView.setBackgroundColor(Color.TRANSPARENT);
        //setting webview features.
        setWebViewSettings();



        int height = this.getResources().getDisplayMetrics().heightPixels*1/4;
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.height = height;
        imageView.setLayoutParams(layoutParams);
        int width = this.getResources().getDisplayMetrics().widthPixels;

        Glide.with(this)
                .load(img_link)
                .override(width, height)
                .fitCenter()
                .placeholder(R.mipmap.haberturk)
                .error(R.mipmap.haberturk)
                .into(imageView);
        new ProgressTask().execute(feed_link);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_share_menu, menu);

        // Get the ActionProvider for later usage
        MenuItem item = menu.findItem(R.id.menu_share);
        provider= (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        doShare();
        return true;
    }

    public void doShare() {
        // populate the share intent with data
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String feed_link = getIntent().getStringExtra("feed_link");
        intent.putExtra(Intent.EXTRA_TEXT, feed_link);
        provider.setShareIntent(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setWebViewSettings() {
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        WebSettings webSettings = webView.getSettings();

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH); //
        webSettings.setJavaScriptEnabled(true); // with this false it is more beautiful.
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }


    private class ProgressTask extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            Log.d("On pre execute: ", "yes");
        }


        @Override
        protected void onPostExecute(final String page) {

            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            TagNode tagNode = new HtmlCleaner(props).clean(page);
            SimpleHtmlSerializer htmlSerializer =
                    new SimpleHtmlSerializer(props);
            webView.loadDataWithBaseURL(null,htmlSerializer.
                    getAsString(tagNode), "text/html", "charset=UTF-8",null);
            progressBar.setVisibility(View.GONE);
            //   webView.loadData(htmlSerializer.getAsString(tagNode),"text/html","UTF-8");

        }

        @Override
        protected String doInBackground(String... params) {
            String newPage="";
            Document doc = null;
            try {
                String url = params[0];
                // url=url.replace("m.","www.");
                doc = Jsoup.connect(url).userAgent("Mozilla").get();

                System.out.println(Jsoup.connect(url).userAgent("Mozilla").get().baseUri());

                System.out.println(doc.html());

                //doc.getElementsByClass("group row-fluid mbottom20 news-wd").remove();

                Elements select = doc.select("div[itemprop=articleBody]");
                select.select(".group").remove();
                newPage = select.html();


            } catch (IOException e) {
                Log.d("Jsoup :", "Error in jsoup");
                e.printStackTrace();
            }


            return newPage;
        }

    }


}
