package tech.ozak.sportmix.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tech.ozak.sportmix.ListActivity;
import tech.ozak.sportmix.R;
import tech.ozak.sportmix.SplashActivity;
import tech.ozak.sportmix.adapter.PostItemAdapter;
import tech.ozak.sportmix.asynTask.DownloadAsyncTask;
import tech.ozak.sportmix.asynTask.DownloadAsyncTaskResponse;
import tech.ozak.sportmix.dto.RssItem;

/**
 * Created by ako on 10/31/2015.
 */
public class NtvSporFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        DownloadAsyncTaskResponse, NativeAdsManager.Listener, AdListener {

    private List<Object> rssItems=new ArrayList<>();
    private PostItemAdapter itemAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View v;
    private List<Object> newRssItems=new ArrayList<>();
    private NativeAdsManager listNativeAdsManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_ntvspor_fragment, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
      /*  alertDialog=new SpotsDialog(getActivity(),R.style.Custom_Progress_Dialog);
        setCustomAlertDialog();*/
        // fillTheData();


        swipeRefreshLayout.setOnRefreshListener(this);

        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        fetchAllNews();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
        );

        //init native ads manager
        listNativeAdsManager = new NativeAdsManager(getActivity(), getString(R.string.test_facebook_unit_id), 2);
        if (getString(R.string.test_ad_mode).equals("T")){
            AdSettings.addTestDevice(getString(R.string.test_device_id));
        }
        listNativeAdsManager.setListener(this);
        listNativeAdsManager.loadAds();
        return v;
    }

    private void fetchAllNews() {

        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          fillTheData();
                                      }
                                  }, 100
        );
    }

    private void fillTheData() {
        ListActivity splashActivity = ListActivity.getInstance();
        if (!splashActivity.getRssItems().equals(Collections.EMPTY_LIST)) {
            rssItems.addAll(splashActivity.getRssItems());

            ListView listView = (ListView) v.findViewById(R.id.postListView);

            itemAdapter = new PostItemAdapter(getActivity(),
                    R.layout.postitem, rssItems);

            // Bind the created avocarrotInstream adapter to your list instead of your listAdapter

            SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(itemAdapter);

            swingBottomInAnimationAdapter.setAbsListView(listView);

            listView.setAdapter(swingBottomInAnimationAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        getNewNews();

    }

    private void getNewNews() {
        newRssItems.clear();
        Resources resources = getActivity().getResources();
        new DownloadAsyncTask(new DownloadAsyncTaskResponse() {
            @Override
            public void processFinish(List<RssItem> output) {
                newRssItems.addAll(output);
            }
        }, swipeRefreshLayout).execute(resources.getString(R.string.ntvspor_feed));

        setNewListAdapter();
    }

    private void setNewListAdapter() {
        if (newRssItems != null && !newRssItems.isEmpty()) {

            ListView listView = (ListView) v.findViewById(R.id.postListView);

            itemAdapter = new PostItemAdapter(getActivity(),
                    R.layout.postitem, newRssItems);


            // Bind the created avocarrotInstream adapter to your list instead of your listAdapter

            SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(itemAdapter);

            swingBottomInAnimationAdapter.setAbsListView(listView);

            listView.setAdapter(swingBottomInAnimationAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void processFinish(List<RssItem> output) {
        newRssItems.addAll(output);
    }

    @Override
    public void onError(Ad ad, AdError adError) {

        Toast.makeText(getActivity(), "Ad failed to load: " +  adError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded(Ad ad) {

    }

    @Override
    public void onAdClicked(Ad ad) {
        Toast.makeText(getActivity(), "Ad Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdsLoaded() {

        //This method will download ad from FB
        //& call addNativeAd method which will add ad to listview

        NativeAd ad = this.listNativeAdsManager.nextNativeAd();
        ad.setAdListener(this);
        if (itemAdapter!=null)
            itemAdapter.addNativeAd(ad);   //postitemadaptere tanımlıyoruz.
    }

    @Override
    public void onAdError(AdError adError) {
        Toast.makeText(getActivity(), "Native ads manager failed to load: " +  adError.getErrorMessage(),
                Toast.LENGTH_SHORT).show();
    }
}
