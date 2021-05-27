package com.example.osproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EmotionFrag extends Fragment {
    private View view;
    private RadioGroup rg_emotions;
    private RadioButton rb_excited, rb_happy, rb_soso, rb_sad, rb_angry;
    private ImageButton bt_camera;
    private Button bt_select;
    private ImageButton bt_settings;
    private ImageView testImage;
    private TextView song_title2;
    private TextView song_singer2;
    private ImageView album_image2;

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
        bt_camera = view.findViewById(R.id.bt_camera);
        bt_settings = view.findViewById(R.id.bt_settings);
        bt_select = view.findViewById(R.id.bt_select);
        testImage = view.findViewById(R.id.testImage);

        song_title2 = view.findViewById(R.id.song_title2);
        song_singer2 = view.findViewById(R.id.song_singer2);
        album_image2 = view.findViewById(R.id.album_image2);

        //이미 선택되었을 경우 버튼 ENABLE = FALSE 처리
        if(((MainActivity)getActivity()).emotion_selected) {
            for (int i = 0; i < rg_emotions.getChildCount(); i++) {
                ((RadioButton) rg_emotions.getChildAt(i)).setEnabled(false);
            }
        }
        //버튼으로 기분 선택
        rg_emotions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_excited) feelingId = 1;
                else if (checkedId == R.id.rb_happy) feelingId = 2;
                else if (checkedId == R.id.rb_soso) feelingId = 3;
                else if (checkedId == R.id.rb_sad) feelingId = 4;
                else if (checkedId == R.id.rb_angry) feelingId = 5;
            }
        });

        //카메라로 기분 인식
        requestCamera();
        bt_camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });
        //기분 설정 창
        bt_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmotionSettingFrag dialog = new EmotionSettingFrag();
                dialog.show(getFragmentManager(), "EmotionSettingFrag");
            }
        });
        //기분 최종 선택 버튼
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rg_emotions.getCheckedRadioButtonId()!=-1 && !((MainActivity) getActivity()).emotion_selected) {
                    for (int i = 0; i < rg_emotions.getChildCount(); i++) {
                        ((MainActivity) getActivity()).emotion_selected = true;
                        rg_emotions.getChildAt(i).setEnabled(false);
                        postEmotionData();
                        if(!((MainActivity) getActivity()).emotion_music_recom){
                            //Thread th = new getEmotionData();
                            //th.start();
                        }
                    }
                }
            }
        });

        if(((MainActivity) getActivity()).emotion_music_recom){
            song_title2.setText(((MainActivity) getActivity()).song_title2);
            song_singer2.setText(((MainActivity) getActivity()).song_singer2);
            if (((MainActivity) getActivity()).album_image2.equals("null") || ((MainActivity) getActivity()).album_image2.equals(""))
                album_image2.setImageResource(R.drawable.ic_baseline_music_note_24);
            else Glide.with(getActivity()).load(((MainActivity) getActivity()).album_image2).into(album_image2);
        }

        return view;
    }

    public class getEmotionData extends Thread{
        public void run() {
            String url = "https://dev.evertime.shop/feeling/music";
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
                                ((MainActivity) getActivity()).emotion_music_recom = true;
                                JSONArray arr = response.getJSONArray("result");
                                JSONObject result = arr.getJSONObject(0);
                                ((MainActivity) getActivity()).song_title2 = result.getString("musicName");
                                ((MainActivity) getActivity()).song_singer2 = result.getString("singer");
                                ((MainActivity) getActivity()).album_image2 = result.getString("imageUrl");
                                song_title2.setText(result.getString("musicName"));
                                song_singer2.setText(result.getString("singer"));
                                String imageURL = result.getString("imageUrl");
                                if (imageURL.equals("null") || imageURL.equals(""))
                                    album_image2.setImageResource(R.drawable.ic_baseline_music_note_24);
                                else Glide.with(getActivity()).load(imageURL).into(album_image2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error getemotiondata:", error.toString());
                        }
                    }
            );
            requestQueue.add(objectRequest);
        }
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
                        Log.e("Error postemotiondata:", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }

    void requestCamera(){
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100){
           //캡쳐화면 가져오기
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            testImage.setImageBitmap(captureImage);
        }
    }
}