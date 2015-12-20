package tech.ozak.bjkhaber.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import tech.ozak.bjkhaber.DisplayContentActivity;
import tech.ozak.bjkhaber.NewsFeed;
import tech.ozak.bjkhaber.R;
import tech.ozak.bjkhaber.adapter.PostItemAdapter;
import tech.ozak.bjkhaber.dto.RssItem;

/**
 * Created by ako on 10/31/2015.
 */
public class NtvSporFragment extends Fragment {

    List<RssItem> rssItems;
    private RssItem[] listData;

    public NtvSporFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_ntvspor_fragment, container, false);
        fillTheData(rootView);
        return rootView;
    }

    private void fillTheData(View v) {
        NewsFeed newsFeed = NewsFeed.getInstance();
        rssItems = newsFeed.getRssItems();
        listData = new RssItem[rssItems.size()];
        for (int i = 0; i < rssItems.size(); i++) {
            listData[i] = rssItems.get(i);
        }

        ListView listView = (ListView) v.findViewById(R.id.postListView);

        PostItemAdapter itemAdapter = new PostItemAdapter(getActivity(),
                R.layout.postitem, listData);
        listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RssItem rssItem = listData[position];
                Intent intent=new Intent(getActivity(), DisplayContentActivity.class);
                intent.putExtra("feed_link",rssItem.getFeedLink());
                startActivity(intent);

            }
        });
    }
}
