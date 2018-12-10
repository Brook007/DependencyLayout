package com.brook.app.android.supportlibrary.adapter;

import android.content.Context;
import android.view.View;

import com.brook.app.android.supportlibrary.util.AttributeMap;
import com.brook.app.android.supportlibrary.util.Metrics;

/**
 * See also {@link TextViewImpl}
 *
 * @author Brook
 * @time 2018/12/5 15:54
 * @target DependentLayout
 */
public interface ViewAdapter<T extends View> {
    /**
     * View的属性适配
     *
     * @param context context对象
     * @param view    需要适配的View的实例
     * @param attr    XML配置的属性
     * @param metrics 配置信息
     */
    void convert(Context context, T view, AttributeMap attr, Metrics metrics);
}
