package nl.flitsmeister.car_common.screens

import androidx.car.app.CarContext
import androidx.car.app.CarToast
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.SectionedItemList
import androidx.car.app.model.Template
import nl.flitsmeister.car_common.MyCarSession
import nl.flitsmeister.car_common.extentions.addItem
import nl.flitsmeister.car_common.extentions.screenManager

class CarMenuScreen(carContext: CarContext) :
    Screen(carContext) {
    override fun onGetTemplate(): Template {
        val templateBuilder = ListTemplate.Builder().apply {
            setTitle("Menu")
            setHeaderAction(Action.BACK)
            addSectionedList(buildNavList())
            addSectionedList(buildDevList())
        }

        return templateBuilder.build()
    }

    private fun buildNavList(): SectionedItemList {
        //Navigation Button
        val navigationRow = Row.Builder().apply {
            setTitle(
                if (MyCarSession.isRouteActive) {
                    "Stop Navigation"
                } else {
                    "Start Navigation"
                }
            )
            setOnClickListener {
                CarToast.makeText(
                    carContext,
                    if (MyCarSession.isRouteActive) {
                        "Stopping Navigation"
                    } else {
                        "Starting Navigation"
                    },
                    CarToast.LENGTH_LONG
                ).show()
                MyCarSession.isRouteActive = !MyCarSession.isRouteActive
                carContext.screenManager.pop()
            }

        }
        val sectionedItemList = SectionedItemList.create(
            ItemList.Builder().apply {
                addItem(navigationRow)
            }.build(),
            "Navigation"
        )
        return sectionedItemList
    }

    private fun buildDevList(): SectionedItemList {
        //Test CarToast
        val testCarToast = Row.Builder().apply {
            setTitle("Test CarToast")
            setOnClickListener {
                CarToast.makeText(
                    carContext,
                    "Test CarToast",
                    CarToast.LENGTH_LONG
                ).show()
                carContext.screenManager.pop()
            }
        }

        val sectionedItemList = SectionedItemList.create(
            ItemList.Builder().apply {
                addItem(testCarToast)
            }.build(),
            "Developer tools"
        )

        return sectionedItemList
    }
}