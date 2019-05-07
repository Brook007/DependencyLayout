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

import com.brook.app.android.view.DependencyLayout;

/**
 * @author Brook
 * @time 2018/12/8 16:15
 * @target DependencyLayout
 */
public class Metrics {
    // 父View设计的宽度
    public DependencyLayout.LayoutParams.Attribute parentDesignWidth;
    // 父View设计的高度
    public DependencyLayout.LayoutParams.Attribute parentDesignHeight;
    // 屏幕宽度
    public int screenWidth;
    // 屏幕高度
    public int screenHeight;
    // 父view的宽度
    public int parentWidth;
    // 父View的高度
    public int parentHeight;
}
