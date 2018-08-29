package com.leessy.liuc.aifacetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.AiChlFace.AiChlFace;
import com.AiChlFace.ReadWriteCryptIC;
import com.AiFace.AiFace;
import com.kaer.sdk.serial.SerialReadClient;
import com.leessy.liuc.aiface.AiFaceDataUtil;
import com.leessy.liuc.aiface.UUIDS;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //test
    public void write(View view) {
//        int a = AiFaceDataUtil.writeSnData("A123456798VVVVBB");
//        Log.d(TAG, "write: =" + a);

//        int a = AiFaceDataUtil.writeSerialNumber("AAAAAAAADAAAAAAA");
//        Log.d(TAG, "write: 写入DN=" + a);
    }

    //test
    public void read(View view) {
//        byte[] bytes1 = "asd03 0123AZSCVacsad".getBytes();
//        for (int i = 0; i < bytes1.length; i++) {
//            Log.d(TAG, "read: =" + bytes1[i]);
//        }

        UUIDS.instance(this).check();
        String uuid = UUIDS.getUUID();
        Log.d(TAG, "read: uuid=" + uuid);
//        String serialNumber = UUIDS.getSerialNumber();
//        Log.d(TAG, "read: 读取DN=" + serialNumber);

        byte[] bytes = new byte[64];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 48;
        }
        int i = AiChlFace.AiDogReadData(bytes, 64);
        Log.d(TAG, "read: i=" + i + "----" + Arrays.toString(bytes));
    }

    //test
    public void sdk(View view) {
        SerialReadClient serialReadClient = SerialReadClient.getInstance();
        int i1 = serialReadClient.openSerialPort("/dev/ttySAC0", 115200);
        Log.d(TAG, "sdk: caer=" + i1);
        ReadWriteCryptIC.SetSerialPortObj(serialReadClient);
        int i = AiChlFace.InitCardLicense(getApplicationContext(), 1);
        Log.d(TAG, "sdk: " + i);
    }

    //test
    public void check(View view) {
        UUIDS.instance(getApplicationContext()).check();
    }

    /**
     * 检验非法字符
     *
     * @param bytes
     * @return
     */
    private static boolean checkChars(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] > 122 || bytes[i] < 48) {
                return false;
            }
        }
        return true;
    }
}
