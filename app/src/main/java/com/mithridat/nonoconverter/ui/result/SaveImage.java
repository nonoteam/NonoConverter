package com.mithridat.nonoconverter.ui.result;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Class for saving bitmap by type (thumbnail or nonogram)
 */
class SaveImage {

    /**
     * Method for saving bitmap
     *
     * @param source - saved bitmap
     * @param type - type of image (thumbnail or nonogram)
     * @return true, if image was saved
     *         false, otherwise
     */
    static boolean saveImage(Bitmap source, String type) {
        File root = getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        String name = getTitle(type) + ".jpg";
        File file = new File(root, name);
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            source.compress(Bitmap.CompressFormat.JPEG,100, stream);
            stream.flush();
            stream.close();

        } catch (IOException e) {
            if (stream != null) closeStream(stream);
            return false;
        }
        return true;
    }

    /**
     * Method for creating title of image by type
     *
     * @param type - type of image (thumbnail or nonogram)
     * @return title of image
     */
    private static String getTitle(String type) {
        Date date = new Date();
        DateFormat dateFormat =
                new SimpleDateFormat("yyyymmdd_hhmmss", Locale.US);
        String strDate = dateFormat.format(date);
        return type + "_" + strDate;
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
