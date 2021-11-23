package android_serialport_api;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
    private static final String TAG = "SerialPort";
    public FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort(Device device) throws SecurityException, IOException {
        File file = new File(device.getPath());
        if (!file.canRead() || !file.canWrite()) {
            try {
                Process su = Runtime.getRuntime().exec("/system/xbin/su");
                String cmd = "chmod 777 " + device.getPath() + "\nexit\n";
                su.getOutputStream().write(cmd.getBytes());
                if (su.waitFor() != 0 || !file.canRead() || !file.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception var5) {
                var5.printStackTrace();
                throw new SecurityException();
            }
        }

        this.mFd = open(device);
        if (this.mFd == null) {
            throw new IOException();
        } else {
            this.mFileInputStream = new FileInputStream(this.mFd);
            this.mFileOutputStream = new FileOutputStream(this.mFd);
        }
    }

    public InputStream getInputStream() {
        return this.mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return this.mFileOutputStream;
    }

    private static native FileDescriptor open(Device var0);

    public native void close();

    static {
        System.loadLibrary("serial_port");
    }
}
