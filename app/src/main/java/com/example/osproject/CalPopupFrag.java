package com.example.osproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CalPopupFrag extends DialogFragment {

    private static final String TAG = "CalPopupFrag";

    private View view;
    private Button bt_OK;
    private ImageView icon_weather;
    private ImageView icon_emotion;
    private ImageView img_weather_cover;
    private ImageView img_emotion_cover;
    private TextView text_weather_title;
    private TextView text_weather_artist;
    private TextView text_emotion_title;
    private TextView text_emotion_artist;
    private TextView text_nomusic1;
    private TextView text_nomusic2;

    public int year;
    public int month;
    public int day;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar_popuplist,container,false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        icon_weather = view.findViewById(R.id.icon_weather);
        icon_emotion = view.findViewById(R.id.icon_emotion);
        img_weather_cover = view.findViewById(R.id.img_weather_cover);
        img_emotion_cover = view.findViewById(R.id.img_emotion_cover);
        text_weather_title = view.findViewById(R.id.text_weather_title);
        text_weather_artist = view.findViewById(R.id.text_weather_artist);
        text_emotion_title = view.findViewById(R.id.text_emotion_title);
        text_emotion_artist = view.findViewById(R.id.text_emotion_artist);
        text_nomusic1 = view.findViewById(R.id.text_nomusic1);
        text_nomusic2 = view.findViewById(R.id.text_nomusic2);
        getPopupData th = new getPopupData();
        th.start();

        bt_OK = view.findViewById(R.id.bt_OK2);
        bt_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }
    public class getPopupData extends Thread{
        public void run() {
            String url = "https://dev.evertime.shop/music/year/" + Integer.toString(year) + "/mon/" + Integer.toString(month) + "/day/" + Integer.toString(day);
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

                                if (response.getInt("code") == 3010) {          //둘다 없을 경우
                                    icon_weather.setVisibility(icon_weather.INVISIBLE);
                                    text_nomusic1.setVisibility(text_nomusic1.VISIBLE);
                                    text_weather_title.setVisibility(text_weather_title.INVISIBLE);
                                    text_weather_artist.setVisibility(text_emotion_artist.INVISIBLE);
                                    img_weather_cover.setVisibility(img_weather_cover.INVISIBLE);
                                    icon_emotion.setVisibility(icon_emotion.INVISIBLE);
                                    text_nomusic2.setVisibility(text_nomusic2.VISIBLE);
                                    text_emotion_title.setVisibility(text_emotion_title.INVISIBLE);
                                    text_emotion_artist.setVisibility(text_emotion_artist.INVISIBLE);
                                    img_emotion_cover.setVisibility(img_emotion_cover.INVISIBLE);
                                    return;
                                }

                                JSONObject[] music = new JSONObject[2];
                                JSONArray result_object = response.getJSONArray("result");
                                for (int i = 0; i < result_object.length(); i++) {
                                    Object obj = result_object.get(i);
                                    if (obj != null) {
                                        if (obj instanceof JSONObject) music[i] = (JSONObject) obj;
                                    }
                                }

                                if (music[0] == null) {
                                    icon_weather.setVisibility(icon_weather.INVISIBLE);
                                    text_nomusic1.setVisibility(text_nomusic1.VISIBLE);
                                    text_weather_title.setVisibility(text_weather_title.INVISIBLE);
                                    text_weather_artist.setVisibility(text_emotion_artist.INVISIBLE);
                                    img_weather_cover.setVisibility(img_weather_cover.INVISIBLE);
                                } else {
                                    String weather = music[0].getString("weather");             //weather에 따른 icon_weather 변경 여기서
                                    setWeatherIcon(weather);

                                    String musicname1 = music[0].getString("musicName");
                                    if (musicname1.length() > 14)
                                        text_weather_title.setTextSize(12);
                                    text_weather_title.setText(musicname1);

                                    String singername1 = music[0].getString("singer");
                                    if (singername1.length() > 20)
                                        text_weather_title.setTextSize(10);
                                    text_weather_artist.setText(singername1);

                                    String imageURL1 = music[0].getString("imageUrl");
                                    if (imageURL1.equals("null") || imageURL1.equals(""))
                                        img_weather_cover.setImageResource(R.drawable.ic_baseline_music_note_24);
                                    else
                                        Glide.with(getActivity()).load(imageURL1).placeholder(R.drawable.ic_baseline_music_note_24).
                                                error(R.drawable.ic_baseline_music_note_24).into(img_weather_cover);
                                }

                                if (music[1] == null) {
                                    icon_emotion.setVisibility(icon_emotion.INVISIBLE);
                                    text_nomusic2.setVisibility(text_nomusic2.VISIBLE);
                                    text_emotion_title.setVisibility(text_emotion_title.INVISIBLE);
                                    text_emotion_artist.setVisibility(text_emotion_artist.INVISIBLE);
                                    img_emotion_cover.setVisibility(img_emotion_cover.INVISIBLE);
                                } else {
                                    Integer feeling = music[1].getInt("feeling");
                                    if (feeling == 1)
                                        icon_emotion.setImageResource(R.drawable.ic_excited);
                                    else if (feeling == 2)
                                        icon_emotion.setImageResource(R.drawable.ic_happy);
                                    else if (feeling == 3)
                                        icon_emotion.setImageResource(R.drawable.ic_soso);
                                    else if (feeling == 4)
                                        icon_emotion.setImageResource(R.drawable.ic_sad);
                                    else if (feeling == 5)
                                        icon_emotion.setImageResource(R.drawable.ic_angry);

                                    String musicname2 = music[1].getString("musicName");
                                    if (musicname2.length() > 14)
                                        text_emotion_title.setTextSize(12);
                                    text_emotion_title.setText(musicname2);

                                    String singername2 = music[1].getString("singer");
                                    if (singername2.length() > 20)
                                        text_emotion_title.setTextSize(10);
                                    text_emotion_artist.setText(music[1].getString("singer"));

                                    String imageURL2 = music[1].getString("imageUrl");
                                    if (imageURL2.equals("null") || imageURL2.equals(""))
                                        img_emotion_cover.setImageResource(R.drawable.ic_baseline_music_note_24);
                                    else
                                        Glide.with(getActivity()).load(imageURL2).placeholder(R.drawable.ic_baseline_music_note_24).
                                                error(R.drawable.ic_baseline_music_note_24).into(img_emotion_cover);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error calpopupfrag:", error.toString());
                        }
                    }
            );
            requestQueue.add(objectRequest);
        }
    }
    void setWeatherIcon(String weather){
        if(weather.equals("thunderstorm with light rain")||weather.equals("thunderstorm with rain")
                ||weather.equals("thunderstorm with heavy rain")||weather.equals("light thunderstorm")
                ||weather.equals("thunderstorm")||weather.equals("heavy thunderstorm")
                ||weather.equals("ragged thunderstorm")||weather.equals("thunderstorm with light drizzle")
                ||weather.equals("thunderstorm with drizzle")||weather.equals("thunderstorm with heavy drizzle")){
            icon_weather.setImageResource(R.drawable.ic_thunder);
        }
        if(weather.equals("drizzle")||weather.equals("light intensity drizzle")
                ||weather.equals("heavy intensity drizzle")||weather.equals("light intensity drizzle rain")
                ||weather.equals("drizzle rain")||weather.equals("heavy intensity drizzle rain")
                ||weather.equals("shower rain and drizzle")||weather.equals("heavy shower rain and drizzle")
                ||weather.equals("shower drizzle")){
            icon_weather.setImageResource(R.drawable.ic_drizzle);
        }
        if(weather.equals("light rain")||weather.equals("moderate rain")
                ||weather.equals("heavy intensity rain")||weather.equals("very heavy rain")
                ||weather.equals("extreme rain")||weather.equals("freezing rain")
                ||weather.equals("light intensity shower rain")||weather.equals("shower rain")
                ||weather.equals("heavy intensity shower rain")||weather.equals("ragged shower rain")){
            icon_weather.setImageResource(R.drawable.ic_rain);
        }
        if(weather.equals("light snow")||weather.equals("Snow")
                ||weather.equals("Heavy snow")||weather.equals("Sleet")
                ||weather.equals("Light shower sleet")||weather.equals("Shower sleet")
                ||weather.equals("Light rain and snow")||weather.equals("Rain and snow")
                ||weather.equals("Light shower snow")||weather.equals("Shower snow")
                ||weather.equals("Heavy shower snow")){
            icon_weather.setImageResource(R.drawable.ic_snow);
        }
        if(weather.equals("mist")||weather.equals("Smoke")
                ||weather.equals("Haze")||weather.equals("sand/ dust whirls")
                ||weather.equals("fog")||weather.equals("sand")
                ||weather.equals("dust")||weather.equals("volcanic ash")
                ||weather.equals("squalls")||weather.equals("tornado")){
            icon_weather.setImageResource(R.drawable.ic_atmosphere);
        }
        if(weather.equals("clear sky")){
            icon_weather.setImageResource(R.drawable.ic_clear);
        }
        if(weather.equals("few clouds")||weather.equals("scattered clouds")
                ||weather.equals("broken clouds")||weather.equals("overcast clouds")){
            icon_weather.setImageResource(R.drawable.ic_clouds);
        }
    }
}
