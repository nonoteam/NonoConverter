package com.mithridat.nonoconverter.ui.result;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;

import com.mithridat.nonoconverter.backend.Field;

public class NonogramDrawThread extends Thread {

    /**
     * Width of the lines in nonorgam grid
     */
    public final int LINES_WIDTH = 3;

    /**
     * flag, marking if thread is now running
     */
    private boolean _isRunning = false;

    /**
     * surface holder
     */
    private SurfaceHolder _surfaceHolder;

    /**
     * brush, containing painting parameters
     */
    private Paint _painter;

    /**
     * background image for surface view
     */
    private Drawable _background = null;

    /**
     * nonogram as Field backend class
     */
    private Field _nonogram = null;

    /**
     * size of cells in screen coordinates
     */
    private int _cellSize = 0;

    public NonogramDrawThread(SurfaceHolder surfaceHolder) {
        _surfaceHolder = surfaceHolder;
        _painter = new Paint();
    }

    @Override
    public void run() {
        Canvas canvas;
        setPainterParams();
        _isRunning = true;

        while (_isRunning) {
            canvas = _surfaceHolder.lockCanvas(null);
            if (canvas != null) {
                canvas.drawColor(Color.WHITE);
                drawNng(canvas);
                _surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * Stop the tread
     */
    public void shutdown() {
        _isRunning = false;
    }

    /**
     * Set nonogram
     *
     * @param nonogramField nonogram as Field backend class
     */
    public void setNonogramField(Field nonogramField) {
        _nonogram = nonogramField;
    }

    /**
     * Set size of the cells in nonogram
     *
     * @param size size of cells in screen coordinates
     */
    public void setCellSize(int size) {
        _cellSize = size;
    }

    /**
     * Set background image for surface view
     *
     * @param background drawable object for background
     */
    public void setBackground(Drawable background) {
        _background = background;
    }

    /**
     * Set lines width, style and color for painter
     */
    private void setPainterParams() {
        _painter.setStyle(Paint.Style.FILL);
        _painter.setStrokeWidth(LINES_WIDTH);
        _painter.setColor(Color.BLACK);
    }

    /**
     * Function for drawing nonogram
     *
     * @param canvas canvas received by surface holder
     */
    private void drawNng(Canvas canvas) {
        if (_nonogram == null)
            return;

        int columns = _nonogram.getCols();
        int rows = _nonogram.getRows();
        int cellGap = _cellSize / 12 + 2;
        int startX = LINES_WIDTH;
        int startY = LINES_WIDTH;
        int stopX = startX + _cellSize * columns;
        int stopY = startY + _cellSize * rows;
        int radius = cellGap;
        _painter.setColor(Color.BLACK);

        //background, if present
        if (_background != null)
            _background.draw(canvas);

        // horizontal lines
        for (int i = 0; i <= rows; i++) {
            canvas.drawLine(startX,
                    startY + i * _cellSize,
                    stopX,
                    startY + i * _cellSize,
                    _painter);
        }

        //vertical lines
        for (int i = 0; i <= columns; i++) {
            canvas.drawLine(startX + i * _cellSize,
                    startY,
                    startX + i * _cellSize,
                    stopY,
                    _painter);
        }

        //filled cells
        RectF rect = new RectF();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                rect.left = startX + j * _cellSize + cellGap;
                rect.top = startY + i * _cellSize + cellGap;
                rect.right = rect.left + _cellSize - 2 * cellGap;
                rect.bottom = rect.top + _cellSize - 2 * cellGap;
                _painter.setColor(_nonogram.getColor(i, j));
                canvas.drawRoundRect(rect, radius, radius, _painter);
            }
        }
    }
}
