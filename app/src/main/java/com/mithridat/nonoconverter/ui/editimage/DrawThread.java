package com.mithridat.nonoconverter.ui.editimage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Class for the thread of Panel.
 */
class DrawThread extends Thread {
    /**
     * Width of the drawable grid
     */
    public int _width = 720;

    /**
     * Height of the drawable grid
     */
    public int _height = 576;

    /**
     * Start height of drawable grid
     */
    public int _start_height = 0;

    /**
     * Start width of drawable grid
     */
    public int _start_width = 0;

    /**
     * Rows count
     */
    public int _count_rows = 5;

    /**
     * Coolumns count
     */
    public int _count_columns = 6;

    /**
     * Is approve running
     */
    private boolean _running = false;

    /**
     * Surface holder of Panel
      */
    private SurfaceHolder _surfaceHolder;

    /**
     * Constructor of DrawThread
     *
     * @param surfaceHolder Surface holder of Panel
     */
    public DrawThread(SurfaceHolder surfaceHolder) {
        this._surfaceHolder = surfaceHolder;
    }

    /**
     * Begin running
     *
     * @param running Is approve running
     */
    public void setRunning(boolean running) {
        this._running = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (_running) {
            canvas = null;
            try {
                canvas = _surfaceHolder.lockCanvas(null);
                if (canvas == null)
                {
                    continue;
                }

                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                drawGrid(canvas);
            } finally {
                if (canvas != null) {
                    _surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    /**
     * Draw grid
     *
     * @param canvas I don't what is it but it's necessary
     */
    private void drawGrid(Canvas canvas) {
        Paint p;
        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3);
        p.setColor(Color.RED);
        int h = _height / _count_rows;
        int w = _width / _count_columns;
        for (int i = 0; i <= _width; i += w) {
            canvas.drawLine(
                            _start_width + i,
                            _start_height,
                            _start_width + i,
                            _start_height + _height,
                            p);
        }
        for (int i = 0; i <= _height; i += h) {
            canvas.drawLine(
                    _start_width,
                    _start_height + i,
                    _start_width + _width,
                    _start_height + i,
                    p);
        }

    }
}
