package com.example.administrator.wishingwell.wishingWell.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.wishingwell.wishingWell.WishingWellApp;
import java.text.SimpleDateFormat;


import java.io.*;
import java.security.*;
import java.util.*;


public class IOHelper {
    public static final String TAG = IOHelper.class.getSimpleName();
    public static int idx = 0;
    protected static char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static String readStringFromStream(InputStream is) {
        InputStreamReader inputReader = new InputStreamReader(is);
        BufferedReader buffReader = new BufferedReader(inputReader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }

    public static File createTempImageFile(String postfix) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        if (postfix != null) {
            imageFileName += "_" + postfix;
        }

        File storageDirFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        storageDirFile = new File(String.format("%s/%s", storageDirFile.getAbsolutePath(), WishingWellApp.getInstance().getString(R.string.app_name)));
        try {
            if (!storageDirFile.exists()) {
                if (storageDirFile.mkdirs()) {
                    Log.d(TAG, "Create dir success: " + storageDirFile.getAbsolutePath());
                } else {
                    Log.w(TAG, "Create dir failed: " + storageDirFile.getAbsolutePath());
                    return null;
                }
            }

            Log.d(TAG, String.format("File name: %s, directory: %s", imageFileName, storageDirFile.getAbsolutePath()));

            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDirFile      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            String path = image.getAbsolutePath();

            Log.d(TAG, "Picture path: " + path);

            return image;
        } catch (IOException ioe) {
            Log.w(TAG, "create image file failed.", ioe);
            return null;
        }
    }

    public static String createCacheVoiceFilePath() {
        return createCacheFilePath("aud");
    }

    public static String createCacheLogFilePath() {
        return createCacheFilePath("log");
    }

    private static long latestNewFileTimestamp = 0;
    private static long latestNewFileIndex = 0;

    public static String createCacheFilePath(String fileSuffix) {
        long nowTime = System.currentTimeMillis();
        latestNewFileIndex = (nowTime / 1000 == latestNewFileTimestamp / 1000) ? latestNewFileIndex + 1 : 0;
        latestNewFileTimestamp = nowTime;

        String fileName = "";
//        if (!TextUtils.isEmpty(filePrefix)) {
//            fileName += filePrefix + "_";
//        }
        fileName += new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(nowTime));
        if (latestNewFileIndex > 0) {
            fileName += "_" + latestNewFileIndex;
        }
        if (!TextUtils.isEmpty(fileSuffix)) {
            fileName += "." + fileSuffix;
        }

        File cacheDir = WishingWellApp.getInstance().getApplicationContext().getCacheDir();
        if (!cacheDir.exists()) {
            try {
                if (cacheDir.createNewFile()) {
                    Log.d(TAG, "Create dir success: " + cacheDir.getAbsolutePath());
                } else {
                    Log.w(TAG, "Create dir failed: " + cacheDir.getAbsolutePath());
                    return null;
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString(), e);
                return null;
            }
        }

        return String.format("%s/%s", cacheDir.getAbsolutePath(), fileName);
    }

    /**
     * 获取字符串UTF-8编码后的MD5
     *
     * @param string
     * @return
     */
    public static String getMD5OfUtf8String(String string) {
        if (StringHelper.isNullOrEmpty(string)) {
            return "";
        }
        try {
            final byte[] byteBuffer = string.getBytes("UTF-8");
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            return bufferToHex(md5.digest(byteBuffer));
        } catch (Throwable t) {
            Log.d(TAG, "getMD5OfUtf8String failed, string: " + string, t);
            return "";
        }
    }

    public static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
