# DependentLayout

## 概述
[![](https://jitpack.io/v/Brook007/DependentLayout.svg)](https://github.com/Brook007/DependentLayout)
[![](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://github.com/Brook007/DependentLayout)
[![](https://img.shields.io/badge/API_Live-14+-brightgreen.svg)](https://github.com/Brook007/DependentLayout)
[![](https://img.shields.io/badge/License-Apache_2-brightgreen.svg)](https://github.com/Brook007/DependentLayout/blob/master/LICENSE)
[![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-Brook007-orange.svg)](https://github.com/Brook007)

一个相对布局容器,，继承自ViewGroup，并支持百分比布局缩放，下面是关于Library的简单介绍，对于代码中的问题，欢迎大家反馈问题并提Issue，喜欢的朋友可以Star一下，您的Star是对开源作者最好的支持

## 引入依赖
### Gradle方式--适合Android Studio用户
在根项目的build.gradle中添加下面的代码
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

然后在需要使用的模块的build.gradle中添加以下代码
```groovy
dependencies {
    implementation 'com.github.Brook007:DependentLayout:1.0.2'
}
```

## 使用

在实例化DependentLayout之前加入以下代码
```java
DependentLayout.sDesignWidth = "设计图中的宽度,750px"
DependentLayout.sDesignHeight = "设计图中高度,如1294px"
```
也可以在XML布局文件中配置
```xml
app:designHeight="1294px"
app:designWidth="750px"
```

当两个同时配置时，默认优先选择Xml中的配置，其他相关可参考[activity_simple.xml](https://github.com/Brook007/DependentLayout/blob/master/app/src/main/res/layout/activity_simple.xml)

## 效果预览
### 下面是在几个分辨率屏幕适配的对比图

<b/>标准720P分辨率和1080P分辨率的对比

[![](/picture/720_1280_AND_1080_1920.png)](https://github.com/Brook007/DependentLayout)

<b/>标准2K分辨率和目前最常见的全面屏分辨率(1080x2340)的对比

[![](/picture/1440_2560_AND_1080_2340.png)](https://github.com/Brook007/DependentLayout)

<b/>分辨率差距极大的480P分辨率和2K分辨率对比的对比

[![](/picture/480_800_AND_1440_2560.png)](https://github.com/Brook007/DependentLayout)

<b/>以及你现在找都找不到的QVGA分辨率和WVGA分辨率对比（也许有些手表是这个级别的分辨率吧）

[![](/picture/240_320_AND_480_800.png)](https://github.com/Brook007/DependentLayout)

### 设计图与实际效果对比

[![](/picture/comparison.png)](https://github.com/Brook007/DependentLayout)

### 示例下载

<a href="/demo/demo.apk" download="Demo.apk" alt="Demo.apk">Demo.apk</a>

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

## 特性

对于使用，基本类似于RelativeLayout，但是比RelativeLayout多一些功能，不然的话也没必要写这个了
对于DependentLayout上面的属性中，除了designWidth，和designHeight外，其他的类型是string的属性都支持以下几种写法

```
字母的含义
// 倒数第二位首字母
p->parent 父View
s->screen 屏幕
m->myself 自身
// 倒数第一位首字母
w->width  宽度
h->height 高度
```
|格式|介绍|
|---|---|
|xx%pw | 父View宽度的xx% |
| xx%ph| 父View高度的xx% |
| xx%sw| 屏幕宽度的xx% |
| xx%sh| 屏幕高度的xx% |
| xx%mw| 自身宽度的xx% |
| xx%mh| 自身高度的xx% |
| xxdp|
| xxpx|
| xx  | 与设计图单位自动匹配 |

### 注意事项

* 当设置DependentLayout中的高度为wrap_content的时候，那么子View中不能设置xx%ph属性，否则会出错，同理，当设置DependentLayout中的宽度为wrap_content的时候，那么子View中不能设置xx%pw属性

* 由于计算精度与类型转换为，计算出的实际大小可能会有1个像素的差别

* 属性间存在优先级问题，如当设置了dependency_paddingBottom时，android:paddingBottom属性就会失效，同理，当设置selfWidth的时候android:layout_width属性就会失效

* **关于演示图中的偏差，由于TextView字体问题，TextView的字体绘制可见下图**

[![](/picture/text_measure.png)](https://github.com/Brook007/DependentLayout)

实际的情况可以看上面的效果预览图中最后一图中的最右边的开启了布局边界的部分，可以验证这个说法

## 更新预告
- 添加TextView字体的支持
- 修复BUG

## 联系作者

Email:brook999999@qq.com

## 关于屏幕适配的前辈方案

1. AutoLayout https://github.com/hongyangAndroid/AndroidAutoLayout
2. ScaleLayout https://github.com/gavinliu/Android-ScaleLayout
3. percent-support-lib https://github.com/JulienGenoud/android-percent-support-lib-sample
4. AutoSize https://github.com/JessYanCoding/AndroidAutoSize

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



