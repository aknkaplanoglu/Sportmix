package tech.ozak.sportmix.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.List;

import tech.ozak.sportmix.R;
import tech.ozak.sportmix.SplashActivity;
import tech.ozak.sportmix.adapter.PostItemAdapter;
import tech.ozak.sportmix.dto.RssItem;

/**
 * Created by ako on 10/31/2015.
 */
public class NtvSporFragment extends Fragment {

    List<RssItem> rssItems;
    private RssItem[] listData;
    private PostItemAdapter itemAdapter;

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
        SplashActivity splashActivity = SplashActivity.getInstance();
        rssItems = splashActivity.getRssItems();
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
    }
}
