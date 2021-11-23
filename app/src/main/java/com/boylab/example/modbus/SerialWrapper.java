package com.boylab.example.modbus;

import com.serotonin.modbus4j.serial.SerialPortWrapper;

import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.Device;
import android_serialport_api.SerialPortManager;

public class SerialWrapper implements SerialPortWrapper {

    private Device device;
    private SerialPortManager serialPortManager;

    public SerialPortManager getSerialPortManager() {
        return serialPortManager;
    }

    @Override
    public void close() {
        if (serialPortManager != null){
            serialPortManager.closeSerialPort();
            serialPortManager = null;
        }
    }

    @Override
    public void open(){
        if(serialPortManager!= null) {
            serialPortManager.closeSerialPort();
        }
        device= new Device("/dev/ttyMT2");
        device.setSpeed(38400);
        device.setParity('e');
        serialPortManager = new SerialPortManager(device, false);   //不开启读线程，交给modbus
    }

    @Override
    public InputStream getInputStream() {
        return serialPortManager.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return serialPortManager.getOutputStream();
    }

    @Override
    public int getBaudRate() {
        return device.getSpeed();
    }

    @Override
    public int getDataBits() {
        return device.getDataBits();
    }

    @Override
    public int getStopBits() {
        return device.getStopBits();
    }

    @Override
    public int getParity() {
        return device.getParityInt();
    }
}
