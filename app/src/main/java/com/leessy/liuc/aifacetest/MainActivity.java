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

    public void write(View view) {
        int a = AiFaceDataUtil.writeSnData("A123456798VVVVBB");
        Log.d(TAG, "write: =" + a);

//        String s = "A123456798AAAABB";
//        int i = AiChlFace.AiDogWriteData(s.getBytes(), s.getBytes().length);
//        int i2 = AiFace.AiDogWriteData(s.getBytes(), s.getBytes().length);
//        Log.d(TAG, "write: =" + i);
//        Log.d(TAG, "write: =" + i2);
    }

    public void read(View view) {
        String uuid = UUIDS.getUUID();
        Log.d(TAG, "read: uuid=" + uuid);
//        byte[] bytes = new byte[64];
//        int i = AiChlFace.AiDogReadData(bytes, 64);
//        Log.d(TAG, "read: i=" + i + "----" + Arrays.toString(bytes));
    }

    public void sdk(View view) {
        SerialReadClient serialReadClient = SerialReadClient.getInstance();
        int i1 = serialReadClient.openSerialPort("/dev/ttySAC0", 115200);
        Log.d(TAG, "sdk: caer=" + i1);
        ReadWriteCryptIC.SetSerialPortObj(serialReadClient);
        int i = AiChlFace.Init(getApplicationContext(), 1);
        Log.d(TAG, "sdk: " + i);
    }

    public void check(View view) {
        UUIDS.instance(getApplicationContext()).check();
    }
}
