package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetDetailsActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

import static com.codepath.apps.restclienttemplate.R.layout.item_tweet;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;


    // Constructor


    public TweetAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // Pass in context and inflate the layouts
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get data from position
        Tweet t = tweets.get(position);
        holder.bind(t);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // ViewHolder class for row
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvName;
        TextView tvRelTime;
        ImageView ivTweetPic;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfile);
            tvBody = itemView.findViewById(R.id.tvTweet);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            tvRelTime = itemView.findViewById(R.id.tvRelTime);
            ivTweetPic = itemView.findViewById(R.id.ivTweetPic);

            itemView.setOnClickListener(this);
        }

        public void bind(Tweet t) {
            tvBody.setText(t.getBody());
            tvName.setText(t.getUser().getName());
            tvScreenName.setText("@" + t.getUser().getScreenName());
            tvRelTime.setText(t.getRelTime());
            Glide.with(context).load(t.getUser().getProfileImageUrl()).into(ivProfileImage);
            Log.i("ADAPTER", t.getMediaUrl());
            Glide.with(context).load(t.getMediaUrl()).into(ivTweetPic);

            if(t.getMediaUrl().isEmpty()){
                ivTweetPic.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TweetDetailsActivity.class);
                    Tweet t = tweets.get(getAdapterPosition());
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(t));
                    context.startActivity(intent);
                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }
}
