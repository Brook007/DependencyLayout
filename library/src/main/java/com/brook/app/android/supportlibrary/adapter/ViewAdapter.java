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
import android.view.View;

import com.brook.app.android.supportlibrary.util.AttributeMap;
import com.brook.app.android.supportlibrary.util.Metrics;

/**
 * See also {@link TextViewImpl}
 *
 * @author Brook
 * @time 2018/12/5 15:54
 * @target DependencyLayout
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
