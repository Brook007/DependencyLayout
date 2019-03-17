/*
 * Copyright (c) 2016-present, Brook007 Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brook.app.android.supportlibrary.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;

import com.brook.app.android.supportlibrary.view.DependencyLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Brook
 * @time 2018/10/8 16:49
 * @target DependencyLayout
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

    private static Pattern pattern = Pattern.compile("(^-\\d+\\.\\d+)|(^\\d+\\.\\d+)|(^\\d+)|(^-\\d+)");

    public static String getValue(String text) {
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
    public static float calculation(DependencyLayout.LayoutParams.Attribute source, DependencyLayout.LayoutParams.Attribute pdw, DependencyLayout.LayoutParams.Attribute pdh, float screenWidth, float screenHeight, View parent, View child, int orientation) {
        float size = 0;
        float parentWidth = parent.getMeasuredWidth();
        float parentHeight = parent.getMeasuredHeight();
        float myWidth = child.getMeasuredWidth();
        float myHeight = child.getMeasuredHeight();

        if (source.unit == DependencyLayout.LayoutParams.Attribute.SW) {
            size = source.value / 100F * screenWidth;
        } else if (source.unit == DependencyLayout.LayoutParams.Attribute.SH) {
            size = source.value / 100F * screenHeight;
        } else if (source.unit == DependencyLayout.LayoutParams.Attribute.PW) {
            size = source.value / 100F * parentWidth;
        } else if (source.unit == DependencyLayout.LayoutParams.Attribute.PH) {
            size = source.value / 100F * parentHeight;
        } else if (source.unit == DependencyLayout.LayoutParams.Attribute.MW) {
            size = source.value / 100F * myWidth;
        } else if (source.unit == DependencyLayout.LayoutParams.Attribute.MH) {
            size = source.value / 100F * myHeight;
        } else if (source.unit == DependencyLayout.LayoutParams.Attribute.PX) {
            if (orientation == DependencyLayout.VERTICAL) {
                if (pdh.unit == DependencyLayout.LayoutParams.Attribute.PX) {
                    size = parentHeight / pdh.value * source.value;
                } else if (pdh.unit == DependencyLayout.LayoutParams.Attribute.DIP) {
                    size = parentHeight / Util.dp2px(pdh.value) * source.value;
                }
            } else {
                if (pdw.unit == DependencyLayout.LayoutParams.Attribute.PX) {
                    size = parentWidth / pdw.value * source.value;
                } else if (pdw.unit == DependencyLayout.LayoutParams.Attribute.DIP) {
                    size = parentWidth / Util.dp2px(pdw.value) * source.value;
                }
            }
        } else if (source.unit == DependencyLayout.LayoutParams.Attribute.DIP) {
            if (orientation == DependencyLayout.VERTICAL) {
                if (pdh.unit == DependencyLayout.LayoutParams.Attribute.PX) {
                    size = parentHeight / pdh.value * Util.dp2px(source.value);
                } else if (pdh.unit == DependencyLayout.LayoutParams.Attribute.DIP) {
                    size = parentHeight / pdh.value * source.value;
                }
            } else {
                if (pdw.unit == DependencyLayout.LayoutParams.Attribute.PX) {
                    size = parentWidth / pdw.value * Util.dp2px(source.value);
                } else if (pdw.unit == DependencyLayout.LayoutParams.Attribute.DIP) {
                    size = parentWidth / pdw.value * source.value;
                }
            }
        } else if (source.unit == DependencyLayout.LayoutParams.Attribute.ERROR) {
            if (orientation == DependencyLayout.VERTICAL) {
                size = parentHeight / pdh.value * source.value;
            } else {
                size = parentWidth / pdw.value * source.value;
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

    public static float toFloat(String text) {
        try {
            return Float.parseFloat(text);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param typedArray
     * @param index      @StyleableRes
     * @return
     */
    public static String getText(TypedArray typedArray, int index) {
        CharSequence temp = typedArray.getText(index);
        if (temp == null) {
            return null;
        } else {
            return temp.toString();
        }
    }


    public static DependencyLayout.LayoutParams.Attribute unbox(String text) {
        if (text == null) {
            return null;
        }
        DependencyLayout.LayoutParams.Attribute attribute = new DependencyLayout.LayoutParams.Attribute();
        String value = getValue(text);
        attribute.value = toFloat(value);
        String unit = text.substring(value.length()).toLowerCase();
        if ("dip".equals(unit) || "dp".equals(unit)) {
            attribute.unit = DependencyLayout.LayoutParams.Attribute.DIP;
        } else if ("sp".equals(unit)) {
            attribute.unit = DependencyLayout.LayoutParams.Attribute.SP;
        } else if ("px".equals(unit)) {
            attribute.unit = DependencyLayout.LayoutParams.Attribute.PX;
        } else if ("%sw".equals(unit)) {
            attribute.unit = DependencyLayout.LayoutParams.Attribute.SW;
        } else if ("%sh".equals(unit)) {
            attribute.unit = DependencyLayout.LayoutParams.Attribute.SH;
        } else if ("%pw".equals(unit)) {
            attribute.unit = DependencyLayout.LayoutParams.Attribute.PW;
        } else if ("%ph".equals(unit)) {
            attribute.unit = DependencyLayout.LayoutParams.Attribute.PH;
        } else if ("%mw".equals(unit)) {
            attribute.unit = DependencyLayout.LayoutParams.Attribute.MW;
        } else if ("%mh".equals(unit)) {
            attribute.unit = DependencyLayout.LayoutParams.Attribute.MH;
        } else {
            attribute.unit = DependencyLayout.LayoutParams.Attribute.ERROR;
        }
        return attribute;
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
    public static float sp2px(float spVal) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (spVal * fontScale + 0.5f);
    }
}
