package com.mithridat.nonoconverter.ui.editimage;

import android.content.Context;
import android.graphics.Bitmap;
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
     * Field for setting image bitmap with updating image view sizes
     */
    boolean _isSetSizes = false;

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
        if (_isSetSizes) {
            _fragmentColumns.setCustomImageViewSizes(getWidth(), getHeight());
        }
    }

    /**
     * Set parent for this view
     *
     * @param fragmentColumns - parent
     */
    public void setParent(FragmentColumns fragmentColumns) {
        _fragmentColumns = fragmentColumns;
    }

    /**
     * Method for setting image bitmap without updating image view sizes
     *
     * @param bmp - bitmap image
     */
    public void setImageComp(Bitmap bmp) {
        _isSetSizes = false;
        setImageBitmap(bmp);
    }

    /**
     * Method for setting image bitmap with updating image view sizes
     *
     * @param bmp - bitmap image
     */
    public void setImageSimple(Bitmap bmp) {
        _isSetSizes = true;
        setImageBitmap(bmp);
    }
}
