package com.example.osproject;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CalendarFrag extends Fragment {
    private View view;
    private CalendarView music_calendar;
    private TextView text_curmonth;
    public int selectedmonth;
    public TextView text_nodata1;
    public LinearLayout total_excited;
    public LinearLayout total_happy;
    public LinearLayout total_soso;
    public LinearLayout total_sad;
    public LinearLayout total_angry;
    public LinearLayout monthly_excited;
    public LinearLayout monthly_happy;
    public LinearLayout monthly_soso;
    public LinearLayout monthly_sad;
    public LinearLayout monthly_angry;

    public double total_excited_value;
    public double total_happy_value;
    public double total_soso_value;
    public double total_sad_value;
    public double total_angry_value;
    public double monthly_excited_value;
    public double monthly_happy_value;
    public double monthly_soso_value;
    public double monthly_sad_value;
    public double monthly_angry_value;
    CalendarFrag(){

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar, container, false);
        text_curmonth = view.findViewById(R.id.text_curmonth);
        music_calendar = view.findViewById(R.id.music_calendar);
        text_nodata1 = view.findViewById(R.id.text_nodata1);
        total_excited = view.findViewById(R.id.total_excited);
        total_happy = view.findViewById(R.id.total_happy);
        total_soso = view.findViewById(R.id.total_soso);
        total_sad = view.findViewById(R.id.total_sad);
        total_angry = view.findViewById(R.id.total_angry);
        monthly_excited = view.findViewById(R.id.monthly_excited);
        monthly_happy = view.findViewById(R.id.monthly_happy);
        monthly_soso = view.findViewById(R.id.monthly_soso);
        monthly_sad = view.findViewById(R.id.monthly_sad);
        monthly_angry = view.findViewById(R.id.monthly_angry);

        selectedmonth = Calendar.DAY_OF_MONTH;
        text_curmonth.setText(selectedmonth+"월");

        getMonthlyData();
        getTotalData();
        setGraph();
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
                getMonthlyData();
                setGraph();
            }
        });

        return view;
    }

    public void getTotalData() {
        String url = "https://dev.evertime.shop/feeling/totalchart";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject result_object = response.getJSONObject("result");
                            total_excited_value = result_object.getDouble("percentage1");
                            total_happy_value = result_object.getDouble("percentage2");
                            total_soso_value = result_object.getDouble("percentage3");
                            total_sad_value = result_object.getDouble("percentage4");
                            total_angry_value = result_object.getDouble("percentage5");
                            Log.e("String Response:", response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    public void getMonthlyData() {
        String url = "https://dev.evertime.shop/feeling/chart/"+Integer.toString(selectedmonth);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject result_object = response.getJSONObject("result");
                            monthly_excited_value = result_object.getDouble("percentage1");
                            monthly_happy_value = result_object.getDouble("percentage2");
                            monthly_soso_value = result_object.getDouble("percentage3");
                            monthly_sad_value = result_object.getDouble("percentage4");
                            monthly_angry_value = result_object.getDouble("percentage5");

                            Log.e("String Response:", response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void setGraph(){
        Resources r = getResources();
        float dip, px;
        float sum = 0.0f;
        //total graph
        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) total_excited.getLayoutParams();
        dip = (float) (350 * total_excited_value);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        param.width = (int) px;
        sum+=px;

        param = (FrameLayout.LayoutParams) total_happy.getLayoutParams();
        dip = (float) (350 * total_happy_value);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        param.width = (int) px;
        param.leftMargin = (int) sum;
        sum+=px;

        param = (FrameLayout.LayoutParams) total_soso.getLayoutParams();
        dip = (float) (350 * total_soso_value);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        param.width = (int) px;
        param.leftMargin = (int) sum;
        sum+=px;

        param = (FrameLayout.LayoutParams) total_sad.getLayoutParams();
        dip = (float) (350 * total_sad_value);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        param.width = (int) px;
        param.leftMargin = (int) sum;
        sum+=px;

        param = (FrameLayout.LayoutParams) total_angry.getLayoutParams();
        dip = (float) (350 * total_angry_value);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        param.width = (int) px;
        param.leftMargin = (int) sum;

        //monthly graph
        sum = 0.0f;
        param = (FrameLayout.LayoutParams) monthly_excited.getLayoutParams();
        dip = (float) (350 * monthly_excited_value);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        param.width = (int)px;
        sum+=px;

        param = (FrameLayout.LayoutParams) monthly_happy.getLayoutParams();
        dip = (float) (350 * monthly_happy_value);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        param.width = (int) px;
        param.leftMargin = (int) sum;
        sum+=px;

        param = (FrameLayout.LayoutParams) monthly_soso.getLayoutParams();
        dip = (float) (350 * monthly_soso_value);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        param.width = (int) px;
        param.leftMargin = (int) sum;
        sum+=px;


        param = (FrameLayout.LayoutParams) monthly_sad.getLayoutParams();
        dip = (float) (350 * monthly_sad_value);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        param.width = (int) px;
        param.leftMargin = (int) sum;
        sum+=px;

        param = (FrameLayout.LayoutParams) monthly_angry.getLayoutParams();
        dip = (float) (350 * monthly_angry_value);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        param.width = (int) px;
        param.leftMargin = (int) sum;
    }
}
