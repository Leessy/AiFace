package com.leessy.liuc.aiface;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class AiFaceUpDateLicense {
    private static final String TAG = "AiFaceUpDateLicense";

    public static boolean UpDateLicense(Context mContext, String strCacheDir) {
        String s = mContext.getApplicationInfo().nativeLibraryDir;
        if (TextUtils.isEmpty(s)) {
            String pkg = mContext.getPackageName();
            s = "/data/data/" + pkg + "/lib";///data/data/com.onfacemind.aiface902
        }
        return cmd_copy(s + "/libUpdateLicense.so", strCacheDir + "/libUpdateLicense.so");
    }

    //拷贝文件
    public static boolean cmd_copy(String dr1, String dr2) {
        Log.d(TAG, "cmd_copy: 开始 拷贝文件" + dr1 + "--" + dr2);
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command1 = "chmod 777 " + dr1 + "\n";
            String command = "cp " + dr1 + " " + dr2 + "\n";//有效 -2文件无效
//            String command = "dd if=" + dr1 + " of=" + dr2;//有效
            String command2 = "chmod 777 " + dr2 + "\n";

            dataOutputStream.write(command1.getBytes(Charset.forName("utf-8")));
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.write(command2.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            Log.d(TAG, "cmd_copy: 复制授权文件 完成");
            result = true;
        } catch (Exception e) {
            android.util.Log.e("TAG", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                android.util.Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }
}
