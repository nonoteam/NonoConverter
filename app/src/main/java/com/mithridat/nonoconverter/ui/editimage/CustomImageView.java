package com.mithridat.nonoconverter.ui.editimage;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;

import android.graphics.Canvas;
import android.util.AttributeSet;

public class CustomImageView extends AppCompatImageView {
    FragmentColumns _fragmentColumns;
    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        _fragmentColumns.setCustomImageViewSizes(getWidth(), getHeight());
    }

    public void setParent( FragmentColumns fragmentColumns) {
        _fragmentColumns = fragmentColumns;
    }
}