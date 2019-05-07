package com.mithridat.nonoconverter.ui.result;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;

import com.mithridat.nonoconverter.ui.result.nonogramDrawable.IDrawable;

import androidx.annotation.Nullable;

/**
 * Manages zooming and panning of the image and forces it to be drawn.
 */
public class ResultImageView extends View implements View.OnTouchListener {

    /**
     * Drawable image, which will be represented on this view.
     */
    private IDrawable _drawable;

    /**
     * Class, whose method is to called process touches.
     */
    private TouchManager _touchManager;

    /**
     * Indents between image and screen borders on the start position.
     */
    private RectF _initialMargins;

    /**
     * Flag, marking if current state must be restored.
     */
    private boolean _restoreState = false;

    /**
     * Relative coordinates of the image center.
     * Used for correct state-restoration.
     */
    private float _centerRelativeX = 0f, _centerRelativeY = 0f;

    /**
     * State of drawable image.
     */
    private Parcelable _drawableState = null;

    public ResultImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _initialMargins = new RectF(50f, 50f, 50f, 50f);
        _touchManager = new TouchManager(context);
        setOnTouchListener(this);
        setListener();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(StringKeys.INITIAL_STATE,
                super.onSaveInstanceState());
        bundle.putParcelable(StringKeys.DRAWABLE_STATE,
                _drawable.saveState());
        bundle.putFloat(StringKeys.CENTER_RELATIVE_X, _centerRelativeX);
        bundle.putFloat(StringKeys.CENTER_RELATIVE_Y, _centerRelativeY);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof Bundle)) {
            super.onRestoreInstanceState(state);
        } else {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle
                    .getParcelable(StringKeys.INITIAL_STATE));
            _drawableState = bundle.getParcelable(StringKeys.DRAWABLE_STATE);
            _centerRelativeX = bundle.getFloat(StringKeys.CENTER_RELATIVE_X);
            _centerRelativeY = bundle.getFloat(StringKeys.CENTER_RELATIVE_Y);
            _restoreState = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null || _drawable == null) return;
        _drawable.draw(canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        _touchManager.processTouch(event);
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    /**
     * Set drawable, return view to initial state and redraw.
     *
     * @param drawable - drawable image
     */
    void setDrawable(IDrawable drawable) {
        _drawable = drawable;
        setInitialState();
        invalidate();
    }

    /**
     * Set OnGlobalLayoutListener for this view.
     * Listener calls "setInitialState()" or "restoreState()"
     * when view is ready to be drawn and then removes itself.
     */
    private void setListener() {
        final View thisView = this;
        final ViewTreeObserver observer = getViewTreeObserver();
        ViewTreeObserver.OnGlobalLayoutListener listener =
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (_restoreState) {
                            restoreState();
                        } else {
                            setInitialState();
                        }
                        invalidate();
                        ViewTreeObserver vto = thisView.getViewTreeObserver();
                        if (vto.isAlive()) {
                            vto.removeOnGlobalLayoutListener(this);
                        }
                    }
                };
        observer.addOnGlobalLayoutListener(listener);
    }

    /**
     * Set initial size and position of view.
     * Called on first appearance or when image has changed.
     */
    private void setInitialState() {
        if (_drawable == null) return;
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        final float drawableWidth =
                viewWidth - _initialMargins.left - _initialMargins.right;
        final float drawableHeight =
                viewHeight - _initialMargins.top - _initialMargins.bottom;
        _drawable.setSizeProportional(drawableWidth, drawableHeight);

        float spaceHor = viewWidth - _drawable.getWidth();
        float spaceVer = viewHeight - _drawable.getHeight();
        spaceHor -= _initialMargins.left + _initialMargins.right;
        spaceVer -= _initialMargins.top + _initialMargins.bottom;
        final float positionX = _initialMargins.left + spaceHor / 2f;
        final float positionY = _initialMargins.top + spaceVer / 2f;
        _drawable.offsetTo(positionX, positionY);
        screenBordersCheck();
    }


    /**
     * Restore image state and calculate it's position
     * by relative center coordinates.
     */
    private void restoreState() {
        if (_drawable == null) return;
        _drawable.restoreState(_drawableState);
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        final float viewCenterX = viewWidth / 2f;
        final float viewCenterY = viewHeight / 2f;
        final float drawableCenterX =
                viewCenterX + _centerRelativeX * _drawable.getWidth();
        final float drawableCenterY =
                viewCenterY + _centerRelativeY * _drawable.getHeight();
        _drawable.offsetCenterTo(drawableCenterX, drawableCenterY);
        screenBordersCheck();
        _restoreState = false;
    }

    /**
     * Does not allow the image to be moved too far from the screen
     * and calculates the relative coordinates.
     * Should be called after every position changing.
     */
    private void screenBordersCheck() {
        if (_drawable == null) return;
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        final float viewCenterX = viewWidth / 2f;
        final float viewCenterY = viewHeight / 2f;
        final float drawableWidth = _drawable.getWidth();
        final float drawableHeight = _drawable.getHeight();
        final float centerDistanceX = viewCenterX - _drawable.getCenterX();
        final float centerDistanceY = viewCenterY - _drawable.getCenterY();
        float offsetX = 0f, offsetY = 0f;

        /* Distance between image center and view center might not be
           greater then half of image size.
           (Distance if calculated as infinite norm, different for X and Y
            directions)
         */
        if (centerDistanceX > drawableWidth / 2f) {
            offsetX += centerDistanceX - drawableWidth / 2f;
        }
        if (centerDistanceX < -drawableWidth / 2f) {
            offsetX += centerDistanceX + drawableWidth / 2f;
        }
        if (centerDistanceY > drawableHeight / 2f) {
            offsetY += centerDistanceY - drawableHeight / 2f;
        }
        if (centerDistanceY < -drawableHeight / 2f) {
            offsetY += centerDistanceY + drawableHeight / 2f;
        }

        _drawable.offset(offsetX, offsetY);
        if (drawableHeight == 0 || drawableWidth == 0) {
            _centerRelativeX = 0f;
            _centerRelativeY = 0f;
        } else {
            _centerRelativeX =
                    (_drawable.getCenterX() - viewCenterX) / drawableWidth;
            _centerRelativeY =
                    (_drawable.getCenterY() - viewCenterY) / drawableHeight;
        }
    }


    /**
     * Class for touch-processing.
     */
    private class TouchManager {
        /**
         * Maximal and minimal total scale values.
         */
        private static final float TOTAL_SCALE_MAX = 10f;
        private static final float TOTAL_SCALE_MIN = 0.8f;

        /**
         * Invalid pointer ID.
         */
        private static final int INVALID_POINTER_ID = -1;

        /**
         * Id of the active pointer.
         */
        private int _activePointerId;

        /**
         * Coordinates of the last touch.
         */
        private float _lastTouchX, _lastTouchY;

        /**
         * Scale detector.
         */
        private ScaleGestureDetector _scaleDetector;

        TouchManager(Context context) {
            _activePointerId = INVALID_POINTER_ID;
            _lastTouchX = 0f;
            _lastTouchY = 0f;
            _scaleDetector = new ScaleGestureDetector(context,
                    new ScaleListener());
        }

        /**
         * Function for processing touches.
         *
         * @param event - motion event
         */
        void processTouch(MotionEvent event) {
            _scaleDetector.onTouchEvent(event);
            if (_drawable == null) return;

            final int actionMask = event.getActionMasked();
            switch (actionMask) {

                /* The first finger is placed, it become an active pointer. */
                case MotionEvent.ACTION_DOWN:
                    _activePointerId = event.getPointerId(0);
                    _lastTouchX = event.getX(0);
                    _lastTouchY = event.getY(0);
                    break;

                /* Track only the movement of the active pointer.
                   Move image only if there are one finger on the display.
                 */
                case MotionEvent.ACTION_MOVE: {
                    final int index = event.findPointerIndex(_activePointerId);
                    final float currentTouchX = event.getX(index);
                    final float currentTouchY = event.getY(index);
                    if (event.getPointerCount() == 1) {
                        final float offsetX = currentTouchX - _lastTouchX;
                        final float offsetY = currentTouchY - _lastTouchY;
                        _drawable.offset(offsetX, offsetY);
                        screenBordersCheck();
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
                default:
                    break;
            }
        }

        /**
         * Inner class to process scaling.
         */
        public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (_drawable == null) return true;
                final float totalScale = _drawable.getTotalScale();
                final float focusX = detector.getFocusX();
                final float focusY = detector.getFocusY();
                float scale = detector.getScaleFactor();
                if (totalScale * scale > TOTAL_SCALE_MAX) {
                    scale = TOTAL_SCALE_MAX / totalScale;
                }
                if (totalScale * scale < TOTAL_SCALE_MIN) {
                    scale = TOTAL_SCALE_MIN / totalScale;
                }
                _drawable.scaleWithFocus(scale, focusX, focusY);
                screenBordersCheck();
                invalidate();
                return true;
            }
        }
    }
}
