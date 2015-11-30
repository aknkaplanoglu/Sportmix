package tech.ozak.bjkhaber.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import tech.ozak.bjkhaber.R;
import tech.ozak.bjkhaber.dto.RssItem;

/**
 * Created by ako on 10/11/2015.
 */
public class PostItemAdapter extends ArrayAdapter<RssItem> implements View.OnClickListener{

  /*  private LayoutInflater inflater;

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
*/
  public tech.ozak.bjkhaber.lazyutil.ImageLoader imageLoader;
    private LayoutInflater inflater;
    private Activity myContext;
    private RssItem[] datas;
    Bitmap img = null;
   // AlertDialog alertDialog;

    public PostItemAdapter(Context context, int textViewResourceId,
                           RssItem[] objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
      //  alertDialog=new SpotsDialog(myContext,R.style.Custom_Progress_Dialog);
        datas = objects;
        inflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new tech.ozak.bjkhaber.lazyutil.ImageLoader(myContext.getApplicationContext());

       /* inflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.sportmix_icon)
                .showImageForEmptyUri(R.drawable.sportmix_icon)
                .showImageOnFail(R.drawable.sportmix_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();*/
        //  setCustomAlertDialog();
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
        View vi=convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.postitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.postThumbView = (ImageView) vi.findViewById(R.id.postThumb);
            holder.postTitleView = (TextView) vi.findViewById(R.id.postTitleLabel);
            holder.postDateView = (TextView) vi.findViewById(R.id.postDateLabel);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        RssItem post = datas[position];
        holder.postTitleView.setText(post.getTitle());
        holder.postDateView.setText(post.getPubDate());
        ImageView image = holder.postThumbView;

        int height = myContext.getResources().getDisplayMetrics().heightPixels*1/4;
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) image.getLayoutParams();
        layoutParams.height = height;
        image.setLayoutParams(layoutParams);

        //DisplayImage function from ImageLoader Class
        imageLoader.DisplayImage(post.getImgLink(), image);

        /******** Set Item Click Listner for LayoutInflater for each row ***********/
        vi.setOnClickListener(new OnItemClickListener(position));
        return vi;
    }

    /*  private void setCustomAlertDialog() {
        Window window = this.alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        this.alertDialog.setCancelable(true);
        alertDialog.setInverseBackgroundForced(true);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }*/

   /* public View getView(int position, View convertView, ViewGroup parent) {
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
       // ImageLoader.getInstance().displayImage(post.getImgLink(), viewHolder.postThumbView, options);

        return convertView;

    }*/


    /*View view = convertView;
    final ViewHolder holder;
    if (convertView == null) {
        view = inflater.inflate(R.layout.postitem, parent, false);
        holder = new ViewHolder();
        holder.postTitleView = (TextView) view.findViewById(R.id.postTitleLabel);
        holder.postThumbView = (ImageView) view.findViewById(R.id.postThumb);
        holder.postDateView = (TextView) view.findViewById(R.id.postDateLabel);
        view.setTag(holder);
    } else {
        holder = (ViewHolder) view.getTag();
    }

    RssItem post = datas[position];
    holder.postTitleView.setText(post.getTitle());
    holder.postDateView.setText(post.getPubDate());

    ImageLoader.getInstance().displayImage(post.getImgLink(), holder.postThumbView, options, animateFirstListener);
    return view;*/
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {

        }
    }


    private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  alertDialog.show();
        }

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
          //  alertDialog.dismiss();
        }
    }
}
