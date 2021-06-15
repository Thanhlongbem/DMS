package com.ziperp.dms.utils.swipedelete;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

public class Utils {
    private Utils() {
    }

    /** Lightweight choice to {@link Math#round(float)} */
    public static int roundFloat(float value) {
        return (int) (value > 0 ? value + 0.5f : value - 0.5f);
    }

    /** Lightweight choice to {@link Math#round(double)} */
    public static long roundDouble(double value) {
        return (long) (value > 0 ? value + 0.5 : value - 0.5);
    }

    public static boolean isLayoutRtl(@NonNull View view) {
        return ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }
}
