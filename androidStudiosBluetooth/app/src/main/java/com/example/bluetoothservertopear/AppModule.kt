package com.example.bluetoothservertopear

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module
@DisableInstallInCheck
object AppModule {
  @Provides
  @Singleton
  fun provideBluetoothController(@ApplicationContext context: Context):BluetoothController {
    return AndroidBluetoothController(context)

  }

}