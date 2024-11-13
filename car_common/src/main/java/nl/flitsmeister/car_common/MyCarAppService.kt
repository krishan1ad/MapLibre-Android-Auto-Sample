package nl.flitsmeister.car_common

import android.content.Context
import android.content.pm.PackageManager
import android.media.ApplicationMediaCapabilities
import android.util.Log
import androidx.car.app.CarAppService
import androidx.car.app.CarContext
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator

class MyCarAppService : CarAppService() {

    override fun onCreateSession(): Session {
        return MyCarSession()
    }

    override fun createHostValidator(): HostValidator {
        checkManifestForDebug(this)
        return if (isDebug) HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
        else HostValidator.Builder(this)
            .addAllowedHosts(R.array.car_template_hosts_allowlist)
            .build()
    }

    companion object {
        const val LOG_TAG = "CarAppService"
        var isDebug = false
        var appPlatform = "ANDROID_AUTO"

        fun checkManifestForDebug(context: Context) {
            try {
                val app = context.packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA
                )
                val bundle = app.metaData
                isDebug = bundle.getBoolean("nl.flitsmeister.maplibrecar.IS_DEBUG") == true
                appPlatform =
                    bundle.getString("nl.flitsmeister.maplibrecar.APP_PLATFORM") ?: "ANDROID_AUTO"
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Failed to check manifest (for debug mode and app platform)", e)
            }
        }
    }
}