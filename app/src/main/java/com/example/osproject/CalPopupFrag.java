package com.example.osproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CalPopupFrag extends DialogFragment {

    private static final String TAG = "CalPopupFrag";

    private View view;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private TextView receivedDate;
    private Button bt_OK;
    public int year;
    public int month;
    public int day;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar_popuplist,container,false);
        receivedDate = view.findViewById(R.id.receivedDate);
        receivedDate.setText(Integer.toString(year)+"/"+Integer.toString(month)+"/"+Integer.toString(day));
        bt_OK = view.findViewById(R.id.bt_OK);
        bt_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

}
