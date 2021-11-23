package android_serialport_api;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReadThread extends AbsLoopThread{

    private InputStream mInputStream;
    private AtomicBoolean isThreadOpen;

    private SerialPortManager.OnPortListener onPortListener = null;

    public ReadThread(InputStream mInputStream, AtomicBoolean isThreadOpen) {
        super();
        this.mInputStream = mInputStream;
        this.isThreadOpen = isThreadOpen;
    }

    public void setOnPortListener(SerialPortManager.OnPortListener onPortListener) {
        this.onPortListener = onPortListener;
    }

    @Override
    protected void beforeLoop() throws Exception {
        super.beforeLoop();
        Log.i(">>>ReadThread", isThreadOpen.get() ? ">>>读取线程开启":">>>读取线程未开启");
    }

    @Override
    protected void runInLoopThread() throws Exception {
        if (isThreadOpen.get()) {
            try {
                byte[] buffer = new byte[1024];
                if (mInputStream == null) {
                    SerialPortManager.Loge(">>>ReadThread", ">>>mInputStream is null");
                    return;
                }

                int size = mInputStream.read(buffer);
                if (size > 0) {
                    byte[] readBytes = new byte[size];
                    System.arraycopy(buffer, 0, readBytes, 0, size);
                    if (onPortListener != null) {
                        onPortListener.onDataReceive(readBytes, size);
                    }
                }
            } catch (IOException e) {
                SerialPortManager.Loge(">>>ReadThread", ">>>数据读取异常：" + e.toString());
            }
        }
    }

    @Override
    protected void loopFinish(Exception e) {
        SerialPortManager.Logi(">>>ReadThread", ">>>读取线程关闭");
    }
}
