package nl.flitsmeister.car_common.extentions

import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.CarIcon
import androidx.car.app.model.ItemList
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.SearchTemplate
import androidx.car.app.navigation.model.Maneuver
import androidx.car.app.navigation.model.NavigationTemplate

/*
 * This should reduce the number of .build()-calls everywhere.
 * Just return the builder, and the .build() will be called when needed.
 */

fun ItemList.Builder.addItem(itemBuilder: Row.Builder): ItemList.Builder {
    this.addItem(itemBuilder.build())
    return this
}

fun ActionStrip.Builder.addAction(actionBuilder: Action.Builder): ActionStrip.Builder {
    this.addAction(actionBuilder.build())
    return this
}

fun Action.Builder.setIcon(carIconBuilder: CarIcon.Builder): Action.Builder {
    this.setIcon(carIconBuilder.build())
    return this
}

fun SearchTemplate.Builder.setItemList(itemListBuilder: ItemList.Builder): SearchTemplate.Builder {
    this.setItemList(itemListBuilder.build())
    return this
}

fun NavigationTemplate.Builder.setActionStrip(actionStripBuilder: ActionStrip.Builder): NavigationTemplate.Builder {
    this.setActionStrip(actionStripBuilder.build())
    return this
}

fun NavigationTemplate.Builder.setMapActionStrip(actionStripBuilder: ActionStrip.Builder): NavigationTemplate.Builder {
    this.setMapActionStrip(actionStripBuilder.build())
    return this
}

fun MessageTemplate.Builder.setIcon(carIcon: CarIcon.Builder): MessageTemplate.Builder {
    this.setIcon(carIcon.build())
    return this
}

fun Row.Builder.setImage(carIcon: CarIcon.Builder): Row.Builder {
    this.setImage(carIcon.build())
    return this
}

fun Maneuver.Builder.setIcon(carIcon: CarIcon.Builder): Maneuver.Builder {
    this.setIcon(carIcon.build())
    return this
}