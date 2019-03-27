package com.mithridat.nonoconverter.ui.editimage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Class for the drawing of grid
 */
public class Panel extends SurfaceView implements SurfaceHolder.Callback {

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
    public int _start_height;

    /**
     * Start width of view
     */
    public int _start_width;

    /**
     * Count of rows
     */
    public int _count_rows;

    /**
     * Count of columns
     */
    public int _count_columns;

    /**
     * Thread for Panel
     */
    public DrawThread _drawThread;

    /**
     * Constructor of class
     *
     * @param context Current context
     * @param attrs Attributes
     */
    public Panel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setZOrderOnTop(true); //necessary
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        getHolder().addCallback(this);

    }

    /**
     * Set sizes of class fields
     *
     * @param start_height left top y
     * @param width width of the grid in px
     * @param height height of the grid in px
     * @param count_rows count rows rows of the grid
     * @param count_columns count columns of the grid
     */
    public void setLengths(int start_width, int start_height, int width, int height, int count_rows, int count_columns)
    {
        _width = width;
        _height = height;
        _start_height = start_height;
        _start_width = start_width;
        _count_rows = count_rows;
        _count_columns = count_columns;
    }

    /**
     * Draw grid over the image
     *
     * @param start_height left top y
     * @param width width of the grid in px
     * @param height height of the grid in px
     * @param count_rows count rows rows of the grid
     * @param count_columns count columns of the grid
     */
    public void doDraw(int start_width, int start_height, int width, int height, int count_rows, int count_columns) {
        _drawThread.setRunning(false);
        _drawThread._width = width;
        _drawThread._height = height;
        _drawThread._start_height = start_height;
        _drawThread._start_width = start_width;
        _drawThread._count_rows = count_rows;
        _drawThread._count_columns = count_columns;
        _drawThread.setRunning(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        _drawThread = new DrawThread(getHolder());
        _drawThread._width = _width;
        _drawThread._height = _height;
        _drawThread._start_height = _start_height;
        _drawThread._start_width = _start_width;
        _drawThread._count_rows = _count_rows;
        _drawThread._count_columns = _count_columns;
        _drawThread.setRunning(true);
        _drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        _drawThread.setRunning(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}

