package com.mithridat.nonoconverter.ui.result;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.mithridat.nonoconverter.backend.nonogram.Field;

/**
 * Class for drawing nonogram.
 */
class NonogramDrawer {

    /**
     * Size of cells in nonogram grid.
     */
    private float _cellSize = 0f;

    /**
     * Position of the top left corner of nonogram.
     */
    private PointF _position;

    /**
     * Brush for painting grid.
     */
    private Paint _gridPainter;

    /**
     * Brush for painting filled cells and background.
     */
    private Paint _cellsPainter;

    /**
     * Rectangles for painting background, grid and filled cells.
     */
    private RectF _backgroundRect, _gridRect, _cellsRect;

    /**
     * Width of the lines in grid.
     */
    private float _linesWidth = 3f;

    /**
     * Indent between round rectangle in filled cell and grid lines.
     */
    private float _cellGap = 0f;

    /**
     * Radius for round rectangles if filled cells.
     */
    private float _radius = 0f;

    NonogramDrawer() {
        _gridPainter = new Paint();
        _cellsPainter = new Paint();
        _backgroundRect = new RectF();
        _gridRect = new RectF();
        _cellsRect = new RectF();
        _position = new PointF(0f, 0f);
        paintersInit();
    }

    /**
     * Set position of the top left corner of nonogram.
     * Used to translate the picture.
     *
     * @param startX - x coordinate
     * @param startY - y coordinate
     */
    void setPosition(float startX, float startY) {
        _position.x = startX;
        _position.y = startY;
        _backgroundRect.offsetTo(startX, startY);
    }

    /**
     * Set size of cells in nonogram grid.
     * Used to scale the picture from the top left corner.
     *
     * @param cellSize - size of cells
     */
    void setCellSize(float cellSize) {
        _cellSize = cellSize;

        _gridRect.top = 0f;
        _gridRect.left = 0f;
        _gridRect.bottom = cellSize;
        _gridRect.right = cellSize;

        /*
        "Magical" constants like '10f' or '8f'
        are the results of the empirical selection.
         */
        _cellGap = _linesWidth / 2f + (_cellSize - _linesWidth) / 8f;
        _cellsRect.top = _cellGap;
        _cellsRect.left = _cellGap;
        _cellsRect.bottom = _cellSize - _cellGap;
        _cellsRect.right = _cellSize - _cellGap;
        _radius = _cellSize / 10f;
    }

    /**
     * Function for drawing nonogram.
     *
     * @param canvas   - canvas received in 'onDraw'
     * @param nonogram - nonogram to draw
     */
    void drawNonogram(Canvas canvas, final Field nonogram) {
        if (canvas == null || nonogram == null) return;

        final int columns = nonogram.getCols();
        final int rows = nonogram.getRows();

        // draw white background
        _backgroundRect.bottom = _position.y + rows * _cellSize;
        _backgroundRect.right = _position.x + columns * _cellSize;
        _cellsPainter.setColor(Color.WHITE);
        canvas.drawRect(_backgroundRect, _cellsPainter);

        //draw grid and filled cells
        _gridRect.offsetTo(_position.x, _position.y);
        _cellsRect.offsetTo(_position.x + _cellGap, _position.y + _cellGap);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                canvas.drawRect(_gridRect, _gridPainter);
                _cellsPainter.setColor(nonogram.getColor(i, j));
                canvas.drawRoundRect(_cellsRect, _radius, _radius, _cellsPainter);
                _gridRect.offset(_cellSize, 0f);
                _cellsRect.offset(_cellSize, 0f);
            }
            _gridRect.offset(0f, _cellSize);
            _cellsRect.offset(0f, _cellSize);
            _gridRect.offsetTo(_position.x, _gridRect.top);
            _cellsRect.offsetTo(_position.x + _cellGap, _cellsRect.top);
        }
    }

    /**
     * Set parameters for painters.
     */
    private void paintersInit() {
        _gridPainter.setStyle(Paint.Style.STROKE);
        _gridPainter.setStrokeWidth(_linesWidth);
        _gridPainter.setColor(Color.BLACK);
        _cellsPainter.setStyle(Paint.Style.FILL);
    }
}
