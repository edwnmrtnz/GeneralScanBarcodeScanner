# GeneralScanBarcodeScanner
### Summary
A Simple helper for GeneralScan Barcode scanner Bluetooth feature.

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

Inside your manifest file, add the following permissions

```xml
 	<uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    
    <!-- Show Alert Dialog in Service -->
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

then add this service inside <application> block of your manifest.

```xml
  <service android:name="com.generalscan.bluetooth.connect.ATService"
            android:enabled="true"/>
```

To connect to barcode scanner, first you must select the device using this method.

```java
 	BarcodeScannerHelper.getInstance().selectScanner();
```

After selection of device, call

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

To start receiving data, Add this line in your Activity's onStart to start broadcastreceiver.

```java
	 BarcodeScannerHelper.getInstance().startBarcodeBrodcastReceiver(context);

	        BarcodeScannerHelper.getInstance().setOnBarcodeDataListener(new BarcodeDataListener() {
	            @Override
	            public void onDataReceived(String data) {
	              toast("Scanned data: " + data);
	            }
	        });
```

Don't forget to stop the broadcastreceiver in your Activity's onStop

```java
 	BarcodeScannerHelper.getInstance().stopBarcodeBroadcastReceiver(context);
```
