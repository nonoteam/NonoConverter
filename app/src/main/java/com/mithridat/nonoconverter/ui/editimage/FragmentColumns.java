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
import android.view.MenuItem;
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
     * Tag for fragment columns
     */
    private static final String COUNT_COLUMNS_TAG = "countColumns";

    /**
     * Tag for fragment main
     */
    private static final String COUNT_ROWS_TAG = "countRows";

    /**
     * Tag for fragment main
     */
    private static final String IDENTIFIER_NAME = "status_bar_height";

    /**
     * Tag for fragment main
     */
    private static final String IDENTIFIER_DEF_TYPE = "dimen";

    /**
     * Tag for fragment main
     */
    private static final String IDENTIFIER_DEF_PACKAGE = "android";

    /**
     * Comma
     */
    private static final String COMMA = ", ";

    /**
     * ImageVies for columns fragment
     */
    private ImageView _ivColumns;

    /**
     * Bitmap for columns fragment
     */
    private Bitmap _bmpImageColumns;

    /**
     * TextView for current number of columns and rows count
     */
    private TextView _tvRowsAndColumns;

    /**
     * View of columns fragment
     */
    private View _vColumnsFragment;

    /**
     * Seekbar for changing columns count
     */
    private SeekBar _sbColumns;

    /**
     * Grid drawer
     */
    private Panel _panel;

    /**
     * Image width
     */
    private int _width;

    /**
     * Image height
     */
    private int _height;

    /**
     * Where start draw in OY
     */
    private int _startHeight;

    /**
     * Where start draw in OX
     */
    private int _startWidth;

    /**
     * Columns count
     */
    private int _countColumns;

    /**
     * Rows count
     */
    private int _countRows;

    /**
     * Boolean for orientation
     */
    private boolean _isLandscape = false;

    /**
     * Proportional coefficient
     */
    private double _propCoefficient;

    /**
     * Width of the Image View
     */
    private int _imageViewWidth;

    /**
     * Height of the Image View
     */
    private int _imageViewHeight;

    /**
     * True if image fulls all ivColumns width
     */
    private boolean _isScreenWidth;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                EditImageActivity editImageActivity =
                        (EditImageActivity)getActivity();
                editImageActivity.setSizes(_countRows, _countColumns);
                editImageActivity._isSelectedColumns = true;
                editImageActivity.getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNT_COLUMNS_TAG, _countColumns);
        outState.putInt(COUNT_ROWS_TAG, _countRows);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean isSaved = false;
        _vColumnsFragment =
                inflater.inflate(R.layout.fragment_columns, null);
        _panel = _vColumnsFragment.findViewById(R.id.panel);
        _ivColumns = _vColumnsFragment.findViewById(R.id.image_view_columns);
        _sbColumns = _vColumnsFragment.findViewById(R.id.seek_bar_Rows);
        _sbColumns.setOnSeekBarChangeListener(this);
        _bmpImageColumns = ((EditImageActivity)getActivity())._bmpCurrentImage;

        _ivColumns.setImageBitmap(_bmpImageColumns);
        _tvRowsAndColumns = _vColumnsFragment
                .findViewById(R.id.text_view_Rows);

        if (savedInstanceState != null) {
            _countColumns = savedInstanceState.getInt(COUNT_COLUMNS_TAG,
                    0);
            _countRows = savedInstanceState.getInt(COUNT_ROWS_TAG,
                    0);
            _tvRowsAndColumns.setText(String.format("%s%s%s",
                    String.valueOf(_countColumns),
                    COMMA,
                    String.valueOf(_countRows)));
            isSaved = true;
        }

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) _isLandscape = true;

        setSizes(isSaved);
        return _vColumnsFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        _sbColumns.setProgress(_countColumns / 5 - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _sbColumns.setProgress(0);
    }

    @Override
    public void onProgressChanged(
            SeekBar seekBar,
            int progress,
            boolean fromUser) {}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        _countColumns = (seekBar.getProgress() + 1) * 5;
        _countRows = _countColumns * _height / _width;
        _tvRowsAndColumns.setText(String.format("%s%s%s",
                String.valueOf(_countColumns),
                COMMA,
                String.valueOf(_countRows)));
        showImage();
        _panel.invalidate();
    }

    /**
     * Compare view rectangle with bitmap rectangle
     *
     * @return true if views length ratio bigger than bitmaps
     */
    private boolean isScreenWidth() {
        double coefView = _imageViewWidth *1.0/(_imageViewHeight *1.0);
        double coefBitmap = _bmpImageColumns.getWidth() * 1.0
                / (_bmpImageColumns.getHeight() * 1.0);
        return !(coefView > coefBitmap);
    }

    /**
     * Compute sizes of drawing grid
     */
    private void computeSizes() {
        if(_isScreenWidth) {
            _width = _imageViewWidth;
            _propCoefficient =
                    _width * 1.0 / (_bmpImageColumns.getWidth() * 1.0);
            _height = (int)(_bmpImageColumns.getHeight() * _propCoefficient);
            _startHeight = (_imageViewHeight - _height)/2;
            _startWidth = 0;
        } else {
            _height = _imageViewHeight;
            _propCoefficient =
                    (_height) * 1.0 / (_bmpImageColumns.getHeight() * 1.0);
            _width = (int)(_bmpImageColumns.getWidth() * _propCoefficient);
            _startHeight = 0;
            _startWidth = (_imageViewWidth - _width) / 2;
        }
    }

    /**
     * Compute sizes in onCreateView
     *
     * @param isSaved - true if SavedInstance != null
     */
    private void setSizes(boolean isSaved) {
        Context context = getContext();
        if (context == null) return;
        WindowManager wm =
                (WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        int resourceId = getResources()
                .getIdentifier(IDENTIFIER_NAME,
                        IDENTIFIER_DEF_TYPE,
                        IDENTIFIER_DEF_PACKAGE);
        int status_bar_height =
                getResources().getDimensionPixelSize(resourceId);

        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        TypedArray styledAttributes = getContext().getTheme()
                .obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int tb_height =
                (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        if (!_isLandscape){
            _imageViewWidth = screenWidth;
            _imageViewHeight = screenHeight - tb_height - status_bar_height
                    - (int)(60 * metrics.density);
        }
        else {
            _imageViewWidth = (int)(screenWidth - 90 * metrics.density);
            _imageViewHeight = screenHeight - tb_height - status_bar_height;
        }

        _isScreenWidth = isScreenWidth();
        computeSizes();

        if (!isSaved) {
            _countColumns = ((EditImageActivity)getActivity())._columns;

            if (_countColumns == 0) _countColumns = 5;

            _countRows = (int)(_countColumns * _height * 1.0 / (_width * 1.0));
            _tvRowsAndColumns.setText(String.format("%s%s%s",
                    String.valueOf(_countColumns),
                    COMMA,
                    String.valueOf(_countRows)));
        }
        _panel.setLengths(_startWidth,
                _startHeight,
                _width,
                _height,
                _countRows,
                _countColumns);
    }

    /**
     * Compute sizes before showing image
     */
    private void showImage() {
        _imageViewWidth = _ivColumns.getWidth();
        _imageViewHeight = _ivColumns.getHeight();

        computeSizes();

        _countRows = (int)(_countColumns * _height * 1.0 / (_width * 1.0));
        _panel.setLengths(_startWidth,
                _startHeight,
                _width,
                _height,
                _countRows,
                _countColumns);
    }
}
