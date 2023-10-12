package com.example.bluetoothservertopear

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scanDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevice: StateFlow<List<BluetoothDevice>>

    fun startDicovery()
    fun stopDiscovery()

    fun release()
}