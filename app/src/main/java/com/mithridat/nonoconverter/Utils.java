package com.mithridat.nonoconverter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import com.google.android.material.snackbar.Snackbar;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;
import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.mithridat.nonoconverter.backend.nonogram.Field.BLACK;
import static com.mithridat.nonoconverter.backend.nonogram.Field.ROW;

/**
 * Class with some helpful functions
 */
public class Utils {

    /**
     * Identifier of the PNG format file
     */
    public static final String PNG_TYPE = ".png";

    /**
     * Method for getting width of the text by specific paint
     *
     * @param paint - the Paint for drawing
     * @param text - the text which width is unknown
     * @return width of the text
     */
    public static int getWidthText(Paint paint, String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.left + bounds.width();
    }

    /**
     * Method for getting height of the text by specific paint
     *
     * @param paint - the Paint for drawing
     * @param text - the text which height is unknown
     * @return height of the text
     */
    public static int getHeightText(Paint paint, String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.bottom + bounds.height();
    }

    /**
     * Method for drawing grid on the canvas
     *
     * @param canvas - the Canvas
     * @param pos - position of the left-top corner of the grid on the canvas
     * @param sizes - sizes of the grid
     * @param cellSize - size of the one cell
     * @param linesWidth - width of the lines
     * @param border - is it needs to draw bold border
     */
    public static void drawGrid(
            Canvas canvas,
            PointF pos,
            Point sizes,
            float cellSize,
            float linesWidth,
            boolean border) {
        if (canvas == null || pos == null || sizes == null) return;
        RectF grid = getRectFGrid(cellSize, cellSize);
        Paint p = new Paint();
        p.setColor(BLACK);
        p.setStrokeWidth(linesWidth);
        p.setStyle(Paint.Style.STROKE);
        grid.offsetTo(pos.x, pos.y);
        for (int i = 0; i < sizes.y; i++) {
            for (int j = 0; j < sizes.x; j++) {
                canvas.drawRect(grid, p);
                grid.offset(cellSize, 0);
            }
            grid.offset(0, cellSize);
            grid.offsetTo(pos.x, grid.top);
        }
        if (border) {
            grid = getRectFGrid(sizes.x * cellSize, sizes.y * cellSize);
            grid.offsetTo(pos.x, pos.y);
            p.setStrokeWidth(2 * linesWidth);
            canvas.drawRect(grid, p);
        }
    }

    /**
     * Method for creating rectangle for grid lines
     *
     * @param w - width of the one cell
     * @param h - height of the one cell
     * @return rectangle
     */
    public static RectF getRectFGrid(float w, float h) {
        RectF grid = new RectF();
        grid.top = 0;
        grid.left = 0;
        grid.bottom = h;
        grid.right = w;
        return grid;
    }

    /**
     * Method for drawing lines on the canvas
     *
     * @param canvas - the Canvas
     * @param pos - the position of the left-top corner of the first line on
     *            the canvas
     * @param distance - the distance between lines
     * @param length - the length of the one line
     * @param linesWidth - the width of the lines
     * @param count - the count of the lines
     * @param color - the color of the lines
     * @param type - the type of the lines
     */
    public static void drawLines(
            Canvas canvas,
            PointF pos,
            float distance,
            float length,
            float linesWidth,
            int count,
            int color,
            int type) {
        if (canvas == null || pos == null) return;
        final boolean isRow = type == ROW;
        final float dx = isRow ? 0f : distance;
        final float dy = isRow ? distance : 0f;
        final float dposx = isRow ? length : 0f;
        final float dposy = isRow ? 0f : length;
        Paint p = new Paint();
        p.setColor(color);
        p.setStrokeWidth(linesWidth);
        for (int i = 0; i < count; i++) {
            canvas.drawLine(pos.x,
                    pos.y,
                    pos.x + dposx,
                    pos.y + dposy,
                    p);
            pos.offset(dx, dy);
        }
    }

    /**
     * Sleep exact time
     *
     * @param time - time in milliseconds
     */
    public static void sleepMillisec(int time) {
        try {
            Thread.sleep(time);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Method for getting string resource by its id
     *
     * @param context - the Context
     * @param id - id of string resource
     * @return - string
     */
    public static String getString(Context context, int id) {
        return context.getString(id);
    }

    /**
     * Method for getting color resource by its id
     *
     * @param context - the Context
     * @param id - id of color resource
     * @return color
     */
    public static int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    /**
     * Method for correct closing stream
     *
     * @param stream - stream to close
     */
    public static void closeStream(OutputStream stream) {
        try {
            stream.close();
        } catch (IOException e) {}
    }

    /**
     * Method for creating title of file with date
     *
     * @param title - title of file without date
     * @return title of file with date
     */
    public static String getTitle(String title) {
        String strDate =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                        .format(new Date());
        return title + "_" + strDate;
    }

    /**
     * Method for sharing image
     *
     * @param context - the Context
     * @param source - shared bitmap
     * @param title - title of image (thumbnail or nonogram)
     * @return true, if image was prepared for the sharing
     *         false, otherwise
     */
    public static boolean shareImage(
            Context context,
            Bitmap source,
            String title) {
        File file;
        OutputStream stream = null;
        try {
            file =
                    File.createTempFile(getTitle(title),
                            PNG_TYPE,
                            context.getCacheDir());
            stream = new FileOutputStream(file);
            source.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            if (stream != null) closeStream(stream);
            return false;
        }
        Uri uri =
                FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file);
        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/png");
        context.startActivity(Intent.createChooser(intent,
                getString(context, R.string.msg_share_image)));
        return true;
    }

    /**
     * Method for adding image to the gallery
     *
     * @param context - instance of the Context class
     * @param contentUri - uri of the image file
     */
    public static void addPicToGallery(Context context, Uri contentUri) {
        Intent mediaScanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * Method for saving bitmap
     *
     * @param context - the Context
     * @param source - saved bitmap
     * @param title - type of image (thumbnail or nonogram)
     * @return path of the image, if image was saved
     *         null, otherwise
     */
    public static String saveImage(
            Context context,
            Bitmap source,
            String title) {
        File root = getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        String name = getTitle(title) + PNG_TYPE;
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
        return file.getPath();
    }

    /**
     * Method for opening image by another application
     *
     * @param context - the Context
     * @param path - path of the image
     */
    public static void openImage(Context context, String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(path)),
                "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent,
                getString(context, R.string.msg_open_image)));
    }

    /**
     * Method for getting snackbar
     *
     * @param activity - the Activity
     * @param text - text of the snackbar
     * @param id - id of CoordinatorLayout
     * @return Snackbar
     */
    public static Snackbar getSnackbar(Activity activity, String text, int id) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(id),
                text,
                Snackbar.LENGTH_LONG)
                .setActionTextColor(getColor(activity, R.color.colorWhite));
        View snackbarView = snackbar.getView();
        TextView textView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        return snackbar;
    }
  
    /**
     * Inner class for async converting image in background.
     */
    public static class AsyncTaskPublish extends AsyncTask<Void, Integer, Nonogram> {

        @Override
        protected Nonogram doInBackground(Void... voids) {
            return null;
        }

        /**
         * Call publish progress
         *
         * @param progress - current progress
         */
        public void publish(int progress) {
            publishProgress(progress);
        }
    }

    /**
     * Class for correct work of FileProvider
     */
    public static class MyFileProvider extends FileProvider {}

}
