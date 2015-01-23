package com.liang.albums.util;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by liang on 15/1/23.
 */
public class ViewPagerUtil {
    public static int dpToPx(Resources res, int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }
}
