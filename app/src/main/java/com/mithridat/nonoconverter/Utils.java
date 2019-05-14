package com.mithridat.nonoconverter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import static com.mithridat.nonoconverter.backend.nonogram.Field.BLACK;
import static com.mithridat.nonoconverter.backend.nonogram.Field.ROW;

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
     * @param linesWidth - width of the lines
     * @param border - is it needs to draw bold border
     */
    public static void drawGrid(
            Canvas canvas,
            PointF pos,
            Point sizes,
            float cellSize,
            float linesWidth,
            boolean border) {
        if (canvas == null || pos == null || sizes == null) return;
        RectF grid = getRectFGrid(cellSize, cellSize);
        Paint p = new Paint();
        p.setColor(BLACK);
        p.setStrokeWidth(linesWidth);
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
        if (border) {
            grid = getRectFGrid(sizes.x * cellSize, sizes.y * cellSize);
            grid.offsetTo(pos.x, pos.y);
            p.setStrokeWidth(2 * linesWidth);
            canvas.drawRect(grid, p);
        }
    }

    /**
     * Method for creating rectangle for grid lines
     *
     * @param w - width of the one cell
     * @param h - height of the one cell
     * @return rectangle
     */
    public static RectF getRectFGrid(float w, float h) {
        RectF grid = new RectF();
        grid.top = 0;
        grid.left = 0;
        grid.bottom = h;
        grid.right = w;
        return grid;
    }

    /**
     * Method for drawing lines on the canvas
     *
     * @param canvas - the Canvas
     * @param pos - the position of the left-top corner of the first line on
     *            the canvas
     * @param distance - the distance between lines
     * @param length - the length of the one line
     * @param linesWidth - the width of the lines
     * @param count - the count of the lines
     * @param color - the color of the lines
     * @param type - the type of the lines
     */
    public static void drawLines(
            Canvas canvas,
            PointF pos,
            float distance,
            float length,
            float linesWidth,
            int count,
            int color,
            int type) {
        if (canvas == null || pos == null) return;
        final boolean isRow = type == ROW;
        final float dx = isRow ? 0f : distance;
        final float dy = isRow ? distance : 0f;
        final float dposx = isRow ? length : 0f;
        final float dposy = isRow ? 0f : length;
        Paint p = new Paint();
        p.setColor(color);
        p.setStrokeWidth(linesWidth);
        for (int i = 0; i < count; i++) {
            canvas.drawLine(pos.x,
                    pos.y,
                    pos.x + dposx,
                    pos.y + dposy,
                    p);
            pos.offset(dx, dy);
        }
    }

}
