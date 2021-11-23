package android_serialport_api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class SerialPortManager {
    public static boolean DEBUG = false;
    private final String TAG = ">>>SerialPortManager";

    private SerialPort mSerialPort = null;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;

    private AtomicBoolean isPortOpen = new AtomicBoolean(false);
    private AtomicBoolean isThreadOpen = new AtomicBoolean(false);
    private ReadThread mReadThread;
    private boolean isOpenRead = false;

    /**
     * 默认开启串口读取
     * @param device
     */
    public SerialPortManager(Device device) {
        this(device, true);
    }

    public SerialPortManager(Device device, boolean isOpenRead) {
        this.isOpenRead = isOpenRead;
        this.openSerialPort(device);
    }

    /**
     * 开启串口
     * @param device
     * @return
     */
    private SerialPort openSerialPort(Device device) {
        try {
            File deviceFile = new File(device.getPath());
            if (!deviceFile.exists()) {
                Logi(TAG, "device is null == ");
                return null;
            }

            this.mSerialPort = new SerialPort(device);
            this.mInputStream = this.mSerialPort.getInputStream();
            this.mOutputStream = this.mSerialPort.getOutputStream();
            this.isPortOpen.set(true);

            if (isOpenRead){
                this.mReadThread = new ReadThread(mInputStream, isThreadOpen);
                this.mReadThread.start();
                this.isThreadOpen.set(true);
            }
        } catch (IOException e) {
            Loge(TAG, ">>>打开串口异常：" + e.toString());
            return this.mSerialPort;
        }
        Logi(TAG, ">>>打开串口成功");
        return this.mSerialPort;
    }

    public void closeSerialPort() {
        try {
            if (this.isPortOpen.get()) {
                this.isPortOpen.set(false);
                this.isThreadOpen.set(false);

                if (isOpenRead){
                    this.mReadThread.shutdown();
                    this.isThreadOpen.set(false);
                }

                this.mInputStream.close();
                this.mOutputStream.close();

                this.mSerialPort.close();

                Logi(TAG, ">>>关闭串口成功");
            }
        } catch (IOException e) {
            Loge(TAG, ">>>关闭串口异常 ：" + e.toString());
        }
    }

    public void sendData(byte[] sendData) {
        try {
            if (sendData.length > 0 && this.isPortOpen.get()) {
                this.mOutputStream.write(sendData);
                this.mOutputStream.flush();
            }
        } catch (IOException e) {
            Loge(TAG, ">>>数据发送失败：" + e.toString());
        }
    }

    public void setOnPortListener(OnPortListener onPortListener) {
        if (mReadThread != null){
            mReadThread.setOnPortListener(onPortListener);
        }
    }

    public SerialPort getSerialPort() {
        return mSerialPort;
    }

    public InputStream getInputStream() {
        return mInputStream;
    }

    public OutputStream getOutputStream() {
        return mOutputStream;
    }

    /**
     * 日志调试入口
     * @param tag
     * @param msg
     */
    protected static void Logi(@Nullable String tag, @NonNull String msg) {
        if (DEBUG){
            Log.i(tag, msg);
        }
    }

    protected static void Loge(@Nullable String tag, @NonNull String msg) {
        if (DEBUG){
            Log.i(tag, msg);
        }
    }

    /**
     * 串口数据回调
     */
    public interface OnPortListener {
        void onDataReceive(byte[] data, int length);
    }

}
