##介绍
>拍照、裁剪、获取本地图片重构 (适配7.0)
正在开发

## 引入

* Gradle 
   
   ```
    compile 'com.zm.picture.lib:album_picture:1.0.0'
   ```
* Maven
	
	```
    <dependency>
      <groupId>com.zm.picture.lib</groupId>
      <artifactId>album_picture</artifactId>
      <version>1.0.0</version>
      <type>pom</type>
    </dependency>
	
	```




## 具备功能
        1.拍照
        2.图片裁剪
        3.压缩图片
        4.获取本地图片
        
## 需要权限
      <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /><!--写存储卡-->
      <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /><!--读存储卡-->
      <uses-permission android:name="android.permission.CAMERA" /><!--打开摄像头-->

#### 代码混淆
     
>如果你的项目中启用了代码混淆，可在混淆规则文件(如：proguard-rules.pro)中添加如下代码：
     
    -keep class com.zm.picture.lib.** { *; }
    -dontwarn com.zm.picture.lib.**
     
        
## 具体用法请查看DEMO
>[MainActivity](https://github.com/scalling/AlbumPicture/blob/master/app/src/main/java/com/zm/picture/sample/MainActivity.java)
        