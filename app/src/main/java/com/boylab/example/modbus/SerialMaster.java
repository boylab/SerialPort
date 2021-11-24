package com.boylab.example.modbus;

import android.util.Log;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilResponse;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteCoilsResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

import java.util.concurrent.atomic.AtomicBoolean;

import android_serialport_api.SerialPortManager;

public class SerialMaster {

    private ModbusMaster rtuMaster;
    private SerialWrapper serialWrapper;
    private AtomicBoolean isWrapperOpen = new AtomicBoolean(false);

    private static SerialMaster instance;

    public static SerialMaster newInstance(){
        if(instance==null){
            instance=new SerialMaster();
        }
        return instance;
    }

    public SerialMaster() {

    }

    public void setDebug(boolean isDebug) {
        SerialPortManager.DEBUG = isDebug;
    }

    public void startSerial(){
        if (isWrapperOpen.get()){
            if (SerialPortManager.DEBUG){
                Log.i("___boylab>>>___", "防止重复开启modbus");
            }
            return;
        }
        try {
            if (serialWrapper == null){
                serialWrapper = new SerialWrapper();
            }
            ModbusFactory modbusFactory = new ModbusFactory();
            rtuMaster = modbusFactory.createRtuMaster(serialWrapper);
            rtuMaster.setTimeout(500);
            rtuMaster.setRetries(0);
            rtuMaster.init();

            isWrapperOpen.set(true);
        } catch (Exception e) {
            e.printStackTrace();
            isWrapperOpen.set(false);
            if (SerialPortManager.DEBUG){
                Log.i("___boylab>>>___", "modbus开启异常" + e.getMessage());
            }
        }
    }

    public void stopSerial(){
        if (serialWrapper != null){
            serialWrapper.close();
            isWrapperOpen.set(false);
        }
    }

    //读输入寄存器
    public ReadInputRegistersResponse readInputRegisters(int slaveId, int start, int len) {
        try {
            ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, start, len);
            return (ReadInputRegistersResponse) rtuMaster.send(request);
        }
        catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        return null;
    }

    //读保持寄存器
    public ReadHoldingRegistersResponse readHoldingRegisters(int slaveId, int start, int len) {
        try {
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);
            return (ReadHoldingRegistersResponse) rtuMaster.send(request);
        }
        catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        return null;
    }

    //读离散量
    public ReadDiscreteInputsResponse readDiscreteInputs(int slaveId, int start, int len) {
        try {
            ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, start, len);
            return (ReadDiscreteInputsResponse) rtuMaster.send(request);
        }
        catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        return null;
    }

    //读线圈
    public ReadCoilsResponse readCoils(int slaveId, int start, int len) {
        try {
            ReadCoilsRequest request = new ReadCoilsRequest(slaveId, start, len);
            return (ReadCoilsResponse) rtuMaster.send(request);
        }
        catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        return null;
    }

    //写单个线圈
    public WriteCoilResponse writeCoils(int slaveId, int startOffset, boolean bdata) {
        try {
            WriteCoilRequest request = new WriteCoilRequest(slaveId, startOffset, bdata);
            return (WriteCoilResponse) rtuMaster.send(request);
        }
        catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        return null;
    }

    //写多个线圈
    public WriteCoilsResponse writeCoils(int slaveId, int startOffset, boolean[] bdata) {
        try {
            WriteCoilsRequest request = new WriteCoilsRequest(slaveId, startOffset, bdata);
            return (WriteCoilsResponse) rtuMaster.send(request);
        }
        catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        return null;
    }

    //写单个保持寄存器
    public WriteRegisterResponse writeHoldingRegister(int slaveId, int writeOffset, int writeValue) {
        try {
            WriteRegisterRequest request = new WriteRegisterRequest(slaveId, writeOffset, writeValue);
            return (WriteRegisterResponse) rtuMaster.send(request);
        }
        catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        return null;
    }

    //写多个寄存器
    public WriteRegistersResponse writeHoldingRegisters(int slaveId, int writeOffset, short[] writeValue) {
        try {
            WriteRegistersRequest request = new WriteRegistersRequest(slaveId, writeOffset, writeValue);
            return (WriteRegistersResponse) rtuMaster.send(request);
        }
        catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        return null;
    }


}
