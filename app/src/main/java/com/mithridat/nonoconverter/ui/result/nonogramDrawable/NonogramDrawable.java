package com.mithridat.nonoconverter.ui.result.nonogramDrawable;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;

import com.mithridat.nonoconverter.backend.nonogram.Nonogram;
import com.mithridat.nonoconverter.ui.result.StringKeys;

/**
 * Class implementing INonogramDrawable interface.
 * Represents nonogram solution with no numbers left or top.
 */
public class NonogramDrawable implements INonogramDrawable {

    /**
     * Nonogram as backend class.
     */
    private Nonogram _nonogram;

    /**
     * Size of cells in nonogram grid.
     */
    private float _cellSize;

    /**
     * Total scale.
     */
    private float _totalScale;

    /**
     * Position of the top left corner of nonogram.
     */
    private PointF _topLeftPos;

    /**
     * Position of the center of nonogram.
     */
    private PointF _centerPos;

    public NonogramDrawable(Nonogram nonogram) {
        _topLeftPos = new PointF();
        _centerPos = new PointF();
        _nonogram = nonogram;
        restoreDefaultState();
    }

    @Override
    public int getRows() {
        if (_nonogram == null) return 0;
        return _nonogram.getField().getRows();
    }

    @Override
    public int getColumns() {
        if (_nonogram == null) return 0;
        return _nonogram.getField().getCols();
    }

    @Override
    public int getColor(int row, int column) {
        if (_nonogram == null) return 0;
        return _nonogram.getField().getColor(row, column);
    }

    @Override
    public float getHeight() {
        return getRows() * _cellSize;
    }

    @Override
    public float getWidth() {
        return getColumns() * _cellSize;
    }

    @Override
    public float getTopLeftX() {
        return _topLeftPos.x;
    }

    @Override
    public float getTopLeftY() {
        return _topLeftPos.y;
    }

    @Override
    public float getCenterX() {
        return _centerPos.x;
    }

    @Override
    public float getCenterY() {
        return _centerPos.y;
    }

    @Override
    public float getTotalScale() {
        return _totalScale;
    }

    @Override
    public float getCellSize() {
        return _cellSize;
    }

    @Override
    public void offset(float offsetX, float offsetY) {
        _centerPos.x += offsetX;
        _centerPos.y += offsetY;
        _topLeftPos.x += offsetX;
        _topLeftPos.y += offsetY;
    }

    @Override
    public void offsetTo(float newX, float newY) {
        _topLeftPos.x = newX;
        _topLeftPos.y = newY;
        centerPosCalculate();
    }

    @Override
    public void offsetCenterTo(float newCenterX, float newCenterY) {
        final float offsetX = newCenterX - _centerPos.x;
        final float offsetY = newCenterY - _centerPos.y;
        offset(offsetX, offsetY);
    }

    @Override
    public void scaleWithFocus(float factor, float focusX, float focusY) {
        final float dx = focusX - _topLeftPos.x;
        final float dy = focusY - _topLeftPos.y;
        _cellSize *= factor;
        _totalScale *= factor;
        offsetTo(focusX - dx * factor, focusY - dy * factor);

    }

    @Override
    public void setSizeProportional(float width, float height) {
        if (_nonogram == null) {
            _cellSize = 0f;
        } else {
            final float cellSizeHor = width / _nonogram.getField().getCols();
            final float cellSizeVer = height / _nonogram.getField().getRows();
            _cellSize = Math.min(cellSizeHor, cellSizeVer);
        }
        _totalScale = 1f;
        centerPosCalculate();
    }

    @Override
    public void draw(Canvas canvas) {
        NonogramDrawer.drawNonogram(canvas, this);
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = new Bundle();
        bundle.putFloat(StringKeys.CELL_SIZE, _cellSize);
        bundle.putFloat(StringKeys.TOTAL_SCALE, _totalScale);
        bundle.putFloat(StringKeys.POSITION_X, _topLeftPos.x);
        bundle.putFloat(StringKeys.POSITION_Y, _topLeftPos.y);
        return bundle;
    }

    @Override
    public void restoreState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            _cellSize = bundle.getFloat(StringKeys.CELL_SIZE, 0f);
            _totalScale = bundle.getFloat(StringKeys.TOTAL_SCALE, 1f);
            _topLeftPos.x = bundle.getFloat(StringKeys.POSITION_X, 0f);
            _topLeftPos.y = bundle.getFloat(StringKeys.POSITION_Y, 0f);
        } else {
            restoreDefaultState();
        }
        centerPosCalculate();
    }

    /**
     * Calculate position of nonogram's center by it's top left corner.
     */
    protected void centerPosCalculate() {
        _centerPos.x = _topLeftPos.x + getWidth() / 2f;
        _centerPos.y = _topLeftPos.y + getHeight() / 2f;
    }

    /**
     * Restores nonogram back to default state.
     */
    protected void restoreDefaultState() {
        _totalScale = 1f;
        _cellSize = 0f;
        _topLeftPos.set(0f, 0f);
        _centerPos.set(0f, 0f);
    }
}
