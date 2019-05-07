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

import android.view.View;

import com.android.annotations.NonNull;
import com.brook.app.android.adapter.ViewAdapter;
import com.brook.app.android.view.DependencyLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Brook
 * @time 2018/12/5 16:00
 * @target DependencyLayout
 */
public class DependencyLayoutConfig {

    private static DependencyLayoutConfig config;

    // 设计图的宽度
    private DependencyLayout.LayoutParams.Attribute designWidth = new DependencyLayout.LayoutParams.Attribute("750px");
    // 设计图的高度
    private DependencyLayout.LayoutParams.Attribute designHeight = new DependencyLayout.LayoutParams.Attribute("1294px");

    private Map<Class<? extends View>, ViewAdapter> viewAdapterMap = new HashMap<Class<? extends View>, ViewAdapter>();

    private DependencyLayoutConfig() {

    }

    public static DependencyLayoutConfig getInstance() {
        if (config == null) {
            synchronized (DependencyLayoutConfig.class) {
                if (config == null) {
                    config = new DependencyLayoutConfig();
                }
            }
        }
        return config;
    }

    public String getDesignWidthSource() {
        return designWidth.toString();
    }

    public DependencyLayout.LayoutParams.Attribute getDesignWidth() {
        return designWidth;
    }

    public DependencyLayoutConfig setDesignWidth(String designWidth) {
        this.designWidth = new DependencyLayout.LayoutParams.Attribute(designWidth);
        return this;
    }

    public DependencyLayout.LayoutParams.Attribute getDesignHeight() {
        return designHeight;
    }

    public DependencyLayoutConfig setDesignHeight(String designHeight) {
        this.designHeight = new DependencyLayout.LayoutParams.Attribute(designHeight);
        return this;
    }

    public String getDesignHeightSource() {
        return designHeight.toString();
    }

    /**
     * @param target      需要是配的View的Class对象
     * @param viewAdapter 实现了ViewAdapter接口的实现类
     * @return
     */
    public DependencyLayoutConfig addViewAdapterHandler(Class<? extends View> target, @NonNull ViewAdapter viewAdapter) {
        viewAdapterMap.put(target, viewAdapter);
        return this;
    }

    public Map<Class<? extends View>, ViewAdapter> getViewAdapterMap() {
        return viewAdapterMap;
    }
}
