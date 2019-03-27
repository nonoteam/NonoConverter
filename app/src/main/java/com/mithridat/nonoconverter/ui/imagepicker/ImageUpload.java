package com.mithridat.nonoconverter.ui.imagepicker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;

import com.mithridat.nonoconverter.R;

import androidx.core.content.ContextCompat;

import static android.graphics.Bitmap.Config.RGB_565;

/**
 * Class for working with ImagePicker
 */
public class ImageUpload {

    /**
     * Method for getting color from resources as string
     *
     * @param activity - activity that needs for getting resources
     * @param id - id of color from resources
     * @return color as string, for example #47525E
     */
    public static String getColor(Activity activity, int id) {
        return String.format("#%06x",
                ContextCompat.getColor(activity, id) & 0xffffff);
    }

    /**
     * Method for starting ImagePicker activity
     *
     * @param activity - activity on that ImagePicker is started
     * @param requestCode - request code for calling ImagePicker activity
     */
    public static void startImagePicker(Activity activity, int requestCode) {
        String white = getColor(activity, R.color.colorWhite);
        ImagePicker.with(activity)
                .setToolbarColor(getColor(activity, R.color.colorPrimary))
                .setStatusBarColor(getColor(activity, R.color.colorPrimaryDark))
                .setToolbarTextColor(white)
                .setToolbarIconColor(white)
                .setProgressBarColor(white)
                .setBackgroundColor(getColor(activity, R.color.colorRiverBed))
                .setCameraOnly(false)
                .setMultipleMode(false)
                .setMaxSize(1)
                .setRequestCode(requestCode)
                .start();
    }

    /**
     * Method for getting bitmap from path of the image,
     * taking into account the rotation
     *
     * @param path - path of the image
     * @return image as bitmap
     */
    public static Bitmap getBitmapFromPath(String path) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPreferredConfig = RGB_565;
        bmOptions.inDither = true;
        Matrix matrix = new Matrix();
        matrix.postRotate(getRotation(path));
        Bitmap bitmapOrg = null, image = null;
        try {
            bitmapOrg = BitmapFactory.decodeFile(path, bmOptions);
            image = Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(),
                    bitmapOrg.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            if (bitmapOrg != null) {
                image = bitmapOrg;
            }
        }
        return image;
    }

    /**
     * Method for getting rotation of the image
     *
     * @param path - path of the image
     * @return rotation of the image
     */
    private static int getRotation(String path) {
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

}
