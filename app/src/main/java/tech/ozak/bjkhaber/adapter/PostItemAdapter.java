package tech.ozak.bjkhaber.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tech.ozak.bjkhaber.DisplayContentActivity;
import tech.ozak.bjkhaber.R;
import tech.ozak.bjkhaber.dto.RssItem;

/**
 * Created by ako on 10/11/2015.
 */
public class PostItemAdapter extends ArrayAdapter<RssItem> implements View.OnClickListener{

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
        inflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            RssItem rssItem = datas[mPosition];
            Intent intent=new Intent(myContext, DisplayContentActivity.class);
            intent.putExtra("feed_link",rssItem.getFeedLink());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(intent);
     //       myContext.overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        }
    }


}
