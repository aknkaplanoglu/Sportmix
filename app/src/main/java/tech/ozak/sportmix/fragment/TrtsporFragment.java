package tech.ozak.sportmix.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.List;

import tech.ozak.sportmix.ListActivity;
import tech.ozak.sportmix.R;
import tech.ozak.sportmix.adapter.PostItemAdapter;
import tech.ozak.sportmix.asynTask.DownloadAsyncTask;
import tech.ozak.sportmix.asynTask.DownloadAsyncTaskResponse;
import tech.ozak.sportmix.dto.RssItem;

/**
 * Created by ako on 26-Dec-15.
 */
public class TrtsporFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DownloadAsyncTaskResponse {

    private PostItemAdapter itemAdapter;
    private RssItem[] listData;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View v;
    private List<RssItem> newRssItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_aspor_fragment, container, false);
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
                                    }
                                }
        );


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

        List<RssItem> rssItems = ListActivity.getInstance().getRssItems();


        if (rssItems != null && !rssItems.isEmpty()) {

            Log.d("Rss Size ASPOR: ", String.valueOf(rssItems.size()));
            listData = new RssItem[rssItems.size()];
            for (int i = 0; i < rssItems.size(); i++) {
                listData[i] = rssItems.get(i);
            }

            ListView listView = (ListView) v.findViewById(R.id.postListView);

            itemAdapter = new PostItemAdapter(getActivity(),
                    R.layout.postitem, listData);

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
        Resources resources = getActivity().getResources();
        new DownloadAsyncTask(new DownloadAsyncTaskResponse() {
            @Override
            public void processFinish(List<RssItem> output) {
                newRssItems = output;
            }
        }, swipeRefreshLayout).execute(resources.getString(R.string.trtspor_feed));

        setNewListAdapter();
    }

    private void setNewListAdapter() {
        if (newRssItems != null && !newRssItems.isEmpty()) {
            Log.d("Rss Size ASPOR: ", String.valueOf(newRssItems.size()));
            listData = new RssItem[newRssItems.size()];
            for (int i = 0; i < newRssItems.size(); i++) {
                listData[i] = newRssItems.get(i);
            }

            ListView listView = (ListView) v.findViewById(R.id.postListView);

            itemAdapter = new PostItemAdapter(getActivity(),
                    R.layout.postitem, listData);

            SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(itemAdapter);
            swingBottomInAnimationAdapter.setAbsListView(listView);

            listView.setAdapter(swingBottomInAnimationAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void processFinish(List<RssItem> output) {

        newRssItems = output;
    }
}

