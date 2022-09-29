package com.jnt.guide;

import android.content.Context;

public class GuideUtils {
    public static int convertDpToPx(Context context, int value) { return (int) (value * context.getResources().getDisplayMetrics().density); }

    public static int getNoneDimen(Context context) { return convertDpToPx(context, 0); }

    public static int getExtraTightDimen(Context context) { return convertDpToPx(context, 2); }

    public static int getTightestDimen(Context context) { return convertDpToPx(context, 4); }

    public static int getTighterDimen(Context context) { return convertDpToPx(context, 8); }

    public static int getTightDimen(Context context) { return convertDpToPx(context, 12); }

    public static int getBaseDimen(Context context) { return convertDpToPx(context, 16); }
}
