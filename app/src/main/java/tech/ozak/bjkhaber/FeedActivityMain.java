package tech.ozak.bjkhaber;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

    ArrayAdapter adapter;
    ArrayList<String> headlines;
    ArrayList<String> links;
    List<RssItem> rssItems;
    private RssItem[] listData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_feed);



       // Intent i = getIntent();
       // getExtrasFromPrevious(i);
        NewsFeed newsFeed = NewsFeed.getInstance();
        rssItems = newsFeed.getRssItems();
        listData=new RssItem[rssItems.size()];
        for (int i=0;i<rssItems.size();i++){
            listData[i]=rssItems.get(i);
        }
       /* ArrayAdapter<RssItem> rssItemArrayAdapter=new ArrayAdapter<RssItem>(this,
                android.R.layout.simple_list_item_1, rssItems);*/
//        adapter = new ArrayAdapter(this,
//                android.R.layout.simple_list_item_1, headlines);

        ListView listView = (ListView) this.findViewById(R.id.postListView);

        PostItemAdapter itemAdapter = new PostItemAdapter(this,
                R.layout.postitem, listData);
        listView.setAdapter(itemAdapter);




       // setListAdapter(rssItemArrayAdapter);
    }

    private void getExtrasFromPrevious(Intent i) {
        links = i.getStringArrayListExtra("links");
        headlines = i.getStringArrayListExtra("headlines");
    }

    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {


        Uri uri = Uri.parse(listData[position].getLink());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }*/
}
