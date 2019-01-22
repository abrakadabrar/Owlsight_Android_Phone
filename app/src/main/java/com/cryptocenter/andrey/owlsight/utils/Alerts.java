package com.cryptocenter.andrey.owlsight.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.utils.listeners.OnAlertAddGroupListener;
import com.cryptocenter.andrey.owlsight.utils.listeners.OnAlertEditGroupListener;
import com.cryptocenter.andrey.owlsight.utils.listeners.OnAlertMonitorsListener;
import com.cryptocenter.andrey.owlsight.utils.listeners.OnAlertSelectDateListener;
import com.cryptocenter.andrey.owlsight.utils.listeners.OnAlertWarningDeleteListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import cn.aigestudio.datepicker.bizs.calendars.DPCManager;
import cn.aigestudio.datepicker.bizs.decors.DPDecor;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

import static android.widget.LinearLayout.LayoutParams.MATCH_PARENT;

public class Alerts {

    public static KProgressHUD loader(Context context) {
        return KProgressHUD.create(Objects.requireNonNull(context))
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
    }

    public static void showGroupAddDialog(Context context, OnAlertAddGroupListener listener) {
        final View view = LayoutInflater.from(context).inflate(R.layout.view_add_group, null);
        final EditText userInput = view.findViewById(R.id.input_text_add_group);

        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppCompatAlertDialogStyleAddGroup))
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, id) -> listener.onOkClick(userInput.getText().toString()))
                .setNegativeButton("cancel", (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    public static void showGroupEditDialog(Context context, String title, OnAlertEditGroupListener listener) {
        final EditText input = new EditText(context);
        input.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        input.setText(title);
        input.setSelection(title.length());
        new AlertDialog.Builder(context)
                .setView(input)
                .setCancelable(true)
                .setTitle("Редактирование группы: ")
                .setPositiveButton("Ок", (dialog, which) -> listener.onOkClick(input.getText().toString()))
                .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel())
                .show();
    }

    public static void showAlertWarningDeleteDialog(Context context, OnAlertWarningDeleteListener listener) {
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("Удалить группу? ")
                .setIcon(R.drawable.ic_add_group_icon)
                .setPositiveButton("Удалить", (dialog, which) -> listener.onOkClick())
                .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel())
                .show();
    }

    public static void showAlertCalendar(Context context, Camera camera, OnAlertSelectDateListener listener) {
        final List<String> tmp = camera.getFolders();
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        int curMonth = Calendar.getInstance().get(Calendar.MONTH);
        for (int i = 0; tmp.size() > i; i++) {
            String year = tmp.get(i).substring(0, 4);
            String month = tmp.get(i).substring(5, 7);
            String day = tmp.get(i).substring(8, 10);
            String newDay;
            String newMonth;

            if (day.substring(0, 1).equals("0")) {
                newDay = day.substring(1, 2);
            } else {
                newDay = day;
            }
            if (month.substring(0, 1).equals("0")) {
                newMonth = month.substring(1, 2);
            } else {
                newMonth = month;
            }
            tmp.set(i, year + "-" + newMonth + "-" + newDay);
        }

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();

        final DatePicker picker = new DatePicker(context);
        picker.setDate(curYear, curMonth + 1);
        picker.setMode(DPMode.SINGLE);
        DPCManager.getInstance().setDecorBG(tmp);
        picker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(Color.RED);
                canvas.drawCircle(rect.centerX(), rect.centerY() - 5f, rect.width() / 2F - 5f, paint);
            }
        });
        picker.setOnDatePickedListener(date -> {
            String[] dates = date.split("-");
            if (dates[2].length() == 1) {
                dates[2] = String.format("0%s", dates[2]);
            }

            if (dates[1].length() == 1) {
                dates[1] = String.format("0%s", dates[1]);
            }

            listener.onOkClick(String.format("%s-%s-%s", dates[0], dates[1], dates[2]));
            Toast.makeText(context, date, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.getWindow().setContentView(picker, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    public static void showDarkCalendar(Camera camera, OnAlertSelectDateListener listener, FragmentManager fragmentManager) {
        final DarkCalDialog darkCalDialog = DarkCalDialog.newInstance(camera);
        darkCalDialog.setListener(listener);
        darkCalDialog.show(fragmentManager,DarkCalDialog.class.getSimpleName());

    }

    public static void showAlertMonitors(Context context, List<Monitor> monitors, OnAlertMonitorsListener monitorsListener) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item);
        for (Monitor monitor : monitors) {
            arrayAdapter.add(String.format("%s (%s)", monitor.getViewName(), String.valueOf(monitor.getCams().size())));
        }

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.header))
                .setNegativeButton(R.string.alert_cancel, (dialog, which) -> dialog.dismiss())
                .setAdapter(arrayAdapter, (dialog, which) -> monitorsListener.onOkClick(monitors.get(which)))
                .show();
    }

}
