package com.example.administrator.wishingwell.wishingWell.utils;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;


public class PhotoHelper {
    private final static String TAG = PhotoHelper.class.getSimpleName();

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_GALLERY = 2;
    public static final int REQUEST_IMAGE_CUSTOM_CAMERA = 3;

    public static void cropPhoto(Activity activity, File origiPhoto, File croppedPhoto) {
        Uri croppedImageUri = Uri.fromFile(croppedPhoto);
        Log.d(TAG, "croppedImage uri: " + croppedImageUri.toString());

        Uri origiImageFileUri = Uri.fromFile(origiPhoto);

        Log.d(TAG, "origiImageFile uri: " + origiImageFileUri);
        new Crop(origiImageFileUri).output(croppedImageUri).asSquare().start(activity);
    }

    /**
     * 仅对图片二次确认，不产生任何修改
     */
    public static void confirmPhoto(Activity activity, File origiPhoto) {
        Uri origiImageFileUri = Uri.fromFile(origiPhoto);
        new Crop(origiImageFileUri).asConfirm().start(activity);
    }

    public static File takePhoto(final Activity activity) {
        File photoFile = IOHelper.createTempImageFile("origi");

        if (photoFile == null) {
            return null;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {

            takePictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));

            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        return photoFile;
    }

    /**
     * 使用自定义相机拍照并获得方形照片
     *
     * @param activity 调用者
     * @param isLandscape 相机初始持向
     * @return
     */
    public static File takeSquaredPhoto(final Activity activity, boolean isLandscape) {
        File photoFile = IoHelper.createTempImageFile(null);
        Intent takePictureIntent = new Intent(activity, CameraActivity.class);
        if (photoFile != null) {
            takePictureIntent.setData(Uri.fromFile(photoFile));
        }
        takePictureIntent.putExtra(CameraActivity.ORIENTATION_LANDSCAPE_KEY, isLandscape);
        activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CUSTOM_CAMERA);

        return photoFile;
    }

    public static void selectPhoto(Activity activity) {
        Intent intent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }
}
