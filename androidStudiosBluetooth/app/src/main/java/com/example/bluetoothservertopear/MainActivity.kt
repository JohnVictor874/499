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
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build

import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.bluetoothservertopear.BluetoothViewModel
import dagger.hilt.android.qualifiers.ApplicationContext


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)

    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Set up your theme here
            BluetoothChatTheme {
                val viewModel: BluetoothViewModel by viewModel()
                val state by viewModel.state.collectAsState()

                val enableBluetoothLauncher = registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ){/* not needed*/}

                val permissionLaucher = registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions(),
                ){ perms ->
                    val canEnebleBluetooth = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                        perms[Manifest.permission.BLUETOOTH_CONNECT] == true
                    } else true

                }

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
