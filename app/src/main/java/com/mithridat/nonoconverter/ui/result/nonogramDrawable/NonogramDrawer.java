package com.mithridat.nonoconverter.ui.result.nonogramDrawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import static com.mithridat.nonoconverter.Utils.getHeightText;
import static com.mithridat.nonoconverter.Utils.getWidthText;

/**
 * Class for drawing nonograms.
 */
class NonogramDrawer {

    /**
     * Brush for painting grid.
     */
    private static Paint _gridPainter;

    /**
     * Brush for painting filled cells and background.
     */
    private static Paint _cellsPainter;

    /**
     * Brush for painting numbers.
     */
    private static Paint _numbersPainter;

    /**
     * Rectangles for painting background, grid and filled cells.
     */
    private static RectF _backgroundRect, _gridRect, _cellsRect;

    /**
     * Width of the lines in grid.
     */
    private static float _linesWidth = 3f;

    static {
        _gridPainter = new Paint();
        _cellsPainter = new Paint();
        _numbersPainter = new Paint();
        _backgroundRect = new RectF();
        _gridRect = new RectF();
        _cellsRect = new RectF();
        paintersInit();
    }

    /**
     * Function for drawing extended nonogram with main, top and left fields.
     *
     * @param canvas   - canvas received in 'onDraw'
     * @param nonogram - nonogram to draw
     */
    static void drawNonogram(Canvas canvas, final INonogramExtended nonogram) {
        if (canvas == null || nonogram == null) return;
        final float cellSize = nonogram.getCellSize();
        final int maxNumber = nonogram.getMaxNumber();
        float font = cellSize / Integer.toString(maxNumber).length();
        _numbersPainter.setTextSize(font);
        drawLeftField(canvas, nonogram);
        drawTopField(canvas, nonogram);
        drawNonogram(canvas, (INonogram) nonogram);
    }

    /**
     * Function for drawing main field of nonogram.
     *
     * @param canvas   - canvas received in 'onDraw'
     * @param nonogram - nonogram to draw
     */
    static void drawNonogram(Canvas canvas, final INonogram nonogram) {
        if (canvas == null || nonogram == null) return;

        /*
        "Magical" constants like '10f' or '8f'
        are the results of the empirical selection.
         */
        final int columns = nonogram.getColumns();
        final int rows = nonogram.getRows();
        final float startX = nonogram.getMainFieldTopLeftX();
        final float startY = nonogram.getMainFieldTopLeftY();
        final float cellSize = nonogram.getCellSize();
        final float cellGap = _linesWidth / 2f + (cellSize - _linesWidth) / 8f;
        final float radius = cellSize / 10f;

        // draw white background
        _backgroundRect.top = startY;
        _backgroundRect.left = startX;
        _backgroundRect.bottom = startY + nonogram.getMainFieldHeight();
        _backgroundRect.right = startX + nonogram.getMainFieldWidth();
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
     * Function for drawing left field of nonogram.
     *
     * @param canvas   - canvas received in 'onDraw'
     * @param nonogram - nonogram to draw
     */
    private static void drawLeftField(
            Canvas canvas,
            final INonogramExtended nonogram) {
        if (canvas == null || nonogram == null) return;

        final float cellSize = nonogram.getCellSize();
        final int rows = nonogram.getRows();
        final int leftFieldColumns = nonogram.getLeftFieldColumns();
        final float startX = nonogram.getMainFieldTopLeftX();
        final float startY = nonogram.getMainFieldTopLeftY();

        String text;
        int length, index;
        int textWidth, textHeight;

        _cellsPainter.setColor(Color.WHITE);
        _backgroundRect.top = startY;
        _backgroundRect.left = startX - nonogram.getLeftFieldWidth();
        _backgroundRect.bottom =
                _backgroundRect.top + nonogram.getLeftFieldHeight();
        _backgroundRect.right =
                _backgroundRect.left + nonogram.getLeftFieldWidth();
        canvas.drawRect(_backgroundRect, _cellsPainter);

        _gridRect.top = _backgroundRect.top;
        _gridRect.left = _backgroundRect.left;
        _gridRect.bottom = _gridRect.top + cellSize;
        _gridRect.right = _gridRect.left + cellSize;
        for (int i = 0; i < rows; i++) {
            length = nonogram.getLeftRowLength(i);
            for (int j = 0; j < leftFieldColumns; j++) {
                canvas.drawRect(_gridRect, _gridPainter);
                if (j >= leftFieldColumns - length) {
                    index = j - leftFieldColumns + length;
                    text = Integer.toString(nonogram.getLeftNumber(i, index));
                    textWidth = getWidthText(_numbersPainter, text);
                    textHeight = getHeightText(_numbersPainter, text);
                    canvas.drawText(text,
                            _gridRect.left + (cellSize - textWidth) / 2,
                            _gridRect.bottom - (cellSize - textHeight) / 2,
                            _numbersPainter);
                }
                _gridRect.offset(cellSize, 0f);
            }
            _gridRect.offset(0f, cellSize);
            _gridRect.offsetTo(_backgroundRect.left, _gridRect.top);
        }
    }

    /**
     * Function for drawing top field of nonogram.
     *
     * @param canvas   - canvas received in 'onDraw'
     * @param nonogram - nonogram to draw
     */
    private static void drawTopField(
            Canvas canvas,
            final INonogramExtended nonogram) {
        if (canvas == null || nonogram == null) return;

        final float cellSize = nonogram.getCellSize();
        final int columns = nonogram.getColumns();
        final int topFieldRows = nonogram.getTopFieldRows();
        final float startX = nonogram.getMainFieldTopLeftX();
        final float startY = nonogram.getMainFieldTopLeftY();

        String text;
        int length, index;
        int textWidth, textHeight;

        _cellsPainter.setColor(Color.WHITE);
        _backgroundRect.top = startY - nonogram.getTopFieldHeight();
        _backgroundRect.left = startX;
        _backgroundRect.bottom =
                _backgroundRect.top + nonogram.getTopFieldHeight();
        _backgroundRect.right =
                _backgroundRect.left + nonogram.getTopFieldWidth();
        canvas.drawRect(_backgroundRect, _cellsPainter);

        _gridRect.top = _backgroundRect.top;
        _gridRect.left = _backgroundRect.left;
        _gridRect.bottom = _gridRect.top + cellSize;
        _gridRect.right = _gridRect.left + cellSize;
        for (int i = 0; i < columns; i++) {
            length = nonogram.getTopColumnLength(i);
            for (int j = 0; j < topFieldRows; j++) {
                canvas.drawRect(_gridRect, _gridPainter);
                if (j >= topFieldRows - length) {
                    index = j - topFieldRows + length;
                    text = Integer.toString(nonogram.getTopNumber(i, index));
                    textWidth = getWidthText(_numbersPainter, text);
                    textHeight = getHeightText(_numbersPainter, text);
                    canvas.drawText(text,
                            _gridRect.left + (cellSize - textWidth) / 2,
                            _gridRect.bottom - (cellSize - textHeight) / 2,
                            _numbersPainter);
                }
                _gridRect.offset(0f, cellSize);
            }
            _gridRect.offset(cellSize, 0f);
            _gridRect.offsetTo(_gridRect.left, _backgroundRect.top);
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
        _numbersPainter.setTypeface(Typeface.create(Typeface.MONOSPACE,
                Typeface.NORMAL));
    }
}
