package nl.flitsmeister.car_common.extentions

import android.content.Context
import android.view.WindowManager

val Context.windowManager: WindowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

