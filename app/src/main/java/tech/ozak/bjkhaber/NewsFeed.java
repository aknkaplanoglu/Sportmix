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
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import tech.ozak.bjkhaber.dto.RssItem;
import tech.ozak.bjkhaber.handler.RssReader;
import tech.ozak.bjkhaber.progressBar.CircleProgress;


public class NewsFeed extends Activity {

    public static NewsFeed mInstance;
    static final int DIALOG_ERROR_CONNECTION = 1;
    List<String> headlines;
    List<String> links;
    List<RssItem> rssItems;
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    AlertDialog alertDialog;
    private CircleProgress mProgressView;


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
            mProgressView = (CircleProgress) findViewById(R.id.progress);
           /* alertDialog=new SpotsDialog(NewsFeed.this,R.style.Custom_Progress_Dialog);
            alertDialog.show();
            setCustomAlertDialog();*/
            mProgressView.startAnim();
            //mProgressView.setRadius();

             /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    new ProgressTask(NewsFeed.this).execute();
                }
            }, SPLASH_DISPLAY_LENGTH);


        }

    }

    private void setCustomAlertDialog() {
        Window window = this.alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        this.alertDialog.setCancelable(true);
        alertDialog.setInverseBackgroundForced(true);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        private Activity activity;

        private Thread thread;
        /** progress dialog to show user that the backup is processing. */

        public ProgressTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /** application context. */
        private Context context;

        protected void onPreExecute() {


        }


        @Override
        protected void onPostExecute(final Boolean success) {



             /* Create an Intent that will start the Menu-Activity. */
            Intent i = new Intent(NewsFeed.this, FeedActivityMain.class);
            startActivity(i);
            // Dont return back to the splash screen
            finish();
            mProgressView.stopAnim();
           // alertDialog.dismiss();


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
