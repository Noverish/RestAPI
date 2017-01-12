package com.noverish.restapi.other;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Noverish on 2017-01-12.
 */

public class GlobalProgressDialog extends ProgressDialog {
    private static ProgressDialog dialog;

    private GlobalProgressDialog(Context context) {
        super(context);
    }

    public static void showDialog(Context context) {
        if(dialog != null)
            dialog.dismiss();
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("로딩중입니다.");
        dialog.show();
    }

    public static void dismissDialog() {
        if(dialog != null)
            dialog.dismiss();
        dialog = null;
    }
}
