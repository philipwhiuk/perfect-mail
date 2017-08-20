package com.whiuk.philip.perfect.helper;

import android.content.Context;
import android.util.TypedValue;

import com.whiuk.philip.perfect.PerfectMail;
import com.whiuk.philip.perfect.R;
import com.whiuk.philip.perfect.activity.misc.ContactPictureLoader;

public class ContactPicture {

    public static ContactPictureLoader getContactPictureLoader(Context context) {
        final int defaultBgColor;
        if (!PerfectMail.isColorizeMissingContactPictures()) {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.contactPictureFallbackDefaultBackgroundColor,
                    outValue, true);
            defaultBgColor = outValue.data;
        } else {
            defaultBgColor = 0;
        }

        return new ContactPictureLoader(context, defaultBgColor);
    }
}
