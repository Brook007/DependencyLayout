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

package com.brook.app.android.supportlibrary.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;

import com.brook.app.android.supportlibrary.util.AttributeMap;
import com.brook.app.android.supportlibrary.util.Metrics;
import com.brook.app.android.supportlibrary.util.Util;
import com.brook.app.android.supportlibrary.view.DependencyLayout;

/**
 *
 * @author Brook
 * @time 2018/12/5 15:58
 * @target DependencyLayout
 */
public class TextViewImpl implements ViewAdapter<TextView> {

    private DependencyLayout.LayoutParams.Attribute designWidth = null;

    @Override
    public void convert(Context context, TextView view, AttributeMap attr, Metrics metrics) {
        // fixme 计算方式有待调整
        // 获取XML中的属性原始值，字符串形式，大小写敏感
        String textSize = attr.getString("textSize");
        if (textSize != null) {
            DependencyLayout.LayoutParams.Attribute attribute = Util.unbox(textSize);
            if (designWidth == null) {
                designWidth = metrics.parentDesignWidth;
            }
            float size = 0;
            if (attribute.unit == designWidth.unit) {
                // 尺寸单位相同
                float px = attribute.value;
                size = px * (metrics.screenWidth / designWidth.value);
            } else if (attribute.unit == DependencyLayout.LayoutParams.Attribute.SP
                    || attribute.unit == DependencyLayout.LayoutParams.Attribute.DIP) {
                float px;
                if (designWidth.unit == DependencyLayout.LayoutParams.Attribute.DIP) {
                    // dp
                    px = attribute.value;
                } else {
                    // px
                    px = Util.sp2px(attribute.value);
                }
                size = px * (metrics.screenWidth / designWidth.value);
            } else {
                size = attribute.value * (metrics.screenWidth / designWidth.value);
            }
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
    }
}
