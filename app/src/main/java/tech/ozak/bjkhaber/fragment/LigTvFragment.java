package tech.ozak.bjkhaber.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.List;

import dmax.dialog.SpotsDialog;
import tech.ozak.bjkhaber.FeedActivityMain;
import tech.ozak.bjkhaber.R;
import tech.ozak.bjkhaber.adapter.PostItemAdapter;
import tech.ozak.bjkhaber.dto.RssItem;
import tech.ozak.bjkhaber.handler.RssReader;

/**
 * Created by ako on 10/31/2015.
 */
public class LigTvFragment extends Fragment{


    List<RssItem> rssItems;
    private RssItem[] listData;
    AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_ligtv_fragment, container, false);

       /* alertDialog=new SpotsDialog(getActivity(),R.style.Custom_Progress_Dialog);
        setCustomAlertDialog();*/

        fillTheData(rootView);
        return rootView;
    }




    private void setCustomAlertDialog() {
        Window window = this.alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        this.alertDialog.setCancelable(true);
        alertDialog.setInverseBackgroundForced(false);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void fillTheData(View v) {
        rssItems = FeedActivityMain.getInstance().getRssItems();

        if (this.rssItems !=null && !this.rssItems.isEmpty()){
            Log.e("Rss item size : ",String.valueOf(this.rssItems.size()));
            listData = new RssItem[this.rssItems.size()];
            for (int i = 0; i < this.rssItems.size(); i++) {
                listData[i] = this.rssItems.get(i);
            }
            ListView listView = (ListView) v.findViewById(R.id.postListView);

            PostItemAdapter itemAdapter = new PostItemAdapter(getActivity(),
                    R.layout.postitem, listData);
            listView.setAdapter(itemAdapter);
            listView.setClickable(true);
        }

    }


}