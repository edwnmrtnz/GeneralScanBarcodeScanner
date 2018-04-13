package com.github.edwnmrtnz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.generalscan.SendConstant;

/**
 * Created by edwin on 8/7/17.
 */

public class BarcodeBroadcastReceiver extends BroadcastReceiver {

    BarcodeDataListener barcodeDataListener = null;

    public void setBarcodeDataListener(BarcodeDataListener barcodeDataListener1){
        barcodeDataListener = barcodeDataListener1;
    }

    public BarcodeBroadcastReceiver() {
    }

    String barcode = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SendConstant.GetBatteryDataAction)) {
            String data = intent.getStringExtra(SendConstant.GetBatteryData);
            //Toast.makeText(context, "Battery data: " + data, Toast.LENGTH_SHORT).show();
        }
        if (intent.getAction().equals(SendConstant.GetDataAction)) {
            String data = intent.getStringExtra(SendConstant.GetData);
            barcode += data;
            if(data.matches("[\\n\\r]+")){
                //   Toast.makeText(context, "Barcode: " + barcode, Toast.LENGTH_SHORT).show();

                barcodeDataListener.onDataReceived(barcode);

                barcode = "";
            }
        }
    }
}
