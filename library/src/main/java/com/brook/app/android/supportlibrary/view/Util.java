package com.brook.app.android.supportlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Brook
 * @time 2018/10/8 16:49
 * @target DependentLayout
 */
public class Util {
    /*
     * p -> parent 父View
     * m -> myself 自身
     * s -> screen 屏幕
     * w -> width 宽度
     * h -> height 高度
     *
     * pw ph
     * mw mh
     * sw sh
     */

    public static String getValue(String text) {
        Pattern pattern = Pattern.compile("(^-\\d+\\.\\d+)|(^\\d+\\.\\d+)|(^\\d+)|(^-\\d+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public static float getValueToFloat(String text) {
        return Float.parseFloat(getValue(text));
    }

    public static String getText(TypedArray typedArray, @StyleableRes int index) {
        CharSequence temp = typedArray.getText(index);
        if (temp == null) {
            return null;
        } else {
            return temp.toString();
        }
    }

    /**
     * dp转px
     *
     * @param dpVal
     * @return
     */
    public static float dp2px(Context context, float dpVal) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (dpVal * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        final float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spVal * fontScale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity);
    }
}
