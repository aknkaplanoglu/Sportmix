package tech.ozak.sportmix.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.ads.AdSettings;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.thefinestartist.finestwebview.FinestWebView;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import tech.ozak.sportmix.R;
import tech.ozak.sportmix.dto.RssItem;

/**
 * Created by ako on 10/11/2015.
 */
public class PostItemAdapter extends BaseAdapter implements View.OnClickListener {

    private LayoutInflater inflater;
    private Activity myContext;
    private List<Object> datas;

    private NativeAd ad;
    private static final int AD_INDEX = 3;

    public PostItemAdapter(Context context, int textViewResourceId,
                           List<Object> objects) {
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        //  alertDialog=new SpotsDialog(myContext,R.style.Custom_Progress_Dialog);
        this.datas = objects;
        inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // imageLoader.clearCache();

    }

    @Override
    public void onClick(View v) {

    }

    static class ViewHolder {
        TextView postTitleView;
        TextView postDateView;
        ImageView postThumbView;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (position == AD_INDEX && ad != null){
            //return ad view
            return (View) datas.get(position);
        }
        else {

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
            } else{

                holder = (ViewHolder) vi.getTag();
            }


            RssItem post = (RssItem) datas.get(position);
            if (holder!=null){

                holder.postTitleView.setText(post.getTitle());
                holder.postDateView.setText(post.getPubDate());
                ImageView image = holder.postThumbView;

                int height = myContext.getResources().getDisplayMetrics().heightPixels * 1 / 4;
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
                layoutParams.height = height;
                image.setLayoutParams(layoutParams);

                int width = myContext.getResources().getDisplayMetrics().widthPixels;

                String imgLink = post.getImgLink();
                String feedLink = post.getFeedLink();
                decideWhichImageOnListItem(holder, height, width, imgLink, feedLink);


                /******** Set Item Click Listner for LayoutInflater for each row ***********/
                vi.setOnClickListener(new OnItemClickListener(position));
            }


            return vi;
        }
    }




    private void decideWhichImageOnListItem(ViewHolder holder, int height, int width, String imgLink, String feedLink) {
        if (StringUtils.isBlank(imgLink)) {
            if (StringUtils.containsIgnoreCase(feedLink, "ntv")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ntvspor)
                        .error(R.mipmap.ntvspor)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "sabah")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.sabah)
                        .error(R.mipmap.sabah)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "trt")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.trtspor)
                        .error(R.mipmap.trtspor)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "haberturk")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.haberturk)
                        .error(R.mipmap.haberturk)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "ligtv")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ligtv)
                        .error(R.mipmap.ligtv)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "fotomac")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.fotomac)
                        .error(R.mipmap.fotomac)
                        .into(holder.postThumbView);

            } else if (StringUtils.containsIgnoreCase(feedLink, "sporx")) {

                Glide.with(myContext)
                        .load(imgLink)
                        .override(width, height)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.sporx)
                        .error(R.mipmap.sporx)
                        .into(holder.postThumbView);

            } else {
            }

        } else {
            Glide.with(myContext)
                    .load(imgLink)
                    .override(width, height)
                    .fitCenter()
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

        //    Intent intent = null;

            RssItem rssItem = (RssItem) datas.get(mPosition);
            String feedLink = rssItem.getFeedLink();
         //   String imgLink = rssItem.getImgLink();
            String header = rssItem.getTitle();




            new FinestWebView.Builder(myContext)
                    .theme(R.style.FinestWebViewTheme)
                    .titleDefault(header)
                    .showUrl(false)
                    .statusBarColorRes(R.color.bluePrimaryDark)
                    .toolbarColorRes(R.color.bluePrimary)
                    .titleColorRes(R.color.finestWhite)
                    .urlColorRes(R.color.bluePrimaryLight)
                    .iconDefaultColorRes(R.color.finestWhite)
                    .progressBarColorRes(R.color.finestWhite)
                    .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                    .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                    .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                    .showSwipeRefreshLayout(true)
                    .swipeRefreshColorRes(R.color.bluePrimaryDark)
                    .menuSelector(R.drawable.selector_light_theme)
                    .menuTextGravity(Gravity.CENTER)
                    .menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft)
                    .dividerHeight(0)
                    .gradientDivider(false)
                    .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                    .show(feedLink);




        }
    }



    public synchronized void addNativeAd(NativeAd ad) {
        if (ad == null) {
            return;

        }
        if (this.ad != null) {
            // Clean up the old ad before inserting the new one
            this.ad.unregisterView();
            this.datas.remove(AD_INDEX);
            this.ad = null;
            this.notifyDataSetChanged();
        }
        this.ad = ad;
        View adView = inflater.inflate(R.layout.ad_list, null);
        inflateAd(ad, adView);
        datas.add(AD_INDEX, adView);
        this.notifyDataSetChanged();
    }


    //Method to inflate ads
    private void inflateAd(NativeAd nativeAd, View adView) {
        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
        MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
        nativeAdMedia.setAutoplay(AdSettings.isVideoAutoplay());
        TextView nativeAdSocialContext =
                (TextView) adView.findViewById(R.id.native_ad_social_context);
        Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(View.VISIBLE);
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());

        // Downloading and setting the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Downloading and setting the cover image.
        NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
        int bannerWidth = adCoverImage.getWidth();
        int bannerHeight = adCoverImage.getHeight();
        DisplayMetrics metrics = myContext.getResources().getDisplayMetrics();
        int mediaWidth = adView.getWidth() > 0 ? adView.getWidth() : metrics.widthPixels;
        nativeAdMedia.setLayoutParams(new LinearLayout.LayoutParams(
                mediaWidth,
                Math.min(
                        (int) (((double) mediaWidth / (double) bannerWidth) * bannerHeight),
                        metrics.heightPixels / 7))); // eskiden 3
        nativeAdMedia.setNativeAd(nativeAd);

        // Wire up the View with the native ad, the whole nativeAdContainer will be clickable.
        nativeAd.registerViewForInteraction(adView);
    }









}
