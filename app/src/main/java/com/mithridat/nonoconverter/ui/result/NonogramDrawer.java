package com.mithridat.nonoconverter.ui.result;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mithridat.nonoconverter.backend.nonogram.Field;

/**
 * Class for drawing nonogram.
 */
public class NonogramDrawer extends View {

    /**
     * Brush, containing painting parameters.
     */
    private Paint _painter;

    /**
     * Nonogram as Field backend class.
     */
    private Field _nonogram = null;

    /**
     * Width of the lines in grid.
     */
    private int _linesWidth = 3;

    /**
     * Size of cells in screen coordinates.
     */
    private int _cellSize = 0;

    /**
     * Minimal horizontal and vertical indents from screen borders.
     */
    private int _minMarginHor = 0;
    private int _minMarginVer = 0;

    public NonogramDrawer(Context context) {
        super(context);
        painterInit();
    }

    public NonogramDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        painterInit();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) return;
        canvas.drawColor(Color.WHITE);
        drawNng(canvas);
    }

    /**
     * Set nonogram, recalculate metrics and redraw view.
     *
     * @param nonogramField nonogram as Field backend class
     */
    void setNonogramField(Field nonogramField) {
        _nonogram = nonogramField;
        recalculateSizes();
        invalidate();
    }

    /**
     * Set minimal vertical margin, recalculate metrics and redraw view.
     *
     * @param marginVer vertical margin
     */
    void setMarginVer(int marginVer) {
        _minMarginVer = marginVer;
        recalculateSizes();
        invalidate();
    }

    /**
     * Set minimal horizontal margin, recalculate metrics and redraw view.
     *
     * @param marginHor horizontal margin
     */
    void setMarginHor(int marginHor) {
        _minMarginHor = marginHor;
        recalculateSizes();
        invalidate();
    }

    /**
     * Set initial painter parameters.
     */
    private void painterInit() {
        _painter = new Paint();
        _painter.setStyle(Paint.Style.FILL);
        _painter.setStrokeWidth(_linesWidth);
        _painter.setColor(Color.BLACK);
    }

    /**
     * Recalculate cell size, height and width of view.
     * Called if nonogram or margins has changed.
     */
    private void recalculateSizes() {
        if (_nonogram == null) return;

        int cellSizeHor =
                (getDeviceWidth(getContext()) - 2 * _minMarginHor)
                        / _nonogram.getCols();
        int cellSizeVer =
                (getDeviceHeight(getContext()) - 2 * _minMarginVer)
                        / _nonogram.getRows();
        _cellSize = Math.min(cellSizeHor, cellSizeVer);

        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width =
                _cellSize * _nonogram.getCols() + _linesWidth;
        params.height =
                _cellSize * _nonogram.getRows() + _linesWidth;
        setLayoutParams(params);
    }

    /**
     * Function for getting device width.
     *
     * @param context current context
     * @return current device screen width
     */
    private static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * Function for getting device height.
     *
     * @param context current context
     * @return current device screen height
     */
    private static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * Function for drawing nonogram.
     *
     * @param canvas canvas received in 'onDraw'
     */
    private void drawNng(Canvas canvas) {
        if (_nonogram == null) return;
        /*
        "Magical" constants like '10' or '8'
        are the results of the empirical selection.
         */
        int columns = _nonogram.getCols();
        int rows = _nonogram.getRows();
        int cellGap = _linesWidth + (_cellSize - _linesWidth) / 10;
        int startX = _linesWidth / 2;
        int startY = _linesWidth / 2;
        int stopX = startX + _cellSize * columns;
        int stopY = startY + _cellSize * rows;
        int radius = _cellSize / 8 + 1;
        _painter.setColor(Color.BLACK);

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



