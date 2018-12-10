package com.brook.app.android.supportlibrary.util;

import android.content.Context;
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

    private DependentLayoutConfig(Context context) {
        Util.setContext(context);
    }

    public static DependentLayoutConfig getInstance(Context context) {
        if (config == null) {
            synchronized (DependentLayoutConfig.class) {
                if (config == null) {
                    config = new DependentLayoutConfig(context);
                }
            }
        }
        return config;
    }

    // 设计图的宽度
    private String designWidth = "750px";
    // 设计图的高度
    private String designHeight = "1294px";

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

    private Map<Class<? extends View>, ViewAdapter> viewAdapterMap = new HashMap();

    /**
     *
     * @param target 需要是配的View的Class对象
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
