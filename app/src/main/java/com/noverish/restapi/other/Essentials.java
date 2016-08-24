package com.noverish.restapi.other;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.noverish.restapi.R;

/**
 * Created by Noverish on 2016-08-21.
 */
public class Essentials {
    public static void makeHipassPopup(Context context, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.article_add, null);

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 223, context.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, context.getResources().getDisplayMetrics());

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

        Button button = (Button) popupView.findViewById(R.id.article_add_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        EditText editText = (EditText) popupView.findViewById(R.id.article_add_edit_text);
        editText.setText("오늘의 서울 최고 온도 26.6℃");

        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
}
