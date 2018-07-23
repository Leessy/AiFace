package com.leessy.liuc.aiface;

import android.text.TextUtils;
import android.util.Log;

import com.AiChlFace.AiChlFace;
import com.AiChlIrFace.AiChlIrFace;
import com.AiFace.AiFace;
import com.AiIrFace.AiIrFace;

import java.util.Arrays;

public class AiFaceDataUtil {
    private static final String TAG = "AiFaceDataUtil";
    /**
     * 读取存储数据最大长度
     */
    public static final int MAX_LENGTH = 64;
    public static final int SN_MAX_LENGTH = 16;
    public static final int SERIAL_NUMBER_MAX_LENGTH = 8;

    /**
     * 读出全部数据
     * <p>
     * 根据 调用init方法的结果来进行准确调用
     *
     * @param bytes 读取数据内容
     * @return 读取成功 返回 0,失败返回其他
     */
    public static int readData64(byte[] bytes) {
        int ret = -1;
        if (bytes == null || bytes.length != MAX_LENGTH) {
            return ret;
        }
        if (AiFace.inits == 0 && AiFace.AiDogReadData(bytes, MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlFace.inits == 0 && AiChlFace.AiDogReadData(bytes, MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiIrFace.inits == 0 && AiIrFace.AiDogReadData(bytes, MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlIrFace.inits == 0 && AiChlIrFace.AiDogReadData(bytes, MAX_LENGTH) == 0) {
            ret = 0;
        }
        return ret;
    }

    /**
     * 写入设备编码数据
     *
     * @param sn 设备sn
     * @return 返回0成功  其他失败
     */
    public static int writeSnData(String sn) {
        int ret = -1;
        if (TextUtils.isEmpty(sn) || sn.length() != SN_MAX_LENGTH) {
            return ret;
        }

        byte[] bytes = sn.getBytes();
        if (AiFace.inits == 0 && AiFace.AiDogWriteData(sn.getBytes(), SN_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlFace.inits == 0 && AiChlFace.AiDogWriteData(sn.getBytes(), SN_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiIrFace.inits == 0 && AiIrFace.AiDogWriteData(sn.getBytes(), SN_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlIrFace.inits == 0 && AiChlIrFace.AiDogWriteData(sn.getBytes(), SN_MAX_LENGTH) == 0) {
            ret = 0;
        }
        return ret;
    }

    /**
     * 写入 设备编码数据
     *
     * @param SerialNumber 设备编码
     * @return 返回0成功  其他失败
     */
    public static int writeSerialNumber(String SerialNumber) {
        int ret = -1;
        if (TextUtils.isEmpty(SerialNumber) || SerialNumber.length() != SERIAL_NUMBER_MAX_LENGTH) {
            return ret;
        }

        byte[] bytes = new byte[MAX_LENGTH];
        int i = readData64(bytes);
        if (i != 0) {
            return ret;
        }
        //获取 UUID 拼接数据重新写入
        String uuid = getUUID(bytes);
        if (TextUtils.isEmpty(uuid) || uuid.length() != SN_MAX_LENGTH) {
            uuid = "0000000000000000";//UUID 为空时默认数据
        }
        String data = uuid + SerialNumber;
        if (AiFace.inits == 0 && AiFace.AiDogWriteData(data.getBytes(), SN_MAX_LENGTH + SERIAL_NUMBER_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiIrFace.inits == 0 && AiIrFace.AiDogReadData(data.getBytes(), SN_MAX_LENGTH + SERIAL_NUMBER_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlFace.inits == 0 && AiChlFace.AiDogReadData(data.getBytes(), SN_MAX_LENGTH + SERIAL_NUMBER_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlIrFace.inits == 0 && AiChlIrFace.AiDogReadData(data.getBytes(), SN_MAX_LENGTH + SERIAL_NUMBER_MAX_LENGTH) == 0) {
            ret = 0;
        }
        return ret;
    }

    /**
     * 截取内容中 UUID 0-16
     *
     * @param bytes
     * @return
     */
    public static String getUUID(byte[] bytes) {
        if (bytes == null || bytes.length != MAX_LENGTH) {
            return "";
        }
        return new String(bytes, 0, 16).trim();
    }

    /**
     * 截取 设备编号 16-24
     *
     * @param bytes
     * @return
     */
    public static String getSerialNumber(byte[] bytes) {
        if (bytes == null || bytes.length != MAX_LENGTH) {
            return "";
        }
        return new String(bytes, 16, 8).trim();
    }
}
