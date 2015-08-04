package com.example.administrator.wishingwell.wishingWell.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.Log;


import java.io.File;
import java.io.IOException;

public final class ImageHelper {
    private static final String TAG = ImageHelper.class.getSimpleName();

    private ImageHelper() {
    }

    public static Bitmap extractThumbnail(File imgFile, int dstWidth, int dstHeight) {
        if (!imgFile.exists()) {
            return null;
        }
        Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        if (b == null) {
            return null;
        }
        Bitmap ret = ThumbnailUtils.extractThumbnail(b, dstWidth, dstHeight);
        b.recycle();
        return ret;
    }

    /**
     * ��ȡͼƬ���ԣ���ת�ĽǶ�
     *
     * @param path ͼƬ����·��
     * @return degree��ת�ĽǶ�
     */

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    // ��ȡͼƬ����ת�Ƕ�?

//    int angle = readPictureDegree(filePath);
//    if(angle!=0)    {
//        ?bm = rotateImageView(angle, bm_load);
//        if (bm == null) {
//        YLog.error(TAG, "rotaing picture fail: angle = " + angle);
//            return null; // ��תͼƬʧ��?
//        }
//        ?}
//
//    else
//
//    {?bm = bm_load;?bm_load = null;?}

    /**
     * ��תͼƬ(Ŀǰ֧����ȷ��ѡװ90�ȵ�λ)
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotateImageView(int angle, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        // ��תͼƬ ����?
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();

        // ?? �����µ�ͼƬ?
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

//    public static void setupGlideDiskCacheStrategy(final Context context) {
//        Glide.with(context).setDefaultOptions(new RequestManager.DefaultOptions() {
//            @Override
//            public <T> void apply(GenericRequestBuilder<T, ?, ?, ?> genericRequestBuilder) {
//                Log.d(TAG, "apply diskCacheStrategy to glide, DiskCacheStrategy.ALL");
//                genericRequestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
//            }
//        });
//    }
//
//    public static void setupGlideDiskCacheStrategy(final Fragment fragment) {
//        Glide.with(fragment).setDefaultOptions(new RequestManager.DefaultOptions() {
//            @Override
//            public <T> void apply(GenericRequestBuilder<T, ?, ?, ?> genericRequestBuilder) {
//                Log.d(TAG, "apply diskCacheStrategy to glide, DiskCacheStrategy.ALL");
//                genericRequestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
//            }
//        });
//    }
}
