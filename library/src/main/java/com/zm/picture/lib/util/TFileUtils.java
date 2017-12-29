package com.zm.picture.lib.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Author: crazycodeboy
 * Date: 2016/11/5 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.devio.org/
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class TFileUtils {
    private static final String TAG="TFileUtils";
    private static String DEFAULT_DISK_CACHE_DIR = "takephoto_cache";
    public static File getPhotoCacheDir(Context context, File file) {
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            File mCacheDir = new File(cacheDir,DEFAULT_DISK_CACHE_DIR);
            if (!mCacheDir.mkdirs() && (!mCacheDir.exists() || !mCacheDir.isDirectory())) {
                return file;
            }else {
                return new File(mCacheDir, file.getName());
            }
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return file;
    }

    public static void delete(String path) {
        try {
            if(path == null) {
                return ;
            }
            File file = new File(path);
            if(!file.delete()) {
                file.deleteOnExit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        Log.e(TAG, "ExternalStorage not mounted");
        return false;
    }

    private static File createMediaFile(Context context, String parentPath, String timeStamp){
        File cacheDir = !isExternalStorageWritable()? context.getFilesDir(): context.getExternalCacheDir();
        File folderDir = new File(cacheDir.getAbsolutePath() + parentPath);
        if (!folderDir.exists() && folderDir.mkdirs()){

        }
        String fileName =timeStamp + "";
        File tmpFile = new File(folderDir, fileName + ".png");
        return tmpFile;
    }
    public static File createCameraFile(Context context) {
        return createMediaFile(context,"/Camera/", System.currentTimeMillis()+"");
    }
}
