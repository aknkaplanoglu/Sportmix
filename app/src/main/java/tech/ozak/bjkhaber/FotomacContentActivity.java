package tech.ozak.bjkhaber;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by ako on 07-Feb-16.
 */
public class FotomacContentActivity extends Activity {

    private ImageView imageView;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_show_content);
        imageView= (ImageView) findViewById(R.id.imagevw);
        webView= (WebView) findViewById(R.id.webViewFeed);

        //setting webview features.
        setWebViewSettings();

        Intent i = getIntent();
        String feed_link = i.getStringExtra("feed_link");
        String img_link = i.getStringExtra("img_link");


        int height = this.getResources().getDisplayMetrics().heightPixels*1/4;
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.height = height;
        imageView.setLayoutParams(layoutParams);
        int width = this.getResources().getDisplayMetrics().widthPixels;

        Glide.with(this)
                .load(img_link)
                .override(width, height)
                .placeholder(R.mipmap.fotomac)
                .error(R.mipmap.fotomac)
                .into(imageView);
        new ProgressTask().execute(feed_link);




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
                Element divDetailNews = doc.getElementById("divDetailNews");
                divDetailNews.getElementsByClass("row").remove();
                newPage = divDetailNews.html();


            } catch (IOException e) {
                Log.d("Jsoup :", "Error in jsoup");
                e.printStackTrace();
            }


            return newPage;
        }

    }


}

