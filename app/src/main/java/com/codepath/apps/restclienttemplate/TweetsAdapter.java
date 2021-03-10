package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    //after creating viewholder u extend to it and complaint occurs and android studio helps with the other parts by using the context action: implement methods all three
    Context context;
    List<Tweet> tweets;


    //Pass in the context and list of tweets (this is through constructor so at the top)
    //generate constructor
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    //For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the data at position
        Tweet tweet = tweets.get(position);
        //Bind the tweet with the view holder, the one passed in
        holder.bind(tweet);   //complaint and so u create method bind pops up at the very bottom
        //holder.tvStamp.setText(tweet.getFormattedTimestamp());
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //add clear and add methods to clean all elements of the recycler
    public void clear(){
        //can't do tweets = new ArrayList<>(); because u would clear out it but u create empty 'tweets' diff reference
        //so screws up recycler view so make sure to modify existing 'tweets'
     tweets.clear();
     notifyDataSetChanged();
    }
    //add a list of items to data set
    public void addAll(List<Tweet> tweetList){
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    //Define a viewholder - start here when u create adapter

    public class ViewHolder extends RecyclerView.ViewHolder{ //this will complain so need add constructor which is what's below
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //references to each of the elements in design
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvStamp = itemView.findViewById(R.id.tvStamp);

        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            //tvBody is body of tweet so on forth
            //laod in image based on image url for the user done through glide import it when u get complaint
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
            tvStamp.setText(tweet.getFormattedTimestamp());
        } //can use adapter now in Timeline Activity which is basically main activity file but named itmeline bc it is twitter
    }

}

