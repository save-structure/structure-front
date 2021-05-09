package com.example.osproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EmotionFrag extends Fragment {
    private View view;
    private RadioGroup rg_emotions;
    private RadioButton rb_excited, rb_happy, rb_soso, rb_sad, rb_angry;
    private Button bt_camera;
    private int sel_emotion = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.emotion,container,false);

        rg_emotions = view.findViewById(R.id.rg_emotions);
        rb_excited = view.findViewById(R.id.rb_excited);
        rb_happy = view.findViewById(R.id.rb_happy);
        rb_soso = view.findViewById(R.id.rb_soso);
        rb_sad = view.findViewById(R.id.rb_sad);
        rb_angry = view.findViewById(R.id.rb_angry);
        //bt_camera = view.findViewById(R.id.bt_camera);

        rg_emotions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_excited) sel_emotion = 1;
                else if(checkedId == R.id.rb_happy) sel_emotion = 2;
                else if(checkedId == R.id.rb_soso) sel_emotion = 3;
                else if(checkedId == R.id.rb_sad) sel_emotion = 4;
                else if(checkedId == R.id.rb_angry) sel_emotion = 5;

                //Toast.makeText(getActivity(),Integer.toString(sel_emotion),Toast.LENGTH_SHORT).show();
                //sel_emotion으로 emotion정보 반환
            }
        });

        return view;
    }
}
