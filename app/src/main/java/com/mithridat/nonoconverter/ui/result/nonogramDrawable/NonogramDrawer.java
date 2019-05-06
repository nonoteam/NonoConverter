package com.mithridat.nonoconverter.ui.result.nonogramDrawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Class for drawing nonograms.
 */
class NonogramDrawer {

    /**
     * Brush for painting grid.
     */
    static private Paint _gridPainter;

    /**
     * Brush for painting filled cells and background.
     */
    static private Paint _cellsPainter;

    /**
     * Rectangles for painting background, grid and filled cells.
     */
    static private RectF _backgroundRect, _gridRect, _cellsRect;

    /**
     * Width of the lines in grid.
     */
    static private float _linesWidth = 3f;

    static {
        _gridPainter = new Paint();
        _cellsPainter = new Paint();
        _backgroundRect = new RectF();
        _gridRect = new RectF();
        _cellsRect = new RectF();
        paintersInit();
    }

    /**
     * Function for drawing nonogram.
     *
     * @param canvas   - canvas received in 'onDraw'
     * @param nonogram - nonogram to draw
     */
    static void drawNonogram(Canvas canvas, final INonogramDrawable nonogram) {
        if (canvas == null || nonogram == null) return;

        final int columns = nonogram.getColumns();
        final int rows = nonogram.getRows();
        final float startX = nonogram.getTopLeftX();
        final float startY = nonogram.getTopLeftY();
        final float cellSize = nonogram.getCellSize();
        final float cellGap = _linesWidth / 2f + (cellSize - _linesWidth) / 8f;
        final float radius = cellSize / 10f;

        // draw white background
        _backgroundRect.top = startY;
        _backgroundRect.left = startX;
        _backgroundRect.bottom = startY + nonogram.getHeight();
        _backgroundRect.right = startX + nonogram.getWidth();
        _cellsPainter.setColor(Color.WHITE);
        canvas.drawRect(_backgroundRect, _cellsPainter);

        //draw grid and filled cells
        _gridRect.top = startY;
        _gridRect.left = startX;
        _gridRect.bottom = startY + cellSize;
        _gridRect.right = startX + cellSize;
        _cellsRect.top = startY + cellGap;
        _cellsRect.left = startX + cellGap;
        _cellsRect.bottom = startY + cellSize - cellGap;
        _cellsRect.right = startX + cellSize - cellGap;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                canvas.drawRect(_gridRect, _gridPainter);
                _cellsPainter.setColor(nonogram.getColor(i, j));
                canvas.drawRoundRect(_cellsRect, radius, radius, _cellsPainter);
                _gridRect.offset(cellSize, 0f);
                _cellsRect.offset(cellSize, 0f);
            }
            _gridRect.offset(0f, cellSize);
            _cellsRect.offset(0f, cellSize);
            _gridRect.offsetTo(startX, _gridRect.top);
            _cellsRect.offsetTo(startX + cellGap, _cellsRect.top);
        }
    }

    /**
     * Set parameters for painters.
     */
    private static void paintersInit() {
        _gridPainter.setStyle(Paint.Style.STROKE);
        _gridPainter.setStrokeWidth(_linesWidth);
        _gridPainter.setColor(Color.BLACK);
        _cellsPainter.setStyle(Paint.Style.FILL);
    }
}
