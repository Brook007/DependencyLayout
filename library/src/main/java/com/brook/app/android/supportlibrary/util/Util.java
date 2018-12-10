package com.brook.app.android.supportlibrary.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;
import android.view.View;

import com.brook.app.android.supportlibrary.view.DependentLayout;

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
     * parentWidth parentHeight
     * mw mh
     * screenWidth screenHeight
     */

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    public static void setContext(Context context) {
        sContext = context.getApplicationContext();
    }

    public static String getValue(String text) {
        Pattern pattern = Pattern.compile("(^-\\d+\\.\\d+)|(^\\d+\\.\\d+)|(^\\d+)|(^-\\d+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    /**
     * 仅支持xx%screenWidth,xx%sh,xx%pw,xx%ph,xx%mw,xx%mh
     * xx%sw（xx,xxdp,xxpx,xxdip）
     * xx%sh
     * xx%pw
     * xx%ph
     * xx%mw
     * xx%mh
     */
    public static float calculation(String source, String pdw, String pdh, float screenWidth, float screenHeight, View parent, View child, int orientation) {
        float size = 0;
        float pw = parent.getMeasuredWidth();
        float ph = parent.getMeasuredHeight();
        float mw = child.getMeasuredWidth();
        float mh = child.getMeasuredHeight();

        if (source.endsWith("%sw")) {
            size = Util.getValueToFloat(source) / 100F * screenWidth;
        } else if (source.endsWith("%sh")) {
            size = Util.getValueToFloat(source) / 100F * screenHeight;
        } else if (source.endsWith("%pw")) {
            size = Util.getValueToFloat(source) / 100F * pw;
        } else if (source.endsWith("%ph")) {
            size = Util.getValueToFloat(source) / 100F * ph;
        } else if (source.endsWith("%mw")) {
            size = Util.getValueToFloat(source) / 100F * mw;
        } else if (source.endsWith("%mh")) {
            size = Util.getValueToFloat(source) / 100F * mh;
        } else if (source.endsWith("px")) {
            if (orientation == DependentLayout.VERTICAL) {
                if (pdh.endsWith("px")) {
                    size = ph / Util.getValueToFloat(pdh) * Util.getValueToFloat(source);
                } else if (pdh.endsWith("dp") || pdh.endsWith("dip")) {
                    size = ph / Util.dp2px(Util.getValueToFloat(pdh)) * Util.getValueToFloat(source);
                }
            } else {
                if (pdw.endsWith("px")) {
                    size = pw / Util.getValueToFloat(pdw) * Util.getValueToFloat(source);
                } else if (pdw.endsWith("dp") || pdw.endsWith("dip")) {
                    size = pw / Util.dp2px(Util.getValueToFloat(pdw)) * Util.getValueToFloat(source);
                }
            }
        } else if (source.endsWith("dp") || source.endsWith("dip")) {
            if (orientation == DependentLayout.VERTICAL) {
                if (pdh.endsWith("px")) {
                    size = ph / Util.getValueToFloat(pdh) * Util.dp2px(Util.getValueToFloat(source));
                } else if (pdh.endsWith("dp") || pdh.endsWith("dip")) {
                    size = ph / Util.getValueToFloat(pdh) * Util.getValueToFloat(source);
                }
            } else {
                if (pdw.endsWith("px")) {
                    size = pw / Util.getValueToFloat(pdw) * Util.dp2px(Util.getValueToFloat(source));
                } else if (pdw.endsWith("dp") || pdw.endsWith("dip")) {
                    size = pw / Util.getValueToFloat(pdw) * Util.getValueToFloat(source);
                }
            }
        } else if (source.matches("^\\d+$")) {
            if (orientation == DependentLayout.VERTICAL) {
                size = ph / Util.getValueToFloat(pdh) * Util.getValueToFloat(source);
            } else {
                size = pw / Util.getValueToFloat(pdw) * Util.getValueToFloat(source);
            }
        } else {
            throw new IllegalArgumentException("参数错误");
        }
        return size;
    }

    public static float getValueToFloat(String text) {
        try {
            return Float.parseFloat(getValue(text));
        } catch (Exception e) {
            return 0;
        }
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
    public static float dp2px(float dpVal) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (dpVal * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spVal
     * @return
     */
    public static int sp2px(float spVal) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spVal * fontScale + 0.5f);
    }
}
