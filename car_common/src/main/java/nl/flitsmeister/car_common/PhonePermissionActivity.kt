package nl.flitsmeister.car_common

import android.Manifest
import android.app.Activity
import android.os.Bundle

class PhonePermissionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 123)
    }
}