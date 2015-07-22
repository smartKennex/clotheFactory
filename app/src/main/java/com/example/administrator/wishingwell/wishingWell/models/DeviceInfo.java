package com.example.administrator.wishingwell.wishingWell.models;

/**
 * Created by kennex on 2015/7/17.
 */

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class DeviceInfo {
    private static final String TAG = DeviceInfo.class.getSimpleName();

    private static class Phone {
        public final String manufacturer, model;

        public Phone(String manufacturer, String model) {
            this.manufacturer = manufacturer;
            this.model = model;
        }
    }

    final static List<Phone> CompatiblePhones = Collections.unmodifiableList(Arrays.asList(

            new Phone("小米", "MI 3W"),
            new Phone("三星", "GT-N7100"),
            new Phone("三星", "SM-N9006"),
            new Phone("小米", "MI NOTE LTE"),
            new Phone("三星", "GT-I9508V"),
            new Phone("华为", "HUAWEI MT7-TL10"),
            new Phone("小米", "2014812"),
            new Phone("三星", "SM-N9100"),
            new Phone("华为", "HUAWEI H60-L01"),
            new Phone("酷派", "Coolpad 8690"),
            new Phone("Vivo", "Vivo X5Max+"),
            new Phone("联想", "lenovo K30-T"),
            new Phone("OPPO", "x9007"),
            new Phone("三星", "SM-N9100"),
            new Phone("小米", "MI 4LTE")));


    final static List<Phone> phonesForToast = Collections.unmodifiableList(Arrays.asList(
            new Phone("三星S4", "I9508V/移动4G版"),
            new Phone("三星note2", "N7100/联通3G版"),
            new Phone("三星note3", "N9006/联通3G版"),
            new Phone("小米3", "联通3G版"),
            new Phone("小米note", "移动联通4G版"),
            new Phone("小米4", "移动联通哦4G版"),
            new Phone("红米2", "移动4G版"),
            new Phone("华为Mate7", "移动联通4G版"),
            new Phone("华为荣耀6", "联通3G版"),
            new Phone("酷派大神X7", "移动联通4G版"),
            new Phone("oppoFind7", "移动联通4G版"),
            new Phone("VIVO X5max", "移动4G版"),
            new Phone("联想乐檬K3", "移动联通4G版"),
            new Phone("三星note4", "N9108V/移动4G版")));


    private static final String UNKNOWN = "UNKNOWN";

    private String kernelVersion;

    private String androidVersion;
    private String device;
    private String model;
    private String product, manufacturer;
    private int sdkLevel;
    private String phoneNumber;
    private String androidId;
    private String imei;

    private final String fullDeviceId;

    private String adaptedDevice;

    public String getImei() {
        return imei;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public String getDevice() {
        return device;
    }

    public String getModel() {
        return model;
    }

    public String getProduct() {
        return product;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getSdkLevel() {
        return sdkLevel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAndroidId() {
        return androidId;
    }

    public DeviceInfo(Context context) {
        kernelVersion = System.getProperty("os.version"); // OS version


        androidVersion = Build.VERSION.RELEASE;
        manufacturer = Build.MANUFACTURER;
        device = Build.DEVICE;
        model = Build.MODEL;
        product = Build.PRODUCT;
        sdkLevel = Build.VERSION.SDK_INT;

        Log.d(TAG, String.format("OS Version: %s, build release: %s, Device: %s, Model: %s, Product: %s, Manufacturer: %s, SDK Level: %d",
                kernelVersion,
                androidVersion,      // API Level
                device,    // Device
                model,     // Model
                product,   // Product
                manufacturer,
                sdkLevel
        ));
        try {
            androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Android device id: " + androidId);
        } catch (Throwable t) {
            androidId = "";
        }

        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            phoneNumber = telephonyManager.getLine1Number();
            imei = telephonyManager.getDeviceId();
            Log.d(TAG, "phoneNumber: " + phoneNumber + ", IMEI: " + imei);
        } catch (Throwable t) {
            phoneNumber = "";
            imei = "";
        }

        fullDeviceId = (TextUtils.isEmpty(androidId) ? UNKNOWN : androidId) + "-" +
                (TextUtils.isEmpty(phoneNumber) ? UNKNOWN : phoneNumber) + "-" +
                (TextUtils.isEmpty(imei) ? UNKNOWN : imei);

        Log.d(TAG, "Full Device Id: " + fullDeviceId);

        adaptedDevice = "目前适配的机型如下：\n";
        for (int index = 0; index < phonesForToast.size(); index++) {
            Phone phone = phonesForToast.get(index);
            String text = (index == phonesForToast.size() - 1) ?
                    phone.manufacturer + "(" + phone.model + ")。 " :
                    phone.manufacturer + "(" + phone.model + ")、";
            adaptedDevice += text;
        }
    }

    public String getDeviceId() {
        return fullDeviceId;
    }

    public void showCompatibleNotice() {
    }
}
