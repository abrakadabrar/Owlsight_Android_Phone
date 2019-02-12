package com.cryptocenter.andrey.owlsight.utils;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.utils.listeners.OnAlertSelectDateListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class DarkCalDialog extends androidx.fragment.app.DialogFragment {
    public static final String CAMERA = "DarkCalDialog.CAMERA";

    Camera camera;
    CaldroidFragment caldroidFragment;
    CaldroidListener caldroidListener;
    OnAlertSelectDateListener listener;

    public static DarkCalDialog newInstance(Camera camera) {
        DarkCalDialog dialog = new DarkCalDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CAMERA, camera);
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setListener(OnAlertSelectDateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgs();
    }

    private void getArgs() {
        if (getArguments() != null) {
            camera = getArguments().getParcelable(CAMERA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_dark_cal, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true);
        caldroidFragment.setArguments(args);
        caldroidFragment.setThemeResource(R.style.CaldroidDefaultDark);

        Map<Date, Drawable> dates = new HashMap<>();
        final Map<Date, Integer> colors = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final ArrayList<Date> markedDays = new ArrayList<>();
        try {
            for (String date : camera.getFolders()) {
                markedDays.add(format.parse(date));
            }
        } catch (ParseException e) {

        }
        for (int i = 0; i < markedDays.size(); i++) {
            dates.put(markedDays.get(i), getResources().getDrawable(R.drawable.white_background_vector));
        }

        for (int i = 0; i < markedDays.size(); i++) {
            colors.put(markedDays.get(i), (R.color.color_bg_error));
        }
        caldroidFragment.setBackgroundDrawableForDates(dates);
        caldroidFragment.setTextColorForDates(colors);
        createCaldroidListener();
        caldroidFragment.setCaldroidListener(caldroidListener);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.calendar_containter, caldroidFragment)
                .commit();
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }

    private void createCaldroidListener() {
        caldroidListener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                dateSecelcted(date);
            }

            @Override
            public void onCaldroidViewCreated() {
                super.onCaldroidViewCreated();
                caldroidFragment.getWeekdayGridView().setBackground(getResources().getDrawable(R.drawable.week_background));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = 30;
                params.topMargin = 30;
                params.leftMargin = 30;
                params.rightMargin = 30;
                caldroidFragment.getWeekdayGridView().setLayoutParams(params);
            }
        };
    }

    private void dateSecelcted(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        listener.onOkClick(format.format(date));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
