package tech.ozak.bjkhaber;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tech.ozak.bjkhaber.dto.RssItem;
import tech.ozak.bjkhaber.handler.RssReader;


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
                rssItems=RssReader.getLatestRssFeed();
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
            return null;
        }

    }

}
