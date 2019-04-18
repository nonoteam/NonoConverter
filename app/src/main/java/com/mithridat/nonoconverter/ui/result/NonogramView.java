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
     * Class, whose method is to called process touches.
     */
    private TouchManager _touchManager;

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


    public NonogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _nonogramDrawer = new NonogramDrawer();
        _initialMargins = new RectF(50f, 50f, 50f, 50f);
        _nonogramPos = new PointF(0f, 0f);
        _touchManager = new TouchManager(context, 1f);
        setListener();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) return;
        _nonogramDrawer.drawNonogram(canvas, _nonogram);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        _touchManager.processTouch(event);
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
        _touchManager.setInitialState();
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

        final float minCellSize =
                _cellSize / _touchManager.getTotalScale()
                        * TouchManager.TOTAL_SCALE_MIN;

        final float spaceHor = viewWidth - _nonogram.getCols() * minCellSize;
        final float spaceVer = viewHeight - _nonogram.getRows() * minCellSize;

        final float marginHor = spaceHor / 2f;
        final float marginVer = spaceVer / 2f;

        if (_nonogramPos.x > marginHor)
            _nonogramPos.x = marginHor;
        if (_nonogramPos.y > marginVer)
            _nonogramPos.y = marginVer;
        if (nonogramRight < viewWidth - marginHor)
            _nonogramPos.x =
                    viewWidth - marginHor - _nonogram.getCols() * _cellSize;
        if (nonogramBot < viewHeight - marginVer)
            _nonogramPos.y =
                    viewHeight - marginVer - _nonogram.getRows() * _cellSize;
    }


    /**
     * Class for touch - processing.
     */
    private class TouchManager {
        /**
         * Maximal and minimal total scale values.
         */
        static final float TOTAL_SCALE_MAX = 10f;
        static final float TOTAL_SCALE_MIN = 0.8f;

        /**
         * Invalid pointer ID.
         */
        private static final int INVALID_POINTER_ID = -1;

        /**
         * Scale of the nonogram compare to initial.
         */
        private float _totalScale;

        /**
         * Id of the active pointer.
         */
        private int _activePointerId = INVALID_POINTER_ID;

        /**
         * Scale detector
         */
        private ScaleGestureDetector _scaleDetector;

        /**
         * Coordinates of the last touch.
         */
        private float _lastTouchX = 0f, _lastTouchY = 0f;


        TouchManager(Context context, float startScale) {
            if (startScale > TOTAL_SCALE_MAX)
                _totalScale = TOTAL_SCALE_MAX;
            else if (startScale < TOTAL_SCALE_MIN)
                _totalScale = TOTAL_SCALE_MIN;
            else
                _totalScale = startScale;
            _scaleDetector = new ScaleGestureDetector(context,
                    new ScaleListener());
        }

        /**
         * Returns touch manager to initial state.
         * Now it will consider current total scale as default (= 1f).
         */
        void setInitialState() {
            _totalScale = 1f;
            _lastTouchX = 0f;
            _lastTouchY = 0f;
            _activePointerId = INVALID_POINTER_ID;
        }

        /**
         * Function for getting total scale.
         *
         * @return total scale
         */
        float getTotalScale() {
            return _totalScale;
        }

        /**
         * Function for processing touches.
         *
         * @param event motion event
         */
        void processTouch(MotionEvent event) {
            _scaleDetector.onTouchEvent(event);

            final int actionMask = event.getActionMasked();
            switch (actionMask) {

                /* The first finger is placed, it become an active pointer. */
                case MotionEvent.ACTION_DOWN:
                    _activePointerId = event.getPointerId(0);
                    _lastTouchX = event.getX(0);
                    _lastTouchY = event.getY(0);
                    break;

                /* Track only the movement of the active pointer.
                   Move nonogram only if there are one finger on the display.
                 */
                case MotionEvent.ACTION_MOVE: {
                    final int index = event.findPointerIndex(_activePointerId);
                    final float currentTouchX = event.getX(index);
                    final float currentTouchY = event.getY(index);
                    if (event.getPointerCount() == 1) {
                        _nonogramPos.x += currentTouchX - _lastTouchX;
                        _nonogramPos.y += currentTouchY - _lastTouchY;
                        screenBordersCheck();
                        _nonogramDrawer.setPosition(_nonogramPos.x,
                                _nonogramPos.y);
                        invalidate();
                    }
                    _lastTouchX = currentTouchX;
                    _lastTouchY = currentTouchY;
                    break;
                }

                /* One of the fingers is removed from the display.
                   If it is an active pointer, chose a new one.
                  */
                case MotionEvent.ACTION_POINTER_UP: {
                    final int index = event.getActionIndex();
                    final int id = event.getPointerId(index);
                    if (id == _activePointerId) {
                        final int newIndex = (index == 0) ? 1 : 0;
                        _lastTouchX = event.getX(newIndex);
                        _lastTouchY = event.getY(newIndex);
                        _activePointerId = event.getPointerId(newIndex);
                    }
                    break;
                }

                /* The last finger is removed from the display */
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    _activePointerId = INVALID_POINTER_ID;
                    break;
            }

            performClick();
        }


        /**
         * Inner class to process scaling.
         */
        public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

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
}
