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
import com.brook.app.android.supportlibrary.dependencylayout.R;
import com.brook.app.android.supportlibrary.util.AttributeMap;
import com.brook.app.android.supportlibrary.util.DependencyLayoutConfig;
import com.brook.app.android.supportlibrary.util.Metrics;
import com.brook.app.android.supportlibrary.util.Util;

import java.util.Map;


/**
 * @author Brook
 * @time 2018/9/26 10:41
 * @target DependencyLayout
 */
public class DependencyLayout extends ViewGroup {

    /*
     * 1、取出属性值
     * 2、计算依赖关系
     *      1)、对父View依赖关系
     *      2)、对兄弟View依赖关系
     * 3、计算值
     * 4、测量自身
     */

    // 设计图的宽度
    private LayoutParams.Attribute mDesignWidth;
    // 设计图的高度
    private LayoutParams.Attribute mDesignHeight;

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
    private LayoutParams.Attribute paddingLeftSource;
    // 上边距的设置参数
    private LayoutParams.Attribute paddingTopSource;
    // 右边距的设置参数
    private LayoutParams.Attribute paddingRightSource;
    // 下边距的设置参数
    private LayoutParams.Attribute paddingBottomSource;

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
    private int parentWidth;
    private int parentHeight;

    public DependencyLayout(Context context) {
        this(context, null);
    }

    public DependencyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DependencyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        this.metrics = new Metrics();

        Util.setContext(this.getContext());

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DependencyLayout);
            String designWidth = typedArray.getString(R.styleable.DependencyLayout_designWidth);
            if (designWidth == null) {
                mDesignWidth = Util.unbox(DependencyLayoutConfig.getInstance().getDesignWidth());
            } else {
                mDesignWidth = Util.unbox(designWidth);
            }
            String designHeight = typedArray.getString(R.styleable.DependencyLayout_designHeight);
            if (designHeight == null) {
                mDesignHeight = Util.unbox(DependencyLayoutConfig.getInstance().getDesignHeight());
            } else {
                mDesignHeight = Util.unbox(designHeight);
            }
            String padding = typedArray.getString(R.styleable.DependencyLayout_dependency_padding);
            if (padding != null) {
                paddingLeftSource = paddingTopSource = paddingRightSource = paddingBottomSource = Util.unbox(padding);
            } else {
                paddingLeftSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_dependency_paddingLeft));
                paddingTopSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_dependency_paddingTop));
                paddingRightSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_dependency_paddingRight));
                paddingBottomSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_dependency_paddingBottom));
            }

            float systemPadding = typedArray.getDimension(R.styleable.DependencyLayout_android_padding, 0);
            if (systemPadding > 0) {
                systemPaddingLeft = systemPaddingTop = systemPaddingRight = systemPaddingBottom = typedArray.getDimension(R.styleable.DependencyLayout_android_padding, 0);
            } else {
                systemPaddingLeft = typedArray.getDimension(R.styleable.DependencyLayout_android_paddingLeft, 0);
                systemPaddingTop = typedArray.getDimension(R.styleable.DependencyLayout_android_paddingTop, 0);
                systemPaddingRight = typedArray.getDimension(R.styleable.DependencyLayout_android_paddingRight, 0);
                systemPaddingBottom = typedArray.getDimension(R.styleable.DependencyLayout_android_paddingBottom, 0);
            }
            typedArray.recycle();
        }

        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);
        if (metrics.widthPixels <= 0) {
            if (mDesignWidth.unit == LayoutParams.Attribute.DIP) {
                screenWidth = (int) Util.dp2px(mDesignWidth.value);
            } else if (mDesignWidth.unit == LayoutParams.Attribute.PX) {
                screenWidth = (int) mDesignWidth.value;
            } else {
                screenWidth = 1080;
            }
        } else {
            screenWidth = metrics.widthPixels;
        }
        if (metrics.heightPixels <= 0) {
            if (mDesignHeight.unit == LayoutParams.Attribute.DIP) {
                screenHeight = (int) Util.dp2px(mDesignHeight.value);
            } else if (mDesignHeight.unit == LayoutParams.Attribute.PX) {
                screenHeight = (int) mDesignHeight.value;
            } else {
                screenHeight = 1920;
            }
        } else {
            screenHeight = metrics.heightPixels;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 自身的宽度
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        // 自身的高度
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);

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
        metrics.parentWidth = parentWidth;
        metrics.parentHeight = parentHeight;
        metrics.screenWidth = screenWidth;
        metrics.screenHeight = screenHeight;

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            // 宽度warp_content，或者UNSPECIFIED， 高度必须指定
            int widthMakeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (maxWidth + getPaddingRight()), MeasureSpec.EXACTLY);
            this.setMeasuredDimension(widthMakeMeasureSpec, heightMeasureSpec);

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                if (layoutParams.centerInParentHorizontal || layoutParams.centerInParent) {
                    layoutParams.left = (getMeasuredWidth() - child.getMeasuredWidth()) / 2F + layoutParams.marginLeft;
                    layoutParams.right = layoutParams.left + child.getMeasuredWidth();
                }
            }
        } else if (heightMode != MeasureSpec.EXACTLY) {
            // 高度warp_content，或者UNSPECIFIED， 宽度必须指定
            int heightMakeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (maxHeight + getPaddingBottom()), MeasureSpec.EXACTLY);
            this.setMeasuredDimension(widthMeasureSpec, heightMakeMeasureSpec);

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                if (layoutParams.centerInParentVertical || layoutParams.centerInParent) {
                    layoutParams.top = (getMeasuredHeight() - child.getMeasuredHeight()) / 2F + layoutParams.marginTop;
                    layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
                }
            }
        }
    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {

        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);
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
                        // 计算高度
                        layoutParams.resultHeight = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfHeight, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
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
                            // 计算高度
                            layoutParams.resultWidth = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfWidth, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                        } else {
                            // 指定了自定义宽高 layoutParams.selfHeight != null
                            if (layoutParams.selfWidth.unit == LayoutParams.Attribute.MH) {
                                if (layoutParams.selfHeight.unit == DependencyLayout.LayoutParams.Attribute.MW) {
                                    // ERROR，不能互相依赖自身的宽高
                                    throw new IllegalArgumentException("参数异常");
                                } else {
                                    layoutParams.resultHeight = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfHeight, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                                    child.measure(MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.AT_MOST), layoutParams.resultHeight);
                                    layoutParams.resultWidth = MeasureSpec.makeMeasureSpec((int) Util.calculation(layoutParams.selfWidth, mDesignWidth, mDesignHeight, screenWidth, screenHeight, this, child, HORIZONTAL), MeasureSpec.EXACTLY);
                                }
                            } else {
                                if (layoutParams.selfHeight.unit == LayoutParams.Attribute.MW) {
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

                Map<Class<? extends View>, ViewAdapter> viewAdapterMap = DependencyLayoutConfig.getInstance().getViewAdapterMap();

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
                layoutParams.bottom = parentHeight - getPaddingBottom() - layoutParams.marginBottom;
                if (hasVerticalAttr) {
                    layoutParams.top = layoutParams.bottom - child.getMeasuredHeight();
                }
            }

            if (layoutParams.alignParentCenterHorizontalTop) {
                layoutParams.bottom = parentHeight / 2F - layoutParams.marginBottom;
                if (hasVerticalAttr) {
                    layoutParams.top = layoutParams.bottom - child.getMeasuredHeight();
                }
            }

            if (layoutParams.alignParentCenterHorizontalBottom) {
                layoutParams.top = parentHeight / 2F + layoutParams.marginTop;
                if (hasVerticalAttr) {
                    layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
                }
            }

            // 与父View的水平中间对齐
            if (layoutParams.centerInParentVertical) {
                layoutParams.top = parentHeight / 2F - child.getMeasuredHeight() / 2F + layoutParams.marginTop;
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
                layoutParams.bottom = params.top + dependencies.getMeasuredHeight() / 2F - layoutParams.marginBottom;
                if (hasVerticalAttr) {
                    layoutParams.top = layoutParams.bottom - child.getMeasuredHeight();
                }
            }
            // 在某个View水平中线的的下面
            if (layoutParams.alignCenterHorizontalBottom > 0) {
                View dependencies = findViewById(layoutParams.alignCenterHorizontalBottom);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.top = params.top + dependencies.getMeasuredHeight() / 2F + layoutParams.marginTop;
                if (hasVerticalAttr) {
                    layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
                }
            }

            if (layoutParams.centerVerticalOf > 0) {
                View dependencies = findViewById(layoutParams.centerVerticalOf);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.top = params.top + dependencies.getMeasuredHeight() / 2F - child.getMeasuredHeight() / 2F;
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
                layoutParams.right = parentWidth - getPaddingRight() - layoutParams.marginRight;
                if (hasHorizontalAttr) {
                    layoutParams.left = layoutParams.right - child.getMeasuredWidth();
                }
            }

            if (layoutParams.toParentCenterVerticalLeft) {
                layoutParams.right = parentWidth / 2F - layoutParams.marginRight;
                if (hasHorizontalAttr) {
                    layoutParams.left = layoutParams.right - child.getMeasuredWidth();
                }
            }

            // 对齐父View水平中线的右边
            if (layoutParams.toParentCenterVerticalRight) {
                layoutParams.left = parentWidth / 2F + layoutParams.marginLeft;
                if (hasHorizontalAttr) {
                    layoutParams.right = layoutParams.left + child.getMeasuredWidth();
                }
            }

            if (layoutParams.centerInParentHorizontal) {
                if (parentWidth > 0) {
                    layoutParams.left = parentWidth / 2F - child.getMeasuredWidth() / 2F + layoutParams.marginLeft;
                } else {
                    layoutParams.left = layoutParams.marginLeft;
                }
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
                layoutParams.right = params.left + dependencies.getMeasuredWidth() / 2F - layoutParams.marginLeft;
                if (hasHorizontalAttr) {
                    layoutParams.left = layoutParams.right - child.getMeasuredWidth();
                }
            }

            // 左边对齐某个View垂直中线的右边
            if (layoutParams.toCenterVerticalRight > 0) {
                View dependencies = findViewById(layoutParams.toCenterVerticalRight);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.left = params.left + dependencies.getMeasuredWidth() / 2F;
                if (hasHorizontalAttr) {
                    layoutParams.right = layoutParams.left + child.getMeasuredWidth();
                }
            }


            // 与某个View的水平居中
            if (layoutParams.centerHorizontalOf > 0) {
                View dependencies = findViewById(layoutParams.centerHorizontalOf);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();
                layoutParams.left = (params.left + dependencies.getMeasuredWidth() / 2F) - child.getMeasuredWidth() / 2F;
                layoutParams.right = layoutParams.left + child.getMeasuredWidth();
            }


            // 影响水平与垂直的属性
            if (layoutParams.centerInParent) {
                layoutParams.left = parentWidth / 2F - child.getMeasuredWidth() / 2F;
                layoutParams.right = layoutParams.left + child.getMeasuredWidth();

                layoutParams.top = parentHeight / 2F - child.getMeasuredHeight() / 2F;
                layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();
            }

            // 在与某个View的中心对齐
            if (layoutParams.centerOf > 0) {
                View dependencies = findViewById(layoutParams.centerOf);
                LayoutParams params = (LayoutParams) dependencies.getLayoutParams();

                layoutParams.top = params.top + dependencies.getMeasuredHeight() / 2F - child.getMeasuredHeight() / 2F;
                layoutParams.bottom = layoutParams.top + child.getMeasuredHeight();

                layoutParams.left = params.left + dependencies.getMeasuredWidth() / 2F - child.getMeasuredWidth() / 2F;
                layoutParams.right = layoutParams.left + child.getMeasuredWidth();
            }

            if (maxWidth < layoutParams.right + layoutParams.marginRight) {
                maxWidth = layoutParams.right + layoutParams.marginRight;
            }

            if (maxHeight < layoutParams.bottom + layoutParams.marginBottom) {
                maxHeight = layoutParams.bottom + layoutParams.marginBottom;
            }

            if (parentWidthMode != MeasureSpec.EXACTLY) {
                parentWidth = (int) maxHeight;
            }

            if (parentHeightMode != MeasureSpec.EXACTLY) {
                parentHeight = (int) maxHeight;
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
        return DependencyLayout.class.getName();
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

        private Attribute marginLeftSource;
        private Attribute marginTopSource;
        private Attribute marginRightSource;
        private Attribute marginBottomSource;

        private float systemMarginLeft;
        private float systemMarginTop;
        private float systemMarginRight;
        private float systemMarginBottom;

        private Attribute selfWidth;
        private Attribute selfHeight;

        private Attribute paddingLeftSource;
        private Attribute paddingTopSource;
        private Attribute paddingRightSource;
        private Attribute paddingBottomSource;

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

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DependencyLayout_Layout);

            alignParentLeft = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_alignParentLeft, false);
            alignParentTop = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_alignParentTop, false);
            alignParentRight = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_alignParentRight, false);
            alignParentBottom = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_alignParentBottom, false);

            toParentCenterVerticalLeft = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_toParentCenterVerticalLeft, false);
            toParentCenterVerticalRight = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_toParentCenterVerticalRight, false);
            alignParentCenterHorizontalTop = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_alignParentCenterHorizontalTop, false);
            alignParentCenterHorizontalBottom = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_alignParentCenterHorizontalBottom, false);

            centerInParentHorizontal = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_centerInParentHorizontal, false);
            centerInParentVertical = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_centerInParentVertical, false);
            centerInParent = typedArray.getBoolean(R.styleable.DependencyLayout_Layout_centerInParent, false);

            aboveTo = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_aboveTo, 0);
            toRightOf = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_toRightOf, 0);
            belowTo = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_belowTo, 0);
            toLeftOf = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_toLeftOf, 0);

            alignTop = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_alignTop, 0);
            alignRight = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_alignRight, 0);
            alignBottom = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_alignBottom, 0);
            alignLeft = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_alignLeft, 0);

            toCenterVerticalLeft = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_toCenterVerticalLeft, 0);
            toCenterVerticalRight = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_toCenterVerticalRight, 0);
            alignCenterHorizontalTop = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_alignCenterHorizontalTop, 0);
            alignCenterHorizontalBottom = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_alignCenterHorizontalBottom, 0);

            centerVerticalOf = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_centerVerticalOf, 0);
            centerHorizontalOf = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_centerHorizontalOf, 0);
            centerOf = typedArray.getResourceId(R.styleable.DependencyLayout_Layout_centerOf, 0);

            marginLeftSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_Layout_dependency_marginLeft));
            marginTopSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_Layout_dependency_marginTop));
            marginRightSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_Layout_dependency_marginRight));
            marginBottomSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_Layout_dependency_marginBottom));

            selfWidth = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_Layout_selfWidth));
            selfHeight = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_Layout_selfHeight));

            systemWidth = calculationSystem(typedArray, R.styleable.DependencyLayout_Layout_android_layout_width, context.getResources().getDisplayMetrics());
            systemHeight = calculationSystem(typedArray, R.styleable.DependencyLayout_Layout_android_layout_height, context.getResources().getDisplayMetrics());
            //
            systemMarginLeft = typedArray.getDimension(R.styleable.DependencyLayout_Layout_android_layout_marginLeft, 0);
            systemMarginTop = typedArray.getDimension(R.styleable.DependencyLayout_Layout_android_layout_marginTop, 0);
            systemMarginRight = typedArray.getDimension(R.styleable.DependencyLayout_Layout_android_layout_marginRight, 0);
            systemMarginBottom = typedArray.getDimension(R.styleable.DependencyLayout_Layout_android_layout_marginBottom, 0);

            // padding
            String dependencyPadding = typedArray.getString(R.styleable.DependencyLayout_Layout_dependency_padding);
            if (dependencyPadding != null) {
                paddingLeftSource = paddingTopSource = paddingRightSource = paddingBottomSource = Util.unbox(dependencyPadding);
            } else {
                paddingLeftSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_Layout_dependency_paddingLeft));
                paddingTopSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_Layout_dependency_paddingTop));
                paddingRightSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_Layout_dependency_paddingRight));
                paddingBottomSource = Util.unbox(typedArray.getString(R.styleable.DependencyLayout_Layout_dependency_paddingBottom));
            }

            float systemPadding = typedArray.getDimension(R.styleable.DependencyLayout_Layout_android_padding, 0);
            if (systemPadding > 0) {
                systemPaddingLeft = systemPaddingTop = systemPaddingRight = systemPaddingBottom = systemPadding;
            } else {
                systemPaddingLeft = typedArray.getDimension(R.styleable.DependencyLayout_Layout_android_paddingLeft, 0);
                systemPaddingTop = typedArray.getDimension(R.styleable.DependencyLayout_Layout_android_paddingTop, 0);
                systemPaddingRight = typedArray.getDimension(R.styleable.DependencyLayout_Layout_android_paddingRight, 0);
                systemPaddingBottom = typedArray.getDimension(R.styleable.DependencyLayout_Layout_android_paddingBottom, 0);
            }
            typedArray.recycle();
        }

        private float calculationSystem(TypedArray typedArray, int attr, DisplayMetrics displayMetrics) {
            String text = Util.getText(typedArray, attr);
            String value = Util.getValue(text);
            String unit = text.substring(value.length()).toLowerCase();
            if ("dip".equals(unit) || "dp".equals(unit)) {
                return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Util.toFloat(value), displayMetrics);
            } else if ("px".endsWith(unit)) {
                return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, Util.toFloat(value), displayMetrics);
            } else if ("sp".endsWith(unit)) {
                return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, Util.toFloat(value), displayMetrics);
            } else {
                return Util.toFloat(value);
            }
        }

        public static class Attribute {
            public static final int UNIT_NOT_SET = -1;
            public static final int DIP = 0;
            public static final int SP = 1;
            public static final int PX = 2;

            //%sw
            public static final int SW = 0x1001;
            public static final int SH = 0x1002;
            public static final int PW = 0x1003;
            public static final int PH = 0x1004;
            public static final int MW = 0x1005;
            public static final int MH = 0x1006;

            public static final int ERROR = 0x8888;

            public float value = 0F;
            public int unit = UNIT_NOT_SET;
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

    }
}
