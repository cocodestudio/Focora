package com.cocode.focora.utils;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;

public final class FocoraUtils {
    private FocoraUtils() {}

    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float spToPx(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static boolean isReducedMotionEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            float scale = android.provider.Settings.Global.getFloat(
                    context.getContentResolver(),
                    android.provider.Settings.Global.ANIMATOR_DURATION_SCALE, 1f
            );
            return scale == 0f;
        }
        return false;
    }

    public static long resolveAnimDuration(Context context, boolean respectReducedMotion, long defaultMs) {
        if (respectReducedMotion && isReducedMotionEnabled(context)) return 0L;
        return defaultMs;
    }
}