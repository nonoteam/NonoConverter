package com.mithridat.nonoconverter.ui.editimage;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;

import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * class for Image View
 */
public class CustomImageView extends AppCompatImageView {

    /**
     * Parent of CustomImageView
     */
    FragmentColumns _fragmentColumns;

    /**
     * Constructor for CustomImageView
     *
     * @param context - current context
     */
    public CustomImageView(Context context) {
        super(context);
    }

    /**
     * Constructor for CustomImageView
     *
     * @param context - current context
     * @param attrs - current attributes
     */
    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor for CustomImageView
     *
     * @param context - current context
     * @param attrs - current attributes
     * @param defStyleAttr - current def style
     */
    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        _fragmentColumns.setCustomImageViewSizes(getWidth(), getHeight());
    }

    /**
     * Set parent for this view
     *
     * @param fragmentColumns - parent
     */
    public void setParent(FragmentColumns fragmentColumns) {
        _fragmentColumns = fragmentColumns;
    }
}
