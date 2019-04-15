package com.mithridat.nonoconverter.ui.result;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;

import com.mithridat.nonoconverter.backend.Field;

/**
 * Manages zooming and panning of the nonogram and forces it to be drawn.
 */
public class NonogramView extends View implements View.OnTouchListener {

    /**
     * Class, whose method is called to draw nonorgam.
     */
    private NonogramDrawer _nonogramDrawer;

    /**
     * Scale detector
     */
    protected ScaleGestureDetector _scaleDetector;

    /**
     * Nonogram as Field backend class.
     */
    private Field _nonogram = null;

    /**
     * Size of cells in nonogram grid.
     */
    private float _cellSize = 0f;

    /**
     * Position of the top left corner of nonogram.
     */
    private PointF _nonogramPos;

    /**
     * Indents between nonogram and screen borders on the start position.
     */
    private RectF _initialMargins;

    /**
     * Coordinates of the last touch.
     */
    private float _lastTouchX = 0f, _lastTouchY = 0f;

    /**
     * Scale of the nonogram compare to initial.
     */
    private float _totalScale = 1f;


    public NonogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _nonogramDrawer = new NonogramDrawer();
        _initialMargins = new RectF(50f, 50f, 50f, 50f);
        _nonogramPos = new PointF(0f, 0f);
        setListener();
        _scaleDetector = new ScaleGestureDetector(context, new ScaleListener());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) return;
        _nonogramDrawer.drawNonogram(canvas, _nonogram);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float currentTouchX = event.getX();
        float currentTouchY = event.getY();

        _scaleDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                _lastTouchX = currentTouchX;
                _lastTouchY = currentTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    _nonogramPos.x += currentTouchX - _lastTouchX;
                    _nonogramPos.y += currentTouchY - _lastTouchY;
                    _lastTouchX = currentTouchX;
                    _lastTouchY = currentTouchY;
                    screenBordersCheck();
                    _nonogramDrawer.setPosition(_nonogramPos.x, _nonogramPos.y);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }


    /**
     * Set nonogram, return view to initial state and redraw.
     *
     * @param nonogramField nonogram as Field backend class
     */
    void setNonogramField(Field nonogramField) {
        _nonogram = nonogramField;
        setInitialState();
        invalidate();
    }

    /**
     * Set OnGlobalLayoutListener for this view.
     * Listener calls "setInitialState()" when view is ready to be drawn
     * and then removes itself.
     */
    private void setListener() {
        final View thisView = this;
        final ViewTreeObserver observer = this.getViewTreeObserver();
        ViewTreeObserver.OnGlobalLayoutListener listener =
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        setInitialState();
                        ViewTreeObserver vto = thisView.getViewTreeObserver();
                        if (vto.isAlive()) {
                            vto.removeOnGlobalLayoutListener(this);
                        }
                    }
                };
        observer.addOnGlobalLayoutListener(listener);
    }

    /**
     * Set initial cell size and position of view.
     * Called on first appearance or when nonogram has changed.
     */
    private void setInitialState() {
        final float viewWidth = this.getWidth();
        final float viewHeight = this.getHeight();

        calculateInitialCellSize(viewWidth, viewHeight);
        _nonogramDrawer.setCellSize(_cellSize);
        calculateInitialPosition(viewWidth, viewHeight);
        _nonogramDrawer.setPosition(_nonogramPos.x, _nonogramPos.y);
        _totalScale = 1f;
    }

    /**
     * Calculate initial cell size.
     *
     * @param viewWidth  width of the view
     * @param viewHeight height of the view
     */
    private void calculateInitialCellSize(float viewWidth, float viewHeight) {
        if (_nonogram == null) {
            _cellSize = 0f;
            return;
        }

        final float cellSizeHor =
                (viewWidth - _initialMargins.left - _initialMargins.right)
                        / _nonogram.getCols();
        final float cellSizeVer =
                (viewHeight - _initialMargins.top - _initialMargins.bottom)
                        / _nonogram.getRows();
        _cellSize = Math.min(cellSizeHor, cellSizeVer);
    }

    /**
     * Calculate initial nonogram position.
     *
     * @param viewWidth  width of the view
     * @param viewHeight height of the view
     */
    private void calculateInitialPosition(float viewWidth, float viewHeight) {
        if (_nonogram == null) {
            _nonogramPos.set(0f, 0f);
            return;
        }

        float spaceHor = viewWidth - _cellSize * _nonogram.getCols();
        float spaceVer = viewHeight - _cellSize * _nonogram.getRows();
        spaceHor -= _initialMargins.left + _initialMargins.right;
        spaceVer -= _initialMargins.top + _initialMargins.bottom;
        _nonogramPos.x = _initialMargins.left + spaceHor / 2f;
        _nonogramPos.y = _initialMargins.top + spaceVer / 2f;
    }

    /**
     * Does not allow the nonogram to be moved too far from the screen.
     */
    private void screenBordersCheck() {
        final float viewWidth = this.getWidth();
        final float viewHeight = this.getHeight();

        float nonogramRight = _nonogramPos.x + _nonogram.getCols() * _cellSize;
        float nonogramBot = _nonogramPos.y + _nonogram.getRows() * _cellSize;

        if (nonogramRight - _nonogramPos.x <= viewWidth) {
            if (_nonogramPos.x < 0f)
                _nonogramPos.x = 0f;
            else if (nonogramRight > viewWidth)
                _nonogramPos.x = viewWidth - _nonogram.getCols() * _cellSize;
        } else {
            if (_nonogramPos.x < 0f && nonogramRight < viewWidth)
                _nonogramPos.x = viewWidth - _nonogram.getCols() * _cellSize;
            else if (_nonogramPos.x > 0f && nonogramRight > viewWidth)
                _nonogramPos.x = 0f;
        }

        if (nonogramBot - _nonogramPos.y <= viewHeight) {
            if (_nonogramPos.y < 0f)
                _nonogramPos.y = 0f;
            else if (nonogramBot > viewHeight)
                _nonogramPos.y = viewHeight - _nonogram.getRows() * _cellSize;
        } else {
            if (_nonogramPos.y < 0f && nonogramBot < viewHeight)
                _nonogramPos.y = viewHeight - _nonogram.getRows() * _cellSize;
            else if (_nonogramPos.y > 0f && nonogramBot > viewHeight)
                _nonogramPos.y = 0f;
        }
    }

    /**
     * Inner class to process scaling.
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        /**
         * Maximal and minimal total scale values.
         */
        private final float TOTAL_SCALE_MAX = 10f;
        private final float TOTAL_SCALE_MIN = 1f;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            if (_totalScale * scale > TOTAL_SCALE_MAX) {
                scale = TOTAL_SCALE_MAX / _totalScale;
            }
            if (_totalScale * scale < TOTAL_SCALE_MIN) {
                scale = TOTAL_SCALE_MIN / _totalScale;
            }
            _totalScale *= scale;
            _cellSize *= scale;
            _nonogramDrawer.setCellSize(_cellSize);

            final float focusX = detector.getFocusX();
            final float focusY = detector.getFocusY();
            final float dx = focusX - _nonogramPos.x;
            final float dy = focusY - _nonogramPos.y;

            _nonogramPos.x = focusX - dx * scale;
            _nonogramPos.y = focusY - dy * scale;
            screenBordersCheck();
            _nonogramDrawer.setPosition(_nonogramPos.x, _nonogramPos.y);
            invalidate();
            return true;
        }
    }
}
