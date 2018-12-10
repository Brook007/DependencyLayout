package com.brook.app.android.supportlibrary.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;

import com.brook.app.android.supportlibrary.util.AttributeMap;
import com.brook.app.android.supportlibrary.util.Metrics;
import com.brook.app.android.supportlibrary.util.Util;

/**
 * @author Brook
 * @time 2018/12/5 15:58
 * @target DependentLayout
 */
public class TextViewImpl implements ViewAdapter<TextView> {

    private String designWidthUnit = null;
    private float designWidth = 0;

    @Override
    public void convert(Context context, TextView view, AttributeMap attr, Metrics metrics) {
        // 获取XML中的属性原始值，字符串形式，大小写敏感
        String textSize = attr.getString("textSize");
        if (textSize != null) {
            String value = Util.getValue(textSize);
            String unit = textSize.substring(value.length());
            if (designWidthUnit == null) {
                String designValue = Util.getValue(metrics.parentDesignWidth);
                designWidth = Float.parseFloat(designValue);
                designWidthUnit = metrics.parentDesignWidth.substring(designValue.length());
            }
            float size = 0;
            if (unit.equals(designWidthUnit)) {
                // 尺寸单位相同
                float px = Float.parseFloat(value);
                size = px * (metrics.screenWidth / designWidth);
            } else if (unit.equals("sp") || unit.equals("dip")) {
                float px;
                if (designWidthUnit.equals("dp")) {
                    // dp
                    px = Float.parseFloat(value);
                } else {
                    // px
                    px = Util.sp2px(Float.parseFloat(value));
                }
                size = px * (metrics.screenWidth / designWidth);
            } else {
                float px = Float.parseFloat(value);
                size = px * (metrics.screenWidth / designWidth);
            }
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }

    }
}
