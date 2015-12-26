package tech.ozak.bjkhaber.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import tech.ozak.bjkhaber.FeedActivityMain;
import tech.ozak.bjkhaber.R;
import tech.ozak.bjkhaber.adapter.PostItemAdapter;
import tech.ozak.bjkhaber.dto.RssItem;

/**
 * Created by ako on 26-Dec-15.
 */
public class HaberTurkFragment extends Fragment {


    private RssItem[] listData;
    // AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_haberturk_fragment, container, false);

      /*  alertDialog=new SpotsDialog(getActivity(),R.style.Custom_Progress_Dialog);
        setCustomAlertDialog();*/

        fillTheData(rootView);
        return rootView;
    }




   /* private void setCustomAlertDialog() {
        Window window = this.alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        this.alertDialog.setCancelable(true);
        alertDialog.setInverseBackgroundForced(false);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }*/

    private void fillTheData(View v) {

        List<RssItem> rssItems = FeedActivityMain.getInstance().getRssItems();


        if (rssItems !=null && !rssItems.isEmpty()){

            Log.d("Rss Size HABERTURK: ", String.valueOf(rssItems.size()));
            listData = new RssItem[rssItems.size()];
            for (int i = 0; i < rssItems.size(); i++) {
                listData[i] = rssItems.get(i);
            }

            ListView listView = (ListView) v.findViewById(R.id.postListView);

            PostItemAdapter itemAdapter = new PostItemAdapter(getActivity(),
                    R.layout.postitem, listData);
            listView.setAdapter(itemAdapter);
            listView.setClickable(true);
        }

    }




}