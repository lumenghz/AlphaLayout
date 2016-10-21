# AlphaLayout

> This project depends on another project called `PullToRefresh` written by me.

[![Twitter](https://img.shields.io/badge/Twitter-@LuMengHZ-blue.svg?style=flat-square)](https://twitter.com/LuMengHZ)

### Preview

#### 1. sample_ListView (with viewpager)
<img src=./art/sample_list.gif width=300/>

#### 2. sample_RecyclerView
<img src=./art/sample_recycler.gif width=300/>

#### 3. sample_ScrollView
<img src=./art/sample_scroll.gif width=300/>

### Usage
- Add jitpack repository to your root build file

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

- Add the dependency

```groovy
dependencies {
    compile 'com.github.lubeast:AlphaLayout:1.0.0'
}
```

- Add `AlphaLayout` widget in your layout

```xml
<com.alhpalayout.AlphaLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/alpha_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:headerLayout="@layout/layout_header"
    app:transparent_distance="150dp"
    tools:context="com.alphalayout.activity.ListSampleActivity">

    <ListView
        android:id="@+id/list_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:divider="@null"
        android:dividerHeight="0dp" />

</com.alhpalayout.AlphaLayout>
```

- implements AlphaLayout.OnRefreshListener in your `Activity`

```java
    @Override
    public void onRefresh() {
        alphaLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                alphaLayout.setRefreshing(false);
            }
        }, REFRESH_DELAY);
    }

    @Override
    public void onScroll(int direction, float percent) {
        if (direction == AlphaLayout.DIRECTION_DOWN) {
            ViewCompat.setAlpha(alphaLayout.getHeaderLayout(), 1.0f - percent);
        } else {
            alphaLayout.getHeaderLayout().getBackground().setAlpha((int) (255 * percent));
            mTitleView.getBackground().mutate().setAlpha((int) (255 * (1 - percent)));
        }
    }
```

### Attention
- You should use `AlphaScrollView` which been provided instead of `ScrollView`.
- I also provided `AlphaHeaderLayout` which can make good performance easily.

**See sample project for detail**

### Download Sample apk
#### 1. Fir.im
<img src=./art/fir_1.0.0_release.jpeg width=300 />
#### 2.download apk directly
[release_1.0.0_github.apk](https://raw.githubusercontent.com/lubeast/alphalayout/master/art/alpha-release-1.0.0-github.apk)
