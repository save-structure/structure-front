package com.example.osproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalendarFrag extends Fragment {
    private View view;
    private CalendarView music_calendar;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button bt_OK;
    private TextView receivedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar, container, false);

        music_calendar = view.findViewById(R.id.music_calendar);
        music_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                CalPopupFrag dialog = new CalPopupFrag();
                dialog.year = year;
                dialog.month = month + 1;
                dialog.day = dayOfMonth;
                dialog.show(getFragmentManager(), "CalPopupFrag");
            }
        });

        return view;
    }
}
