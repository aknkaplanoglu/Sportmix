package tech.ozak.bjkhaber.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tech.ozak.bjkhaber.R;
import tech.ozak.bjkhaber.dto.RssItem;

/**
 * Created by ako on 10/11/2015.
 */
public class PostItemAdapter extends ArrayAdapter<RssItem> {

    private Activity myContext;
    private RssItem[] datas;
    Bitmap img = null;

    public PostItemAdapter(Context context, int textViewResourceId,
                           RssItem[] objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        datas = objects;
    }

    static class ViewHolder {
        TextView postTitleView;
        TextView postDateView;
        ImageView postThumbView;
        String postThumbViewURL;
        Bitmap bmap;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = myContext.getLayoutInflater();
            convertView = layoutInflater.inflate(R.layout.postitem, null);

            viewHolder = new ViewHolder();
            viewHolder.postThumbView = (ImageView) convertView.findViewById(R.id.postThumb);
            viewHolder.postTitleView = (TextView) convertView.findViewById(R.id.postTitleLabel);
            viewHolder.postDateView = (TextView) convertView.findViewById(R.id.postDateLabel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RssItem post = datas[position];
        viewHolder.postThumbViewURL = post.getImgLink();
        new DownloadAsyncTask().execute(viewHolder);
        viewHolder.postTitleView.setText(post.getTitle());
        viewHolder.postDateView.setText(post.getPubDate());
        return convertView;

    }


    private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            // TODO Auto-generated method stub
            //load image directly
            ViewHolder viewHolder = params[0];
            try {
                URL imageURL = new URL(viewHolder.postThumbViewURL);
                InputStream is = imageURL.openStream();
                viewHolder.bmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                // TODO: handle exception
                Log.e("error", "Downloading Image Failed");
                viewHolder.bmap = null;
            }

            return viewHolder;
        }

        @Override
        protected void onPostExecute(ViewHolder result) {
            // TODO Auto-generated method stub
            if (result.bmap == null) {
                result.postThumbView.setImageResource(R.drawable.imglogo);
            } else {
                result.postThumbView.setImageBitmap(result.bmap);
            }
        }
    }
}
