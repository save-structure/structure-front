package com.example.osproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private WeatherFrag weather;
    private EmotionFrag emotion;
    private CalendarFrag calendar;
    private PlaylistFrag playlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        weather = new WeatherFrag();
        emotion = new EmotionFrag();
        calendar = new CalendarFrag();
        playlist = new PlaylistFrag();
        setFrag(0);         //첫 프래그먼트 화면 지정

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