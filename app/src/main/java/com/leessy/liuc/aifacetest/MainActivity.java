package com.leessy.liuc.aifacetest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.AiChlFace.AiChlFace;
import com.AiChlFace.ReadWriteCryptIC;
import com.AiChlIrFace.AiChlIrFace;
import com.AiFace.AiFace;
import com.kaer.sdk.KaerReadClient;
import com.kaer.sdk.OnClientCallback;
import com.kaer.sdk.otg.OtgReadClient;
import com.kaer.sdk.serial.SerialReadClient;
import com.kaer.sdk.union.Keys.Kaer;
import com.kaer.sdk.utils.CardCode;
import com.kaer.sdk.utils.LogUtils;
import com.leessy.liuc.aiface.AiFaceDataUtil;
import com.leessy.liuc.aiface.UUIDS;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Log.d(TAG, "onCreate: 启动一次");
    }

    //test
    public void write(View view) {
//        int a = AiFaceDataUtil.writeSnData("A123456798VVVVBB");
//        Log.d(TAG, "write: =" + a);

//        int a = AiFaceDataUtil.writeSerialNumber("AAAAAAAADAAAAAAA");
//        Log.d(TAG, "write: 写入DN=" + a);
//        KaerReadClient.getVersionInfo()
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
        initOTG(1155, 22352);

//        SerialReadClient serialReadClient = SerialReadClient.getInstance();
//        int i1 = serialReadClient.openSerialPort("/dev/ttySAC0", 115200);
//        Log.d(TAG, "sdk: caer=" + i1);
//        ReadWriteCryptIC.SetSerialPortObj(serialReadClient);
//        int i = AiChlFace.InitCardLicense(getApplicationContext(), 1);
//        int i = AiChlFace.InitDm2016License(getApplicationContext(), 1);
//        int i = AiFace.InitComplex(this);
//        int i = AiFace.InitDebug(this);
//        Log.d(TAG, "sdk: " + i);
//        Log.d(TAG, "sdk: ==" + i);
//        Toast.makeText(this, "算法初始化=" + i, Toast.LENGTH_LONG).show();
    }


    /**
     * 初始化OTG读卡模块
     *
     * @param vid
     * @param pid
     */
    private void initOTG(int vid, int pid) {
        final OtgReadClient mOtgReadClient = OtgReadClient.getInstance();
        mOtgReadClient.setClientCallback(new OnClientCallback() {
            @Override
            public void preExcute(long l) {
                Log.d(TAG, "preExcute: ******");
            }

            @Override
            public void updateProgress(int i) {
                Log.d(TAG, "updateProgress: *****");
            }

            @Override
            public void onConnectChange(int i) {
                Log.d(TAG, "onConnectChange: *********");
            }
        });

        mOtgReadClient.setVidPid(vid, pid);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mOtgReadClient.getOtgState() != mOtgReadClient.Connected) {
                    int i = mOtgReadClient.connectDevice(mContext);
                    Log.d(TAG, "run: 卡尔启动返回值===" + i);
                    if (i != CardCode.KT8000_Success) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }


                //设置参数
                if (OtgReadClient.getInstance().getOtgState() == OtgReadClient.getInstance().Connected) {
                    ReadWriteCryptIC.SetOtgReadPort(OtgReadClient.getInstance());
                    com.AiChlIrFace.ReadWriteCryptIC.SetOtgReadPort(OtgReadClient.getInstance());
                } else if (SerialReadClient.getInstance().getSerialState() == SerialReadClient.getInstance().Connected) {
                    ReadWriteCryptIC.SetSerialPortObj(SerialReadClient.getInstance());
                    com.AiChlIrFace.ReadWriteCryptIC.SetSerialPortObj(SerialReadClient.getInstance());
                }
                int i = AiChlIrFace.InitCardLicense(getApplicationContext(), 4);
                Log.d(TAG, "run: 初始化结果=" + i);
                //板载加密芯片 本地读卡不需要联网
//                Connection();
                //开启自动身份证扫描
//                startScanCard();
            }
        }).start();


        /**********关闭一些log***********/
        if (!BuildConfig.DEBUG) {
            LogUtils.setDebug(false);//关闭卡尔log
            LogUtils.LOG_FILE = false;
            com.serenegiant.utils.LogUtils.logLevel(0);//相机包log 为最低
        }
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
