package com.brook.app.android.supportlibrary.util;

import android.view.View;

import com.brook.app.android.supportlibrary.adapter.ViewAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Brook
 * @time 2018/12/5 16:00
 * @target DependentLayout
 */
public class DependentLayoutConfig {

    private static DependentLayoutConfig config;

    // 设计图的宽度
    private String designWidth = "750px";
    // 设计图的高度
    private String designHeight = "1294px";

    private Map<Class<? extends View>, ViewAdapter> viewAdapterMap = new HashMap();

    private DependentLayoutConfig() {

    }

    public static DependentLayoutConfig getInstance() {
        if (config == null) {
            synchronized (DependentLayoutConfig.class) {
                if (config == null) {
                    config = new DependentLayoutConfig();
                }
            }
        }
        return config;
    }

    public String getDesignWidth() {
        return designWidth;
    }

    public DependentLayoutConfig setDesignWidth(String designWidth) {
        this.designWidth = designWidth;
        return this;
    }

    public String getDesignHeight() {
        return designHeight;
    }

    public DependentLayoutConfig setDesignHeight(String designHeight) {
        this.designHeight = designHeight;
        return this;
    }

    /**
     * @param target      需要是配的View的Class对象
     * @param viewAdapter 实现了ViewAdapter接口的实现类
     * @return
     */
    public DependentLayoutConfig addViewAdapterHandler(Class<? extends View> target, ViewAdapter viewAdapter) {
        viewAdapterMap.put(target, viewAdapter);
        return this;
    }

    public Map<Class<? extends View>, ViewAdapter> getViewAdapterMap() {
        return viewAdapterMap;
    }
}
