package com.mithridat.nonoconverter.ui.result;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Class for saving bitmap by type (thumbnail or nonogram)
 */
class ImageSaver {

    /**
     * Identifier of the PNG format file
     */
    private static final String PNG_TYPE = ".png";

    /**
     * Underscore symbol
     */
    private static final String UNDERSCORE = "_";

    /**
     * Space symbol
     */
    private static final String SPACE = " ";

    /**
     * Method for saving bitmap
     *
     * @param source - saved bitmap
     * @param type - type of image (thumbnail or nonogram)
     * @return true, if image was saved
     *         false, otherwise
     */
    static String saveImage(Context context, Bitmap source, String type) {
        File root = getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        String name = getTitle(type) + PNG_TYPE;
        File file = new File(root, name);
        Uri contentUri = Uri.fromFile(file);
        OutputStream stream = null;
        try {
            root.mkdirs();
            stream = new FileOutputStream(file, false);
            source.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            addPicToGallery(context, contentUri);
        } catch (IOException e) {
            if (stream != null) closeStream(stream);
            return null;
        }
        return SPACE + file.getPath();
    }

    /**
     * Method for creating title of image by type
     *
     * @param type - type of image (thumbnail or nonogram)
     * @return title of image
     */
    private static String getTitle(String type) {
        String strDate =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                        .format(new Date());
        return type + UNDERSCORE + strDate;
    }

    /**
     * Method for adding image to the gallery
     *
     * @param context - instance of the Context class
     * @param contentUri - uri of the image file
     */
    private static void addPicToGallery(Context context, Uri contentUri) {
        Intent mediaScanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * Method for correct closing stream
     *
     * @param stream - stream to close
     */
    private static void closeStream(OutputStream stream) {
        try {
            stream.close();
        } catch (IOException e) {}
    }
}
