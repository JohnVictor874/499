package com.example.bluetoothservertopear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.bluetoothservertopear.ui.theme.BluetoothChatTheme
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager

import android.content.Intent
import android.os.Build



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)

    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){/* not needed*/}

        val permissionLaucher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ){ perms ->
            val canEnebleBluetooth = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true

            if(canEnebleBluetooth && !isBluetoothEnabled){
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            permissionLaucher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }

        setContent {
            // Set up your theme here
            BluetoothChatTheme {
                val viewModel: BluetoothViewModel by viewModel()
                val state by viewModel.state.collectAsState()


                Surface (
                    color = MaterialTheme.colorScheme.background
                ){
                    DevicesScreen(
                        state = state,
                        onStatrtScan = viewModel::startScan,
                        onStopScan = viewModel::stopScan
                    )
                }

            }
        }
    }

}
