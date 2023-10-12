package com.example.bluetoothservertopear

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevice: List<BluetoothDevice> = emptyList(),


)
