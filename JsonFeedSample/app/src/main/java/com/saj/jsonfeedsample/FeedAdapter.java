package com.saj.jsonfeedsample;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saj.jsonfeedsample.models.FeedItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * List Adapter for the Feeds
 * Created by thekkesa on 10/02/2015.
 */
public class FeedAdapter extends BaseAdapter {

    private List<FeedItem> feedList;
    private Context context;

    public FeedAdapter(Context context, List<FeedItem> feedItemList) {
        super();
        feedList = feedItemList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return feedList.size();
    }

    @Override
    public Object getItem(int i) {
        return feedList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tv_description);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        if (feedList != null && feedList.size() > 0) {
            final FeedItem item = feedList.get(position);
            holder.tvTitle.setText(item.getTitle());
            holder.tvDescription.setText(item.getDescription());

            // lazily load the images using Picasso, in case of errors show the default image
            Picasso.with(context).load(Uri.parse(item.getImageUrl())).
                    noFade().
                    placeholder(R.drawable.ic_launcher).
                    error(R.drawable.ic_launcher).
                    resize(100, 100).
                    into(holder.ivImage);


        }
        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        ImageView ivImage;
    }
}
