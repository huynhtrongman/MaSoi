package com.huynhtrongman.masoi.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DiaLog extends AlertDialog.Builder {
    // Đăng Xuất
    public DiaLog(Context context, String mess) {
        super(context);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(mess);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });

        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.create();
        alert.show();

    }
}
