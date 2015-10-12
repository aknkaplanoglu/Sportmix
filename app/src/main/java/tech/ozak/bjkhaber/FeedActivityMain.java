package tech.ozak.bjkhaber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tech.ozak.bjkhaber.adapter.PostItemAdapter;
import tech.ozak.bjkhaber.dto.RssItem;

/**
 * Created by ako on 10/9/2015.
 */
public class FeedActivityMain extends Activity {

    ArrayList<String> headlines;
    ArrayList<String> links;
    List<RssItem> rssItems;
    private RssItem[] listData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_feed);

        NewsFeed newsFeed = NewsFeed.getInstance();
        rssItems = newsFeed.getRssItems();
        listData=new RssItem[rssItems.size()];
        for (int i=0;i<rssItems.size();i++){
            listData[i]=rssItems.get(i);
        }

        ListView listView = (ListView) this.findViewById(R.id.postListView);

        PostItemAdapter itemAdapter = new PostItemAdapter(this,
                R.layout.postitem, listData);
        listView.setAdapter(itemAdapter);


    }


}
