package com.github.edwnmrtnz.generalscanbarcodescanner;

import android.app.Application;

import com.github.edwnmrtnz.BarcodeScannerHelper;

public class ScannerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BarcodeScannerHelper.newInstance(this);
    }
}
