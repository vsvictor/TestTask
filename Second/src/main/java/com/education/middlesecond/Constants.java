package com.education.middlesecond;

import android.net.Uri;

public class Constants {
    public static final String TABLE_LINKS = "links";
    public static final String AUTHORITY = "com.education.middle";
    public static final Uri URI_LINKS = Uri.parse("content://" + AUTHORITY + "/" + TABLE_LINKS);

}
