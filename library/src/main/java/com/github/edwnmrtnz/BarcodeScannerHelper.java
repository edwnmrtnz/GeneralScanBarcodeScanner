package com.github.edwnmrtnz;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.generalscan.NotifyStyle;
import com.generalscan.OnConnectFailListener;
import com.generalscan.OnConnectedListener;
import com.generalscan.SendConstant;
import com.generalscan.bluetooth.BluetoothConnect;
import com.generalscan.bluetooth.BluetoothSettings;

/**
 * Created by edwin on 9/28/17.
 */

public class BarcodeScannerHelper {
    private static final String TAG = BarcodeScannerHelper.class.getSimpleName();
    private static BarcodeScannerHelper instance = null;
    private static Context context;
    private BarcodeBroadcastReceiver barcodeBroadcastReceiver;

    public BarcodeScannerHelper(Context context){
        this.context = context;

        initScanner();
    }

    public static void newInstance(Context context){
        if(instance == null){
            instance = new BarcodeScannerHelper(context);
        }
    }

    public static BarcodeScannerHelper getInstance(){
        if(instance == null)
            instance = new BarcodeScannerHelper(context);
        return instance;
    }

    public void initScanner(){
        BluetoothConnect.CurrentNotifyStyle = NotifyStyle.NotificationStyle1;
        BluetoothConnect.BindService(context);
    }


    public void selectScanner(){
        checkRequirements();

        BluetoothSettings.SetScaner(context);
    }

    public void connectScanner(final BarcodeScannerConnectionListener barcodeScannerConnectionListener){
        checkRequirements();

        BluetoothConnect.Connect();


        BluetoothConnect.SetOnConnectedListener(new OnConnectedListener() {
            @Override
            public void Connected() {
                Log.e(TAG, "Connecting to barcode scanner success!");
                barcodeScannerConnectionListener.onConnected();
            }
        });

        BluetoothConnect.SetOnConnectFailListener(new OnConnectFailListener() {
            @Override
            public void ConnectFail() {
                Log.e(TAG, "Connecting to barcode scanner failed!");
                barcodeScannerConnectionListener.onDisconnected();
            }
        });

    }

    public void isConnected(final BarcodeScannerConnectionListener barcodeScannerConnectionListener){
        checkRequirements();

        BluetoothConnect.SetOnConnectedListener(new OnConnectedListener() {
            @Override
            public void Connected() {
                Log.d(TAG, "Barcode Scanner is connected :D");
                barcodeScannerConnectionListener.onConnected();
            }
        });

        BluetoothConnect.SetOnConnectFailListener(new OnConnectFailListener() {
            @Override
            public void ConnectFail() {
                Log.e(TAG, "Barcode Scanner is disconnected :(");
                barcodeScannerConnectionListener.onDisconnected();
            }
        });
    }

    public void disconnectScanner(){
        checkRequirements();

        BluetoothConnect.Stop(context);
    }

    public boolean isConnected(){
        checkRequirements();

        return BluetoothConnect.isConnected();
    }

    public void startBarcodeBrodcastReceiver(Context mContext){

        checkRequirements();

        barcodeBroadcastReceiver = new BarcodeBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SendConstant.GetDataAction);
        filter.addAction(SendConstant.GetReadDataAction);
        filter.addAction(SendConstant.GetBatteryDataAction);

        try {
            mContext.registerReceiver(barcodeBroadcastReceiver, filter);
            Log.d(TAG, "barcodeBroadcastReceiver was registered for " + mContext.getClass().getSimpleName());
        }catch (Exception e){
            Log.e(TAG, "Something went wrong while registering barcodeBroadcastReceiver.");
        }
    }

    public void setOnBarcodeDataListener(final BarcodeDataListener barcodeDataListener){
        if(barcodeDataListener != null){
            barcodeBroadcastReceiver.setBarcodeDataListener(new BarcodeDataListener() {
                @Override
                public void onDataReceived(String data) {
                    barcodeDataListener.onDataReceived(data);
                }
            });
        }else{
            throw new NullPointerException("Broadcast receiver is null. You must call startBarcodeBrodcastReceiver().");
        }
    }

    public void stopBarcodeBroadcastReceiver(Context mContext){
        try {
            if(barcodeBroadcastReceiver != null){
                mContext.unregisterReceiver(barcodeBroadcastReceiver);
                Log.d(TAG, "barcodeBroadcastReceiver was stopped for " + mContext.getClass().getSimpleName());
            }
        }catch (Exception e){
            Log.e(TAG, "Something went wrong while unregistering barcodeBroadcastReceiver. " + e.getMessage());
        }
    }

    protected void checkRequirements() {
        if(instance == null){
            throw new NullPointerException("Please call newInstance(context) in application class. Thanks!");
        }
    }

}