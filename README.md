# commonlib

这个库用于快速构建android工程，如果想快速构建一个app而不在意依赖的aar细节时(因为可能引入多余的库)，可以使用本库进行快速开发。

本库依赖一些常用的开源库，并支持kotlin，使用方式

```groovy
implementation('com.sonicers:commonlib:0.0.8') {
    transitive = true//依赖传递为true时才能把依赖的库打进去
}
```

## 使用到的开源库

### rxjava

```groovy
    // rxJava
    api 'io.reactivex.rxjava2:rxandroid:2.1.1'
    api 'io.reactivex.rxjava2:rxjava:2.2.9'
```

### 网络：

这里已经同时包含了gson

```groovy
    // 网络
    api 'com.squareup.okhttp3:okhttp:3.10.0'
    api 'com.squareup.okhttp3:logging-interceptor:3.10.0'
    api 'com.squareup.retrofit2:retrofit:2.3.0'
    api 'com.squareup.retrofit2:converter-gson:2.3.0'
    api 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
```

### 权限

```groovy
    // 动态权限申请
    api 'pub.devrel:easypermissions:1.3.0'
```

### 播放器

```groovy
    // 播放器
    api 'com.google.android.exoplayer:exoplayer:2.8.3'
    api 'com.google.android.exoplayer:exoplayer-core:2.8.3'
```

### 图片加载

 ```groovy
    // glide 图片加载框架
    api 'com.github.bumptech.glide:glide:4.8.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'
 ```

### 日志打印工具

```groovy
    //日志打印工具logger
    api 'com.orhanobut:logger:2.2.0'
```

### 工具utils

*要使用utils需要在application进行初始化，详见下方详细使用*

```groovy
    //工具类utils
    api 'com.blankj:utilcode:1.23.0'
```

### 二维码

```groovy
    //二维码QRCode
    api 'com.google.zxing:core:3.3.3'
```

### view绑定

```groovy
    // view绑定
    api 'com.jakewharton:butterknife:10.1.0'
```

详细可以参见本库的gradle文件

## 详细使用

### rxjava

封装rxjava线程切换符，可以使用`compose(RxSchedulers.io_main())`实现在io线程执行耗时操作，主线程回调。

### 网络

通常网络操作对于同一个服务器的返回结果可以进行很多同样处理，这也是库里BaseObserver的使用方法，通过继承这个Observer可以实现剥离数据的观察者。使用方式

```java
.subscribeWith(new BaseObserver<DataBean>() {}
```

### 单例

一些单例包在singleton包里，可以方便使用

### 工具类

一些常用工具类封装，只里只例举很常用的，其他可以在util包里面查看,工具类初始化需要在Application初始化时调用

```java
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
```



- SharePreference工具
- Json格式化
- 界面全屏工具
- SoundPool播放声音工具
- ToastUtil

### 控件

- AzimuthCircle方向控件(带中间按钮)

- 圆角控件

- SteerView(上，下，左，右四个按钮)

- 圆角布局 RCRelativeLayout

- 圆角ImageView RCImageView

- 通用LoadingDialog

# License

```
MIT License

Copyright (c) 2019 sonicers

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```