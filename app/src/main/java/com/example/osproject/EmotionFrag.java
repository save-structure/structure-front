package com.example.osproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;


public class EmotionFrag extends Fragment {
    private View view;
    private RadioGroup rg_emotions;
    private RadioButton rb_excited, rb_happy, rb_soso, rb_sad, rb_angry;
    private Button bt_camera;
    private ImageButton bt_settings;
    private Integer feelingId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.emotion, container, false);

        rg_emotions = view.findViewById(R.id.rg_emotions);
        rb_excited = view.findViewById(R.id.rb_excited);
        rb_happy = view.findViewById(R.id.rb_happy);
        rb_soso = view.findViewById(R.id.rb_soso);
        rb_sad = view.findViewById(R.id.rb_sad);
        rb_angry = view.findViewById(R.id.rb_angry);
        //bt_camera = view.findViewById(R.id.bt_camera);

        rg_emotions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_excited) feelingId = 1;
                else if (checkedId == R.id.rb_happy) feelingId = 2;
                else if (checkedId == R.id.rb_soso) feelingId = 3;
                else if (checkedId == R.id.rb_sad) feelingId = 4;
                else if (checkedId == R.id.rb_angry) feelingId = 5;
                //feelingId으로 emotion정보 반환

                postEmotionData();
            }
        });

        bt_settings = view.findViewById(R.id.bt_settings);
        bt_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmotionSettingFrag dialog = new EmotionSettingFrag();
                dialog.show(getFragmentManager(), "EmotionSettingFrag");
            }
        });

        return view;
    }

    public void postEmotionData() {
        String url = "https://dev.evertime.shop/feeling/" + String.valueOf(feelingId);
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