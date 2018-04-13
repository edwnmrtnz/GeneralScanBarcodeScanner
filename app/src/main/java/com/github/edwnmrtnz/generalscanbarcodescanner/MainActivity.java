package com.github.edwnmrtnz.generalscanbarcodescanner;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.edwnmrtnz.BarcodeDataListener;
import com.github.edwnmrtnz.BarcodeScannerConnectionListener;
import com.github.edwnmrtnz.BarcodeScannerHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btnConnectScanner;
    private Button btnDisconnectScanner;
    private Button btnSelectDevice;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        bindViews();

        clickHandler();

        checkSystemPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startBarcodeScanner();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopBarcodeScanner();
    }


    //region Click Event handlers
    private void clickHandler() {
        btnSelectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectScanner();

            }
        });

        btnDisconnectScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectScanner();
            }
        });

        btnConnectScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectScanner();
            }
        });
    }
    //endregion

    /**
     * CONNECT SCANNER
     * You must select your scanner through selectScanner()
     * before calling this method.
     * */
    private void connectScanner() {
        if(!BarcodeScannerHelper.getInstance().isConnected()){
            toast("Connecting to barcode scanner...");
            BarcodeScannerHelper.getInstance().connectScanner(new BarcodeScannerConnectionListener() {
                @Override
                public void onDisconnected() {
                    toast("Connection failed!");
                }

                @Override
                public void onConnected() {
                    toast("Connection success!");

                }
            });

        }else{
            toast("You are already connected");
        }
    }
    /**
     * DISCONNECT SCANNER
     * */

    private void disconnectScanner() {
        BarcodeScannerHelper.getInstance().disconnectScanner();
    }

    /**
     * SELECT SCANNER
     * This method is provided by generalscan jar library.
     * */
    private void selectScanner() {
        BarcodeScannerHelper.getInstance().selectScanner();
    }

    /**
     * Stop receiving data from barcode scanner
     * */

    private void stopBarcodeScanner() {
        BarcodeScannerHelper.getInstance().stopBarcodeBroadcastReceiver(context);
    }

    /**
     * Start receiving data from barcode Scanner
     * */
    private void startBarcodeScanner() {
        BarcodeScannerHelper.getInstance().startBarcodeBrodcastReceiver(context);

        BarcodeScannerHelper.getInstance().setOnBarcodeDataListener(new BarcodeDataListener() {
            @Override
            public void onDataReceived(String data) {
              toast("Scanned data: " + data);
            }
        });
    }


    private void checkSystemPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {

                List<String> listPermissionsNeeded = new ArrayList<>();

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.BLUETOOTH);
                }

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_ADMIN);
                }

                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 10);
                }
            }else if(checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED){
                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 11);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:{
                if(hasAllPermissionsGranted(grantResults)){
                    checkSystemPermissions();
                }else{
                    toast("Please allow permissions needed");
                    finishAffinity();
                }
                break;
            }
            case 11:{
                if (!Settings.canDrawOverlays(this)){
                    checkSystemPermissions();
                }
                break;
            }
        }
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void toast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void bindViews() {
        btnConnectScanner       = findViewById(R.id.btnConnectScanner);
        btnDisconnectScanner    = findViewById(R.id.btnDisconnectScanner);
        btnSelectDevice         = findViewById(R.id.btnSelectDevice);
    }
}
