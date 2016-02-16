package tech.ozak.sportmix.asynTask;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import java.util.List;

import tech.ozak.sportmix.dto.RssItem;
import tech.ozak.sportmix.handler.RssReader;

/**
 * Created by ako on 16-Feb-16.
 */
public class DownloadAsyncTask extends AsyncTask<String, Void, Boolean> {

    public List<RssItem> rssItems;
    public DownloadAsyncTaskResponse delegate;
    public  SwipeRefreshLayout sw;

    public DownloadAsyncTask(DownloadAsyncTaskResponse listener,SwipeRefreshLayout swipeRefreshLayout){
        this.delegate=listener;
        this.sw=swipeRefreshLayout;
    }

    protected void onPreExecute() {
        sw.setRefreshing(true);
    }


    @Override
    protected void onPostExecute(final Boolean success) {
        delegate.processFinish(rssItems);
        sw.setRefreshing(false);
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            // Create RSS reader
            rssItems= RssReader.getLatestRssFeed(params[0]);
        } catch (Exception e) {
            Log.e("ITCRssReader", e.getMessage());
        }
        return true;
    }

}

