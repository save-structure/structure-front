package com.example.osproject;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import okhttp3.Request;
//import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.concurrent.Executor;

import static android.content.Context.LOCATION_SERVICE;

// implements LocationListener
public class WeatherFrag extends Fragment {
    private View view;
    RequestQueue queue;

    LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isGetLocation = false;
    Location location;
    double lat; // 위도
    double lon; // 경도
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;

    TextView loc, con, temp, max_temp, min_temp, main_text, des_text;
    ImageView pic;

    TextView title, singer;
    ImageView al_pic;
    String iconUrl;

    Button play_b;
    MediaPlayer mediaPlayer = new MediaPlayer();
    public LocationListener locationListener;


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
        des_text = (TextView)view.findViewById(R.id.mDescriptionTextView);

        pic = (ImageView)view.findViewById(R.id.weather_pic);

        //날씨 기반 음악 추천
        title = (TextView)view.findViewById(R.id.song_title);
        singer = (TextView)view.findViewById(R.id.song_singer);
        al_pic = (ImageView)view.findViewById(R.id.album_image);

        find_weather();
        //find_weatherbase_music();

        return view;
    }

    public void find_weather()
    {
        String url = "https://dev.evertime.shop/weather";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
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
                    String desttx = weather.getString("description");

//                    Double long = coord_object.getDouble("lon");
//                    Double lati = coord_object.getDouble("lat");

                    loc.setText(location + " ,     ");
                    con.setText(country);
                    //temp.setText(temperature);
                    temp.setText(String.valueOf((int)(temperature-273.15)));
                    max_temp.setText(String.valueOf((int)(maxtemp-273.15)));
                    min_temp.setText(String.valueOf((int)(mintemp-273.15)));
                    main_text.setText(maintx);
                    des_text.setText(desttx);

                    String icon = weather.getString("icon");
                    loadIcon(icon);
                }catch(JSONException e)
                {
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
    private void loadIcon(String icon) {
        Ion.with(this).load("http://openweathermap.org/img/wn/" + icon + "@2x.png").intoImageView(pic);
    }


    public void find_weatherbase_music()
    {
        String url = "https://dev.evertime.shop/weather/music";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject result_object = response.getJSONObject("result");
                    String tit = result_object.getString("musicName");
                    String sin = result_object.getString("singer");

                    title.setText(tit);
                    singer.setText(sin);

//                    String album_img = result_object.getString("imageUrl");
//                    Picasso.get().load(album_img).into(pic);

                }catch(JSONException e)
                {
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


//        // 재생버튼 노래 재생
//        play_b = (Button)view.findViewById(R.id.play_butt);
//        play_b.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                MediaPlayer mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                try {
//                    mediaPlayer.setDataSource(utube);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mediaPlayer.start();
//            }
//        });

    }


//    public Location getLocation(){
//        try{
//            AccessibilityService mContext = null;
//            locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
//            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//            if(!isGPSEnabled && !isNetworkEnabled){
//            }
//            else{
//                this.isGetLocation = true;
//                if(isNetworkEnabled){
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
//                    if(locationManager != null){
//                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if(location !=null){
//                            lat = location.getLatitude();
//                            lon = location.getLongitude();
//                        }
//                    }
//                }
//
//                if(isGPSEnabled){
//                    if(location == null){
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
//                        if(locationManager != null){
//                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            if(location !=null){
//                                lat = location.getLatitude();
//                                lon = location.getLongitude();
//                            }
//                        }
//                    }
//                }
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return location;
//    }
}



