package ru.lazybones.jkh.jkhapp;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReserveRVadapter extends RecyclerView.Adapter<ReserveRVadapter.CustomViewHolder> {
    private ArrayList<PreOrder> feedItemList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public ReserveRVadapter(Context context, ArrayList<PreOrder> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_reserve, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        PreOrder feedItem = feedItemList.get(i);

        //Render image using Picasso library
//        if (!TextUtils.isEmpty(feedItem.getParkphotoURL())) {
//        String sr = feedItem.getParkphotoURL();
//
//
//        Picasso.get().load(sr)
//                .error(R.drawable.main_icon)
//                .placeholder(R.drawable.main_icon)
//                .into(customViewHolder.imageView);
//                }

        final int n = i;

         //Setting text view title
            //do if it is only book
            customViewHolder.layout.setBackgroundResource(R.color.yellow);
            Date timeend = new Date(feedItem.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yy", Locale.UK);
            customViewHolder.textView3.setText("Время заявки: " + dateFormat.format(timeend));


            // do if it is park
            customViewHolder.layout.setBackgroundResource(R.color.white);


//        String devid = feedItem.getDevicenumber();

//        customViewHolder.deviceidtv.setText(devid);

        customViewHolder.textView.setText(Html.fromHtml(feedItem.getStatus()));
       // customViewHolder.textView2.setText(Html.fromHtml(feedItem.getTime()));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(feedItemList.get(n), v);
            }
        };
        customViewHolder.imageView.setOnClickListener(listener);
        customViewHolder.textView.setOnClickListener(listener);
        customViewHolder.layout.setOnClickListener(listener);


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;

        protected TextView deviceidtv;
        protected ImageView rentim;

        protected TextView textView;
        protected TextView textView2;
        protected TextView textView3;
        protected RelativeLayout layout;

        public CustomViewHolder(View view) {
            super(view);
            this.layout= (RelativeLayout) view.findViewById(R.id.itemlayout);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);

            this.deviceidtv = (TextView) view.findViewById(R.id.deviceidtv);
            this.textView = (TextView) view.findViewById(R.id.title);
            this.textView2 = (TextView) view.findViewById(R.id.phone);
            this.textView3 = (TextView) view.findViewById(R.id.adres);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PreOrder item, View v);
    }

}


