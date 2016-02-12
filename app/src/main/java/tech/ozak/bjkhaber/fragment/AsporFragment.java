package tech.ozak.bjkhaber.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.List;

import tech.ozak.bjkhaber.ListActivity;
import tech.ozak.bjkhaber.R;
import tech.ozak.bjkhaber.adapter.PostItemAdapter;
import tech.ozak.bjkhaber.dto.RssItem;

/**
 * Created by ako on 26-Dec-15.
 */
public class AsporFragment extends Fragment  {

    private PostItemAdapter itemAdapter;
    private RssItem[] listData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_aspor_fragment, container, false);


      /*  alertDialog=new SpotsDialog(getActivity(),R.style.Custom_Progress_Dialog);
        setCustomAlertDialog();*/

        fillTheData(rootView);


        return rootView;
    }



    private void fillTheData(View v) {

        List<RssItem> rssItems = ListActivity.getInstance().getRssItems();


        if (rssItems !=null && !rssItems.isEmpty()){

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
        }

    }

}

