package tech.ozak.sportmix;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.hanks.htextview.HTextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tech.ozak.sportmix.dto.RssItem;
import tech.ozak.sportmix.handler.RssReader;

/**
 * Created by ako on 09-Feb-16.
 */
public class SplashActivity extends Activity {

    public static SplashActivity mInstance;
    static final int DIALOG_ERROR_CONNECTION = 1;
    List<RssItem> rssItems;

    private static String splash_text="Spormix";

 //   private ProgressWheel pwOne;
   // private CircleProgress mProgressView;


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
            setContentView(R.layout.splash_screen);
            bindLogo();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    // go to the main activity
                    new ProgressTask(SplashActivity.this).execute(getString(R.string.ntvspor_feed));

                }
            };
            // Show splash screen for 3 seconds
            new Timer().schedule(task, getResources().getInteger(R.integer.splash_display_length));


        }

    }


    private void bindLogo(){
        // Start animating the image
        final HTextView text2 = (HTextView)findViewById(R.id.htextView1);
        // //
        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                text2.animateText(splash_text);
                if (!splash_text.equals(getString(R.string.splash_one))){
                    splash_text=getString(R.string.splash_one);
                }
                else{
                    splash_text=getString(R.string.splash_two);
                }

                h.postDelayed(this, 1800);
            }
        });
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

    public static SplashActivity getInstance() {
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
            Intent i = new Intent(SplashActivity.this, ListActivity.class);
            startActivity(i);
            // kill current activity
            splash_text="Spormix";
            finish();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                // Create RSS reader
                rssItems= RssReader.getLatestRssFeed(getResources().getString(R.string.ntvspor_feed));
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
            return null;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }


}

