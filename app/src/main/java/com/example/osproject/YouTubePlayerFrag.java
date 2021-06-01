package com.example.osproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
<<<<<<< HEAD
import android.view.View;
import android.widget.Button;

=======
>>>>>>> Boeun
import androidx.annotation.Nullable;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

<<<<<<< HEAD
import static com.example.osproject.WeatherFrag.ptube;

=======
>>>>>>> Boeun
public class YouTubePlayerFrag extends YouTubeBaseActivity {
    private static final String TAG = "YouTubePlayerFrag";

    YouTubePlayerView mYouTubePlayerView;
<<<<<<< HEAD
    Button btnPlay;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

=======
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    String youtube;
>>>>>>> Boeun
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubeplayer);
<<<<<<< HEAD
        btnPlay = findViewById(R.id.btnPlay);
        mYouTubePlayerView = findViewById(R.id.youtubePlay);
        Log.d(TAG, "onCreate:starting.");

        Intent intent = getIntent();
        //mYouTubePlayerView.initialize(YoutubeConfig.getApiKey(),mOnInitializedListener);

=======

        mYouTubePlayerView = findViewById(R.id.youtubePlay);
>>>>>>> Boeun
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onClick:Done initializing.");
<<<<<<< HEAD
                youTubePlayer.cueVideo(ptube);
=======
                youTubePlayer.cueVideo(youtube);
>>>>>>> Boeun
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onClick:Failed to initialize.");
            }
        };
<<<<<<< HEAD
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:Initializing Youtube player.");
                mYouTubePlayerView.initialize(YouTubeConfig.getApiKey(),mOnInitializedListener);
            }
        });

    }
}
=======
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Intent intent = getIntent();
        youtube = intent.getStringExtra("youtube");
        if(intent.getStringExtra("data").equals("play"))
            mYouTubePlayerView.initialize(YoutubeConfig.getApiKey(),mOnInitializedListener);

    }

}
>>>>>>> Boeun
