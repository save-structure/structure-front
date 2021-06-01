package com.example.osproject;
import android.content.Intent;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    static public String ptube;

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

    Intent intent;

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
            bg_color.setBackgroundResource(R.drawable.night);

        //날씨 기반 음악 추천
        song_title = (TextView)view.findViewById(R.id.song_title);
        song_singer = (TextView)view.findViewById(R.id.song_singer);
        album_image = (ImageView)view.findViewById(R.id.album_image);
        text_nomusic3 = view.findViewById(R.id.text_nomusic3);
        bt_select2 = view.findViewById(R.id.bt_select2);

        Thread find_weather_th = new find_weather();
        find_weather_th.start();

         //재생버튼 노래 재생
        play_b = view.findViewById(R.id.play_butt);
        play_b.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),YouTubePlayerFrag.class);
                startActivityForResult(intent,1);
            }
        });

        // 좋아요, 싫어요
        t_up = (Button)view.findViewById(R.id.thumbs_up);
        t_down = (Button)view.findViewById(R.id.thumbs_down);

        t_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postUpData();
            }
        });
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
                    Thread find_weatherbase_music_th = new find_weatherbase_music();
                    find_weatherbase_music_th.start();

                }
                new find_weatherbase_music();
            }
        });

        if(((MainActivity) getActivity()).weather_music_recom){
            song_title.setText(((MainActivity) getActivity()).song_title);
            song_singer.setText(((MainActivity) getActivity()).song_singer);
            if (((MainActivity) getActivity()).album_image.equals("null") || ((MainActivity) getActivity()).album_image.equals(""))
                album_image.setImageResource(R.drawable.ic_baseline_music_note_24);
            else Glide.with(getActivity()).load(((MainActivity) getActivity()).album_image).into(album_image);
        }
        return view;
    }
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

                        loc.setText(location + " ,     ");
                        con.setText(country);
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

                                int idx = utube.indexOf("=");
                                ptube = utube.substring(idx + 1);
                                Log.e("ptube", " : " + ptube);

                                Log.e("find_weatherbase_music", "utube : " + utube);
                                if(utube.equals("null")){          //utube url 이 null인 경우
                                    play_b.setVisibility(play_b.INVISIBLE);
                                    text_nomusic3.setVisibility(text_nomusic3.VISIBLE);
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
    public void postUpData() {
            String url = "https://dev.evertime.shop/playlist/like/" + String.valueOf(song_id);

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



