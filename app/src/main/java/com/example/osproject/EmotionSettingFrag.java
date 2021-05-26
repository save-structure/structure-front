package com.example.osproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class EmotionSettingFrag extends DialogFragment {

    private static final String TAG = "EmotionSettingFrag";

    private View view;
    private Button bt_OK2;

    private RadioGroup rg_type1;
    private RadioButton rb_type1_1, rb_type1_2;
    private RadioGroup rg_type2;
    private RadioButton rb_type2_1, rb_type2_2;

    private Integer type1, type2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.emotion_setting, container, false);

        rg_type1 = view.findViewById(R.id.rg_type1);
        rg_type2 = view.findViewById(R.id.rg_type2);
        rb_type1_1 = view.findViewById(R.id.rb_type1_1);
        rb_type1_2 = view.findViewById(R.id.rb_type1_2);
        rb_type2_1 = view.findViewById(R.id.rb_type2_1);
        rb_type2_2 = view.findViewById(R.id.rb_type2_2);

        type1 = ((MainActivity) getActivity()).type1;
        type2 = ((MainActivity) getActivity()).type2;

        if (type1 == 1) rg_type1.check(rb_type1_1.getId());
        else rg_type1.check(rb_type1_2.getId());
        if (type2 == 1) rg_type2.check(rb_type2_1.getId());
        else rg_type2.check(rb_type2_2.getId());


        rg_type1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_type1_1) type1 = 1;
                else if (checkedId == R.id.rb_type1_2) type1 = 2;
            }
        });
        rg_type2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_type2_1) type2 = 1;
                else if (checkedId == R.id.rb_type2_2) type2 = 2;
            }
        });

        bt_OK2 = view.findViewById(R.id.bt_OK2);
        bt_OK2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).type1 = type1;
                ((MainActivity) getActivity()).type2 = type2;
                postSettingData();

                getDialog().dismiss();
            }
        });

        return view;
    }

    public void postSettingData() {
        String url = "https://dev.evertime.shop/user/"+Integer.toString(type1)+"/"+Integer.toString(type2);
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