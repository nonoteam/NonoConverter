package com.mithridat.nonoconverter.ui.editimage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import androidx.fragment.app.Fragment;

import com.mithridat.nonoconverter.R;

/**
 * Class for the columns fragment
 */
public class FragmentColumns extends Fragment implements OnSeekBarChangeListener, OnClickListener {

    /**
     * Tag for fragment columns
     */
    private static final String COUNT_COLUMNS_TAG = "countColumns";

    /**
     * Tag for fragment main
     */
    private static final String COUNT_ROWS_TAG = "countRows";

    /**
     * Comma
     */
    private static final String COMMA = ", ";

    /**
     * ImageVies for columns fragment
     */
    private CustomImageView _civColumns;

    /**
     * Bitmap for columns fragment
     */
    private Bitmap _bmpImageColumns;

    /**
     * TextView for current number of columns and rows count
     */
    private TextView _tvRowsAndColumns;

    /**
     * TextView for current number of columns and rows count
     */
    private TextView _tvOutRowsAndColumns;

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
    private float _width;

    /**
     * Image height
     */
    private float _height;

    /**
     * Where start draw in OY
     */
    private float _startHeight;

    /**
     * Where start draw in OX
     */
    private float _startWidth;

    /**
     * Columns count
     */
    private int _countColumns;

    /**
     * Rows count
     */
    private int _countRows;

    /**
     * Proportional coefficient
     */
    private float _propCoefficient;

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

    /**
     * True if sizes were saved
     */
    private boolean _isSaved;

    /**
     * Proportional coefficient
     */
    private float _coefBitmap;

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
                int old_columns = editImageActivity._columns;
                editImageActivity._isSelectedColumns = true;
                if (old_columns != _countColumns) {
                    editImageActivity.setSizes(_countRows, _countColumns);
                }
                editImageActivity.getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_add:
                if (_countColumns < _sbColumns.getMax() + 5) {
                    _sbColumns.setProgress(_sbColumns.getProgress() + 1);
                }
                break;
            case R.id.button_remove:
                if (_countColumns > 5) {
                    _sbColumns.setProgress(_sbColumns.getProgress() - 1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNT_COLUMNS_TAG, _countColumns);
        outState.putInt(COUNT_ROWS_TAG, _countRows);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        _isSaved = false;
        _vColumnsFragment =
                inflater.inflate(R.layout.fragment_columns, null);
        _panel = _vColumnsFragment.findViewById(R.id.panel);
        _civColumns = _vColumnsFragment.findViewById(R.id.image_view_columns);
        _civColumns.setParent(this);
        _sbColumns = _vColumnsFragment.findViewById(R.id.seek_bar_rows);
        _sbColumns.setOnSeekBarChangeListener(this);
        _bmpImageColumns = ((EditImageActivity)getActivity())._bmpCurrentImage;

        _civColumns.setImageBitmap(_bmpImageColumns);
        _tvRowsAndColumns =
                _vColumnsFragment.findViewById(R.id.text_view_exact);
        _tvOutRowsAndColumns =
                _vColumnsFragment.findViewById(R.id.text_view_range);

        _vColumnsFragment.findViewById(R.id.button_add)
                .setOnClickListener(this);
        _vColumnsFragment.findViewById(R.id.button_remove)
                .setOnClickListener(this);

        _coefBitmap =
                _bmpImageColumns.getHeight() * 1f / _bmpImageColumns.getWidth();
        if (savedInstanceState != null) {
            _countColumns = savedInstanceState.getInt(COUNT_COLUMNS_TAG,
                    0);
            _countRows = savedInstanceState.getInt(COUNT_ROWS_TAG,
                    0);
            setTextViews();
            _isSaved = true;
        }
        return _vColumnsFragment;
    }

    @Override
    public void onProgressChanged(
            SeekBar seekBar,
            int progress,
            boolean fromUser) {
        if (_width == 0) return;
        _countColumns = (progress + 5);
        updateScreenElements();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    /**
     * Set sizes of drew image and compute current
     *
     * @param width - width of the image
     * @param height - height of the image
     */
    public void setCustomImageViewSizes(int width, int height) {
        _imageViewWidth = width;
        _imageViewHeight = height;
        setSizes();
    }

    /**
     * Recompute countRows, redraw sizes and grid
     */
    private void updateScreenElements()
    {
        _countRows = (int) (_countColumns * _coefBitmap);
        setTextViews();
        _panel.setGridSizes(_countRows, _countColumns);
        _panel.invalidate();
    }

    /**
     * Set columns and rows count to the _tvRowsAndColumns
     */
    private void setTextViews() {
        setTextViewRowsAndColumnsText();
        setOutTextViewRowsAndColumnsText();
    }

    /**
     * Set columns and rows count to the _tvRowsAndColumns
     */
    private void setTextViewRowsAndColumnsText() {
        _tvRowsAndColumns.setText(String.format("%s%s%s",
                String.valueOf(_countColumns),
                COMMA,
                String.valueOf(_countRows)));
    }

    /**
     * Set columns and rows count to the _tvOutRowsAndColumns
     */
    private void setOutTextViewRowsAndColumnsText() {
        int remainder = Math.round(_countColumns / 10f);
        int bmWidth = _bmpImageColumns.getWidth();
        if (remainder == 0) {
            _tvOutRowsAndColumns.setText(String.format("%s%s%s",
                    String.valueOf(_countColumns),
                    COMMA,
                    String.valueOf(_countRows)));
        } else {
            int maxColumns = (_countColumns + remainder) > 90
                    ? 90 : (_countColumns + remainder);
            maxColumns = maxColumns > bmWidth ? bmWidth : maxColumns;
            int minColumns = (_countColumns - remainder) < 5
                    ? 5 : (_countColumns - remainder);
            int maxRowsRound = (int) (maxColumns * _coefBitmap);
            _tvOutRowsAndColumns.setText(String.format("%s%s%s%s%s%s%s",
                    String.valueOf(minColumns),
                    "\u00F7",
                    String.valueOf(maxColumns),
                    COMMA,
                    String.valueOf((int) (minColumns * _coefBitmap)),
                    "\u00F7",
                    String.valueOf(maxRowsRound)));
        }
    }

    /**
     * Compare view rectangle with bitmap rectangle
     *
     * @return true if views length ratio bigger than bitmaps
     */
    private boolean isScreenWidth() {
        double coefView = _imageViewWidth * 1.0 / _imageViewHeight;
        double coefBitmap =
                _bmpImageColumns.getWidth() * 1.0 / _bmpImageColumns.getHeight();
        return !(coefView > coefBitmap);
    }

    /**
     * Compute sizes of drawing grid
     */
    private void computeSizes() {
        if(_isScreenWidth) {
            _width = _imageViewWidth;
            _propCoefficient = _width / _bmpImageColumns.getWidth();
            _height = _bmpImageColumns.getHeight() * _propCoefficient;
            _startHeight = (_imageViewHeight - _height) / 2;
            _startWidth = 0;
        } else {
            _height = _imageViewHeight;
            _propCoefficient = _height / _bmpImageColumns.getHeight();
            _width = _bmpImageColumns.getWidth() * _propCoefficient;
            _startHeight = 0;
            _startWidth = (_imageViewWidth - _width) / 2;
        }
    }

    /**
     * Compute sizes in onCreateView
     */
    private void setSizes() {
        _isScreenWidth = isScreenWidth();
        computeSizes();
        int bmWidth = _bmpImageColumns.getWidth();
        if (bmWidth < 90) {
            _sbColumns.setMax(bmWidth-5);
        } else {
            _sbColumns.setMax(85);
        }
        if (!_isSaved) {
            _countColumns = ((EditImageActivity)getActivity())._columns;
            if (bmWidth < 90) {
                if (_countColumns > bmWidth) {
                    _countColumns = bmWidth;
                }
            }
            _countRows = (int) (_countColumns * _coefBitmap);
            setTextViews();
        }
        _panel.setLengths(_startWidth,
                _startHeight,
                _width,
                _height,
                _countRows,
                _countColumns);
        _sbColumns.setProgress(_countColumns - 5);
    }
}
