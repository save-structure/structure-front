package com.example.osproject;

import android.app.AlertDialog;
import android.icu.util.Calendar;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarFrag extends Fragment {
    private View view;
    private CalendarView music_calendar;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button bt_OK;
    private TextView text_curmonth;
    private int selectedmonth;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar, container, false);
        text_curmonth = view.findViewById(R.id.text_curmonth);
        music_calendar = view.findViewById(R.id.music_calendar);

        selectedmonth = Calendar.DAY_OF_MONTH;
        text_curmonth.setText(selectedmonth+"월");

        music_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedmonth = month + 1;
                text_curmonth.setText(selectedmonth+"월");

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
