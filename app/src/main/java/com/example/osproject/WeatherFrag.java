package com.example.osproject;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

//import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
//import com.google.android.exoplayer2.ExoPlayerFactory;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.ui.PlayerView;
//import com.google.android.exoplayer2.util.Util;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import okhttp3.Request;
//import okhttp3.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;

import static android.content.Context.LOCATION_SERVICE;
//import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;

// implements LocationListener
public class WeatherFrag extends Fragment {
    private View view;
    RequestQueue queue;

    // 날씨 View 선언
    TextView loc, con, temp, max_temp, min_temp, main_text;
    ImageView pic;
    //노래추천 View 선언
    TextView song_title, song_singer;
    ImageView album_image;
    String iconUrl;

    //재생버튼
    Button play_b;
    String utube;

    //utube url이 없을 때, 재생불가 메세지
    TextView text_nomusic3;

    // 노래추천실행 버튼
    Button bt_select2;

    //좋아요, 싫어요 버튼
    Button t_up, t_down;
    Button t_up_selected;
    int bb = 0;

    // 현재시간 불러오기
    long curTime = System.currentTimeMillis();
    Date mReDate = new Date(curTime);
    SimpleDateFormat mFormat = new SimpleDateFormat("HH",Locale.KOREA);
    String formatDate = mFormat.format(mReDate);

    // 현재 날짜
    Date date = new Date(curTime);
    SimpleDateFormat sdf_y = new SimpleDateFormat("yyyy");
    SimpleDateFormat sdf_m = new SimpleDateFormat("MM");
    SimpleDateFormat sdf_d = new SimpleDateFormat("dd");
    String year = sdf_y.format(date);
    String month = sdf_m.format(date);
    String day = sdf_d.format(date);

    int recomId = 0;
    int song_id;

    // 뮤직 재생
//    PlayerView playerView;
//    SimpleExoPlayer player;
//    boolean playWhenReady = true;
//    int currentWindow = 0;
//    long playbackPosition;
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if(Util.SDK_INT >= 24)
//            initPlayer();
//    }
//
//    @Override
//    public void onStop() {
//        if(Util.SDK_INT>=24)
//            releasePlayer();
//        super.onStop();
//    }
//
//    private void releasePlayer() {
//        if(player != null)
//        {
//            playWhenReady= player.getPlayWhenReady();
//            playbackPosition = player.getCurrentPosition();
//            currentWindow = player.getCurrentWindowIndex();
//            player.release();
//            player = null;
//
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if((Util.SDK_INT <24 || player == null))
//        {
//            initPlayer();
//            hideSystemUI();
//        }
//    }
//
//    private void hideSystemUI() {
//        playerView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LOW_PROFILE |
//                        View.SYSTEM_UI_FLAG_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        );
//    }
//
//    @Override
//    public void onPause() {
//        if(Util.SDK_INT < 24)
//            releasePlayer();
//        super.onPause();
//    }

    //    private PlayerView playerView;
//    private SimpleExoPlayer player;
//    private ImaAdsLoader adsLoader;
//    private PlayerView exoPlayerView;
//    private SimpleExoPlayer player;
//
//    private Boolean playWhenReady = true;
//    private int currentWindow = 0;
//    private Long playbackPosition = 0L;

//    LocationManager locationManager;
//    boolean isGPSEnabled = false;
//    boolean isNetworkEnabled = false;
//    boolean isGetLocation = false;
//    Location location;
//    double lat; // 위도
//    double lon; // 경도
//    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
//    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.weather,container,false);

        //날씨 위젯
        loc = (TextView)view.findViewById(R.id.mLocationTextView);
        con = (TextView)view.findViewById(R.id.mCountryTextView);
        temp = (TextView)view.findViewById(R.id.mTempTextView);
        max_temp = (TextView)view.findViewById(R.id.mMax_TempTextView);
        min_temp = (TextView)view.findViewById(R.id.mMin_TempTextView);
        main_text = (TextView)view.findViewById(R.id.mMainTextView);

        pic = (ImageView)view.findViewById(R.id.weather_pic);

        //날씨 위젯 배경색 시간대 별로 달라지게
        FrameLayout bg_color  = (FrameLayout) view.findViewById(R.id.background_color);
        if(formatDate.compareTo("19")>0 && formatDate.compareTo("23")<0)
            bg_color.setBackgroundResource(R.drawable.morning);
        else if(formatDate.compareTo("23")>0 && formatDate.compareTo("08")<0)
            bg_color.setBackgroundResource(R.drawable.day);
        else
            bg_color.setBackgroundResource(R.drawable.round);

        //날씨 기반 음악 추천
        song_title = (TextView)view.findViewById(R.id.song_title);
        song_singer = (TextView)view.findViewById(R.id.song_singer);
        album_image = (ImageView)view.findViewById(R.id.album_image);
        text_nomusic3 = view.findViewById(R.id.text_nomusic3);
        bt_select2 = view.findViewById(R.id.bt_select2);

        Thread find_weather_th = new find_weather();
        find_weather_th.start();
        //find_weather();

         //재생버튼 노래 재생
        play_b = (Button)view.findViewById(R.id.play_butt);
        play_b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Thread play_music_th = new play_music();
//                play_music_th.start();
            }
        });

        //좋아요, 싫어요
        t_up = (Button)view.findViewById(R.id.thumbs_up);
        t_down = (Button)view.findViewById(R.id.thumbs_down);
//        t_up_selected =(Button)view.findViewById(R.id.thumbs_up_selected);
        t_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postUpData();
            }
        });
//        if(bb == 0 ) {
//            t_up.setVisibility(t_up.VISIBLE);
//            t_up_selected.setVisibility(t_up_selected.INVISIBLE);
//            t_up.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    postUpData();
//                }
//            });
//        }
//        else{
//            t_up.setVisibility(t_up.INVISIBLE);
//            t_up_selected.setVisibility(t_up_selected.VISIBLE);
//            t_up_selected.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    postUpData();
//                }
//            });
//        }
        t_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        bt_select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((MainActivity) getActivity()).weather_music_recom){
                    //Thread find_weatherbase_music_th = new find_weatherbase_music();
                    //find_weatherbase_music_th.start();
                }
                //find_weatherbase_music();
            }
        });


        if(((MainActivity) getActivity()).weather_music_recom){
            song_title.setText(((MainActivity) getActivity()).song_title);
            song_singer.setText(((MainActivity) getActivity()).song_singer);
            if (((MainActivity) getActivity()).album_image.equals("null") || ((MainActivity) getActivity()).album_image.equals(""))
                album_image.setImageResource(R.drawable.ic_baseline_music_note_24);
            else Glide.with(getActivity()).load(((MainActivity) getActivity()).album_image).into(album_image);
        }
//        playerView = (PlayerView) view.findViewById(R.id.video_view);
//        initPlayer();


        return view;
    }

//    private void initPlayer() {
//        player = new SimpleExoPlayer.Builder(getActivity()).build();
//        playerView.setPlayer(player);
//
//        playYoutubeVideo("utube");
//    }
//
//    private void playYoutubeVideo(String utube) {
//        new YouTubeExtractor(this)
//        {
//
//        }.extract(utube, false, true);
//    }

    public class find_weather extends Thread
    {
        public void run() {
            String url = "https://dev.evertime.shop/weather";

            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject result_object = response.getJSONObject("result");
                        JSONArray weather_array = result_object.getJSONArray("weather");
                        JSONObject system_object = result_object.getJSONObject("sys");
                        JSONObject main_object = result_object.getJSONObject("main");
                        JSONObject weather = weather_array.getJSONObject(0);
                        JSONObject coord_object = result_object.getJSONObject("coord");

                        String location = result_object.getString("name");
                        String country = system_object.getString("country");
                        Double temperature = main_object.getDouble("temp");
                        Double maxtemp = main_object.getDouble("temp_max");
                        Double mintemp = main_object.getDouble("temp_min");
                        String maintx = weather.getString("main");

//                    Double long = coord_object.getDouble("lon");
//                    Double lati = coord_object.getDouble("lat");


                        loc.setText(location + " ,     ");
                        con.setText(country);
                        //temp.setText(temperature);
                        temp.setText(String.valueOf((int) (temperature - 273.15)));
                        max_temp.setText(String.valueOf((int) (maxtemp - 273.15)));
                        min_temp.setText(String.valueOf((int) (mintemp - 273.15)));
                        main_text.setText(maintx);

                        String icon = weather.getString("icon");
                        if(!icon.equals("null"))
                            loadIcon(icon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
	    );
            queue = Volley.newRequestQueue(getActivity());
            queue.add(jor);
        }
    }
    public void loadIcon(String icon) {
        Ion.with(this).load("http://openweathermap.org/img/wn/" + icon + "@2x.png").intoImageView(pic);
    }

    public class find_weatherbase_music extends Thread
    {
        public void run() {
            String url = "https://dev.evertime.shop/weather/music";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("String Response:", response.toString());
                            try {
                                ((MainActivity) getActivity()).weather_music_recom = true;
                                JSONObject result = response.getJSONObject("result");
                                song_title.setText(result.getString("musicName"));
                                if(song_title.length()>13) song_title.setTextSize(15);
                                else song_title.setTextSize(20);
                                song_singer.setText(result.getString("singer"));
                                String imageURL = result.getString("imageUrl");
                                ((MainActivity) getActivity()).song_title = result.getString("musicName");
                                ((MainActivity) getActivity()).song_singer = result.getString("singer");
                                ((MainActivity) getActivity()).album_image = result.getString("imageUrl");
                                utube = result.getString("youtubeUrl");
                                Log.e("find_weatherbase_music", "utube : " + utube);
                                if(utube.equals("null")){          //utube url 이 null인 경우
                                    play_b.setVisibility(play_b.INVISIBLE);
                                    text_nomusic3.setVisibility(text_nomusic3.VISIBLE);
//                                    return;
                                }

                                song_id = result.getInt("musicId");
                                if (imageURL.equals("null") || imageURL.equals(""))
                                    album_image.setImageResource(R.drawable.ic_baseline_music_note_24);
                                else Glide.with(getActivity()).load(imageURL).into(album_image);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error get weathermusic", error.toString());
                        }
                    }
            );
            objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(objectRequest);
        }
    }

    public void get_recomId() {
        String url = "https://dev.evertime.shop/music/year/" + year + "/mon/" + month + "/day/" + day;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("String Response:", response.toString());
                            JSONArray result_array = response.getJSONArray("result");
                            JSONObject music = result_array.getJSONObject(0);
                            recomId = music.getInt("recomId");
                            Log.e("GetRecomId", "recomId : " + recomId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error getting response:", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }

    public void postUpData() {

            String url = "https://dev.evertime.shop/playlist/like/" + String.valueOf(song_id);

//            if(bb == 0 ) {
//                t_up.setVisibility(t_up.INVISIBLE);
//                t_up_selected.setVisibility(t_up_selected.VISIBLE);
//                bb=1;
//            }
//            else{
//                t_up.setVisibility(t_up.VISIBLE);
//                t_up_selected.setVisibility(t_up_selected.INVISIBLE);
//                bb=0;
//            }

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("String Response:", response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error getting response:", error.toString());
                        }
                    }
            );
            requestQueue.add(objectRequest);

    }

}



