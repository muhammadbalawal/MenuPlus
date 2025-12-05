package com.example.emptyactivity

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the MenuPlus app.
 *
 * This class serves as the entry point for the application and initializes Hilt
 * dependency injection. The @HiltAndroidApp annotation generates the necessary
 * code for Hilt to work with the application.
 *
 * This class must be registered in the AndroidManifest.xml file.
 *
 * Mostly created by: Muhammad
 */
@HiltAndroidApp
class MenuPlusApplication : Application()
