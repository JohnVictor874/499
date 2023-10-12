package com.example.bluetoothservertopear

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.bluetoothservertopear.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain{
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}