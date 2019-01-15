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

package com.brook.app.android.supportlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.brook.app.android.supportlibrary.adapter.ViewAdapter;
import com.brook.app.android.supportlibrary.dependentlayout.R;
import com.brook.app.android.supportlibrary.util.AttributeMap;
import com.brook.app.android.supportlibrary.util.DependentLayoutConfig;
import com.brook.app.android.supportlibrary.util.Metrics;
import com.brook.app.android.supportlibrary.util.Util;

import java.util.Map;


/**
 * @author Brook
 * @time 2018/9/26 10:41
 * @target DependentLayout
 */
public class DependentLayout extends ViewGroup {

    /*
     * 1、取出属性值
     * 2、计算依赖关系
     *      1)、对父View依赖关系
     *      2)、对兄弟View依赖关系
     * 3、计算值
     * 4、测量自身
     */

    // 设计图的宽度
    private String mDesignWidth;
    // 设计图的高度
    private String mDesignHeight;

    // 屏幕的宽度
    private int screenWidth;
    // 屏幕的宽度
    private int screenHeight;

    //方向
    // 垂直方向
    public static final int VERTICAL = 0;
    // 水平方向
    public static final int HORIZONTAL = 1;

    // 左边距的设置参数
    private String paddingLeftSource;
    // 上边距的设置参数
    private String paddingTopSource;
    // 右边距的设置参数
    private String paddingRightSource;
    // 下边距的设置参数
    private String paddingBottomSource;

    // 系统左边距的值
    private float systemPaddingLeft;
    // 系统上边距的值
    private float systemPaddingTop;
    // 系统右边距的值
    private float systemPaddingRight;
    // 系统下边距的值
    private float systemPaddingBottom;

    // 最终的左边距结果
    private int paddingLeft;
    // 最终的上边距结果
    private int paddingTop;
    // 最终的右边距结果
    private int paddingRight;
    // 最终的下边距结果
    private int paddingBottom;

    // 设置WarpContent时的最大宽度
    private float maxWidth;
    // 设置WarpContent时最大的高度
    private float maxHeight;

    private Metrics metrics;

    public DependentLayout(Context context) {
        this(context, null);
    }

    public DependentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DependentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        this.metrics = new Metrics();

        Util.setContext(this.getContext());

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DependentLayout);
            String designWidth = typedArray.getString(R.styleable.DependentLayout_designWidth);
            if (designWidth == null) {
                mDesignWidth = DependentLayoutConfig.getInstance().getDesignWidth();
            } else {
                mDesignWidth = designWidth;
            }
            String designHeight = typedArray.getString(R.styleable.DependentLayout_designHeight);
            if (designHeight == null) {
                mDesignHeight = DependentLayoutConfig.getInstance().getDesignHeight();
            } else {
                mDesignHeight = designHeight;
            }
            String padding = typedArray.getString(R.styleable.DependentLayout_dependency_padding);
            if (padding != null) {
                paddingLeftSource = paddingTopSource = paddingRightSource = paddingBottomSource = padding;
            } else {
                paddingLeftSource = typedArray.getString(R.styleable.DependentLayout_dependency_paddingLeft);
                paddingTopSource = typedArray.getString(R.styleable.DependentLayout_dependency_paddingTop);
                paddingRightSource = typedArray.getString(R.styleable.DependentLayout_dependency_paddingRight);
                paddingBottomSource = typedArray.getString(R.styleable.DependentLayout_dependency_paddingBottom);
            }

            float systemPadding = typedArray.getDimension(R.styleable.DependentLayout_android_padding, 0);
            if (systemPadding > 0) {
                systemPaddingLeft = systemPaddingTop = systemPaddingRight = systemPaddingBottom = typedArray.getDimension(R.styleable.DependentLayout_android_padding, 0);
            } else {
                systemPaddingLeft = typedArray.getDimension(R.styleable.DependentLayout_android_paddingLeft, 0);
                systemPaddingTop = typedArray.getDimension(R.styleable.DependentLayout_android_paddingTop, 0);
                systemPaddingRight = typedArray.getDimension(R.styleable.DependentLayout_android_paddingRight, 0);
                systemPaddingBottom = typedArray.getDimension(R.styleable.DependentLayout_android_paddingBottom, 0);
            }
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 自身的宽度
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        // 自身的高度
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            // 宽度warp_content， 高度必须指定
            this.setMeasuredDimension(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY));
        } else if (heightMode != MeasureSpec.EXACTLY) {
            // 高度warp_content， 宽度必须指定
            this.setMeasuredDimension(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.AT_MOST));
        } else {
            // 两个都是指定
            this.setMeasuredDimension(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY));
        }

        if (paddingLeftSource != null) {
            paddingLeft = (int) Util.calculation(paddingLeftSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, this, HORIZONTAL);
        } else {
            paddingLeft = (int) systemPaddingLeft;
        }

        if (paddingTopSource != null) {
            paddingTop = (int) Util.calculation(paddingTopSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, this, HORIZONTAL);
        } else {
            paddingTop = (int) systemPaddingTop;
        }

        if (paddingRightSource != null) {
            paddingRight = (int) Util.calculation(paddingRightSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, this, HORIZONTAL);
        } else {
            paddingRight = (int) systemPaddingRight;
        }

        if (paddingBottomSource != null) {
            paddingBottom = (int) Util.calculation(paddingBottomSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, this, HORIZONTAL);
        } else {
            paddingBottom = (int) systemPaddingBottom;
        }

        metrics.parentDesignWidth = mDesignWidth;
        metrics.parentDesignHeight = mDesignHeight;
        metrics.parentHeight = getMeasuredHeight();
        metrics.parentWidth = getMeasuredWidth();
        metrics.screenWidth = screenWidth;
        metrics.screenHeight = screenHeight;

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            // 宽度warp_content， 高度必须指定
            int widthMakeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (maxWidth + getPaddingRight()), MeasureSpec.EXACTLY);
            this.setMeasuredDimension(widthMakeMeasureSpec, heightMeasureSpec);
        } else if (heightMode != MeasureSpec.EXACTLY) {
            // 高度warp_content， 宽度必须指定
            int heightMakeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (maxHeight + getPaddingBottom()), MeasureSpec.EXACTLY);
            this.setMeasuredDimension(widthMeasureSpec, heightMakeMeasureSpec);
        }
    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        maxWidth = 0;
        maxHeight = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() == View.GONE) {
                layoutParams.resultWidth = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
                layoutParams.resultHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            } else {
                // 需要先临时测量一下子View的宽高
                if (layoutParams.selfWidth == null && layoutParams.selfHeight == null) {
                    // 没有设置自定义宽高，使用系统的宽高
                    if (layoutParams.systemWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                        if (layoutParams.systemHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
                            layoutParams.resultHeight = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY);
                        } else if (layoutParams.systemHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
                            layoutParams.resultHeight = MeasureSpec.makeMeasureSpec(screenHeight, MeasureSpec.AT_MOST);
                        } else {
                            // 高度设置了指定值
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
                            layoutParams.resultHeight = MeasureSpec.makeMeasureSpec((int) layoutParams.systemHeight, MeasureSpec.EXACTLY);
                        }
                    } else if (layoutParams.systemWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                        if (layoutParams.systemHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.AT_MOST);
                            layoutParams.resultHeight = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY);
                        } else if (layoutParams.systemHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.AT_MOST);
                            layoutParams.resultHeight = MeasureSpec.makeMeasureSpec(screenHeight, MeasureSpec.AT_MOST);
                        } else {
                            // 高度设置了指定值
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.AT_MOST);
                            layoutParams.resultHeight = MeasureSpec.makeMeasureSpec((int) layoutParams.systemHeight, MeasureSpec.EXACTLY);
                        }
                    } else {
                        // 指定了宽度值
                        if (layoutParams.systemHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec((int) layoutParams.systemWidth, MeasureSpec.EXACTLY);
                            layoutParams.resultHeight = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY);
                        } else if (layoutParams.systemHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec((int) layoutParams.systemWidth, MeasureSpec.EXACTLY);
                            layoutParams.resultHeight = MeasureSpec.makeMeasureSpec(screenHeight, MeasureSpec.AT_MOST);
                        } else {
                            // 高度和宽度设置了指定值
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec((int) layoutParams.systemWidth, MeasureSpec.EXACTLY);
                            layoutParams.resultHeight = MeasureSpec.makeMeasureSpec((int) layoutParams.systemHeight, MeasureSpec.EXACTLY);
                        }
                    }
                } else {
                    /*
                     * 仅支持xxxdp,xxxpx,xxxdip,xx%sw,xx%sh,xx%pw,xx%ph,xx%mw,xx%mh
                     */
                    // 设置自定义宽高
                    if (layoutParams.selfWidth == null) {
                        // layoutParams.selfHeight != null
                        // 指定了自定义高度，未指定宽度
                        if (layoutParams.systemWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
                        } else if (layoutParams.systemWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.AT_MOST);
                        } else {
                            // 指定了宽度值
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec((int) layoutParams.systemWidth, MeasureSpec.EXACTLY);
                        }
                        //                    child.measure(layoutParams.resultWidth, MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.AT_MOST));
                        // 计算高度
                        layoutParams.resultHeight = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfHeight, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                        //                    child.measure(layoutParams.resultWidth, layoutParams.resultHeight);
                    } else {
                        if (layoutParams.selfHeight == null) {
                            // 指定了自定义宽度，未指定高度
                            if (layoutParams.systemHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                                layoutParams.resultHeight = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY);
                            } else if (layoutParams.systemHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                                layoutParams.resultHeight = MeasureSpec.makeMeasureSpec(screenHeight, MeasureSpec.AT_MOST);
                            } else {
                                // 指定了宽度值
                                layoutParams.resultHeight = MeasureSpec.makeMeasureSpec((int) layoutParams.systemHeight, MeasureSpec.EXACTLY);
                            }
                            //                        child.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), layoutParams.resultHeight);
                            // 计算高度
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfWidth, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                            //                    child.measure(layoutParams.resultWidth, layoutParams.resultHeight);
                        } else {
                            // 指定了自定义宽高 layoutParams.selfHeight != null
                            if (layoutParams.selfWidth.endsWith("%mh")) {
                                if (layoutParams.selfHeight.endsWith("%mw")) {
                                    // ERROR，不能互相依赖自身的宽高
                                    throw new IllegalArgumentException("参数异常");
                                } else {
                                    layoutParams.resultHeight = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfHeight, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                                    child.measure(MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.AT_MOST), layoutParams.resultHeight);
                                    layoutParams.resultWidth = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfWidth, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                                }
                            } else {
                                if (layoutParams.selfHeight.endsWith("%mw")) {
                                    layoutParams.resultWidth = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfWidth, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                                    child.measure(layoutParams.resultWidth, MeasureSpec.makeMeasureSpec(screenHeight, MeasureSpec.AT_MOST));
                                    layoutParams.resultHeight = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfHeight, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                                } else {
                                    layoutParams.resultWidth = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfWidth, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                                    layoutParams.resultHeight = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfHeight, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                                }
                            }
                        }
                    }
                }


                // Padding
                if (layoutParams.paddingLeftSource != null) {
                    layoutParams.paddingLeft = (int) Util.calculation(layoutParams.paddingLeftSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL);
                } else {
                    layoutParams.paddingLeft = (int) layoutParams.systemPaddingLeft;
                }

                if (layoutParams.paddingTopSource != null) {
                    layoutParams.paddingTop = (int) Util.calculation(layoutParams.paddingTopSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL);
                } else {
                    layoutParams.paddingTop = (int) layoutParams.systemPaddingTop;
                }

                if (layoutParams.paddingRightSource != null) {
                    layoutParams.paddingRight = (int) Util.calculation(layoutParams.paddingRightSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL);
                } else {
                    layoutParams.paddingRight = (int) layoutParams.systemPaddingRight;
                }

                if (layoutParams.paddingBottomSource != null) {
                    layoutParams.paddingBottom = (int) Util.calculation(layoutParams.paddingBottomSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL);
                } else {
                    layoutParams.paddingBottom = (int) layoutParams.systemPaddingBottom;
                }

                child.setPadding(layoutParams.paddingLeft, layoutParams.paddingTop, layoutParams.paddingRight, layoutParams.paddingBottom);

                Map<Class<? extends View>, ViewAdapter> viewAdapterMap = DependentLayoutConfig.getInstance().getViewAdapterMap();

                for (Map.Entry<Class<? extends View>, ViewAdapter> next : viewAdapterMap.entrySet()) {
                    if (next.getKey().isInstance(child)) {
                        ViewAdapter value = next.getValue();
                        value.convert(getContext(), child, layoutParams.attrs, metrics);
                    }
                }

            }
            child.measure(layoutParams.resultWidth, layoutParams.resultHeight);


            // Margin
            if (layoutParams.marginLeftSource != null) {
                layoutParams.marginLeft = Util.calculation(layoutParams.marginLeftSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL);
            } else {
                layoutParams.marginLeft = layoutParams.systemMarginLeft;
            }

            if (layoutParams.marginTopSource != null) {
                layoutParams.marginTop = Util.calculation(layoutParams.marginTopSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL);
            } else {
                layoutParams.marginTop = layoutParams.systemMarginTop;
            }

            if (layoutParams.marginRightSource != null) {
                layoutParams.marginRight = Util.calculation(layoutParams.marginRightSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL);
            } else {
                layoutParams.marginRight = layoutParams.systemMarginRight;
            }

            if (layoutParams.marginBottomSource != null) {
                layoutParams.marginBottom = Util.calculation(layoutParams.marginBottomSource, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL);
            } else {
                layoutParams.marginBottom = layoutParams.systemMarginBottom;
            }

            //            if (child.getMeasuredWidth() <= 0 && child.getMeasuredHeight() <= 0) {
            //                continue;
            //            }

            float tempLeft = getPaddingLeft() + layoutParams.marginLeft;
            float tempRight = tempLeft + child.getMeasuredWidth();
            layoutParams.left = tempLeft;
            if (parentWidth > 0 && tempRight > parentWidth - layoutParams.marginRight) {
                layoutParams.right = parentWidth - layoutParams.marginRight;
            } else {
                layoutParams.right = tempRight;
            }
            tempRight = layoutParams.right;

            float tempTop = getPaddingTop() + layoutParams.marginTop;
            float tempBottom = tempTop + child.getMeasuredHeight();

            layoutParams.top = tempTop;
            if (parentHeight > 0 && tempBottom > parentHeight - layoutParams.marginBottom) {
                layoutParams.bottom = parentHeight - layoutParams.marginBottom;
            } else {
                layoutParams.bottom = tempBottom;
            }
            tempBottom = layoutParams.bottom;


            // dependencies
            if (layoutParams.alignParentTop) {
                layoutParams.top = getPaddingLeft() + layoutParams.marginLeft;
            }


            boolean hasVerticalAttr = !layoutParams.hasVerticalAttr(layoutParams);

            if (layoutParams.alignParentBottom) {
                layoutParams.bottom = getMeasuredHeight() - getPaddingBottom() - layoutParams.marginBottom;
                if (hasVerticalAttr) {
                    layoutParams.top = layoutParams.bottom - child.getMeasuredHeight();
                }
            }

            if (layoutParams.alignParentCenterHorizontalTop) {
                layoutParams.bottom = getMeasuredHeight() / 2 - layoutParams.marginBottom;
                if (hasVerticalAttr) {
                    layoutParams.top = layoutParams.bottom - child.getMeasuredHeight();
                }
            }

            if (layoutParams.alignParentCenterHorizontalBottom) {
                layoutParams.top = getMeasuredHeight() / 2 + layoutParams.marginTop;
                if (hasVerticalAttr) {
                    layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
                }
            }

            // 与父View的水平中间对齐
            if (layoutParams.centerInParentVertical) {
                layoutParams.top = getMeasuredHeight() / 2 - child.getMeasuredHeight() / 2 + layoutParams.marginTop;
                layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
            }

            // 在某个View的上面
            if (layoutParams.aboveTo > 0) {
                View dependencies = findViewById(layoutParams.aboveTo);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.bottom = params.top - params.marginTop - layoutParams.marginBottom;
                if (hasVerticalAttr) {
                    layoutParams.top = layoutParams.bottom - child.getMeasuredHeight();
                }
            }
            // 在某个View的下面
            if (layoutParams.belowTo > 0) {
                View dependencies = findViewById(layoutParams.belowTo);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.top = params.bottom + params.marginBottom + layoutParams.marginTop;
                if (hasVerticalAttr) {
                    layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
                }
            }
            // 对齐某个View的顶部
            if (layoutParams.alignTop > 0) {
                View dependencies = findViewById(layoutParams.alignTop);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.top = params.top + layoutParams.marginTop;
                if (hasVerticalAttr) {
                    layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
                }
            }
            // 对齐某个View的底部
            if (layoutParams.alignBottom > 0) {
                View dependencies = findViewById(layoutParams.alignBottom);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.bottom = params.bottom - layoutParams.marginBottom;
                if (hasVerticalAttr) {
                    layoutParams.top = layoutParams.bottom - child.getMeasuredHeight();
                }
            }

            // 在某个View水平中线的的上面
            if (layoutParams.alignCenterHorizontalTop > 0) {
                View dependencies = findViewById(layoutParams.alignCenterHorizontalTop);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.bottom = params.top + dependencies.getMeasuredHeight() / 2 - layoutParams.marginBottom;
                if (hasVerticalAttr) {
                    layoutParams.top = layoutParams.bottom - child.getMeasuredHeight();
                }
            }
            // 在某个View水平中线的的下面
            if (layoutParams.alignCenterHorizontalBottom > 0) {
                View dependencies = findViewById(layoutParams.alignCenterHorizontalBottom);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.top = params.top + dependencies.getMeasuredHeight() / 2 + layoutParams.marginTop;
                if (hasVerticalAttr) {
                    layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
                }
            }

            if (layoutParams.centerVerticalOf > 0) {
                View dependencies = findViewById(layoutParams.centerVerticalOf);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.top = params.top + dependencies.getMeasuredHeight() / 2 - child.getMeasuredHeight() / 2;
                layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
            }

            ////////////////////////////////////////////////////////////////
            boolean hasHorizontalAttr = !layoutParams.hasHorizontalAttr(layoutParams);
            // 水平属性
            if (layoutParams.alignParentLeft) {
                layoutParams.left = getPaddingLeft() + layoutParams.marginLeft;
                if (hasHorizontalAttr) {
                    layoutParams.right = layoutParams.left + child.getMeasuredWidth();
                }
            }

            if (layoutParams.alignParentRight) {
                layoutParams.right = getMeasuredWidth() - getPaddingRight() - layoutParams.marginRight;
                if (hasHorizontalAttr) {
                    layoutParams.left = layoutParams.right - child.getMeasuredWidth();
                }
            }

            if (layoutParams.toParentCenterVerticalLeft) {
                layoutParams.right = getMeasuredWidth() / 2 - layoutParams.marginRight;
                if (hasHorizontalAttr) {
                    layoutParams.left = layoutParams.right - child.getMeasuredWidth();
                }
            }

            // 对齐父View水平中线的右边
            if (layoutParams.toParentCenterVerticalRight) {
                layoutParams.left = getMeasuredWidth() / 2 + layoutParams.marginLeft;
                if (hasHorizontalAttr) {
                    layoutParams.right = layoutParams.left + child.getMeasuredWidth();
                }
            }

            if (layoutParams.centerInParentHorizontal) {
                layoutParams.left = getMeasuredWidth() / 2 - child.getMeasuredWidth() / 2 + layoutParams.marginLeft;
                layoutParams.right = layoutParams.left + child.getMeasuredWidth();
            }

            // 左边对齐某个View的右边
            if (layoutParams.toRightOf > 0) {
                View dependencies = findViewById(layoutParams.toRightOf);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.left = params.right + params.marginRight + layoutParams.marginLeft;
                if (hasHorizontalAttr) {
                    layoutParams.right = layoutParams.left + child.getMeasuredWidth();
                }
            }

            // 右边对齐某个View的左边
            if (layoutParams.toLeftOf > 0) {
                View dependencies = findViewById(layoutParams.toLeftOf);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.right = params.left - params.marginLeft - layoutParams.marginRight;
                if (hasHorizontalAttr) {
                    layoutParams.left = layoutParams.right - child.getMeasuredWidth();
                }
            }

            // 对齐某个View的右边
            if (layoutParams.alignRight > 0) {
                View dependencies = findViewById(layoutParams.alignRight);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.right = params.right - layoutParams.marginRight;
                if (hasHorizontalAttr) {
                    layoutParams.left = layoutParams.right - child.getMeasuredWidth();
                }
            }

            // 对齐某个View的左边
            if (layoutParams.alignLeft > 0) {
                View dependencies = findViewById(layoutParams.alignLeft);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.left = params.left + layoutParams.marginLeft;
                if (hasHorizontalAttr) {
                    layoutParams.right = layoutParams.left + child.getMeasuredWidth();
                }
            }

            // 右边对齐某个View垂直中线的左边
            if (layoutParams.toCenterVerticalLeft > 0) {
                View dependencies = findViewById(layoutParams.toCenterVerticalLeft);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.right = params.left + dependencies.getMeasuredWidth() / 2 - layoutParams.marginLeft;
                if (hasHorizontalAttr) {
                    layoutParams.left = layoutParams.right - child.getMeasuredWidth();
                }
            }

            // 左边对齐某个View垂直中线的右边
            if (layoutParams.toCenterVerticalRight > 0) {
                View dependencies = findViewById(layoutParams.toCenterVerticalRight);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.left = params.left + dependencies.getMeasuredWidth() / 2;
                if (hasHorizontalAttr) {
                    layoutParams.right = layoutParams.left + child.getMeasuredWidth();
                }
            }


            // 与某个View的水平居中
            if (layoutParams.centerHorizontalOf > 0) {
                View dependencies = findViewById(layoutParams.centerHorizontalOf);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.left = (params.left + dependencies.getMeasuredWidth() / 2) - child.getMeasuredWidth() / 2;
                layoutParams.right = layoutParams.left + child.getMeasuredWidth();
            }


            // 影响水平与垂直的属性
            if (layoutParams.centerInParent) {
                layoutParams.left = getMeasuredWidth() / 2 - child.getMeasuredWidth() / 2;
                layoutParams.right = layoutParams.left + child.getMeasuredWidth();

                layoutParams.top = getMeasuredHeight() / 2 - child.getMeasuredHeight() / 2;
                layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
            }

            // 在与某个View的中心对齐
            if (layoutParams.centerOf > 0) {
                View dependencies = findViewById(layoutParams.centerOf);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();

                layoutParams.top = params.top + dependencies.getMeasuredHeight() / 2 - child.getMeasuredHeight() / 2;
                layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();

                layoutParams.left = params.left + dependencies.getMeasuredWidth() / 2 - child.getMeasuredWidth() / 2;
                layoutParams.right = layoutParams.left + child.getMeasuredWidth();
            }

            if (maxWidth < layoutParams.right + layoutParams.marginRight) {
                maxWidth = layoutParams.right + layoutParams.marginRight;
            }

            if (maxHeight < layoutParams.bottom + layoutParams.marginBottom) {
                maxHeight = layoutParams.bottom + layoutParams.marginBottom;
            }

            layoutParams.width = (int) Math.ceil(layoutParams.right - layoutParams.left);
            layoutParams.height = (int) Math.ceil(layoutParams.bottom - layoutParams.top);
            child.setLayoutParams(layoutParams);


            if (Math.ceil(tempRight - tempLeft) != layoutParams.width || Math.ceil(tempBottom - tempTop) != layoutParams.height) {
                child.measure(MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            child.layout((int) layoutParams.left, (int) layoutParams.top, (int) layoutParams.right, (int) layoutParams.bottom);
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return DependentLayout.class.getName();
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        private boolean alignParentLeft;
        private boolean alignParentTop;
        private boolean alignParentRight;
        private boolean alignParentBottom;

        private boolean toParentCenterVerticalLeft;
        private boolean toParentCenterVerticalRight;
        private boolean alignParentCenterHorizontalTop;
        private boolean alignParentCenterHorizontalBottom;

        private boolean centerInParentHorizontal;
        private boolean centerInParentVertical;
        private boolean centerInParent;

        private int aboveTo;
        private int toRightOf;
        private int belowTo;
        private int toLeftOf;

        private int alignTop;
        private int alignRight;
        private int alignBottom;
        private int alignLeft;

        private int toCenterVerticalLeft;
        private int toCenterVerticalRight;
        private int alignCenterHorizontalTop;
        private int alignCenterHorizontalBottom;

        private int centerVerticalOf;
        private int centerHorizontalOf;
        private int centerOf;

        private String marginLeftSource;
        private String marginTopSource;
        private String marginRightSource;
        private String marginBottomSource;

        private float systemMarginLeft;
        private float systemMarginTop;
        private float systemMarginRight;
        private float systemMarginBottom;

        private String selfWidth;
        private String selfHeight;

        private String paddingLeftSource;
        private String paddingTopSource;
        private String paddingRightSource;
        private String paddingBottomSource;

        private float systemPaddingLeft;
        private float systemPaddingTop;
        private float systemPaddingRight;
        private float systemPaddingBottom;

        // 以下值均需计算得出
        private float marginLeft;
        private float marginTop;
        private float marginRight;
        private float marginBottom;

        private float left;
        private float top;
        private float right;
        private float bottom;

        private int paddingLeft;
        private int paddingTop;
        private int paddingRight;
        private int paddingBottom;

        private int resultWidth;
        private int resultHeight;

        private float systemWidth;
        private float systemHeight;

        private AttributeMap attrs;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);

            this.attrs = new AttributeMap(attrs);

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DependentLayout_Layout);

            alignParentLeft = typedArray.getBoolean(R.styleable.DependentLayout_Layout_alignParentLeft, false);
            alignParentTop = typedArray.getBoolean(R.styleable.DependentLayout_Layout_alignParentTop, false);
            alignParentRight = typedArray.getBoolean(R.styleable.DependentLayout_Layout_alignParentRight, false);
            alignParentBottom = typedArray.getBoolean(R.styleable.DependentLayout_Layout_alignParentBottom, false);

            toParentCenterVerticalLeft = typedArray.getBoolean(R.styleable.DependentLayout_Layout_toParentCenterVerticalLeft, false);
            toParentCenterVerticalRight = typedArray.getBoolean(R.styleable.DependentLayout_Layout_toParentCenterVerticalRight, false);
            alignParentCenterHorizontalTop = typedArray.getBoolean(R.styleable.DependentLayout_Layout_alignParentCenterHorizontalTop, false);
            alignParentCenterHorizontalBottom = typedArray.getBoolean(R.styleable.DependentLayout_Layout_alignParentCenterHorizontalBottom, false);

            centerInParentHorizontal = typedArray.getBoolean(R.styleable.DependentLayout_Layout_centerInParentHorizontal, false);
            centerInParentVertical = typedArray.getBoolean(R.styleable.DependentLayout_Layout_centerInParentVertical, false);
            centerInParent = typedArray.getBoolean(R.styleable.DependentLayout_Layout_centerInParent, false);

            aboveTo = typedArray.getResourceId(R.styleable.DependentLayout_Layout_aboveTo, 0);
            toRightOf = typedArray.getResourceId(R.styleable.DependentLayout_Layout_toRightOf, 0);
            belowTo = typedArray.getResourceId(R.styleable.DependentLayout_Layout_belowTo, 0);
            toLeftOf = typedArray.getResourceId(R.styleable.DependentLayout_Layout_toLeftOf, 0);

            alignTop = typedArray.getResourceId(R.styleable.DependentLayout_Layout_alignTop, 0);
            alignRight = typedArray.getResourceId(R.styleable.DependentLayout_Layout_alignRight, 0);
            alignBottom = typedArray.getResourceId(R.styleable.DependentLayout_Layout_alignBottom, 0);
            alignLeft = typedArray.getResourceId(R.styleable.DependentLayout_Layout_alignLeft, 0);

            toCenterVerticalLeft = typedArray.getResourceId(R.styleable.DependentLayout_Layout_toCenterVerticalLeft, 0);
            toCenterVerticalRight = typedArray.getResourceId(R.styleable.DependentLayout_Layout_toCenterVerticalRight, 0);
            alignCenterHorizontalTop = typedArray.getResourceId(R.styleable.DependentLayout_Layout_alignCenterHorizontalTop, 0);
            alignCenterHorizontalBottom = typedArray.getResourceId(R.styleable.DependentLayout_Layout_alignCenterHorizontalBottom, 0);

            centerVerticalOf = typedArray.getResourceId(R.styleable.DependentLayout_Layout_centerVerticalOf, 0);
            centerHorizontalOf = typedArray.getResourceId(R.styleable.DependentLayout_Layout_centerHorizontalOf, 0);
            centerOf = typedArray.getResourceId(R.styleable.DependentLayout_Layout_centerOf, 0);

            marginLeftSource = typedArray.getString(R.styleable.DependentLayout_Layout_dependency_marginLeft);
            marginTopSource = typedArray.getString(R.styleable.DependentLayout_Layout_dependency_marginTop);
            marginRightSource = typedArray.getString(R.styleable.DependentLayout_Layout_dependency_marginRight);
            marginBottomSource = typedArray.getString(R.styleable.DependentLayout_Layout_dependency_marginBottom);

            selfWidth = typedArray.getString(R.styleable.DependentLayout_Layout_selfWidth);
            selfHeight = typedArray.getString(R.styleable.DependentLayout_Layout_selfHeight);

            systemWidth = calculationSystem(typedArray, R.styleable.DependentLayout_Layout_android_layout_width, context.getResources().getDisplayMetrics());
            systemHeight = calculationSystem(typedArray, R.styleable.DependentLayout_Layout_android_layout_height, context.getResources().getDisplayMetrics());
            //
            systemMarginLeft = typedArray.getDimension(R.styleable.DependentLayout_Layout_android_layout_marginLeft, 0);
            systemMarginTop = typedArray.getDimension(R.styleable.DependentLayout_Layout_android_layout_marginTop, 0);
            systemMarginRight = typedArray.getDimension(R.styleable.DependentLayout_Layout_android_layout_marginRight, 0);
            systemMarginBottom = typedArray.getDimension(R.styleable.DependentLayout_Layout_android_layout_marginBottom, 0);

            // padding
            String dependencyPadding = typedArray.getString(R.styleable.DependentLayout_Layout_dependency_padding);
            if (dependencyPadding != null) {
                paddingLeftSource = paddingTopSource = paddingRightSource = paddingBottomSource = dependencyPadding;
            } else {
                paddingLeftSource = typedArray.getString(R.styleable.DependentLayout_Layout_dependency_paddingLeft);
                paddingTopSource = typedArray.getString(R.styleable.DependentLayout_Layout_dependency_paddingTop);
                paddingRightSource = typedArray.getString(R.styleable.DependentLayout_Layout_dependency_paddingRight);
                paddingBottomSource = typedArray.getString(R.styleable.DependentLayout_Layout_dependency_paddingBottom);
            }

            float systemPadding = typedArray.getDimension(R.styleable.DependentLayout_Layout_android_padding, 0);
            if (systemPadding > 0) {
                systemPaddingLeft = systemPaddingTop = systemPaddingRight = systemPaddingBottom = systemPadding;
            } else {
                systemPaddingLeft = typedArray.getDimension(R.styleable.DependentLayout_Layout_android_paddingLeft, 0);
                systemPaddingTop = typedArray.getDimension(R.styleable.DependentLayout_Layout_android_paddingTop, 0);
                systemPaddingRight = typedArray.getDimension(R.styleable.DependentLayout_Layout_android_paddingRight, 0);
                systemPaddingBottom = typedArray.getDimension(R.styleable.DependentLayout_Layout_android_paddingBottom, 0);
            }
            typedArray.recycle();
        }

        private float calculationSystem(TypedArray typedArray, int attr, DisplayMetrics displayMetrics) {
            String text = Util.getText(typedArray, attr);
            String value = Util.getValue(text);
            String unit = text.substring(value.length(), text.length()).toLowerCase();
            if ("dip".equals(unit)) {
                return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.valueOf(value), displayMetrics);
            } else if ("px".endsWith(unit)) {
                return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, Float.valueOf(value), displayMetrics);
            }
            return Float.valueOf(value);
        }

        /**
         * 判断是否存在两个及以上的反向属性
         *
         * @param layoutParams
         * @return
         */
        private boolean hasVerticalAttr(LayoutParams layoutParams) {
            int topCount = 0;
            int bottomCount = 0;
            if (layoutParams.alignParentTop) {
                topCount++;
            }
            if (layoutParams.alignParentBottom) {
                bottomCount++;
            }

            if (layoutParams.alignParentCenterHorizontalTop) {
                bottomCount++;
            }
            if (layoutParams.alignParentCenterHorizontalBottom) {
                topCount++;
            }
            if (layoutParams.centerInParentVertical || layoutParams.centerInParent) {
                topCount++;
                bottomCount++;
            }

            if (layoutParams.aboveTo > 0) {
                bottomCount++;
            }

            if (layoutParams.belowTo > 0) {
                topCount++;
            }

            if (layoutParams.alignTop > 0) {
                topCount++;
            }

            if (layoutParams.alignBottom > 0) {
                bottomCount++;
            }

            if (layoutParams.alignCenterHorizontalTop > 0) {
                bottomCount++;
            }

            if (layoutParams.alignCenterHorizontalBottom > 0) {
                topCount++;
            }

            if (layoutParams.centerVerticalOf > 0 || layoutParams.centerOf > 0) {
                topCount++;
                bottomCount++;
            }
            return topCount > 0 && bottomCount > 0;
        }

        /**
         * 判断是否存在两个及以上的反向属性
         *
         * @param layoutParams
         * @return
         */
        private boolean hasHorizontalAttr(LayoutParams layoutParams) {
            int leftCount = 0;
            int rightCount = 0;

            if (layoutParams.alignParentLeft) {
                leftCount++;
            }
            if (layoutParams.alignParentRight) {
                rightCount++;
            }
            if (layoutParams.toParentCenterVerticalLeft) {
                rightCount++;
            }
            if (layoutParams.toParentCenterVerticalRight) {
                leftCount++;
            }
            if (layoutParams.centerInParentHorizontal || layoutParams.centerInParent) {
                leftCount++;
                rightCount++;
            }
            if (layoutParams.toRightOf > 0) {
                leftCount++;
            }
            if (layoutParams.toLeftOf > 0) {
                rightCount++;
            }
            if (layoutParams.alignRight > 0) {
                rightCount++;
            }
            if (layoutParams.alignLeft > 0) {
                leftCount++;
            }
            if (layoutParams.toCenterVerticalLeft > 0) {
                rightCount++;
            }
            if (layoutParams.toCenterVerticalRight > 0) {
                leftCount++;
            }
            if (layoutParams.centerHorizontalOf > 0 || layoutParams.centerOf > 0) {
                leftCount++;
                rightCount++;
            }
            return leftCount > 0 && rightCount > 0;
        }

        public boolean isAlignParentLeft() {
            return alignParentLeft;
        }

        public void setAlignParentLeft(boolean alignParentLeft) {
            this.alignParentLeft = alignParentLeft;
        }

        public boolean isAlignParentTop() {
            return alignParentTop;
        }

        public void setAlignParentTop(boolean alignParentTop) {
            this.alignParentTop = alignParentTop;
        }

        public boolean isAlignParentRight() {
            return alignParentRight;
        }

        public void setAlignParentRight(boolean alignParentRight) {
            this.alignParentRight = alignParentRight;
        }

        public boolean isAlignParentBottom() {
            return alignParentBottom;
        }

        public void setAlignParentBottom(boolean alignParentBottom) {
            this.alignParentBottom = alignParentBottom;
        }

        public boolean isToParentCenterVerticalLeft() {
            return toParentCenterVerticalLeft;
        }

        public void setToParentCenterVerticalLeft(boolean toParentCenterVerticalLeft) {
            this.toParentCenterVerticalLeft = toParentCenterVerticalLeft;
        }

        public boolean isToParentCenterVerticalRight() {
            return toParentCenterVerticalRight;
        }

        public void setToParentCenterVerticalRight(boolean toParentCenterVerticalRight) {
            this.toParentCenterVerticalRight = toParentCenterVerticalRight;
        }

        public boolean isAlignParentCenterHorizontalTop() {
            return alignParentCenterHorizontalTop;
        }

        public void setAlignParentCenterHorizontalTop(boolean alignParentCenterHorizontalTop) {
            this.alignParentCenterHorizontalTop = alignParentCenterHorizontalTop;
        }

        public boolean isAlignParentCenterHorizontalBottom() {
            return alignParentCenterHorizontalBottom;
        }

        public void setAlignParentCenterHorizontalBottom(boolean alignParentCenterHorizontalBottom) {
            this.alignParentCenterHorizontalBottom = alignParentCenterHorizontalBottom;
        }

        public boolean isCenterInParentHorizontal() {
            return centerInParentHorizontal;
        }

        public void setCenterInParentHorizontal(boolean centerInParentHorizontal) {
            this.centerInParentHorizontal = centerInParentHorizontal;
        }

        public boolean isCenterInParentVertical() {
            return centerInParentVertical;
        }

        public void setCenterInParentVertical(boolean centerInParentVertical) {
            this.centerInParentVertical = centerInParentVertical;
        }

        public boolean isCenterInParent() {
            return centerInParent;
        }

        public void setCenterInParent(boolean centerInParent) {
            this.centerInParent = centerInParent;
        }

        public int getAboveTo() {
            return aboveTo;
        }

        public void setAboveTo(int aboveTo) {
            this.aboveTo = aboveTo;
        }

        public int getToRightOf() {
            return toRightOf;
        }

        public void setToRightOf(int toRightOf) {
            this.toRightOf = toRightOf;
        }

        public int getBelowTo() {
            return belowTo;
        }

        public void setBelowTo(int belowTo) {
            this.belowTo = belowTo;
        }

        public int getToLeftOf() {
            return toLeftOf;
        }

        public void setToLeftOf(int toLeftOf) {
            this.toLeftOf = toLeftOf;
        }

        public int getAlignTop() {
            return alignTop;
        }

        public void setAlignTop(int alignTop) {
            this.alignTop = alignTop;
        }

        public int getAlignRight() {
            return alignRight;
        }

        public void setAlignRight(int alignRight) {
            this.alignRight = alignRight;
        }

        public int getAlignBottom() {
            return alignBottom;
        }

        public void setAlignBottom(int alignBottom) {
            this.alignBottom = alignBottom;
        }

        public int getAlignLeft() {
            return alignLeft;
        }

        public void setAlignLeft(int alignLeft) {
            this.alignLeft = alignLeft;
        }

        public int getToCenterVerticalLeft() {
            return toCenterVerticalLeft;
        }

        public void setToCenterVerticalLeft(int toCenterVerticalLeft) {
            this.toCenterVerticalLeft = toCenterVerticalLeft;
        }

        public int getToCenterVerticalRight() {
            return toCenterVerticalRight;
        }

        public void setToCenterVerticalRight(int toCenterVerticalRight) {
            this.toCenterVerticalRight = toCenterVerticalRight;
        }

        public int getAlignCenterHorizontalTop() {
            return alignCenterHorizontalTop;
        }

        public void setAlignCenterHorizontalTop(int alignCenterHorizontalTop) {
            this.alignCenterHorizontalTop = alignCenterHorizontalTop;
        }

        public int getAlignCenterHorizontalBottom() {
            return alignCenterHorizontalBottom;
        }

        public void setAlignCenterHorizontalBottom(int alignCenterHorizontalBottom) {
            this.alignCenterHorizontalBottom = alignCenterHorizontalBottom;
        }

        public int getCenterVerticalOf() {
            return centerVerticalOf;
        }

        public void setCenterVerticalOf(int centerVerticalOf) {
            this.centerVerticalOf = centerVerticalOf;
        }

        public int getCenterHorizontalOf() {
            return centerHorizontalOf;
        }

        public void setCenterHorizontalOf(int centerHorizontalOf) {
            this.centerHorizontalOf = centerHorizontalOf;
        }

        public int getCenterOf() {
            return centerOf;
        }

        public void setCenterOf(int centerOf) {
            this.centerOf = centerOf;
        }

        public String getMarginLeftSource() {
            return marginLeftSource;
        }

        public void setMarginLeftSource(String marginLeftSource) {
            this.marginLeftSource = marginLeftSource;
        }

        public String getMarginTopSource() {
            return marginTopSource;
        }

        public void setMarginTopSource(String marginTopSource) {
            this.marginTopSource = marginTopSource;
        }

        public String getMarginRightSource() {
            return marginRightSource;
        }

        public void setMarginRightSource(String marginRightSource) {
            this.marginRightSource = marginRightSource;
        }

        public String getMarginBottomSource() {
            return marginBottomSource;
        }

        public void setMarginBottomSource(String marginBottomSource) {
            this.marginBottomSource = marginBottomSource;
        }

        public float getSystemMarginLeft() {
            return systemMarginLeft;
        }

        public void setSystemMarginLeft(float systemMarginLeft) {
            this.systemMarginLeft = systemMarginLeft;
        }

        public float getSystemMarginTop() {
            return systemMarginTop;
        }

        public void setSystemMarginTop(float systemMarginTop) {
            this.systemMarginTop = systemMarginTop;
        }

        public float getSystemMarginRight() {
            return systemMarginRight;
        }

        public void setSystemMarginRight(float systemMarginRight) {
            this.systemMarginRight = systemMarginRight;
        }

        public float getSystemMarginBottom() {
            return systemMarginBottom;
        }

        public void setSystemMarginBottom(float systemMarginBottom) {
            this.systemMarginBottom = systemMarginBottom;
        }

        public String getSelfWidth() {
            return selfWidth;
        }

        public void setSelfWidth(String selfWidth) {
            this.selfWidth = selfWidth;
        }

        public String getSelfHeight() {
            return selfHeight;
        }

        public void setSelfHeight(String selfHeight) {
            this.selfHeight = selfHeight;
        }

        public String getPaddingLeftSource() {
            return paddingLeftSource;
        }

        public void setPaddingLeftSource(String paddingLeftSource) {
            this.paddingLeftSource = paddingLeftSource;
        }

        public String getPaddingTopSource() {
            return paddingTopSource;
        }

        public void setPaddingTopSource(String paddingTopSource) {
            this.paddingTopSource = paddingTopSource;
        }

        public String getPaddingRightSource() {
            return paddingRightSource;
        }

        public void setPaddingRightSource(String paddingRightSource) {
            this.paddingRightSource = paddingRightSource;
        }

        public String getPaddingBottomSource() {
            return paddingBottomSource;
        }

        public void setPaddingBottomSource(String paddingBottomSource) {
            this.paddingBottomSource = paddingBottomSource;
        }

        public float getSystemPaddingLeft() {
            return systemPaddingLeft;
        }

        public void setSystemPaddingLeft(float systemPaddingLeft) {
            this.systemPaddingLeft = systemPaddingLeft;
        }

        public float getSystemPaddingTop() {
            return systemPaddingTop;
        }

        public void setSystemPaddingTop(float systemPaddingTop) {
            this.systemPaddingTop = systemPaddingTop;
        }

        public float getSystemPaddingRight() {
            return systemPaddingRight;
        }

        public void setSystemPaddingRight(float systemPaddingRight) {
            this.systemPaddingRight = systemPaddingRight;
        }

        public float getSystemPaddingBottom() {
            return systemPaddingBottom;
        }

        public void setSystemPaddingBottom(float systemPaddingBottom) {
            this.systemPaddingBottom = systemPaddingBottom;
        }
    }
}
