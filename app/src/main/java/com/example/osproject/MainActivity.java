package com.example.osproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private WeatherFrag weather;
    private EmotionFrag emotion;
    private CalendarFrag calendar;
    private PlaylistFrag playlist;

    public int type1 = 1, type2 = 1;
    public boolean emotion_selected = false;
    public boolean emotion_music_recom = false;
    public boolean weather_music_recom = false;
    public String song_title2;
    public String song_singer2;
    public String album_image2;
    public String song_title;
    public String song_singer;
    public String album_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weather = new WeatherFrag();
        emotion = new EmotionFrag();
        calendar = new CalendarFrag();
        playlist = new PlaylistFrag();

        setFrag(0);         //첫 프래그먼트 화면 지정

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_weather:
                        setFrag(0);
                        break;
                    case R.id.action_emotion:
                        setFrag(1);
                        break;
                    case R.id.action_calendar:
                        setFrag(2);
                        break;
                    case R.id.action_playlist:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

    }

    //프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch(n){
            case 0:
                ft.replace(R.id.main_frame, weather);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, emotion);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, calendar);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, playlist);
                ft.commit();
                break;
        }
    }

}