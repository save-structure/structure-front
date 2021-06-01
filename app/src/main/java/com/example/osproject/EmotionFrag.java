package com.example.osproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;



public class EmotionFrag extends Fragment {
    private View view;
    private RadioGroup rg_emotions;
    private ImageButton bt_camera;
    private Button bt_select;
    private ImageButton bt_settings;
    private TextView song_title2;
    private TextView song_singer2;
    private ImageView album_image2;
    private TextView nomusic5;
    private Button play_butt2;
    private TextView addmusic2;
    private Button bt_like2;
    private FrameLayout player_frame2;

    private Integer feelingId = 0;
    Integer musicId = 0;

    String currentPhotoPath;
    private static final int IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.emotion, container, false);

        rg_emotions = view.findViewById(R.id.rg_emotions);
        bt_camera = view.findViewById(R.id.bt_camera);
        bt_settings = view.findViewById(R.id.bt_settings);
        bt_select = view.findViewById(R.id.bt_select);
        bt_like2 = view.findViewById(R.id.bt_like2);
        addmusic2 = view.findViewById(R.id.addmusic2);
        play_butt2 = view.findViewById(R.id.play_butt2);
        player_frame2 = view.findViewById(R.id.player_frame2);
        nomusic5 = view.findViewById(R.id.text_nomusic5);
        song_title2 = view.findViewById(R.id.song_title2);
        song_singer2 = view.findViewById(R.id.song_singer2);
        album_image2 = view.findViewById(R.id.album_image2);

        //카메라로 이미 feelingId가 만들어졌을 경우
        if(((MainActivity)getActivity()).camera_clicked){
            Log.e("camera ","already clicked");
            bt_camera.setImageResource(R.drawable.ic_camera_selected);
        }

        //이미 선택되었을 경우 버튼 ENABLE = FALSE 처리
        if(((MainActivity)getActivity()).emotion_selected) {
            for (int i = 0; i < rg_emotions.getChildCount(); i++) {
                ((RadioButton) rg_emotions.getChildAt(i)).setEnabled(false);
            }
            bt_camera.setEnabled(false);
        }

        //버튼으로 기분 선택
        rg_emotions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FrameLayout bg_color = view.findViewById(R.id.background_color2);
                if (checkedId == R.id.rb_excited) {
                    feelingId = 1;
                    bg_color.setBackgroundResource(R.drawable.bg_excited);}
                else if (checkedId == R.id.rb_happy) {
                    feelingId = 2;
                    bg_color.setBackgroundResource(R.drawable.bg_happy);}
                else if (checkedId == R.id.rb_soso) {
                    feelingId = 3;
                    bg_color.setBackgroundResource(R.drawable.bg_soso);}
                else if (checkedId == R.id.rb_sad) {
                    feelingId = 4;
                    bg_color.setBackgroundResource(R.drawable.bg_sad);}
                else if (checkedId == R.id.rb_angry) {
                    feelingId = 5;
                    bg_color.setBackgroundResource(R.drawable.bg_angry);}
            }
        });

        //CFR API
        requestCamera();
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
                bt_camera.setImageResource(R.drawable.ic_camera_selected);
            }
        });

        //기분 설정 팝업창
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
                if(!((MainActivity) getActivity()).emotion_selected) {
                    ((MainActivity) getActivity()).emotion_selected = true;
                    for (int i = 0; i < rg_emotions.getChildCount(); i++) {
                        rg_emotions.getChildAt(i).setEnabled(false);
                    }
                    bt_camera.setEnabled(false);
                    postEmotionData(new EmotionFrag.VolleyCallBack(){
                        @Override
                        public void onSuccess() {
                            if(!((MainActivity) getActivity()).emotion_music_recom){
                                Thread th = new getEmotionData();
                                th.start();
                            }
                        }
                    });
                }
            }
        });
        //이미 추천받았을 시 초기화
        if(((MainActivity) getActivity()).emotion_music_recom){
            player_frame2.setBackgroundResource(R.drawable.round3);
            song_title2.setVisibility(View.VISIBLE);
            song_singer2.setVisibility(View.VISIBLE);
            album_image2.setVisibility(View.VISIBLE);
            play_butt2.setVisibility(View.VISIBLE);
            bt_like2.setVisibility(View.VISIBLE);
            addmusic2.setVisibility(View.VISIBLE);
            nomusic5.setVisibility(View.INVISIBLE);

            String title = ((MainActivity) getActivity()).song_title2;
            if(title.length()>15) song_title2.setTextSize(18);
            song_title2.setText(title);
            song_singer2.setText(((MainActivity) getActivity()).song_singer2);
            if (((MainActivity) getActivity()).album_image2.equals("null") || ((MainActivity) getActivity()).album_image2.equals(""))
                album_image2.setImageResource(R.drawable.ic_baseline_music_note_24);
            else Glide.with(getActivity()).load(((MainActivity) getActivity()).album_image2).placeholder(R.drawable.ic_baseline_music_note_24).
                    error(R.drawable.ic_baseline_music_note_24).into(album_image2);
        }
        else{
            player_frame2.setBackground(null);
            song_title2.setVisibility(View.INVISIBLE);
            song_singer2.setVisibility(View.INVISIBLE);
            album_image2.setVisibility(View.INVISIBLE);
            play_butt2.setVisibility(View.INVISIBLE);
            bt_like2.setVisibility(View.INVISIBLE);
            addmusic2.setVisibility(View.INVISIBLE);
            nomusic5.setVisibility(View.VISIBLE);
        }

        if(((MainActivity)getActivity()).emotion_like)
            bt_like2.setBackgroundResource(R.drawable.ic_baseline_favorite_24_selected);
        else bt_like2.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
        bt_like2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((MainActivity)getActivity()).emotion_like) {
                    bt_like2.setBackgroundResource(R.drawable.ic_baseline_favorite_24_selected);
                    ((MainActivity)getActivity()).emotion_like = true;
                }
                else {
                    bt_like2.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    ((MainActivity)getActivity()).emotion_like = false;
                }
                postPlaylistAdd();

            }
        });
        play_butt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),YouTubePlayerFrag.class);
                intent.putExtra("data","play");
                intent.putExtra("youtube", ((MainActivity)getActivity()).youtube2);
                startActivityForResult(intent,2);
            }
        });

        return view;
    }

    //CFR API
    class getAPI extends Thread{
        @Override
        public void run(){
            StringBuffer reqStr = new StringBuffer();
            String clientId = "";
            String clientSecret = "";

            try {
                String paramName = "image";
                String imgFile = currentPhotoPath;
                File uploadFile = new File(imgFile);
                String apiURL = "https://naveropenapi.apigw.ntruss.com/vision/v1/face";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                String boundary = "---" + System.currentTimeMillis() + "---";
                con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
                con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
                OutputStream outputStream = con.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);
                String LINE_FEED = "\r\n";

                String fileName = uploadFile.getName();
                writer.append("--" + boundary).append(LINE_FEED);
                writer.append("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
                writer.append("Content-Type: "  + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
                writer.append(LINE_FEED);
                writer.flush();
                FileInputStream inputStream = new FileInputStream(uploadFile);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                inputStream.close();
                writer.append(LINE_FEED).flush();
                writer.append("--" + boundary + "--").append(LINE_FEED);
                writer.close();
                BufferedReader br = null;
                int responseCode = con.getResponseCode();
                if(responseCode==200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    System.out.println("error!!!!!!! responseCode= " + responseCode);
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                }
                String inputLine;
                if(br != null) {
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    feelingId = getfeelingId(response);
                    ((MainActivity)getActivity()).camera_clicked = true;
                    System.out.println(response.toString());
                } else {
                    System.out.println("error !!!");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    //촬영한 사진에서 feelingId 얻어오기
    int getfeelingId(StringBuffer res){
        int feelingId = -1;
        try {
            JSONObject response = new JSONObject(res.toString());
            JSONObject face_array = (JSONObject) response.getJSONArray("faces").get(0);
            JSONObject emotion_object = face_array.getJSONObject("emotion");
            String feeling = emotion_object.getString("value");

            switch(feeling){
                case "laugh": case "surprise": feelingId = 1; break;
                case "smile": feelingId = 2; break;
                case "neutral": case "talking": feelingId = 3; break;
                case "sad": case "fear": feelingId = 4; break;
                case "angry": case "disgust": feelingId = 5; break;
                default: break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("feelingId2:", String.valueOf(feelingId));
        return feelingId;
    }

    //카메라 권한 요청
    void requestCamera(){
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }
    }

    //사진촬영
    public void captureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getActivity().getPackageManager())!=null){
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(imageFile!=null){
                Uri imageUri = FileProvider.getUriForFile(getActivity(),"com.example.android.provider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(cameraIntent,IMAGE_REQUEST);
            }
        }
    }

    //사진촬영 이후 작업
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST) {
            File f = new File(currentPhotoPath);
            if(f.exists()) {
                Thread th = new getAPI();
                th.start();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    //좋아요 누른 음악 서버 전송
    public void postPlaylistAdd() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://dev.evertime.shop/playlist/like/"+Integer.toString(musicId);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response postplaylist:", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error postplaylist :", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }

    //감정 기반 음악 추천
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
                                String title = result.getString("musicName");
                                if(title.length()>15) song_title2.setTextSize(18);
                                song_title2.setText(title);
                                song_singer2.setText(result.getString("singer"));
                                String imageURL = result.getString("imageUrl");
                                musicId = result.getInt("musicId");
                                String utube = result.getString("youtubeUrl");
                                int idx = utube.indexOf("=");
                                ((MainActivity)getActivity()).youtube2 = utube.substring(idx + 1);
                                if (imageURL.equals("null") || imageURL.equals(""))
                                    album_image2.setImageResource(R.drawable.ic_baseline_music_note_24);
                                else Glide.with(getActivity()).load(imageURL).placeholder(R.drawable.ic_baseline_music_note_24).
                                        error(R.drawable.ic_baseline_music_note_24).into(album_image2);
                                player_frame2.setBackgroundResource(R.drawable.round3);
                                song_title2.setVisibility(View.VISIBLE);
                                song_singer2.setVisibility(View.VISIBLE);
                                album_image2.setVisibility(View.VISIBLE);
                                play_butt2.setVisibility(View.VISIBLE);
                                bt_like2.setVisibility(View.VISIBLE);
                                addmusic2.setVisibility(View.VISIBLE);
                                nomusic5.setVisibility(View.INVISIBLE);
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
            objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(objectRequest);
        }
    }

    //감정 서버 전송
    public void postEmotionData(final VolleyCallBack callBack) {
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
                        callBack.onSuccess();
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