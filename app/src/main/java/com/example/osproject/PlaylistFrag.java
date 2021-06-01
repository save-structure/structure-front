package com.example.osproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    int wf = -1;
    int ff = -1;

    int weather_idx;
    int feeling_idx;

    Button filterbt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.playlist, container, false);

        Log.e("onCreateView : ", "start of fun. : ");

        playlist = (RecyclerView) view.findViewById(R.id.rv);
        setPlaylist();

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

        filterbt = view.findViewById(R.id.filter_bt);
        get_thumbup_pl(new PlaylistFrag.VolleyCallBack(){
            @Override
            public void onSuccess() {
                setPlaylist();
            }
        });
        set_weather_filter(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                setPlaylist();
            }
        });
        set_feeling_filter(new VolleyCallBack(){
            @Override
            public void onSuccess() {
                setPlaylist();
            }
        });
        rg_weather.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_no_noweather) wf = 0;
                else if (checkedId == R.id.rb_thunderstorm) wf = 1;
                else if (checkedId == R.id.rb_drizzle) wf = 2;
                else if (checkedId == R.id.rb_rain) wf = 3;
                else if (checkedId == R.id.rb_snow) wf = 4;
                else if (checkedId == R.id.rb_atmosphere) wf = 5;
                else if (checkedId == R.id.rb_clear) wf = 6;
                else if (checkedId == R.id.rb_clouds) wf = 7;
                else wf = -1;
                weather_idx = wf;
            }
        });
        rg_emotions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_no_nofeeling) ff = 0;
                else if (checkedId == R.id.rb_excited) ff = 1;
                else if (checkedId == R.id.rb_happy) ff = 2;
                else if (checkedId == R.id.rb_soso) ff = 3;
                else if (checkedId == R.id.rb_sad) ff = 4;
                else if (checkedId == R.id.rb_angry) ff = 5;
                else ff = -1;
                feeling_idx = ff;
            }
        });
        filterbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilter();
            }
        });

        Log.e("onCreate :", "wf :" + wf + " , " + "ff :"+ ff );

        return view;
    }
    private void setPlaylist(){
        Log.e("setPlaylist :", "settingplaylist adpater!");
        songAdapter = new SongAdapter(favList);
        playlist.setAdapter(songAdapter);
        playlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        songAdapter.notifyDataSetChanged();
    }
    private void setFilter() {
        if(wf==0){
            favList = new ArrayList<Song>();
            if(ff==0 || ff == -1) get_thumbup_pl(new PlaylistFrag.VolleyCallBack() {
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
            else set_feeling_filter(new PlaylistFrag.VolleyCallBack(){
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
        }
        else if(wf == 1){
            favList = new ArrayList<Song>();
            if(ff==0 || ff == -1) set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
            else {
                set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
                set_feeling_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
            }
        }
        else if(wf == 2){
            favList = new ArrayList<Song>();
            if(ff==0 || ff == -1) set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
            else {
                set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
                set_feeling_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
            }
        }
        else if(wf == 3){
            favList = new ArrayList<Song>();
            if(ff==0 || ff == -1) set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
            else {
                set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
                set_feeling_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
            }
        }
        else if(wf == 4){
            favList = new ArrayList<Song>();
            if(ff==0 || ff == -1) set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
            else {
                set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
                set_feeling_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
            }
        }
        else if(wf == 5){
            favList = new ArrayList<Song>();
            if(ff==0 || ff == -1) set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
            else {
                set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
                set_feeling_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
            }
        }
        else if(wf == 6){
            favList = new ArrayList<Song>();
            if(ff==0 || ff == -1) set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
            else {
                set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
                set_feeling_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
            }
        }
        else if(wf == 7){
            favList = new ArrayList<Song>();
            if(ff==0 || ff == -1) set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
            else {
                set_weather_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
                set_feeling_filter(new PlaylistFrag.VolleyCallBack(){
                    @Override
                    public void onSuccess() {
                        setPlaylist();
                    }
                });
            }
        }
        else{  // 날씨 필터 선택안함
            favList = new ArrayList<Song>();
            if(ff == 0 || ff == -1) get_thumbup_pl(new PlaylistFrag.VolleyCallBack() {
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
            else set_feeling_filter(new PlaylistFrag.VolleyCallBack(){
                @Override
                public void onSuccess() {
                    setPlaylist();
                }
            });
        }
    }

    public void get_thumbup_pl(final VolleyCallBack callBack) {
        String url = "https://dev.evertime.shop/playlist";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest thumbsup_pl = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int code = response.getInt("code");
                    if(code == 3014){
                        System.out.println("result_array is null");
                        callBack.onSuccess();
                        return;
                    }
                    else {
                        JSONArray result_array = response.getJSONArray("result");
                        for (int i = 0; i < result_array.length(); i++) {
                            JSONObject songObject = result_array.getJSONObject(i);
                            String singer = songObject.getString("singer");
                            String title = songObject.getString("musicName");
                            String imgUrl = songObject.getString("imageUrl");
                            Song song = new Song(title, singer, imgUrl);
                            favList.add(i, song);
                            Log.e("JsonParsing_thumb_up", "Singer : " + singer);
                            callBack.onSuccess();
                        }
                    }
                } catch (JSONException e) {
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
        queue.add(thumbsup_pl);
    }
    public void set_weather_filter(final VolleyCallBack callBack){
        String url = "https://dev.evertime.shop/playlist/weather/" + String.valueOf(weather_idx);
        JsonObjectRequest weather_pl = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int code = response.getInt("code");
                    if(code == 3013){
                        System.out.println("result_array is null");
                        callBack.onSuccess();
                        return;
                    }
                    else {
                        JSONArray result_array = response.getJSONArray("result");
                        for (int i = 0; i < result_array.length(); i++) {
                            JSONObject songObject = result_array.getJSONObject(i);
                            String singer = songObject.getString("singer");
                            String title = songObject.getString("musicName");
                            String imgUrl = songObject.getString("imageUrl");

                            Song song = new Song(title, singer, imgUrl);
                            favList.add(i, song);
                            Log.e("JsonParsing_weather", "Singer : " + singer);
                            callBack.onSuccess();
                        }
                    }
                } catch (JSONException e) {
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
    public void set_feeling_filter(final VolleyCallBack callBack){
        String url2 = "https://dev.evertime.shop/playlist/feeling/" + String.valueOf(feeling_idx);
        JsonObjectRequest feeling_pl = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int code = response.getInt("code");
                    if(code == 3012){
                        System.out.println("result_array is null");
                        callBack.onSuccess();
                        return;
                    }
                    else{
                    JSONArray result_array = response.getJSONArray("result");
                        for (int i = 0; i < result_array.length(); i++) {
                            JSONObject songObject = result_array.getJSONObject(i);
                            String singer = songObject.getString("singer");
                            String title = songObject.getString("musicName");
                            String imgUrl = songObject.getString("imageUrl");
                            Song song = new Song(title, singer, imgUrl);
                            favList.add(i, song);
                            Log.e("JsonParsing_feeling", "Singer : " + singer);
                            callBack.onSuccess();
                        }
                    }
                } catch (JSONException e) {
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
        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
        queue2.add(feeling_pl);
    }

    public interface VolleyCallBack{
        void onSuccess();
    }
    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        mContext = context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}
