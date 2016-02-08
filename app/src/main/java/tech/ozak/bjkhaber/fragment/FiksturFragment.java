package tech.ozak.bjkhaber.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import tech.ozak.bjkhaber.R;

/**
 * Created by ako on 08-Feb-16.
 */
public class FiksturFragment  extends Fragment {

    AlertDialog alertDialog;
    private WebView webView;
    String fikstur_url = "";
    // AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_puandurumu_fragment, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView1);

        setWebViewSettings();
        alertDialog=new SpotsDialog(getActivity(),R.style.Custom_Progress_Dialog);
        setCustomAlertDialog();
       /* Intent i = getIntent();
        String feed_link = i.getStringExtra("feed_link");*/
        fikstur_url = getResources().getString(R.string.fikstur_url);
        Log.d("fikstur_url url: ", fikstur_url);
        new ProgressTask(getActivity()).execute(fikstur_url);

        return rootView;
    }

    private void setCustomAlertDialog() {
        Window window = this.alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        this.alertDialog.setCancelable(true);
        alertDialog.setInverseBackgroundForced(false);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        private ProgressBar progressBar;
        Context c;

        public ProgressTask(Context context) {

            c=context;
        }


        protected void onPreExecute() {
            alertDialog.show();
            Log.d("On pre execute: ", "yes");
        }


        @Override
        protected void onPostExecute(final String page) {

            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            TagNode tagNode = new HtmlCleaner(props).clean(page);
            SimpleHtmlSerializer htmlSerializer =
                    new SimpleHtmlSerializer(props);
            webView.loadDataWithBaseURL(fikstur_url, htmlSerializer.
                    getAsString(tagNode), "text/html", "charset=UTF-8", null);
            alertDialog.dismiss();

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
                Elements mainLeagueTablePage = doc.select(".fiksAlani");

                if ( null != mainLeagueTablePage){
                    Elements hafta = mainLeagueTablePage.select(".hafta");
                    hafta.attr("style", "color:#D0D0D0 ;font-weight: bold;background-color:#505050");

                    Elements fiksTarihDiv = mainLeagueTablePage.select(".fiksTarihDiv");
                    fiksTarihDiv.attr("style", "color:#D0D0D0 ;font-weight: bold;background-color:#505050");

                    Elements fiksTakimlarDiv = mainLeagueTablePage.select(".fiksTakimlarDiv");
                    fiksTakimlarDiv.attr("style", "color:#0099CC ;background-color:#D8D8D8");

                    Elements fiksTakimlar = mainLeagueTablePage.select(".fiksTakimlar");
                    fiksTakimlar.attr("style", "color:#0099CC ;background-color:#D8D8D8");

                    Elements tk1 = mainLeagueTablePage.select(".tk1");
                    tk1.attr("style", "display:inline-block");

                    Elements tk2 = mainLeagueTablePage.select(".tk2");
                    tk2.attr("style", "display:inline-block");

                    Elements tire = mainLeagueTablePage.select(".tire");
                    tire.attr("style", "display:inline-block");


                    newPage = mainLeagueTablePage.html();

                }


            } catch (IOException e) {
                Log.d("Jsoup :", "Error in jsoup");
                e.printStackTrace();
            }


            return newPage;
        }

    }

}

