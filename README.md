# GeneralScanBarcodeScanner

### Summary
A Simple helper for GeneralScan barcode scanner.

### How to use
Create a class that extends Application and create a newInstance of BarcodeScannerHelper.

```java
public class ScannerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BarcodeScannerHelper.newInstance(this);
    }
}
```

Inside your manifest file, add the following permissions:

```xml****
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />

<!-- Show Alert Dialog in Service -->
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

Add this Service inside the <application> block of your manifest.

```xml
<service android:name="com.generalscan.bluetooth.connect.ATService"
         android:enabled="true"/>
```

To connect to your barcode scanner, select your device through

```java
BarcodeScannerHelper.getInstance().selectScanner();
```

After selecting a device, call connectScanner from BarcodeScannerHelper and implment the BarcodeScannerConnectionListner.

```java
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
```

To start receiving data, you need to start the broadcast receiver. Just call startBarcodeBroacastReceiver() of the BarcodeScannerHelper and implement a BarcodeDataListener.
You can call this inside onCreate() or onStart() of your activity.
```java
BarcodeScannerHelper.getInstance().startBarcodeBrodcastReceiver(context);
BarcodeScannerHelper.getInstance().setOnBarcodeDataListener(new BarcodeDataListener() {
    @Override
    public void onDataReceived(String data) {
      toast("Scanned data: " + data);
    }
});
```

To stop receiving data, just call stopBarcodeBroadcastReceiver(). You can call this inside
onStop() or onDestroy() of your activity.

```java
BarcodeScannerHelper.getInstance().stopBarcodeBroadcastReceiver(context);
```
