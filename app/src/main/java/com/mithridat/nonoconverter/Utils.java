package com.mithridat.nonoconverter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import static com.mithridat.nonoconverter.backend.nonogram.Field.BLACK;

/**
 * Class with some helpful functions
 */
public class Utils {

    /**
     * Method for getting width of the text by specific paint
     *
     * @param paint - the Paint for drawing
     * @param text - the text which width is unknown
     * @return width of the text
     */
    public static int getWidthText(Paint paint, String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.left + bounds.width();
    }

    /**
     * Method for getting height of the text by specific paint
     *
     * @param paint - the Paint for drawing
     * @param text - the text which height is unknown
     * @return height of the text
     */
    public static int getHeightText(Paint paint, String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.bottom + bounds.height();
    }

    /**
     * Method for drawing grid on the canvas
     *
     * @param canvas - the Canvas
     * @param pos - position of the left-top corner of the grid on the canvas
     * @param sizes - sizes of the grid
     * @param cellSize - size of the one cell
     */
    public static void drawGrid(
            Canvas canvas,
            Point pos,
            Point sizes,
            int cellSize) {
        Rect grid = getRectGrid(cellSize);
        Paint p = new Paint();
        p.setColor(BLACK);
        p.setStrokeWidth(1);
        p.setStyle(Paint.Style.STROKE);
        grid.offsetTo(pos.x, pos.y);
        for (int i = 0; i < sizes.y; i++) {
            for (int j = 0; j < sizes.x; j++) {
                canvas.drawRect(grid, p);
                grid.offset(cellSize, 0);
            }
            grid.offset(0, cellSize);
            grid.offsetTo(pos.x, grid.top);
        }
    }

    /**
     * Method for creating rectangle for grid lines
     *
     * @param cellSize - size of the one cell
     * @return rectangle
     */
    public static Rect getRectGrid(int cellSize) {
        Rect grid = new Rect();
        grid.top = 0;
        grid.left = 0;
        grid.bottom = cellSize;
        grid.right = cellSize;
        return grid;
    }

}
