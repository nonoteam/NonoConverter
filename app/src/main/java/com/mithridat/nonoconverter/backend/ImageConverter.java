package com.mithridat.nonoconverter.backend;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Image converting class
 */
public class ImageConverter {

    /**
     * Method for converting image into nonogram field
     *
     * @param bmp - image
     * @param rows - number of nonogram field rows
     * @param cols - number of nonogram field columns
     * @param asyncTask - async task of image converting
     * @return nonogram field
     */
    public static Field convertImage(
            Bitmap bmp,
            int rows,
            int cols,
            AsyncTask<Void, Void, Field> asyncTask) {
        if(!asyncTask.isCancelled()) {
            Bitmap bw =
                    getBlackWhite(bmp.copy(bmp.getConfig(), bmp.isMutable()));
            return getThumbnail(bw, rows, cols, 128);
        }
        return null;
    }

    /**
     * Method for getting black-and-white image from color image
     *
     * @param bmp - color image
     * @return black-and-white image
     */
    public static Bitmap getBlackWhite(Bitmap bmp) {
        Mat imageMat = new Mat();
        Utils.bitmapToMat(bmp, imageMat);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(imageMat, imageMat, new Size(5, 5), 0);
        Imgproc.threshold(imageMat, imageMat, 0, 255, Imgproc.THRESH_OTSU);

        Utils.matToBitmap(imageMat, bmp);

        return bmp;
    }

    /**
     * Method for getting thumbnail from black-and-white image
     *
     * @param bmp - black-and-white image
     * @param rows - number of thumbnail rows
     * @param cols - number of thumbnail columns
     * @param p - fill parameter
     * @return thumbnail
     */
    private static Field getThumbnail(Bitmap bmp, int rows, int cols, int p) {
        Field field = new Field(rows, cols);
        int height = bmp.getHeight(), width = bmp.getWidth();
        int h = height / rows, w = width / cols;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sumColors = 0;
                for (int x = j * w; x < (j + 1) * w; x++) {
                    for (int y = i * h; y < (i + 1) * h; y++) {
                        int color = bmp.getPixel(x, y);
                        if (color == Colors.WHITE) {
                            sumColors += 255;
                        }
                    }
                }
                int avgColor = sumColors / (w * h);
                if (avgColor <= p) {
                    field.setColor(i, j, Colors.BLACK);
                }
            }
        }
        return field;
    }

}
