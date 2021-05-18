package com.example.osproject;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import okhttp3.Request;
//import okhttp3.Response;

import java.time.Instant;

import static android.content.Context.LOCATION_SERVICE;

public class WeatherFrag extends Fragment {
    private View view;

    TextView loc, con, temp, max_temp, min_temp, main_text, des_text;
    ImageView pic;

    TextView title, singer;


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

        find_weather();

        //날씨 기반 음악 추천
        title = (TextView)view.findViewById(R.id.song_title);
        singer = (TextView)view.findViewById(R.id.song_singer);

        find_weatherbase_music();

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

                    String location = result_object.getString("name");
                    String country = system_object.getString("country");
                    Double temperature = main_object.getDouble("temp");
                    Double maxtemp = main_object.getDouble("temp_max");
                    Double mintemp = main_object.getDouble("temp_min");
                    String maintx = weather.getString("main");
                    String desttx = weather.getString("description");

                    loc.setText(location + " ,     ");
                    con.setText(country);
                    //temp.setText(temperature);
                    temp.setText(String.valueOf((int)(temperature-273.15)));
                    max_temp.setText(String.valueOf((int)(maxtemp-273.15)));
                    min_temp.setText(String.valueOf((int)(mintemp-273.15)));
                    main_text.setText(maintx);
                    des_text.setText(desttx);

//                    String icon = weather.getString("icon");
//                    //String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
//                    String iconUrl = "http://openweathermap.org/img/wn/" + icon + "@2x.png";
//                    Picasso.get().load(iconUrl).into(pic);

                    //.setImageDrawable(R.drawable.);


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
    RequestQueue queue = Volley.newRequestQueue(getActivity());
    queue.add(jor);

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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jor);
    }
}
