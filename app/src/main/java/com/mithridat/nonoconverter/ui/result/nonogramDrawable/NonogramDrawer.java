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
     * Constants for directions in function params.
     */
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;

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
     * Brush for painting numbers.
     */
    private static Paint _boldLinesPainter;

    /**
     * Rectangles for painting background, grid and filled cells.
     */
    private static RectF _backgroundRect, _gridRect, _cellsRect;

    static {
        _gridPainter = new Paint();
        _cellsPainter = new Paint();
        _numbersPainter = new Paint();
        _boldLinesPainter = new Paint();
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
        "Magical" constants like '10f', '8f' or '15f'
        are the results of the empirical selection.
         */
        final int columns = nonogram.getColumns();
        final int rows = nonogram.getRows();
        final float startX = nonogram.getMainFieldTopLeftX();
        final float startY = nonogram.getMainFieldTopLeftY();
        final float cellSize = nonogram.getCellSize();
        final float linesWidth = linesWidthCalculate(cellSize);
        final float cellGap = linesWidth / 2f + (cellSize - linesWidth) / 8f;
        final float radius = cellSize / 10f;

        _gridPainter.setStrokeWidth(linesWidth);
        // draw white background
        _backgroundRect.top = startY;
        _backgroundRect.left = startX;
        _backgroundRect.bottom = startY + nonogram.getMainFieldHeight();
        _backgroundRect.right = startX + nonogram.getMainFieldWidth();
        _cellsPainter.setColor(Color.WHITE);
        canvas.drawRect(_backgroundRect, _cellsPainter);
        drawBoldLines(_backgroundRect, canvas, nonogram, VERTICAL);
        drawBoldLines(_backgroundRect, canvas, nonogram, HORIZONTAL);

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
        final float linesWidth = linesWidthCalculate(cellSize);

        String text;
        int length, index;
        int textWidth, textHeight;

        _gridPainter.setStrokeWidth(linesWidth);
        // draw white background
        _backgroundRect.top = startY;
        _backgroundRect.left = startX - nonogram.getLeftFieldWidth();
        _backgroundRect.bottom =
                _backgroundRect.top + nonogram.getLeftFieldHeight();
        _backgroundRect.right =
                _backgroundRect.left + nonogram.getLeftFieldWidth();
        _cellsPainter.setColor(Color.WHITE);
        canvas.drawRect(_backgroundRect, _cellsPainter);
        drawBoldLines(_backgroundRect, canvas, nonogram, HORIZONTAL);

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
        final float linesWidth = linesWidthCalculate(cellSize);

        String text;
        int length, index;
        int textWidth, textHeight;

        _gridPainter.setStrokeWidth(linesWidth);
        // draw white background
        _backgroundRect.top = startY - nonogram.getTopFieldHeight();
        _backgroundRect.left = startX;
        _backgroundRect.bottom =
                _backgroundRect.top + nonogram.getTopFieldHeight();
        _backgroundRect.right =
                _backgroundRect.left + nonogram.getTopFieldWidth();
        _cellsPainter.setColor(Color.WHITE);
        canvas.drawRect(_backgroundRect, _cellsPainter);
        drawBoldLines(_backgroundRect, canvas, nonogram, VERTICAL);

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
     * Function for drawing vertical or horizontal bold lines.
     *
     * @param background - rectangle, which will be filled with lines
     * @param canvas     - canvas received in 'onDraw'
     * @param nonogram   - nonogram to draw
     * @param direction  - direction of lines
     */
    private static void drawBoldLines(
            RectF background,
            Canvas canvas,
            final INonogram nonogram,
            int direction) {
        if (canvas == null || nonogram == null) return;
        final float cellSize = nonogram.getCellSize();
        final float linesWidth = linesWidthCalculate(cellSize);
        float stopX = background.right, stopY = background.bottom;
        float dx = 0f, dy = 0f;
        int number = 0;

        if (direction == VERTICAL) {
            number = nonogram.getColumns();
            dx = cellSize;
            stopX = background.left;
        } else if (direction == HORIZONTAL) {
            number = nonogram.getRows();
            dy = cellSize;
            stopY = background.top;
        }

        _boldLinesPainter.setStrokeWidth(linesWidth * 2f);
        canvas.drawRect(background, _boldLinesPainter);
        for (int i = 0; i < number; i += 5) {
            canvas.drawLine(background.left + i * dx,
                    background.top + i * dy,
                    stopX + i * dx,
                    stopY + i * dy,
                    _boldLinesPainter);
        }
    }

    /**
     * Set parameters for painters.
     */
    private static void paintersInit() {
        _gridPainter.setStyle(Paint.Style.STROKE);
        _gridPainter.setColor(Color.BLACK);
        _cellsPainter.setStyle(Paint.Style.FILL);
        _numbersPainter.setTypeface(Typeface.create(Typeface.MONOSPACE,
                Typeface.NORMAL));
        _boldLinesPainter.setStyle(Paint.Style.STROKE);
        _boldLinesPainter.setColor(Color.BLACK);
    }

    /**
     * Function for calculating lines width by cell size.
     *
     * @param cellSize - size of the cell
     * @return width of the lines
     */
    private static float linesWidthCalculate(float cellSize) {
        return cellSize / 15f + 1f;
    }
}
