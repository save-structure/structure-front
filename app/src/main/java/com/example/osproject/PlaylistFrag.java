package com.example.osproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaylistFrag extends Fragment {
    View view;
    private RadioGroup rg_weather;
    private RadioButton rb_thunderstorm, rb_drizzle, rb_rain, rb_snow, rb_atmosphere, rb_clear, rb_clouds;

    private RadioGroup rg_emotions;
    private RadioButton rb_excited, rb_happy, rb_soso, rb_sad, rb_angry;

    RecyclerView playlist;
    SongAdapter songAdapter;

    //empty arraylist 생성(nonprefixed, 동적인 노래개수)
    static ArrayList<Song> favList = new ArrayList<Song>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.playlist,container,false);

        playlist = (RecyclerView)view.findViewById(R.id.rv);
        songAdapter = new SongAdapter(favList);
        playlist.setAdapter(songAdapter);
        playlist.setLayoutManager(new LinearLayoutManager(getActivity()));


        rg_weather = view.findViewById(R.id.rg_weather);
        rb_thunderstorm = view.findViewById(R.id.rb_thunderstorm);
        rb_drizzle = view.findViewById(R.id.rb_drizzle);
        rb_rain = view.findViewById(R.id.rb_rain);
        rb_snow = view.findViewById(R.id.rb_snow);
        rb_atmosphere = view.findViewById(R.id.rb_atmosphere);
        rb_clear = view.findViewById(R.id.rb_clear);
        rb_clouds = view.findViewById(R.id.rb_clouds);

        rg_emotions = view.findViewById(R.id.rg_emotions);
        rb_excited = view.findViewById(R.id.rb_excited);
        rb_happy = view.findViewById(R.id.rb_happy);
        rb_soso = view.findViewById(R.id.rb_soso);
        rb_sad = view.findViewById(R.id.rb_sad);
        rb_angry = view.findViewById(R.id.rb_angry);

        rg_weather.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_thunderstorm) get_weather_pl(1);
                else if (checkedId == R.id.rb_drizzle) get_weather_pl(2);
                else if (checkedId == R.id.rb_rain) get_weather_pl(3);
                else if (checkedId == R.id.rb_snow) get_weather_pl(4);
                else if (checkedId == R.id.rb_atmosphere) get_weather_pl(5);
                else if (checkedId == R.id.rb_clear) get_weather_pl(6);
                else if (checkedId == R.id.rb_clouds) get_weather_pl(7);

            }
        });

        rg_emotions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_excited) get_feeling_pl(1);
                else if (checkedId == R.id.rb_happy) get_feeling_pl(2);
                else if (checkedId == R.id.rb_soso) get_feeling_pl(3);
                else if (checkedId == R.id.rb_sad) get_feeling_pl(4);
                else if (checkedId == R.id.rb_angry) get_feeling_pl(5);

            }
        });
//        for(int i=0;i<favList.size();i++){
//            Log.e("johyun", favList.get(i).getTitle());
//        }
        return view;
    }

    public void get_weather_pl(int weather_idx){
        String url = "https://dev.evertime.shop/playlist/weather/" + String.valueOf(weather_idx);

        JsonObjectRequest weather_pl = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray result_array = response.getJSONArray("result");
                    favList = new ArrayList<Song>();
                    for(int i=0;i<result_array.length();i++){

                        JSONObject songObject = result_array.getJSONObject(i);
                        String singer = songObject.getString("singer");
                        String title = songObject.getString("musicName");
                        String imgUrl = songObject.getString("imageUrl");

                        Song song = new Song(title, singer, imgUrl);
                        favList.add(i, song);
                        Log.e("JsonParsing", "Singer : " + singer);
                    }
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error getting response:", error.toString());
            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(weather_pl);
    }

    public void get_feeling_pl(int feel_idx){
        String url = "https://dev.evertime.shop/playlist/feeling/" + String.valueOf(feel_idx);

        JsonObjectRequest feeling_pl = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.e("response :", response.toString());
                    JSONArray result_array = response.getJSONArray("result");
                    favList = new ArrayList<Song>();
                    for(int i=0;i<result_array.length();i++){

                        JSONObject songObject = result_array.getJSONObject(i);
                        String singer = songObject.getString("singer");
                        String title = songObject.getString("musicName");
                        String imgUrl = songObject.getString("imageUrl");

                        Song song = new Song(title, singer, imgUrl);
                        favList.add(i, song);

                    }

                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error getting response:", error.toString());
            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(feeling_pl);
    }

}
