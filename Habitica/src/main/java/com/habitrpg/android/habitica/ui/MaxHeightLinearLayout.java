package com.habitrpg.android.habitica.ui;


import com.habitrpg.android.habitica.HabiticaApplication;
import com.habitrpg.android.habitica.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

public class MaxHeightLinearLayout extends LinearLayout {

    private final float defaultHeight = 0.9f;
    private float maxHeight;

    public MaxHeightLinearLayout(Context context) {
        super(context);
    }

    public MaxHeightLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public MaxHeightLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaxHeightLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
            //200 is a defualt value
            maxHeight = styledAttrs.getFloat(R.styleable.MaxHeightScrollView_maxHeightMultiplier, defaultHeight);

            styledAttrs.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (HabiticaApplication.currentActivity != null) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            HabiticaApplication.currentActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = (int) ((int) displaymetrics.heightPixels * maxHeight);

            heightMeasureSpec = Math.min(heightMeasureSpec, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST));
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}