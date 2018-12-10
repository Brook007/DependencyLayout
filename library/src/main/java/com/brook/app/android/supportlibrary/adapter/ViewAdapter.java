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
     * @param context
     * @param view
     * @param attr
     * @param metrics
     */
    void convert(Context context, T view, AttributeMap attr, Metrics metrics);
}
