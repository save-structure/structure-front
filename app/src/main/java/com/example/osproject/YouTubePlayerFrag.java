package com.example.osproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YouTubePlayerFrag extends YouTubeBaseActivity {
    private static final String TAG = "YouTubePlayerFrag";

    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    String youtube;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubeplayer);

        mYouTubePlayerView = findViewById(R.id.youtubePlay);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onClick:Done initializing.");
                youTubePlayer.cueVideo(youtube);
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onClick:Failed to initialize.");
            }
        };

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Intent intent = getIntent();
        youtube = intent.getStringExtra("youtube");
        if(intent.getStringExtra("data").equals("play"))
            mYouTubePlayerView.initialize(YoutubeConfig.getApiKey(),mOnInitializedListener);

    }

}

