package com.cryptocenter.andrey.owlsight.ui.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cryptocenter.andrey.owlsight.R;

public class InitFingerPrintDialog extends AlertDialog {
    private TextView etCancel;

    public InitFingerPrintDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        setCancelable(false);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_init_fingerprint, null, false);
        etCancel = view.findViewById(R.id.tv_dialog_fingerprint_cancel);
        etCancel.setOnClickListener(v -> dismiss());
        setView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
