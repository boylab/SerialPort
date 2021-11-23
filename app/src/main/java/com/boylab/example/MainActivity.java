package com.boylab.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android_serialport_api.Device;
import android_serialport_api.SerialPortManager;

public class MainActivity extends AppCompatActivity {

    private SerialPortManager serialPortManager = null;

    private TextView text_Receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_Receive  = findViewById(R.id.text_Receive);

        findViewById(R.id.btn_Init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Device device= new Device("/dev/ttyMT2");
                device.setSpeed(38400);
                device.setParity('e');
                SerialPortManager.DEBUG = true;
                serialPortManager = new SerialPortManager(device);

                serialPortManager.setOnPortListener(new SerialPortManager.OnPortListener() {
                    @Override
                    public void onDataReceive(byte[] bytes, int j) {
                        text_Receive.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                text_Receive.append("\n数量"+j+"\n");
                                for (int i = 0; i < bytes.length; i++) {
                                    text_Receive.append(" ");
                                    text_Receive.append(String.format("%2x", bytes[i]));
                                }
                            }
                        }, 0);
                    }
                });
            }
        });

        findViewById(R.id.btn_ReadWeigh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] bytes = new byte[]{0x01, 0x04, 0x00, 0x00, 0x00, 0x02, 0x71, (byte) 0xCB};
                serialPortManager.sendData(bytes);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialPortManager.closeSerialPort();
    }
}