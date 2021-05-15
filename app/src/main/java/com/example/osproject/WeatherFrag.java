package com.example.osproject;

import android.accessibilityservice.AccessibilityService;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import okhttp3.Request;
//import okhttp3.Response;

import static android.content.Context.LOCATION_SERVICE;

public class WeatherFrag extends Fragment {
    private View view;

    TextView loc, con, temp, max_temp, min_temp, main_text, des_text;
    //ImageView pic;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.weather,container,false);

        loc = (TextView)view.findViewById(R.id.mLocationTextView);
        con = (TextView)view.findViewById(R.id.mCountryTextView);
        temp = (TextView)view.findViewById(R.id.mTempTextView);
        max_temp = (TextView)view.findViewById(R.id.mMax_TempTextView);
        min_temp = (TextView)view.findViewById(R.id.mMin_TempTextView);
        main_text = (TextView)view.findViewById(R.id.mMainTextView);
        des_text = (TextView)view.findViewById(R.id.mDescriptionTextView);

        //pic = (ImageView)view.findViewById(R.id.weather_pic);

        find_weather();


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
                    String temperature = String.valueOf(main_object.getDouble("temp"));
                    String maxtemp = String.valueOf(main_object.getDouble("temp_max"));
                    String mintemp = String.valueOf(main_object.getDouble("temp_min"));
                    String maintx = weather.getString("main");
                    String desttx = weather.getString("description");

                    loc.setText(location);
                    con.setText(country);
                    temp.setText(temperature);
                    max_temp.setText(maxtemp);
                    min_temp.setText(mintemp);
                    main_text.setText(maintx);
                    des_text.setText(desttx);

//                    double temp_int = Double.parseDouble(String.valueOf(temp));
//                    double centi_temp_int = (temp_int - 32) / 1.80000;
//                    centi_temp_int = Math.round(centi_temp_int);
//                    int i = (int)centi_temp_int;
//                    temp.setText(String.valueOf(i));
//
//                    double max_temp_int = Double.parseDouble(String.valueOf(max_temp));
//                    double centi_max_temp_int = (max_temp_int - 32) / 1.80000;
//                    centi_max_temp_int = Math.round(centi_max_temp_int);
//                    int ii = (int)centi_max_temp_int;
//                    temp.setText(String.valueOf(ii));
//
//                    double min_temp_int = Double.parseDouble(String.valueOf(min_temp));
//                    double centi_min_temp_int = (min_temp_int - 32) / 1.80000;
//                    centi_min_temp_int = Math.round(centi_min_temp_int);
//                    int iii = (int)centi_min_temp_int;
//                    temp.setText(String.valueOf(iii));


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
