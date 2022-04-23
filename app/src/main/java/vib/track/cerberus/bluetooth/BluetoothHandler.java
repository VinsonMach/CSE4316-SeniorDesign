package vib.track.cerberus.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.companion.AssociationRequest;
import android.companion.BluetoothLeDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.UUID;

public class BluetoothHandler /*I hardly know her*/ {
    private static BluetoothGatt gatt;
    private static BluetoothDevice deviceToPair;
    private static ActivityResultLauncher resultLauncher;

    private static BluetoothLeDeviceFilter deviceFilter;
    private static AssociationRequest pairingRequest;
    private static CompanionDeviceManager deviceManager;

    private static UUID serv_uuid = UUID.fromString("19B10000-E8F2-537E-4F6C-D104768A1214");
    private static UUID char_uuid = UUID.fromString("19B10001-E8F2-537E-4F6C-D104768A1214");

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void init(AppCompatActivity app) {
        resultLauncher = app.registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result != null && result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                ScanResult scanResult = data.getParcelableExtra(
                                        CompanionDeviceManager.EXTRA_DEVICE
                                );
                                deviceToPair = scanResult.getDevice();
                                if (deviceToPair != null) {
                                    if (ActivityCompat.checkSelfPermission(app.getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        app.requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
                                    }
                                    gatt = deviceToPair.connectGatt(app.getApplicationContext(), true, new BluetoothGattCallback() {
                                        @SuppressLint("MissingPermission")
                                        @Override
                                        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                                            super.onConnectionStateChange(gatt, status, newState);
                                            Log.d("BLE","connectionStateChange: " + status);

                                            if (BluetoothProfile.STATE_CONNECTED == newState) {
                                                gatt.discoverServices();
                                            }
                                        }

                                        @Override
                                        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                                            super.onServicesDiscovered(gatt, status);
                                            Log.d("BLE", "DISCOVERD");
                                            if (status != BluetoothGatt.GATT_SUCCESS) {
                                                Log.d("BLE", "REJECTED");
                                                return;
                                            }
                                        }
                                    });
                                    gatt.discoverServices();
                                }
                            }
                        }
                    }
                });
        deviceFilter =
                new BluetoothLeDeviceFilter.Builder()
                        .build();
        pairingRequest = new AssociationRequest.Builder()
                .addDeviceFilter(deviceFilter)
                .build();
        deviceManager =
                (CompanionDeviceManager) app.getSystemService(
                        Context.COMPANION_DEVICE_SERVICE
                );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void scan(AppCompatActivity app){
        deviceManager.associate(pairingRequest,
                new CompanionDeviceManager.Callback() {
                    @Override
                    public void onDeviceFound(IntentSender chooseLauncher) {
                        IntentSenderRequest intentSenderRequest =
                                new IntentSenderRequest.Builder(chooseLauncher).build();

                        resultLauncher.launch(intentSenderRequest);
                    }

                    @Override
                    public void onFailure(CharSequence charSequence) {
                        Log.d("BLE", charSequence.toString());
                    }
                }, null);
    }

    @SuppressLint("MissingPermission")
    public static void write(String toWrite){
        if(gatt == null){
            Log.d("BluetoothHandler", "No scan yet");
            return;
        }
        BluetoothGattService serv = gatt.getService(serv_uuid);
        BluetoothGattCharacteristic characteristic = serv.getCharacteristic(char_uuid);
        characteristic.setValue(toWrite);
        gatt.writeCharacteristic(characteristic);
    }

}
