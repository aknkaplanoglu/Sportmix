package tech.ozak.sportmix;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import tech.ozak.sportmix.adapter.NavDrawerListAdapter;
import tech.ozak.sportmix.customtab.CustomTabActivityHelper;
import tech.ozak.sportmix.dto.NavDrawerItem;
import tech.ozak.sportmix.dto.RssItem;
import tech.ozak.sportmix.fragment.EuroSportFragment;
import tech.ozak.sportmix.fragment.FotomacFragment;
import tech.ozak.sportmix.fragment.GoalComFragment;
import tech.ozak.sportmix.fragment.HaberTurkFragment;
import tech.ozak.sportmix.fragment.LigTvFragment;
import tech.ozak.sportmix.fragment.SabahFragment;
import tech.ozak.sportmix.fragment.SporxFragment;
import tech.ozak.sportmix.fragment.TrtsporFragment;
import tech.ozak.sportmix.handler.RssReader;

/**
 * Created by ako on 10/9/2015.
 */
public class ListActivity extends ActionBarActivity {

    public static ListActivity mInstance;
    List<RssItem> rssItems = new ArrayList<RssItem>();
    private RssItem[] listData;

    public List<RssItem> getRssItems() {
        return rssItems;
    }

    public void setRssItems(List<RssItem> rssItems) {
        this.rssItems = rssItems;
    }

    // Drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;


// Drawer end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = this;

        setContentView(R.layout.activity_news_feed);
        // setting home page when activity first load.
        //setHomePage();
        setUpDrawerProcess(savedInstanceState);


        initActionBar();

    }

    public static ListActivity getInstance() {
        return mInstance;
    }


    @SuppressWarnings("ResourceType")
    private void setUpDrawerProcess(Bundle savedInstanceState) {
        //Drawer
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        // puan durumu
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        // fikstür
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        // canliskor
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
        // sporx
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));

        // sporx
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.mipmap.ic_launcher, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //      getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + mTitle));
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //    getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + mTitle));
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };

        setDrawerListWidth();

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    private void setDrawerListWidth() {
        int width = getResources().getDisplayMetrics().widthPixels * 3 / 5;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
        params.width = width;
        mDrawerList.setLayoutParams(params);
    }

    AlertDialog alertDialog;

    private class HaberTurkAsynTask extends AsyncTask<String, Void, Boolean> {

        private Activity activity;

        private Thread thread;

        /**
         * progress dialog to show user that the backup is processing.
         */

        public HaberTurkAsynTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /**
         * application context.
         */
        private Context context;

        protected void onPreExecute() {

            alertDialog.show();
           /* if (rssItems!=null && !rssItems.isEmpty()){
                rssItems.clear();
            }*/
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Log.d("Rss Size FEEDACTIVITY: ", String.valueOf(rssItems.size()));
                Fragment fragment = new HaberTurkFragment();

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();

                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }

            }
            alertDialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                // Create RSS reader
                rssItems = RssReader.getLatestRssFeed(params[0]);
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
            return true;
        }

    }


    private class SporxAsyncTask extends AsyncTask<String, Void, Boolean> {

        private Activity activity;

        private Thread thread;

        /**
         * progress dialog to show user that the backup is processing.
         */

        public SporxAsyncTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /**
         * application context.
         */
        private Context context;

        protected void onPreExecute() {

            alertDialog.show();
           /* if (rssItems!=null && !rssItems.isEmpty()){
                rssItems.clear();
            }*/
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Fragment fragment = new SporxFragment();

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();

                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }

            }
            alertDialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                // Create RSS reader
                rssItems = RssReader.getLatestRssFeed(params[0]);
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
            return true;
        }

    }


    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;

        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        switch (position) {
            case 0:
                fragment = new GoalComFragment();
                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "GOAL.COM"));
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();

                    // update selected item and title, then close the drawer
                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "GOAL.COM"));
                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }
                break;
            case 1:
                alertDialog = new SpotsDialog(this, R.style.Custom_Progress_Dialog);
                setCustomAlertDialog();
                new SporxAsyncTask(this).execute(getResources().getString(R.string.sporx_feed));
                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "SPORX"));
                break;

            case 2:
                alertDialog = new SpotsDialog(this, R.style.Custom_Progress_Dialog);
                setCustomAlertDialog();
                new EuroSportAsyncTask(this).execute(getResources().getString(R.string.eurosport_feed_url));
                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "EUROSPORT"));
                break;

            case 3:
                alertDialog = new SpotsDialog(this, R.style.Custom_Progress_Dialog);
                setCustomAlertDialog();
                new TrtsporAsyncTask(this).execute(getResources().getString(R.string.trtspor_feed));

                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "TRT SPOR"));
                break;

            case 4:
                alertDialog = new SpotsDialog(this, R.style.Custom_Progress_Dialog);
                setCustomAlertDialog();
                new SabahAsyncTask(this).execute(getResources().getString(R.string.sabah_feed));

                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "SABAH"));
                break;

            case 5:
                alertDialog = new SpotsDialog(this, R.style.Custom_Progress_Dialog);
                setCustomAlertDialog();
                new LigTvAsyncTask(this).execute(getResources().getString(R.string.ligtv_feed));

                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "LİG TV"));
                break;

            case 6:
                alertDialog = new SpotsDialog(this, R.style.Custom_Progress_Dialog);
                setCustomAlertDialog();
                new HaberTurkAsynTask(this).execute(getResources().getString(R.string.haberturk_feed));

                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "HABERTÜRK"));
                break;
            // puan durumu
            case 7:
                alertDialog = new SpotsDialog(this, R.style.Custom_Progress_Dialog);
                setCustomAlertDialog();
                new FotomacAsyncTask(this).execute(getResources().getString(R.string.fotomac_feed));
                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "FOTOMAÇ"));
                break;

            // fikstur
            case 8:
                alertDialog = new SpotsDialog(this, R.style.Custom_Progress_Dialog);
                setCustomAlertDialog();
                new PuanDurumuAsyncTask(this).execute("");
                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "PUAN DURUMU"));
                break;

            case 9:
                alertDialog = new SpotsDialog(this, R.style.Custom_Progress_Dialog);
                setCustomAlertDialog();
                new FiksturAsyncTask(this).execute("");

                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "FİKSTÜR"));
                break;

            case 10:
                alertDialog = new SpotsDialog(this, R.style.Custom_Progress_Dialog);
                setCustomAlertDialog();
                new CanliSkorAsyncTask(this).execute("");

                getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + "CANLI SKOR"));
                break;


            default:
                break;
        }

       /* if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }*/
    }

    private void setCustomAlertDialog() {
        Window window = this.alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        this.alertDialog.setCancelable(true);
        alertDialog.setInverseBackgroundForced(false);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void initActionBar() {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.real_action_bar);
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#786a6a'>" + mTitle));
        // getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class LigTvAsyncTask extends AsyncTask<String, Void, Boolean> {

        private Activity activity;

        private Thread thread;

        /**
         * progress dialog to show user that the backup is processing.
         */

        public LigTvAsyncTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /**
         * application context.
         */
        private Context context;

        protected void onPreExecute() {

            alertDialog.show();
           /* if (rssItems!=null && !rssItems.isEmpty()){
                rssItems.clear();
            }*/
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Log.d("Rss Size FEEDACTIVITY: ", String.valueOf(rssItems.size()));
                Fragment fragment = new LigTvFragment();

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();

                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }

            }
            alertDialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                // Create RSS reader
                rssItems = RssReader.getLatestRssFeed(params[0]);
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
            return true;
        }

    }


    private class FotomacAsyncTask extends AsyncTask<String, Void, Boolean> {

        private Activity activity;
        /**
         * progress dialog to show user that the backup is processing.
         */

        public FotomacAsyncTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /**
         * application context.
         */
        private Context context;

        protected void onPreExecute() {

            alertDialog.show();
           /* if (rssItems!=null && !rssItems.isEmpty()){
                rssItems.clear();
            }*/
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Log.d("Rss Size FEEDACTIVITY: ", String.valueOf(rssItems.size()));
                Fragment fragment = new FotomacFragment();

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();

                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }

            }
            alertDialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                // Create RSS reader
                rssItems = RssReader.getLatestRssFeed(params[0]);
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
            return true;
        }

    }


    private class PuanDurumuAsyncTask extends AsyncTask<String, Void, Boolean> {

        private Activity activity;

        /**
         * progress dialog to show user that the backup is processing.
         */

        public PuanDurumuAsyncTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /**
         * application context.
         */
        private Context context;

        protected void onPreExecute() {

        }


        @Override
        protected void onPostExecute(final Boolean success) {


           showCustomTabs(getResources().getString(R.string.puan_durumu_url));

        }

        @Override
        protected Boolean doInBackground(String... params) {

            return true;
        }

    }

    private void showCustomTabs(String feedLink) {
        Uri uri = Uri.parse(feedLink);

        // create an intent builder
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // Begin customizing
        // set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.bluePrimaryDark));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this,R.color.bluePrimaryDark));

        // set start and exit animations
        intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        // add share action to menu list
        intentBuilder.addDefaultShareMenuItem();

        setShareActionIconInCustomTab(intentBuilder,feedLink);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        CustomTabActivityHelper.openCustomTab(this, customTabsIntent, uri,
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        activity.startActivity(intent);
                    }
                });
    }

    private void setShareActionIconInCustomTab(CustomTabsIntent.Builder intentBuilder,String feedLink) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, feedLink);

        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Map the bitmap, text, and pending intent to this icon
        // Set tint to be true so it matches the toolbar color
        intentBuilder.setActionButton(bitmap, "Share Link", pendingIntent, true);
    }

    private class CanliSkorAsyncTask extends AsyncTask<String, Void, Boolean> {

        private Activity activity;
        /**
         * progress dialog to show user that the backup is processing.
         */

        public CanliSkorAsyncTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /**
         * application context.
         */
        private Context context;

        protected void onPreExecute() {

        }


        @Override
        protected void onPostExecute(final Boolean success) {


          showCustomTabs(getResources().getString(R.string.canli_skor_url));
        }

        @Override
        protected Boolean doInBackground(String... params) {

            return true;
        }

    }


    private class FiksturAsyncTask extends AsyncTask<String, Void, Boolean> {

        private Activity activity;

        private Thread thread;

        /**
         * progress dialog to show user that the backup is processing.
         */

        public FiksturAsyncTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /**
         * application context.
         */
        private Context context;

        protected void onPreExecute() {

        }


        @Override
        protected void onPostExecute(final Boolean success) {


         showCustomTabs(getResources().getString(R.string.fikstur_url));

        }

        @Override
        protected Boolean doInBackground(String... params) {

            return true;
        }

    }


    private class SabahAsyncTask extends AsyncTask<String, Void, Boolean> {

        private Activity activity;

        private Thread thread;

        /**
         * progress dialog to show user that the backup is processing.
         */

        public SabahAsyncTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /**
         * application context.
         */
        private Context context;

        protected void onPreExecute() {

            alertDialog.show();
           /* if (rssItems!=null && !rssItems.isEmpty()){
                rssItems.clear();
            }*/
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Log.d("Rss Size FEEDACTIVITY: ", String.valueOf(rssItems.size()));
                Fragment fragment = new SabahFragment();

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();

                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }

            }
            alertDialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                // Create RSS reader
                rssItems = RssReader.getLatestRssFeed(params[0]);
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
            return true;
        }

    }



    private class EuroSportAsyncTask extends AsyncTask<String, Void, Boolean> {

        private Activity activity;

        private Thread thread;

        /**
         * progress dialog to show user that the backup is processing.
         */

        public EuroSportAsyncTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /**
         * application context.
         */
        private Context context;

        protected void onPreExecute() {

            alertDialog.show();
           /* if (rssItems!=null && !rssItems.isEmpty()){
                rssItems.clear();
            }*/
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Fragment fragment = new EuroSportFragment();

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();

                    // update selected item and title, then close the drawer

                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }

            }
            alertDialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                // Create RSS reader
                rssItems = RssReader.getLatestRssFeed(params[0]);
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
            return true;
        }

    }



    private class TrtsporAsyncTask extends AsyncTask<String, Void, Boolean> {

        private Activity activity;

        private Thread thread;

        /**
         * progress dialog to show user that the backup is processing.
         */

        public TrtsporAsyncTask(Activity activity) {
            this.activity = activity;
            context = activity;
        }

        /**
         * application context.
         */
        private Context context;

        protected void onPreExecute() {

            alertDialog.show();
           /* if (rssItems!=null && !rssItems.isEmpty()){
                rssItems.clear();
            }*/
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Log.d("Rss Size FEEDACTIVITY: ", String.valueOf(rssItems.size()));
                Fragment fragment = new TrtsporFragment();

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();

                    // update selected item and title, then close the drawer

                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }

            }
            alertDialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                // Create RSS reader
                rssItems = RssReader.getLatestRssFeed(params[0]);
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
            return true;
        }

    }

    @Override
    @SuppressWarnings("ResourceType")
    public void onBackPressed() {
        if (!mDrawerLayout.isDrawerOpen(mDrawerList)) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(getString(R.string.exit_talk))
                    .setMessage(getString(R.string.are_you_sure))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
        } else {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}
