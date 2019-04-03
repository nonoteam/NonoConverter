package com.mithridat.nonoconverter.ui.editimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Class for the drawing of grid
 */
public class Panel extends View {

    /**
     * Width of view
     */
    public int _width;

    /**
     * Height of view
     */
    public int _height;

    /**
     * Start height of view
     */
    public int _startHeight;

    /**
     * Start width of view
     */
    public int _startWidth;

    /**
     * Count of rows
     */
    public int _countRows;

    /**
     * Count of columns
     */
    public int _countColumns;

    /**
     * Paint for this view
     */
    Paint _paint;

    /**
     * Constructor of class
     *
     * @param context Current context
     * @param attrs Attributes
     */
    public Panel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.bringToFront();
        _paint = new Paint();
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(3);
        _paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGrid(canvas);
    }

    /**
     * Set sizes of class fields
     *
     * @param startWidth left x
     * @param startHeight left top y
     * @param width width of the grid in px
     * @param height height of the grid in px
     * @param countRows count rows rows of the grid
     * @param countColumns count columns of the grid
     */
    public void setLengths
    (int startWidth, int startHeight, int width, int height, int countRows, int countColumns) {
        _width = width;
        _height = height;
        _startHeight = startHeight;
        _startWidth = startWidth;
        _countRows = countRows;
        _countColumns = countColumns;
    }

    /**
     * Draw grid over the image
     */
    public void doDraw() {
        this.invalidate();
    }

    /**
     * Draw grid
     *
     * @param canvas I don't what is it but it's necessary
     */
    private void drawGrid(Canvas canvas) {
        int height = _height / _countRows;
        int width = _width / _countColumns;
        for (int i = 0; i <= _width; i += width) {
            canvas.drawLine(_startWidth + i,
                    _startHeight,
                    _startWidth + i,
                    _startHeight + _height,
                    _paint);
        }
        for (int i = 0; i <= _height; i += height) {
            canvas.drawLine(_startWidth,
                    _startHeight + i,
                    _startWidth + _width,
                    _startHeight + i,
                    _paint);
        }
    }
}

