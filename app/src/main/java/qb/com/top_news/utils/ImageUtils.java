package qb.com.top_news.utils;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageUtils {

    //图片命名常量
    public static final String IMAGE_FILE_NAME = "head_image.jpg";

    //状态常量
    public static final int CODE_GALLERY_REQUEST = 100;
    public static final int CODE_CAMERA_REQUEST = 101;
    public static final int CODE_RESULT_REQUEST = 102;

    public static void chooseImageFromGallery(Activity activity) {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    public static void chooseImageFromCamera(Activity activity) {
        Intent intentFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSDCard()) {
            intentFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        activity.startActivityForResult(intentFromCamera, CODE_CAMERA_REQUEST);
    }

    public static void cropRawPhoto(Activity activity, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 480);
        intent.putExtra("outputY", 480);
        intent.putExtra("return-data", true);

        activity.startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    //判断SD卡是否存在
    public static boolean hasSDCard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    //Bitmap转String 用于存储数据库
    public static String Bitmap2String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appIcon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appIcon, Base64.DEFAULT);
    }

    public static Bitmap String2Bitmap(String st) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}

