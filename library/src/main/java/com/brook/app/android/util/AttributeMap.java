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

package com.brook.app.android.util;

import android.graphics.Color;
import android.util.AttributeSet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Brook
 * @time 2018/12/8 10:35
 * @target DependencyLayout
 */
public class AttributeMap {

    private Map<String, String> map;

    public AttributeMap(AttributeSet attributeSet) {
        map = new HashMap<>();
        int attributeCount = attributeSet.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String key = attributeSet.getAttributeName(i);
            String value = attributeSet.getAttributeValue(i);
            map.put(key, value);
        }
    }

    public String getString(String index) {
        return map.get(index);
    }

    public float getDimension(String index, float defVal) {
        String source = getString(index);
        String value = Util.getValue(index);
        try {
            String unit = source.substring(value.length(), source.length());
            if (unit.equals("dip")) {
                return Util.dp2px(Util.getValueToFloat(value));
            } else if (unit.equals("sp")) {
                return Util.sp2px(Util.getValueToFloat(value));
            } else if (unit.equals("px")) {
                return Util.getValueToFloat(value);
            } else {
                return defVal;
            }
        } catch (Exception e) {
            return defVal;
        }
    }

    public int getInt(String index, int defVal) {
        try {
            return Integer.parseInt(getString(index));
        } catch (Exception e) {
            return defVal;
        }
    }

    public int getResourceId(String index) {
        try {
            String value = Util.getValue(getString(index));
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getColor(String index, int defValue) {
        try {
            return Color.parseColor(getString(index));
        } catch (Exception e) {
            return defValue;
        }
    }

    public int getColor(String index) {
        return getColor(index, 0);
    }

    public boolean getBoolean(String index, boolean defValue) {
        try {
            return Boolean.parseBoolean(getString(index));
        } catch (Exception e) {
            return defValue;
        }
    }
}
