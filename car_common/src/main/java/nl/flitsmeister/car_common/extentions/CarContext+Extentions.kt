package nl.flitsmeister.car_common.extentions

import androidx.car.app.AppManager
import androidx.car.app.CarContext
import androidx.car.app.ScreenManager

val CarContext.screenManager: ScreenManager
    get() = getCarService(CarContext.SCREEN_SERVICE) as ScreenManager

val CarContext.appManager: AppManager
    get() = getCarService(CarContext.APP_SERVICE) as AppManager