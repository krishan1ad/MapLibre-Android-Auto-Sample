package nl.flitsmeister.car_common

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log
import androidx.car.app.CarContext
import androidx.car.app.CarToast
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.core.content.ContextCompat
import nl.flitsmeister.car_common.extentions.screenManager
import nl.flitsmeister.car_common.screens.CarMapScreen
import nl.flitsmeister.car_common.screens.CarPermissionScreen

class MyCarSession : Session() {
    private lateinit var carMapRenderer: CarMapRenderer
    private var carConfiguration: Configuration? = null

    override fun onCreateScreen(intent: Intent): Screen {
        Log.v(LOG_TAG, "onCreateScreen: $intent")
        carMapRenderer = CarMapRenderer(carContext, lifecycle)

        val carMapScreen = CarMapScreen(carContext, carMapRenderer)
        carContext.screenManager.push(carMapScreen)

        //if location permission is not granted; Add the permission screen unto the stack
        if (ContextCompat.checkSelfPermission(
                carContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.v(LOG_TAG, "onCreateScreen: Location permission not granted")
            val carPermissionScreen = CarPermissionScreen(carContext)
            carContext.screenManager.push(carPermissionScreen)
            return carPermissionScreen
        } else {
            return carMapScreen
        }
    }

    override fun onCarConfigurationChanged(newConfiguration: Configuration) {
        Log.v(LOG_TAG, "onCarConfigurationChanged: old: $carConfiguration, new: $newConfiguration")
        carConfiguration = newConfiguration
    }

    override fun onNewIntent(intent: Intent) {
        Log.v(LOG_TAG, "onNewIntent: $intent")
        super.onNewIntent(intent)

        when (intent.action) {
            CarContext.ACTION_NAVIGATE -> {
                // When user speaks "navigate to X" to Google Assistant, this action will be triggered
                navigateFromIntent(intent)
            }
            //TODO: Add your own actions here, for example: When a use clicks a notification.
            INTENT_ACTION_CLICKED_NOTIFICATION -> {
                clickedNotification(intent)
            }
        }
    }

    private fun navigateFromIntent(intent: Intent) {
        val uri = intent.data ?: return
        val query = uri.query ?: return
        if (uri.scheme != "geo") return
        CarToast.makeText(carContext, "Navigating to $query", CarToast.LENGTH_LONG).show()
    }

    private fun clickedNotification(intent: Intent) {
        CarToast.makeText(carContext, "Clicked notification to $intent", CarToast.LENGTH_LONG)
            .show()
    }

    companion object {
        const val LOG_TAG = "MyCarSession"
        const val INTENT_ACTION_CLICKED_NOTIFICATION = "clicked_notification"

        //TODO: Add your own navigation logic
        var isRouteActive = false
    }
}