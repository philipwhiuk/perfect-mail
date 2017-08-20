package com.whiuk.philip.perfect.ui.dialog;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.whiuk.philip.perfect.R;


public class ApgDeprecationWarningDialog extends AlertDialog {
    public ApgDeprecationWarningDialog(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);

        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.dialog_apg_deprecated, null);

        setIcon(R.drawable.ic_apg_small);
        setTitle(R.string.apg_deprecated_title);
        setView(contentView);
        setButton(Dialog.BUTTON_NEUTRAL, context.getString(R.string.apg_deprecated_ok), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancel();
            }
        });
    }
}
