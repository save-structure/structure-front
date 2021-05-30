package com.example.osproject;

import android.app.AlertDialog;
import android.content.Context;
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
import androidx.core.content.ContextCompat;
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

    private static final String TAG = "CalendarFrag";

    private View view;
    private CalendarView music_calendar;
    private TextView text_curmonth;
    public int selectedmonth;
    public TextView text_nodata1;
    public View total_excited;
    public View total_happy;
    public View total_soso;
    public View total_sad;
    public View total_angry;
    public View monthly_excited;
    public View monthly_happy;
    public View monthly_soso;
    public View monthly_sad;
    public View monthly_angry;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar, container, false);
        text_curmonth = view.findViewById(R.id.text_curmonth);
        music_calendar = view.findViewById(R.id.music_calendar);
        text_nodata1 = view.findViewById(R.id.text_nodata1);

        selectedmonth = Calendar.DAY_OF_MONTH;
        text_curmonth.setText(selectedmonth+"월");

        getTotalData(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                setTotalGraph();
            }
        });
        getMonthlyData(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                setMonthlyGraph();
            }
        });

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
                getMonthlyData(new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        setMonthlyGraph();
                    }
                });
            }
        });

        return view;
    }

    public void getMonthlyData(final VolleyCallBack callBack) {
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
                            Log.e("String Response:", response.toString());
                            JSONObject result_object = response.getJSONObject("result");
                            monthly_excited_value = result_object.getDouble("percentage1");
                            monthly_happy_value = result_object.getDouble("percentage2");
                            monthly_soso_value = result_object.getDouble("percentage3");
                            monthly_sad_value = result_object.getDouble("percentage4");
                            monthly_angry_value = result_object.getDouble("percentage5");
                            callBack.onSuccess();
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

    public void getTotalData(final VolleyCallBack callBack) {
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
                            Log.e("String Response:", response.toString());
                            JSONObject result_object = response.getJSONObject("result");
                            total_excited_value = result_object.getDouble("percentage1");
                            total_happy_value = result_object.getDouble("percentage2");
                            total_soso_value = result_object.getDouble("percentage3");
                            total_sad_value = result_object.getDouble("percentage4");
                            total_angry_value = result_object.getDouble("percentage5");
                            callBack.onSuccess();
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
    public void setTotalGraph(){
        int margin = 0;
        total_excited = view.findViewById(R.id.total_excited);
        total_happy = view.findViewById(R.id.total_happy);
        total_soso = view.findViewById(R.id.total_soso);
        total_sad = view.findViewById(R.id.total_sad);
        total_angry = view.findViewById(R.id.total_angry);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) total_excited.getLayoutParams();
        layoutParams.width = pixelTodp((int)(350*total_excited_value));
        margin += layoutParams.width;
        total_excited.setLayoutParams(layoutParams);

        layoutParams = (FrameLayout.LayoutParams) total_happy.getLayoutParams();
        layoutParams.width = pixelTodp((int)(350*total_happy_value));
        layoutParams.leftMargin = margin;
        margin += layoutParams.width;
        total_happy.setLayoutParams(layoutParams);

        layoutParams = (FrameLayout.LayoutParams) total_soso.getLayoutParams();
        layoutParams.width = pixelTodp((int)(350*total_soso_value));
        layoutParams.leftMargin = margin;
        margin += layoutParams.width;
        total_soso.setLayoutParams(layoutParams);

        layoutParams = (FrameLayout.LayoutParams) total_sad.getLayoutParams();
        layoutParams.width = pixelTodp((int)(350*total_sad_value));
        layoutParams.leftMargin = margin;
        margin += layoutParams.width;
        total_sad.setLayoutParams(layoutParams);

        layoutParams = (FrameLayout.LayoutParams) total_angry.getLayoutParams();
        layoutParams.width = pixelTodp((int)(350*total_angry_value));
        layoutParams.leftMargin = margin;
        margin += layoutParams.width;
        total_angry.setLayoutParams(layoutParams);

    }


    public void setMonthlyGraph(){
        int margin = 0;
        monthly_excited = view.findViewById(R.id.monthly_excited);
        monthly_happy = view.findViewById(R.id.monthly_happy);
        monthly_soso = view.findViewById(R.id.monthly_soso);
        monthly_sad = view.findViewById(R.id.monthly_sad);
        monthly_angry = view.findViewById(R.id.monthly_angry);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) monthly_excited.getLayoutParams();
        layoutParams.width = pixelTodp((int)(350*monthly_excited_value));
        margin += layoutParams.width;
        monthly_excited.setLayoutParams(layoutParams);

        layoutParams = (FrameLayout.LayoutParams) monthly_happy.getLayoutParams();
        layoutParams.width = pixelTodp((int)(350*monthly_happy_value));
        layoutParams.leftMargin = margin;
        margin += layoutParams.width;
        monthly_happy.setLayoutParams(layoutParams);

        layoutParams = (FrameLayout.LayoutParams) monthly_soso.getLayoutParams();
        layoutParams.width = pixelTodp((int)(350*monthly_soso_value));
        layoutParams.leftMargin = margin;
        margin += layoutParams.width;
        monthly_soso.setLayoutParams(layoutParams);

        layoutParams = (FrameLayout.LayoutParams) monthly_sad.getLayoutParams();
        layoutParams.width = pixelTodp((int)(350*monthly_sad_value));
        layoutParams.leftMargin = margin;
        margin += layoutParams.width;
        monthly_sad.setLayoutParams(layoutParams);

        layoutParams = (FrameLayout.LayoutParams) monthly_angry.getLayoutParams();
        layoutParams.width = pixelTodp((int)(350*monthly_angry_value));
        layoutParams.leftMargin = margin;
        margin += layoutParams.width;
        monthly_angry.setLayoutParams(layoutParams);

    }
    int pixelTodp(int px){
        return (int)(px*view.getResources().getDisplayMetrics().density);
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
