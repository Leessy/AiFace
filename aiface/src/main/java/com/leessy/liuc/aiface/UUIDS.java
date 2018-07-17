package com.leessy.liuc.aiface;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.AiFace.AiFace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.UUID;

/**
 * <p>
 * 持久化uuid工具类
 */


public class UUIDS {
    private static final String TAG = UUIDS.class.getName();
    private static UUIDS device;
    private Context context;
    private final static String DEFAULT_NAME = "system_device_id";//ps文件名
    private final static String DEFAULT_FILE_NAME = ".system_device_id";//储存卡文件名
    private final static String DEFAULT_DEVICE_ID = "autodervice_id";
    private final static String FILE_ANDROID = Environment.getExternalStoragePublicDirectory("Android") + File.separator + DEFAULT_FILE_NAME;
    private final static String FILE_DCIM = Environment.getExternalStoragePublicDirectory("DCIM") + File.separator + DEFAULT_FILE_NAME;
    private static SharedPreferences preferences = null;
    private static final int SN_DATA_LENGTH = 16;

    public UUIDS(Context context) {
        this.context = context;
    }

    private String uuid;

    public static UUIDS buidleID(Context context) {
        if (device == null) {
            synchronized (UUIDS.class) {
                if (device == null) {
                    device = new UUIDS(context);
                }
            }
        }
        return device;
    }

    /**
     * 获取设备sn
     *
     * @return
     */
    public static String getUUID() {
        if (preferences == null) {
            Log.d(TAG, "Please check the UUIDS.buidleID in Application (this).Check ()");
            return "";
        }
        return preferences.getString(DEFAULT_DEVICE_ID, "");
    }

    /**
     * 获取设备编码
     *
     * @return
     */
    public static String getSerialNumber() {
        String num = "";
        byte[] bytes = new byte[AiFaceDataUtil.MAX_LENGTH];
        int i = AiFaceDataUtil.readData64(bytes);
        if (i == 0) {
            String serialNumber = AiFaceDataUtil.getSerialNumber(bytes);
            if (!TextUtils.isEmpty(serialNumber)) {
                return serialNumber;
            }
        }
        return num;
    }


//    //生成一个128位的唯一标识符
//    private String createUUID() {
////        return UUID.randomUUID().toString();
//        return get12UUID().toUpperCase();//全部转为大写
//    }
//
//    /**
//     * 获得12个长度的十六进制的UUID
//     *
//     * @return UUID
//     */
//    public static String get12UUID() {
//        UUID id = UUID.randomUUID();
//        String[] idd = id.toString().split("-");
//        return idd[0] + idd[1];
//    }

    public void check() {
        preferences = context.getSharedPreferences(DEFAULT_NAME, 0);
        uuid = preferences.getString(DEFAULT_DEVICE_ID, null);

        if (TextUtils.isEmpty(uuid)) {
            String snData = checkAiFaceSnData();
            if (!TextUtils.isEmpty(snData) && snData.length() == SN_DATA_LENGTH) {
                uuid = snData;
                preferences.edit().putString(DEFAULT_DEVICE_ID, uuid).apply();
            }
        }

        if (TextUtils.isEmpty(uuid)) {
            if (checkAndroidFile() == null) {
                uuid = checkDCIMFile();
                saveAndroidFile(uuid);
                Log.d(TAG, "Android directory was not found in UUID, from the DCIM directory to take out UUID\n");
            }

            if (checkDCIMFile() == null) {
                uuid = checkAndroidFile();
                saveDCIMFile(uuid);
                Log.d(TAG, "DCIM directory was not found in UUID, from the Android directory to take out UUID");
            }

            uuid = checkAndroidFile();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(DEFAULT_DEVICE_ID, uuid);
            editor.apply();

            Log.d(TAG, "save uuid SharePref:" + uuid);
        } else {

            if (checkAndroidFile() == null) {
                saveAndroidFile(uuid);
            }

            if (checkDCIMFile() == null) {
                saveDCIMFile(uuid);
            }

            //之前有数据，IC无数据重新写入
            String snData = checkAiFaceSnData();
            if (TextUtils.isEmpty(snData) || snData.length() != SN_DATA_LENGTH) {
                AiFaceDataUtil.writeSnData(snData);
            }
        }


        Log.d(TAG, "result uuid:" + uuid);
    }

    private String checkAndroidFile() {
        BufferedReader reader = null;
        try {
            File file = new File(FILE_ANDROID);
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAndroidFile(String id) {
        try {
            File file = new File(FILE_ANDROID);
            Log.d(TAG, "saveAndroidFile: " + file);
            FileWriter writer = new FileWriter(file);
            writer.write(id);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String checkDCIMFile() {
        BufferedReader reader = null;
        try {
            File file = new File(FILE_DCIM);
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDCIMFile(String id) {
        try {
            File file = new File(FILE_DCIM);
            Log.d(TAG, "saveDCIMFile: " + file);
            FileWriter writer = new FileWriter(file);
            writer.write(id);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private static String MD5(String s) {
//        if (!TextUtils.isEmpty(s)) {
//
//        }
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] bytes = md.digest(s.getBytes("utf-8"));
//            return toHex(bytes);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static String toHex(byte[] bytes) {
//        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
//        StringBuilder ret = new StringBuilder(bytes.length * 2);
//        for (int i = 0; i < bytes.length; i++) {
//            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
//            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
//        }
//        return ret.toString();
//    }

    /**
     * 检查加密IC模块 中sn数据
     */
    private String checkAiFaceSnData() {
        byte[] bytes = new byte[AiFaceDataUtil.MAX_LENGTH];
        int i = AiFaceDataUtil.readData64(bytes);
        if (i == 0) {
            return AiFaceDataUtil.getUUID(bytes);
        } else {
            return null;
        }
    }

}