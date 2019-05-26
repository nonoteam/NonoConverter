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
    private float _width;

    /**
     * Height of view
     */
    private float _height;

    /**
     * Start height of view
     */
    private float _startHeight;

    /**
     * Start width of view
     */
    private float _startWidth;

    /**
     * Count of rows
     */
    private int _countRows;

    /**
     * Count of columns
     */
    private int _countColumns;

    /**
     * Paint for this view
     */
    private Paint _paint;

    /**
     * Constructor of class
     *
     * @param context - current context
     * @param attrs - attributes
     */
    public Panel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.TRANSPARENT);
        bringToFront();
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
     * @param startWidth - left x
     * @param startHeight - left top y
     * @param width - width of the grid in px
     * @param height - height of the grid in px
     * @param countRows - count rows rows of the grid
     * @param countColumns - count columns of the grid
     */
    void setLengths(
            float startWidth,
            float startHeight,
            float width,
            float height,
            int countRows,
            int countColumns) {
        _width = width;
        _height = height;
        _startHeight = startHeight;
        _startWidth = startWidth;
        _countRows = countRows;
        _countColumns = countColumns;
    }

    /**
     * Set sizes of drawing grid
     *
     * @param countRows - count rows rows of the grid
     * @param countColumns - count columns of the grid
     */
    void setGridSizes(int countRows, int countColumns) {
        _countRows = countRows;
        _countColumns = countColumns;
    }

    /**
     * Draw grid
     *
     * @param canvas - canvas received in 'onDraw'
     */
    private void drawGrid(Canvas canvas) {
        if (_countColumns == 0) return;
        float width = _width / _countColumns;
        float height = _height / _countRows;
        for (float i = 0; i < _width; i += width) {
            canvas.drawLine(_startWidth + i,
                    _startHeight,
                    _startWidth + i,
                    _startHeight + _height,
                    _paint);
        }
        for (float i = 0; i < _height; i += height) {
            canvas.drawLine(_startWidth,
                    _startHeight + i,
                    _startWidth + _width,
                    _startHeight + i,
                    _paint);
        }
        canvas.drawLine(_startWidth + _width,
                _startHeight,
                _startWidth + _width,
                _startHeight + _height,
                _paint);
        canvas.drawLine(_startWidth,
                _startHeight + _height,
                _startWidth + _width,
                _startHeight + _height,
                _paint);
    }
}

