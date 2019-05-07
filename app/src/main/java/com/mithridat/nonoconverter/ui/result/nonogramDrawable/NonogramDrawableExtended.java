package com.mithridat.nonoconverter.ui.result.nonogramDrawable;

import android.graphics.Canvas;

import com.mithridat.nonoconverter.backend.nonogram.Nonogram;

import static com.mithridat.nonoconverter.backend.nonogram.Field.COL;
import static com.mithridat.nonoconverter.backend.nonogram.Field.ROW;

public class NonogramDrawableExtended extends NonogramDrawable implements INonogramExtended {

    public NonogramDrawableExtended(Nonogram nonogram) {
        super(nonogram);
    }

    /* -------------- INonogram methods:  -------------- */

    @Override
    public float getMainFieldTopLeftX() {
        return _topLeftPos.x + getLeftFieldWidth();
    }

    @Override
    public float getMainFieldTopLeftY() {
        return _topLeftPos.y + getTopFieldHeight();
    }

    /* -------------- IDrawable methods:  -------------- */

    @Override
    public float getWidth() {
        return (getColumns() + getLeftFieldColumns()) * _cellSize;
    }

    @Override
    public float getHeight() {
        return (getRows() + getTopFieldRows()) * _cellSize;
    }

    @Override
    public void setSizeProportional(float width, float height) {
        if (_nonogram == null) {
            _cellSize = 0f;
        } else {
            final float colsTotal = getColumns() + getLeftFieldColumns();
            final float rowsTotal = getRows() + getTopFieldRows();
            final float cellSizeHor = width / colsTotal;
            final float cellSizeVer = height / rowsTotal;
            _cellSize = Math.min(cellSizeHor, cellSizeVer);
        }
        _totalScale = 1f;
        centerPosCalculate();
    }

    @Override
    public void draw(Canvas canvas) {
        NonogramDrawer.drawNonogramExtended(canvas, this);
    }

    /* -------------- IDrawableExtended methods:  -------------- */

    @Override
    public int getTopFieldRows() {
        if (_nonogram == null) return 0;
        return _nonogram.getMaxLength(COL);
    }

    @Override
    public int getLeftFieldColumns() {
        if (_nonogram == null) return 0;
        return _nonogram.getMaxLength(ROW);
    }

    @Override
    public int getLeftRowLength(int row) {
        if (_nonogram == null) return 0;
        return _nonogram.getLeftRowLength(row);
    }

    @Override
    public int getTopColumnLength(int column) {
        if (_nonogram == null) return 0;
        return _nonogram.getTopColLength(column);
    }

    @Override
    public int getLeftNumber(int row, int index) {
        if (_nonogram == null) return 0;
        return _nonogram.getValue(row, index, ROW);
    }

    @Override
    public int getTopNumber(int column, int index) {
        if (_nonogram == null) return 0;
        return _nonogram.getValue(column, index, COL);
    }

    @Override
    public int getMaxNumber() {
        if (_nonogram == null) return 0;
        return _nonogram.getMaxValue();
    }

    @Override
    public float getLeftFieldWidth() {
        return getLeftFieldColumns() * _cellSize;
    }

    @Override
    public float getLeftFieldHeight() {
        return getRows() * _cellSize;
    }

    @Override
    public float getTopFieldWidth() {
        return getColumns() * _cellSize;
    }

    @Override
    public float getTopFieldHeight() {
        return getTopFieldRows() * _cellSize;
    }
}
