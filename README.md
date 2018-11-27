# DependentLayout

## 概述
[![](https://img.shields.io/badge/LibVersion-0.8-brightgreen.svg)](https://github.com/Brook007/DependentLayout)
[![](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://github.com/Brook007/DependentLayout)
[![](https://img.shields.io/badge/API_Live-14+-brightgreen.svg)](https://github.com/Brook007/DependentLayout)
[![](https://img.shields.io/badge/License-Apache_2-brightgreen.svg)](https://github.com/Brook007/DependentLayout/blob/master/LICENSE)
[![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-Brook007-orange.svg)](https://github.com/Brook007)

一个类似于RelativeLayout的Layout，继承自ViewGroup，并支持百分比布局缩放

## DependentLayout支持的属性
|属性|类型|描述|
|---|---|---|
| designWidth | string | 设计图中的宽度 |
| designHeight | string | 设计图中的高度 |
| dependency_padding | string | DependentLayout支持的的内边距 |
| dependency_paddingLeft | string | DependentLayout支持的的左边距 |
| dependency_paddingTop | string | DependentLayout支持的的上边距 |
| dependency_paddingRight | string | DependentLayout支持的的右边距 |
| dependency_paddingBottom | string | DependentLayout支持的的下边距 |
| android:padding | dimension | 系统的属性的内边距 |
| android:paddingLeft | dimension | 系统的属性的左边距 |
| android:paddingTop | dimension | 系统的属性的上边距 |
| android:paddingRight | dimension | 系统的属性的右边距 |
| android:paddingBottom | dimension | 系统的属性的下边距 |

## DependentLayout子View支持的属性
|属性|类型|描述|
|---|---|---|
| alignParentLeft | boolean | View上边对齐到父View的左边  |
| alignParentTop | boolean | View上边对齐到父View的上边 |
| alignParentRight | boolean | View上边对齐到父View的右边 |
| alignParentBottom | boolean | View上边对齐到父View的下边 |
| toParentCenterVerticalLeft | boolean | View右边对齐到父View垂直中线的左边 |
| toParentCenterVerticalRight | boolean | View左边对齐到父View垂直中线的右边 |
| alignParentCenterHorizontalTop | boolean | View下边对齐到父View水平中线的上边 |
| alignParentCenterHorizontalBottom | boolean | View上边对齐到父View水平中线的下边 |
| centerInParentHorizontal | boolean | View在DependentLayout中水平居中 |
| centerInParentVertical | boolean | View在DependentLayout中垂直居中 |
| centerInParent | boolean | View在DependentLayout中水平和垂直居中 |
| aboveTo | reference | View在引用的View上边 |
| toRightOf | reference | View在引用的View右边 |
| belowTo | reference | View在引用的View下边 |
| toLeftOf | reference | View在引用的View左边 |
| alignTop | reference | View上边与引用的View上边对齐 |
| alignRight | reference | View右边与引用的View右边对齐 |
| alignBottom | reference | View下边与引用的View下边对齐 |
| alignLeft | reference | View左边与引用的View左边对齐  |
| toCenterVerticalLeft | reference | View位于引用的View水平中线左边 |
| toCenterVerticalRight | reference | View位于引用的View水平中线右边 |
| alignCenterHorizontalTop | reference | View位于引用的View垂直中线上边 |
| alignCenterHorizontalBottom | reference | View位于引用的View垂直中线下边 |
| centerVerticalOf | reference | View的水平中线与引用的View的水平中线对齐 |
| centerHorizontalOf | reference | View的垂直中线与引用的View的垂直中线对齐 |
| centerOf | reference | View在引用的View的中间 |
| dependency_marginLeft | string | View左边距与偏移 |
| dependency_marginTop | string | View上边距与偏移 |
| dependency_marginRight | string | View右边距与偏移 |
| dependency_marginBottom | string | View下边距与偏移 |
| dependency_padding | string | View的内边距 |
| dependency_paddingLeft | string | View的左边距 |
| dependency_paddingTop | string | View的上边距 |
| dependency_paddingRight | string | View的右边距 |
| dependency_paddingBottom | string | View的下边距 |
| selfWidth | string | 自身的宽度 |
| selfHeight | string | 自身的高度 |
| android:layout_width | dimension | 使用系统设置的宽度 |
| android:layout_height | dimension | 使用系统设置的高度 |
| android:layout_marginLeft | dimension | 使用系统设置的左边距 |
| android:layout_marginRight | dimension | 使用系统设置的右边距 |
| android:layout_marginTop | dimension | 使用系统设置的上边距 |
| android:layout_marginBottom | dimension | 使用系统设置的下边距 |
| android:padding | dimension | 使用系统设置的内边距 |
| android:paddingLeft | dimension | 使用系统设置的内左边距 |
| android:paddingTop | dimension | 使用系统设置的内上边距 |
| android:paddingRight | dimension | 使用系统设置的内右边距 |
| android:paddingBottom | dimension | 使用系统设置的内下边距 |

## 开源协议  LICENSE


    Copyright (c) 2016-present, Brook007 Contributors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



