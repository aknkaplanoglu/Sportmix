package tech.ozak.sportmix.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import tech.ozak.sportmix.SporxContentActivity;
import tech.ozak.sportmix.TrtsporContentActivity;
import tech.ozak.sportmix.FotomacContentActivity;
import tech.ozak.sportmix.HaberTurkContentActivity;
import tech.ozak.sportmix.LigTvContentActivity;
import tech.ozak.sportmix.R;
import tech.ozak.sportmix.NtvSporContentActivity;
import tech.ozak.sportmix.SabahContentActivity;
import tech.ozak.sportmix.dto.RssItem;

/**
 * Created by ako on 10/11/2015.
 */
public class PostItemAdapter extends ArrayAdapter<RssItem> implements View.OnClickListener {

    public tech.ozak.sportmix.lazyutil.ImageLoader imageLoader;
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
        imageLoader = new tech.ozak.sportmix.lazyutil.ImageLoader(myContext.getApplicationContext());
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
        String feedLink = post.getFeedLink();
        decideWhichImageOnListItem(holder, height, width, imgLink, feedLink);


        /******** Set Item Click Listner for LayoutInflater for each row ***********/
        vi.setOnClickListener(new OnItemClickListener(position));
        return vi;
    }

    private void decideWhichImageOnListItem(ViewHolder holder, int height, int width, String imgLink, String feedLink) {
        if (StringUtils.isBlank(imgLink)) {
            if (StringUtils.containsIgnoreCase(feedLink, "ntv")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ntvspor)
                        .error(R.mipmap.ntvspor)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "sabah")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.sabah)
                        .error(R.mipmap.sabah)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "trt")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.trtspor)
                        .error(R.mipmap.trtspor)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "haberturk")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.haberturk)
                        .error(R.mipmap.haberturk)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "ligtv")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ligtv)
                        .error(R.mipmap.ligtv)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "fotomac")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.fotomac)
                        .error(R.mipmap.fotomac)
                        .into(holder.postThumbView);

            }else if (StringUtils.containsIgnoreCase(feedLink, "sporx")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.sporx)
                        .error(R.mipmap.sporx)
                        .into(holder.postThumbView);

            } else {
            }

        }

        else{
            Glide.with(myContext)
                    .load(imgLink)
                    .override(width, height)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(Color.TRANSPARENT)
                    .error(Color.TRANSPARENT)
                    .into(holder.postThumbView);
        }
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
            String header = rssItem.getTitle();
            intent = decideWhichIntent(feedLink);


            intent.putExtra("feed_link", feedLink);
            intent.putExtra("img_link", imgLink);
            intent.putExtra("header",header);
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
        } else if (StringUtils.containsIgnoreCase(feedLink, "trt")) {
            intent = new Intent(myContext, TrtsporContentActivity.class);
        } else if (StringUtils.containsIgnoreCase(feedLink, "haberturk")) {
            intent = new Intent(myContext, HaberTurkContentActivity.class);
        } else if (StringUtils.containsIgnoreCase(feedLink, "ligtv")) {
            intent = new Intent(myContext, LigTvContentActivity.class);
        } else if (StringUtils.containsIgnoreCase(feedLink, "fotomac")) {
            intent = new Intent(myContext, FotomacContentActivity.class);
        } else if (StringUtils.containsIgnoreCase(feedLink, "sporx")) {
            intent = new Intent(myContext, SporxContentActivity.class);
        }else {
            intent = new Intent(myContext, TrtsporContentActivity.class);
        }
        return intent;
    }


}
