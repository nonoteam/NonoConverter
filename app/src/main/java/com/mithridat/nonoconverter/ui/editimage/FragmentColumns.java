package com.mithridat.nonoconverter.ui.editimage;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;

import com.mithridat.nonoconverter.R;

import static com.mithridat.nonoconverter.backend.ImageConverter.getBlackWhite;

/**
 * Class for the columns fragment
 */
public class FragmentColumns extends Fragment implements OnSeekBarChangeListener, OnClickListener, OnCheckedChangeListener {

    /**
     * Tag for count of columns
     */
    private static final String COUNT_COLUMNS_TAG = "countColumnsFragment";

    /**
     * Tag for count of rows
     */
    private static final String COUNT_ROWS_TAG = "countRowsFragment";

    /**
     * Tag for the inversion
     */
    private static final String IS_INVERT_TAG = "isInvertFragment";

    /**
     * Empty string
     */
    private static final String EMPTY_STRING = "";

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
     * Seekbar for changing columns count
     */
    private SeekBar _sbColumns;

    /**
     * Grid drawer
     */
    private Panel _panel;

    /**
     * Switch for invertion
     */
    private Switch _sInvert;

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
     * True if want invert
     */
    private boolean _isInvert = false;

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
                        (EditImageActivity) getActivity();
                int old_columns = editImageActivity._columns;
                editImageActivity._isInvert = _sInvert.isChecked();
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
        switch (v.getId()) {
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
        outState.putByte(IS_INVERT_TAG,
                _sInvert.isChecked() ? (byte) 1 : (byte) 0);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        _isSaved = false;
        View vColumnsFragment =
                inflater.inflate(R.layout.fragment_columns, null);
        _panel = vColumnsFragment.findViewById(R.id.panel);
        _civColumns = vColumnsFragment.findViewById(R.id.image_view_columns);
        _civColumns.setParent(this);
        _sbColumns = vColumnsFragment.findViewById(R.id.seek_bar_rows);
        _sbColumns.setOnSeekBarChangeListener(this);
        _bmpImageColumns = ((EditImageActivity) getActivity())._bmpCurrentImage;

        _tvRowsAndColumns =
                vColumnsFragment.findViewById(R.id.text_view_exact_and_range);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(_tvRowsAndColumns,
                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        vColumnsFragment.findViewById(R.id.button_add)
                .setOnClickListener(this);
        vColumnsFragment.findViewById(R.id.button_remove)
                .setOnClickListener(this);
        _sInvert = vColumnsFragment.findViewById(R.id.switch_invert);
        _isInvert = ((EditImageActivity) getActivity())._isInvert;
        _coefBitmap =
                _bmpImageColumns.getHeight() * 1f / _bmpImageColumns.getWidth();
        if (savedInstanceState != null) {
            _countColumns =
                    savedInstanceState.getInt(COUNT_COLUMNS_TAG, 0);
            _countRows =
                    savedInstanceState.getInt(COUNT_ROWS_TAG, 0);
            _isInvert =
                    savedInstanceState.getByte(IS_INVERT_TAG, (byte) 0) != 0;
            setOutTextViewRowsAndColumnsText();
            _isSaved = true;
        }
        _sInvert.setOnCheckedChangeListener(this);
        return vColumnsFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        _isSaved = true;
        _isInvert = _sInvert.isChecked();
    }

    @Override
    public void onResume() {
        super.onResume();
        _sInvert.setChecked(_isInvert);
        _civColumns.setImageSimple(getBlackWhite(_bmpImageColumns, _isInvert));
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
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        _civColumns.setImageComp(getBlackWhite(_bmpImageColumns, isChecked));
    }

    /**
     * Set sizes of drew image and compute current
     *
     * @param width  - width of the image
     * @param height - height of the image
     */
    void setCustomImageViewSizes(int width, int height) {
        _imageViewWidth = width;
        _imageViewHeight = height;
        setSizes();
    }

    /**
     * Recompute countRows, redraw sizes and grid
     */
    private void updateScreenElements() {
        _countRows = (int) (_countColumns * _coefBitmap);
        setOutTextViewRowsAndColumnsText();
        _panel.setGridSizes(_countRows, _countColumns);
        _panel.invalidate();
    }

    /**
     * Set text to the _tvRowsAndColumns
     */
    private void setOutTextViewRowsAndColumnsText() {
        int remainder = Math.round(_countColumns / 10f);
        int bmWidth = _bmpImageColumns.getWidth();
        String text = constructString(getContext(), remainder, bmWidth);
        _tvRowsAndColumns.setText(text);
    }

    /**
     * Constructs string for text view
     *
     * @param context   - context
     * @param remainder - half of range
     * @param width     - width of image
     * @return string with converting params
     */
    private String constructString(Context context, int remainder, int width) {
        String stringArg1, stringArg4, stringArg5, stringArg6;
        int orientation = getResources().getConfiguration().orientation;
        if (context == null)
            return EMPTY_STRING;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            stringArg1 = EMPTY_STRING;
            stringArg4 = "\n";
        } else {
            stringArg1 = getString(R.string.title_exact_columns);
            stringArg4 = getString(R.string.title_range_columns);
        }

        if (remainder == 0) {
            stringArg5 = String.valueOf(_countColumns);
            stringArg6 = String.valueOf(_countRows);

        } else {
            int maxColumns = (_countColumns + remainder) > 90
                    ? 90 : (_countColumns + remainder);
            maxColumns = maxColumns > width ? width : maxColumns;
            int minColumns = (_countColumns - remainder) < 5
                    ? 5 : (_countColumns - remainder);
            int maxRowsRound = (int) (maxColumns * _coefBitmap);

            stringArg5 =
                    String.valueOf(minColumns)
                            + "\u00F7"
                            + String.valueOf(maxColumns);

            stringArg6 =
                    String.valueOf((int) (minColumns * _coefBitmap))
                            + "\u00F7"
                            + String.valueOf(maxRowsRound);
        }

        return context.getString(R.string.title_columns_frame,
                stringArg1,
                _countColumns,
                _countRows,
                stringArg4,
                stringArg5,
                stringArg6);
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
        float propCoefficient;
        if (_isScreenWidth) {
            _width = _imageViewWidth;
            propCoefficient = _width / _bmpImageColumns.getWidth();
            _height = _bmpImageColumns.getHeight() * propCoefficient;
            _startHeight = (_imageViewHeight - _height) / 2;
            _startWidth = 0;
        } else {
            _height = _imageViewHeight;
            propCoefficient = _height / _bmpImageColumns.getHeight();
            _width = _bmpImageColumns.getWidth() * propCoefficient;
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
            _sbColumns.setMax(bmWidth - 5);
        } else {
            _sbColumns.setMax(85);
        }
        if (!_isSaved) {
            _countColumns = ((EditImageActivity) getActivity())._columns;
            if (bmWidth < 90) {
                if (_countColumns > bmWidth) {
                    _countColumns = bmWidth;
                }
            }
            _countRows = (int) (_countColumns * _coefBitmap);
            setOutTextViewRowsAndColumnsText();
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
