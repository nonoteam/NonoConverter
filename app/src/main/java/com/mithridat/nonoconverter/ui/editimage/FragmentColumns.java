package com.mithridat.nonoconverter.ui.editimage;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.mithridat.nonoconverter.R;

/**
 * Class for the columns fragment
 */
public class FragmentColumns extends Fragment implements SeekBar.OnSeekBarChangeListener {

    /**
     * ImageVies for columns fragment
     */
    ImageView _ivColumns;

    /**
     * Bitmap for columns fragment
     */
    Bitmap _bitmap;

    /**
     * TextView for current number of columns and rows count
     */
    TextView _tvRowsAndColumns;

    /**
     * View of columns fragment
     */
    View _viewColumnsFragment;

    /**
     * Seekbar for changing columns count
     */
    SeekBar _seekBar;

    /**
     * Grid drawer
     */
    Panel _panel;

    /**
     * Image width
     */
    int _width;

    /**
     * Image height
     */
    int _height;

    /**
     * Where start draw in OY
     */
    int _start_h;

    /**
     * Where start draw in OX
     */
    int _start_w;

    /**
     * Columns count
     */
    int _count_columns;

    /**
     * Rows count
     */
    int _count_rows;

    /**
     * Boolean for orientation
     */
    boolean _is_landscape = false;

    /**
     * Proportional coefficient
     */
    double _coef_a;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_image_columns, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("_count_columns", _count_columns);
        outState.putInt("_count_rows", _count_rows);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _viewColumnsFragment = inflater.inflate(R.layout.fragment_columns, null);
        boolean is_saved = false;
        _panel = _viewColumnsFragment.findViewById(R.id.panel);
        _ivColumns = _viewColumnsFragment.findViewById(R.id.image_view_columns);
        _seekBar = _viewColumnsFragment.findViewById(R.id.seek_bar_Rows);
        _seekBar.setOnSeekBarChangeListener(this);

        EditImageActivity editImageActivity = (EditImageActivity)getActivity();
        _bitmap = editImageActivity._bmpCurrentImage;

        _ivColumns.setImageBitmap(_bitmap);
        _tvRowsAndColumns = _viewColumnsFragment.findViewById(R.id.text_view_Rows);

        if (savedInstanceState != null) {
            _count_columns = savedInstanceState.getInt("_count_columns", 0);
            _count_rows = savedInstanceState.getInt("_count_rows", 0);
            _tvRowsAndColumns
                    .setText(
                            String.valueOf(_count_columns) + "," + String.valueOf(_count_rows));
            is_saved = true;
        }

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) _is_landscape = true;

        computeSizes(is_saved);
        return _viewColumnsFragment;
    }

    /**
     * Compute sizes in onCreateView
     *
     * @param is_saved true if SavedInstance != null
     */
    public void computeSizes(boolean is_saved) {
        Context context = getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        int resourceId = getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        int status_bar_height = getResources().getDimensionPixelSize(resourceId);

        Point size = new Point();
        display.getSize(size);
        int screen_width = size.x;
        int screen_height = size.y;

        TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int tb_height = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        if (!_is_landscape){
            //tb_height = 147;
            //_coef_a = screen_width*1.0/(dimensions.outWidth*1.0);
            _coef_a = screen_width*1.0/(_bitmap.getWidth()*1.0);
            _width = screen_width;
            _height = (int)(_bitmap.getHeight() * _coef_a);
            _start_h =
                    (int)((screen_height - tb_height - status_bar_height - 60*metrics.density*1.0 - _height)*1.0/2.0);
            _start_w = 0;
        }
        else {
            //tb_height = 126;
            _coef_a = (screen_height - tb_height - status_bar_height)*1.0/(_bitmap.getHeight()*1.0);
            _width = (int)(_bitmap.getWidth() * _coef_a );
            _height = screen_height - tb_height - status_bar_height;
            _start_h = 0;//(int)((_ivColumns.getHeight() - _height)*1.0/2.0);
            _start_w = (int)((screen_width - 90*metrics.density*1.0 - _width)*1.0/2.0);
        }

        if (!is_saved)
        {
            _count_columns = 5;
            _count_rows = (int)(_count_columns * _height * 1.0 / (_width * 1.0));
            _tvRowsAndColumns
                    .setText(String.valueOf(_count_columns) + "," + String.valueOf(_count_rows));
        }
        _panel.setLengths(_start_w, _start_h, _width, _height, _count_rows, _count_columns);
    }

    /**
     * Compute sizes befor showing image
     */
    public void showImage() {
        Context context = getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        Point size = new Point();
        display.getSize(size);

        if (!_is_landscape) {
           // _coef_a = screen_width*1.0/(dimensions.outWidth*1.0);
            _start_h = (int)((_ivColumns.getHeight() - _height)*1.0/2.0);
            _start_w = 0;
        }
        else {
            _start_w = (int)((_ivColumns.getWidth() - _width)*1.0/2.0);
            _start_h = 0;
        }

        _count_rows = (int)(_count_columns * _height * 1.0 / (_width * 1.0));
        _panel.setLengths(_start_w, _start_h, _width, _height, _count_rows, _count_columns);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        _count_columns = (seekBar.getProgress()+1)*5;
        _count_rows = (int)(_count_columns * _height * 1.0 / (_width * 1.0));
        _tvRowsAndColumns.setText(String.valueOf(_count_columns));
        _tvRowsAndColumns.append(", ");
        _tvRowsAndColumns.append(String.valueOf(_count_rows));
        showImage();
        ((EditImageActivity)getActivity()).setSizes(_count_rows, _count_columns);
        Panel panel = _viewColumnsFragment.findViewById(R.id.panel);
        panel.doDraw(_start_w, _start_h, _width, _height, _count_rows, _count_columns);
    }
}
