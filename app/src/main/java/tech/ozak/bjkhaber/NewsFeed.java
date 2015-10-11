package tech.ozak.bjkhaber;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tech.ozak.bjkhaber.dto.RssItem;
import tech.ozak.bjkhaber.handler.RssReader;
import tech.ozak.bjkhaber.listener.ListListener;


public class NewsFeed extends Activity {

    public static NewsFeed mInstance;
    static final int DIALOG_ERROR_CONNECTION = 1;
    List<String> headlines;
    List<String> links;
    List<RssItem> rssItems;

    public List<RssItem> getRssItems() {
        return rssItems;
    }

    static Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = this;

        if (!isOnline(this)) {
            showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
        }
        else{
            setContentView(R.layout.activity_splash);
            headlines=new ArrayList<String>();
            links=new ArrayList<String>();
            // Initializing instance variables
            new ProgressTask(NewsFeed.this).execute();

        }

    }

    public boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected())
            return true;
        else
            return false;
    }

    public static NewsFeed getInstance() {
        return mInstance;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DIALOG_ERROR_CONNECTION:
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
                errorDialog.setTitle("Error");
                errorDialog.setMessage("No internet connection.");
                errorDialog.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                startActivityForExit();
                            }
                        });

                AlertDialog errorAlert = errorDialog.create();
                return errorAlert;

            default:
                break;
        }
        return dialog;
    }

    private void startActivityForExit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }



    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private Activity activity;

        public ProgressTask(Activity activity) {
            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }



        /** progress dialog to show user that the backup is processing. */

        /** application context. */
        private Context context;

        protected void onPreExecute() {
            this.dialog.setMessage("Progress start");
            this.dialog.show();
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.getWindow().setGravity(Gravity.BOTTOM);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            // Binding data

            // After completing http call
            // will close this activity and lauch main activity
            Intent i = new Intent(NewsFeed.this, FeedActivityMain.class);
        //    i.putStringArrayListExtra("headlines", (ArrayList<String>)headlines);
         //   i.putStringArrayListExtra("links",(ArrayList<String>)links);
            startActivity(i);

            // close this activity

          /*  ArrayAdapter adapter = new ArrayAdapter(activity,
                    android.R.layout.simple_list_item_1, headlines);
            activity.setListAdapter(adapter);*/


            if (dialog.isShowing()) {
                dialog.dismiss();
                finish();
            }

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                // Create RSS reader
                RssReader rssReader = new RssReader("http://www.ntvspor.net/Rss/anasayfa");
                rssItems=rssReader.getItems();
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
            return null;
        }


        /* protected Boolean doInBackground(final String... args) {
            try {
                URL url = new URL("http://rss.hurriyet.com.tr/rss.aspx?sectionId=14");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                // We will get the XML from an input stream
                xpp.setInput(getInputStream(url), "UTF_8");

        *//* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
         * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
         * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
         * so we should skip the "<title>" tag which is a child of "<channel>" tag,
         * and take in consideration only "<title>" tag which is a child of "<item>"
         *
         * In order to achieve this, we will make use of a boolean variable.
         *//*
                boolean insideItem = false;

                // Returns the type of current event: START_TAG, END_TAG, etc..
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem)
                                headlines.add(xpp.nextText()); //extract the headline
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem)
                                links.add(xpp.nextText()); //extract the link of article
                        }
                    }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem=false;
                    }

                    eventType = xpp.next(); //move to next element
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
*/

    }

}
