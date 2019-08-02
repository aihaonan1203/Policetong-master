package com.example.administrator.policetong.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.example.administrator.policetong.base.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by Administrator on 2018/9/14.
 */

public class FileUtils {
    public static String getBitmapDiskFile(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(DIRECTORY_PICTURES).getAbsolutePath();
        } else {
            cachePath =context.getFilesDir().getAbsolutePath();
        }
        return new File(cachePath +File.separator+ getBitmapFileName()).getAbsolutePath();
    }

    public static final String bitmapFormat = ".png";

    /**
     * 生成bitmap的文件名:日期，md5加密
     *
     * @return
     */
    public static String getBitmapFileName() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            String currentDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            mDigest.update(currentDate.getBytes("utf-8"));
            byte[] b = mDigest.digest();
            for (int i = 0; i < b.length; ++i) {
                String hex = Integer.toHexString(0xFF & b[i]);
                if (hex.length() == 1) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(hex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileName = stringBuilder.toString() + bitmapFormat;
        return fileName;
    }


    /**
     * 读取文本数据
     *
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readAssets(Context context, String fileName)
    {
        InputStream is = null;
        String content = null;
        try
        {
            is = context.getAssets().open(fileName);
            if (is != null)
            {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true)
                {
                    int readLength = is.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            content = null;
        }
        finally
        {
            try
            {
                if (is != null) is.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return content;
    }






    //复制文件到某个文件  有该文件则直接删除
    public static void CopySdcardFile(String fromFile, String toFile) {
        File file = new File(toFile);
        if(file.exists()){
            file.delete();
        }
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();

        } catch (Exception ignored) {
        }
    }





    //删除某个文件
    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }








    public static String getAddress(String name){
        return  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + name;
    }


    public  static Bitmap setImageBitmap(File photoPath){
        //获取imageview的宽和高
        int targetWidth=200;
        int targetHeight=150;

        //根据图片路径，获取bitmap的宽和高
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(photoPath+"",options);
        int photoWidth=options.outWidth;
        int photoHeight=options.outHeight;

        //获取缩放比例
        int inSampleSize=1;
        if(photoWidth>targetWidth||photoHeight>targetHeight){
            int widthRatio=Math.round((float)photoWidth/targetWidth);
            int heightRatio=Math.round((float)photoHeight/targetHeight);
            inSampleSize=Math.min(widthRatio,heightRatio);
        }

        //使用现在的options获取Bitmap
        options.inSampleSize=inSampleSize;
        options.inJustDecodeBounds=false;
        return  BitmapFactory.decodeFile(photoPath+"",options);
    }





    public static String getExternalAddress() {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = App.getApplication().getExternalFilesDir("");

        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = App.getApplication().getFilesDir();

        }

        return filesDir+"";

    }

}