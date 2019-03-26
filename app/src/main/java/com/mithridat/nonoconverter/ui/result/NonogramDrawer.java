package com.mithridat.nonoconverter.ui.result;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mithridat.nonoconverter.backend.Field;

public class NonogramDrawer extends SurfaceView implements SurfaceHolder.Callback {

    private NonogramDrawThread _nngDrawThread;
    private int _minMarginHor = 50;
    private int _minMarginVer = 250;

    public NonogramDrawer(Context context) {
        super(context);
        _nngDrawThread = new NonogramDrawThread(getHolder());
        getHolder().addCallback(this);
    }

    public NonogramDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        _nngDrawThread = new NonogramDrawThread(getHolder());
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        _nngDrawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        //resize
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        threadShutdown();
    }

    public void setNonogramField(Field nonogramField) {
        int cellSizeHor = (getDeviceWidth(getContext()) - 2 * _minMarginHor)
                / nonogramField.getCols();
        int cellSizeVer = (getDeviceHeight(getContext()) - 2 * _minMarginVer)
                / nonogramField.getRows();
        int cellSize = Math.min(cellSizeHor, cellSizeVer);

        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = cellSize * nonogramField.getCols()
                + 2 * _nngDrawThread.LINES_WIDTH;
        params.height = cellSize * nonogramField.getRows()
                + 2 * _nngDrawThread.LINES_WIDTH;
        setLayoutParams(params);

        _nngDrawThread.setNonogramField(nonogramField);
        _nngDrawThread.setCellSize(cellSize);
    }

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        return width;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

    private void threadShutdown() {
        _nngDrawThread.shutdown();
        while (true) {
            try {
                _nngDrawThread.join();
                break;
            } catch (InterruptedException e) {
                //exception here means that drawing thread is still running
            }
        }
    }
}



