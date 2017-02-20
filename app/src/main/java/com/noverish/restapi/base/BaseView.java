package com.noverish.restapi.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Noverish on 2017-02-21.
 */

public class BaseView extends FrameLayout {
    protected Context context;

    public BaseView(Context context) {
        super(context);
        this.context = context;
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
}
