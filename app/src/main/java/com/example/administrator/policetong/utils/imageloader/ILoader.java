package com.example.administrator.policetong.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;

/**
 * Created by wanglei on 2016/11/27.
 */

public interface ILoader {

    void init(Context context);

    void loadNet(ImageView target, String url, Options options);

    void loadNet(Context context, String url, Options options, LoadCallback callback);

    void loadResource(ImageView target, int resId, Options options);

    void loadAssets(ImageView target, String assetName, Options options);

    void loadFile(ImageView target, File file, Options options);

    void clearMemoryCache(Context context);

    void clearDiskCache(Context context);

    void resume(Context context);

    void pause(Context context);

    void loadCircle(String url, ImageView target, Options options);

    void loadCircleWithBorder(String url, ImageView target, Options options, int width, int color);

    void loadCircleWithBorder(Bitmap photo, ImageView target, Options options, int width, int color);

    void loadCircleWithBorder(int photo, ImageView target, Options options, int width, int color);

    void loadCorner(String url, ImageView target, int radius, Options options);

    void loadCorner(Bitmap bitmap, RelativeLayout target, int radius, Options options);

    void loadCorner(String url, RelativeLayout target, int radius, Options options);

    class Options {

        // #imageloader
        public static int IL_LOADING_RES = ILoader.Options.RES_NONE;
        public static int IL_ERROR_RES = ILoader.Options.RES_NONE;

        public int loadingResId = RES_NONE;        //加载中的资源id
        public int loadErrorResId = RES_NONE;      //加载失败的资源id
        public ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

        public static final int RES_NONE = -1;

        public static Options defaultOptions() {
            return new Options(IL_LOADING_RES, IL_ERROR_RES);
        }

        public Options(int loadingResId, int loadErrorResId) {
            this.loadingResId = loadingResId;
            this.loadErrorResId = loadErrorResId;
        }

        public Options scaleType(ImageView.ScaleType scaleType) {
            this.scaleType = scaleType;
            return this;
        }
    }

}
