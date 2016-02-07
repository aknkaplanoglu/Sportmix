package tech.ozak.bjkhaber.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.commons.lang3.StringUtils;

import tech.ozak.bjkhaber.AsporContentActivity;
import tech.ozak.bjkhaber.FotomacContentActivity;
import tech.ozak.bjkhaber.HaberTurkContentActivity;
import tech.ozak.bjkhaber.LigTvContentActivity;
import tech.ozak.bjkhaber.R;
import tech.ozak.bjkhaber.NtvSporContentActivity;
import tech.ozak.bjkhaber.SabahContentActivity;
import tech.ozak.bjkhaber.dto.RssItem;

/**
 * Created by ako on 10/11/2015.
 */
public class PostItemAdapter extends ArrayAdapter<RssItem> implements View.OnClickListener {

    public tech.ozak.bjkhaber.lazyutil.ImageLoader imageLoader;
    private LayoutInflater inflater;
    private Activity myContext;
    private RssItem[] datas;

    public PostItemAdapter(Context context, int textViewResourceId,
                           RssItem[] objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        //  alertDialog=new SpotsDialog(myContext,R.style.Custom_Progress_Dialog);
        datas = objects;
        inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new tech.ozak.bjkhaber.lazyutil.ImageLoader(myContext.getApplicationContext());
        // imageLoader.clearCache();

    }

    @Override
    public void onClick(View v) {

    }

    static class ViewHolder {
        TextView postTitleView;
        TextView postDateView;
        ImageView postThumbView;
        String postThumbViewURL;
        Bitmap bmap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.postitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.postThumbView = (ImageView) vi.findViewById(R.id.postThumb);
            holder.postTitleView = (TextView) vi.findViewById(R.id.postTitleLabel);
            holder.postDateView = (TextView) vi.findViewById(R.id.postDateLabel);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        RssItem post = datas[position];
        holder.postTitleView.setText(post.getTitle());
        holder.postDateView.setText(post.getPubDate());
        ImageView image = holder.postThumbView;

        int height = myContext.getResources().getDisplayMetrics().heightPixels * 1 / 4;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
        layoutParams.height = height;
        image.setLayoutParams(layoutParams);

        int width = myContext.getResources().getDisplayMetrics().widthPixels;

        //DisplayImage function from ImageLoader Class
        // imageLoader.DisplayImage(post.getImgLink(), image);

      /*  Picasso.with(myContext)
                .load(post.getImgLink())
                .resize(width,height)
                .placeholder(R.drawable.sportmix_logo)
                .error(R.drawable.imglogo)
                .into(holder.postThumbView);*/
        String imgLink = post.getImgLink();
        if (StringUtils.isNotBlank(imgLink)) {
            Glide.with(myContext)
                    .load(imgLink)
                    .override(width, height)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.sportmix_logo)
                    .error(R.drawable.imglogo)
                    .into(holder.postThumbView);
        }
        else{
            Glide.with(myContext)
                    .load(imgLink)
                    .override(width, height)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.fotomac)
                    .error(R.mipmap.fotomac)
                    .into(holder.postThumbView);
        }


        /******** Set Item Click Listner for LayoutInflater for each row ***********/
        vi.setOnClickListener(new OnItemClickListener(position));
        return vi;
    }


    /*********
     * Called when Item click in ListView
     ************/
    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {

            Intent intent = null;

            RssItem rssItem = datas[mPosition];
            String feedLink = rssItem.getFeedLink();
            String imgLink = rssItem.getImgLink();

            intent = decideWhichIntent(feedLink);


            intent.putExtra("feed_link", feedLink);
            intent.putExtra("img_link", imgLink);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(intent);
            //       myContext.overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        }
    }

    @NonNull
    private Intent decideWhichIntent(String feedLink) {
        Intent intent;
        if (StringUtils.containsIgnoreCase(feedLink, "ntv")) {

            intent = new Intent(myContext, NtvSporContentActivity.class);

        } else if (StringUtils.containsIgnoreCase(feedLink, "sabah")) {
            intent = new Intent(myContext, SabahContentActivity.class);
        } else if (StringUtils.containsIgnoreCase(feedLink, "aspor")) {
            intent = new Intent(myContext, AsporContentActivity.class);
        } else if (StringUtils.containsIgnoreCase(feedLink, "haberturk")) {
            intent = new Intent(myContext, HaberTurkContentActivity.class);
        } else if (StringUtils.containsIgnoreCase(feedLink, "ligtv")) {
            intent = new Intent(myContext, LigTvContentActivity.class);
        } else if (StringUtils.containsIgnoreCase(feedLink, "fotomac")) {
            intent = new Intent(myContext, FotomacContentActivity.class);
        }else {
            intent = new Intent(myContext, AsporContentActivity.class);
        }
        return intent;
    }


}
