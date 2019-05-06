package com.mithridat.nonoconverter.ui.result.nonogramDrawable;

import android.graphics.Canvas;
import android.os.Parcelable;

/**
 * Interface for drawable objects.
 */
public interface IDrawable {

    /**
     * Function for getting height of image.
     *
     * @return height of image
     */
    float getHeight();

    /**
     * Function for getting width of image.
     *
     * @return width of image
     */
    float getWidth();

    /**
     * Method for getting X coordinate of the top left corner.
     *
     * @return X coordinate of the top left corner
     */
    float getTopLeftX();

    /**
     * Method for getting Y coordinate of the top left corner.
     *
     * @return Y coordinate of the top left corner
     */
    float getTopLeftY();

    /**
     * Method for getting X coordinate of the center.
     *
     * @return X coordinate of the center
     */
    float getCenterX();

    /**
     * Method for getting Y coordinate of the center.
     *
     * @return Y coordinate of the center
     */
    float getCenterY();

    /**
     * Function for getting total(summary) scale.
     *
     * @return total scale
     */
    float getTotalScale();

    /**
     * Move the image by offsetX horizontally and by offsetY vertically.
     *
     * @param offsetX - horizontal offset
     * @param offsetY - vertical offset
     */
    void offset(float offsetX, float offsetY);

    /**
     * Move top left corner to new coordinates.
     *
     * @param newX - new X coordinate of top left corner
     * @param newY - new Y coordinate of top left corner
     */
    void offsetTo(float newX, float newY);

    /**
     * Move center to new coordinates.
     *
     * @param newCenterX - new X coordinate of center
     * @param newCenterY - new X coordinate of center
     */
    void offsetCenterTo(float newCenterX, float newCenterY);

    /**
     * Scale by factor from focus point.
     *
     * @param factor - factor of scaling
     * @param focusX - X coordinate of focus
     * @param focusY - Y coordinate of focus
     */
    void scaleWithFocus(float factor, float focusX, float focusY);

    /**
     * Function for setting size for image with saving proportions.
     * Total scale will be restored to initial state.
     *
     * @param width  - horizontal size
     * @param height - vertical size
     */
    void setSizeProportional(float width, float height);

    /**
     * Draw image on canvas
     *
     * @param canvas - canvas
     */
    void draw(Canvas canvas);

    /**
     * Function for saving state
     *
     * @return state saved as Parcelable
     */
    Parcelable saveState();

    /**
     * Function for restoring state
     *
     * @param state - state to restore
     */
    void restoreState(Parcelable state);
}
