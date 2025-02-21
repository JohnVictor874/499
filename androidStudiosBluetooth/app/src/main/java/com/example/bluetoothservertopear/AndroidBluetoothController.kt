package com.example.bluetoothservertopear

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.security.Permission
import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentFilter

@SuppressLint("MissingPermission")
class AndroidBluetoothController(
    private val context: Context
): BluetoothController {

    private val bluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    init {
        updatePairedDevices()
    }

    private val _scannedDevises = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val scanDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevises.asStateFlow()
    private val _pairedDivices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val pairedDevice: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDivices.asStateFlow()

    private val foundDeviceReceiver = FoundDeviceReceiver{device ->
        _scannedDevises.update { devices ->
            val newDevice = device.toBluetoothDeviceDomain()
            if(newDevice in devices) devices else devices + newDevice
        }

    }

    override fun startDicovery() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_SCAN)){
            return
        }

        context.registerReceiver(
            foundDeviceReceiver, IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND)
        )

        updatePairedDevices()

        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_SCAN)){
            return
        }
        bluetoothAdapter?.cancelDiscovery()
    }

    override fun release() {
        context.unregisterReceiver(foundDeviceReceiver)
    }


    private fun updatePairedDevices() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)){
            return
        }
        bluetoothAdapter
            ?.bondedDevices
            ?.map { it.toBluetoothDeviceDomain() }
            ?.also { devices -> _pairedDivices.update { devices } // Update _pairedDevices using .value
        }
    }

    private fun hasPermission(permission: String):Boolean{
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
}