# magic-dynamic_plugin

主要提供了下面三个用途 
1. 动态加载jar文件   
2. 动态加载带有资源文件的apk  主要满足换皮肤需求  
3. 加载apk 满足动态加载activity和service的需求

## 特点
只需要把magic_plugin.jar文件放入lib目录下面 直接方便

## 使用:
#### 增加权限
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    
### 1. 动态加载java文件  
也就是不涉及到资源文件 在demo中的dynamicclient项目里面 举了一个简单的例子

#### 1.  定义了一个接口IDynamic 只有一个简单的方法getName

通过 在android studio中 生产jar包的方法 我已经在build.gradle中完成了 可以直接使用 通过下面的两部 

在terminal中 首先要使目录到dynamicclient下面
cd dynamicclient
gradle clean build 
gradle makeJar

通过上面的三部
我们可以在build/libs目录下面得到一个dynamic.jar文件 
我们可以得到想使用的jar包 其实里面只有一个DynamicImpl.class文件

最后一个使用android studio自带的dx工具 把.jar文件转换为dex文件 这样dalvik虚拟机才可以识别并加载

dx --dex --output=dynamicdex.jar dynamic.jar

#### 2. 把生产的dynamicdex.jar文件放到你指定的位置 一般使用的时候应该是从服务器端下载
这里为了简便就直接放在了sd卡上 拷贝一份 IDynamic接口

使用实例的demo 如下

```java
private void loadDexJar() {
    new Thread(new Runnable() {
        @Override
        public void run() {
            Context mContext = MainActivity.this;
            String dexPath = PluginDemoHelper.getApkPath(PluginDemoHelper.DynamicJar);
            PluginInfo mPluginInfo = new PluginInfo(dexPath, PluginDemoHelper.DynamicKey);
            PluginManager.loadDexFile(mContext, mPluginInfo);
            PluginResource pluginResource = PluginManager.getResourceBean(mContext, PluginDemoHelper.DynamicKey);
            if (pluginResource != null) {
                try {
                    Class<?> clazz = pluginResource.mClassLoader.loadClass("com.leaf.dynamicclient.dynamicjar.DynamicImpl");
                    IDynamic myDynamic = (IDynamic) clazz.newInstance();
                    if(myDynamic!=null){
                        Log.d(TAG, "name = " + myDynamic.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }).start();
}


```

### 2. 带有资源的apk文件 


#### 1. 客户端 

##### 1. 引用jar包 注意需要使用provided 引用这个jar包 
provided files('libs/magic_plugin.jar')

##### 2. activity和service 必须分别继承PluginClientActivity service必须继承PluginClientService

对应activity 必须注意这个对象mProxyActivity
mProxyActivity.setContentView(R.layout.activity_main);
Activity的跳转使用startPluginActivity

##### 3. 注意在布局文件中的引用字符串和drawable图片资源是不可以的 
只能在代码里面类似下面的
mImage = (ImageView) mProxyActivity.findViewById(R.id.image);
mImage.setImageResource(R.drawable.image_girl);

##### 4. 在pluginClient目录下 使用gradle clean build 得到apk文件 这里为了简化 直接把apk文件放入sdcard下面 


#### 2. 宿主端

##### 1. 在maniefst中
```java
<activity android:name="com.magic.plugin.proxy.PluginProxyActivity"/>
<service android:name="com.magic.plugin.proxy.PluginProxyService"/>
```

##### 2. 实例： 直接在宿主中启动一个activity

```java
private void loadApkResource() {
    new Thread(new Runnable() {
        @Override
        public void run() {
            Context mContext = MainActivity.this;
            String dexPath = PluginDemoHelper.getApkPath(PluginDemoHelper.DynamicApk);
            PluginInfo mPluginInfo = new PluginInfo(dexPath, PluginDemoHelper.DynamicAPKKey);
            PluginManager.loadApk(mContext, mPluginInfo);
            Intent intentPlugin = new Intent();
            intentPlugin.setClass(MainActivity.this, PluginProxyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(PluginProxyHelper.PLUGIN_CLASS_NAME, "com.leaf.apkpluginclient.MainActivity");
            bundle.putString(PluginProxyHelper.PLUGIN_KEY, PluginDemoHelper.DynamicAPKKey);
            intentPlugin.putExtras(bundle);
            startActivity(intentPlugin);

        }
    }).start();
}

```


### 3. 在宿主端访问资源 一般换皮肤的需求需要在宿主端获取资源

下面的例子 是获取字符串和图片资源的例子 通过PluginManager.getDrawable和PluginManager.getString方法获取
需要注意的一点 如果自己的R文件不是包名+R 如com.leaf.dynamicclient.extra.R
那么在PluginInfo的时候需要使用
去指定准备的R.java路径 如

```java
PluginInfo mPluginInfo = new PluginInfo(dexPath, PluginDemoHelper.DynamicAPKKey,PluginDemoHelper.DynamicPkgR);
```

```java
private void loadApkResource() {
    new Thread(new Runnable() {
        @Override
        public void run() {
            Context mContext = MainActivity.this;
            String dexPath = PluginDemoHelper.getApkPath(PluginDemoHelper.DynamicApk);
            PluginInfo mPluginInfo = new PluginInfo(dexPath, PluginDemoHelper.DynamicAPKKey);
            PluginManager.loadApk(mContext, mPluginInfo);
            Drawable pluginDrawable = PluginManager.getDrawable(MainActivity.this, PluginDemoHelper.DynamicAPKKey, "image_girl");
            mHandler.obtainMessage(MSG_UPDATE_PLUGIN_IMAGE, pluginDrawable).sendToTarget();

            String plugStr = PluginManager.getString(MainActivity.this, PluginDemoHelper.DynamicAPKKey, "app_name");
            mHandler.obtainMessage(MSG_UPDATE_PLUGIN_STRING, plugStr).sendToTarget();
        }
    }).start();
}

```





