package tech.ozak.bjkhaber.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import tech.ozak.bjkhaber.R;
import tech.ozak.bjkhaber.dto.RssItem;

/**
 * Created by ako on 10/11/2015.
 */
public class PostItemAdapter extends ArrayAdapter<RssItem> {

    private Activity myContext;
    private RssItem[] datas;

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
        if (post.getPostThumbUrl() != null) {
            viewHolder.postThumbViewURL = post.getPostThumbUrl();
         //   new DownloadImageTask().execute(viewHolder);
            viewHolder.postThumbView.setImageResource(R.drawable.imglogo);
        } else {
            viewHolder.postThumbView.setImageResource(R.drawable.imglogo);
        }

        viewHolder.postTitleView.setText(post.getTitle());
        viewHolder.postDateView.setText(post.getPostDate());
        return convertView;

    }


}
