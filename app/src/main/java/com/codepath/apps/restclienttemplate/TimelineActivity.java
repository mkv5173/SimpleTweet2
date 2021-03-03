package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        swipeContainer = findViewById(R.id.swipeContainer);
        //configure the refrsehing colors so its not jsut solid black color
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            //type in 'new on' enter in first option and u get the rest in {}s
            @Override
            public void onRefresh() {
                //here we know user is doing pull to refresh so log it
                Log.i(TAG, "fetching new data!");
                populateHomeTimeline(); //in this method down there combine addAll and clear instead
                // of doing only tweets.addAll bc that adds data to bottom of set
                //so u use the new clear and addAll methods u created and delete adapter.notifyDataSetChanged()
                // now just have to call setRefreshing(false)
                //to signal that refresh has finished seen in the same section
            }
        });

        //To use adapter:
        //Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);

        //Initalize the list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);


        //Want to configure the recycler view - recycler view setup consist of:layout manager and the adapter
       rvTweets.setLayoutManager(new LinearLayoutManager(this));
       rvTweets.setAdapter(adapter);
       populateHomeTimeline();
        //set up recycler view properly but dont have data from api so we call our method on tweet model taking in json array and giving back list of tweets so going to onsuccess{} below
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
            Log.i(TAG, "onSuccess!" + json.toString());
            //to get data
                JSONArray jsonArray = json.jsonArray; //this is parsing json and if successful instead of creating tweets here u can just use the existing tweets defined above
                try {
                    //surround below line with try/catch so we cna log issues that come up done from context action bc of complaint
                    adapter.clear();
                    adapter.addAll(Tweet.fromJsonArray(jsonArray));
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);

                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure!" + response, throwable);
            }
        });
    }

}

//what we did till now in walkthroughs till 5 one more left which is refresh 9:44 PM
//used api key and api secret key to get my twitter account data constantly: project source files view
// <app<build<generated<source<buildConfig<debug<com<codepath<capps<restclienttemplate<BuildConfig
// - that's where you put in API (consumer key) and secret API (consumer secret)
//completed oauth application
//downloaded template app using a rest client, made network call to twitter api to fetch home timeline for the user which authenticated,
// //deserilized json data into user and tweet objects,
// used reycler view to render each tweet,
// used glide library to do remote image touching and rendering

//next steps:implementing pull to refresh swiperefreshlayout class
// - detects when vertical swipe is happening so u odnt have to consider how much adn waht velocity user has to swipe at in order to trigger pull to refresh,
// and also gives display to notify user something is happening
// lets u trigger callback so u know when the pull to refresh is happening and u can take corresponding action
// - add library under implementing pull to refresh guide the library is swiperefreshlayout
//to use swiperefreshlayout need to wrap recycler view then update recyclerview adapeter and then
//updating consists of adding helper methods into recyclerveiw adapter class tso tweets adapter
//and clean items or add items so when it refreshes it fires off api call and fetch new data and need to clear data and add new data
//configure swipe refresh layout and use it in timeline activity
//already initialized it by using findviewbyid in timeline activity but want to
// hook up swipe refresh layout to be able to get notified of when user has actually done on refresh gesture
// do that by adding listener setOnRefreshListener