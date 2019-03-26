package com.mithridat.nonoconverter.ui;

/**
 * Class with constants for requests activities.
 */
public class ActivitiesConstants {

    /**
     * Request code for starting ImagePicker activity from StartActivity.
     */
    public static final int RC_PICK_IMAGE_START = 1;

    /**
     * Request code for starting ImagePicker activity from EditImageActivity.
     */
    public static final int RC_PICK_IMAGE_EDIT_IMAGE = 2;

    /**
     * Name of extra for putting path of image in intent.
     */
    public static final String EX_IMAGE_PATH = "ImagePath";

    /**
     * Name of extra for putting nonogram field of image in intent.
     */
    public static final String EX_NONO_FIELD = "NonoField";

}
