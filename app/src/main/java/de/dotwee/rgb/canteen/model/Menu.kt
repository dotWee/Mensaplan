package de.dotwee.rgb.canteen.model

import java.util.*

class Menu {

    var location: Location? = null
        private set
    var date: Date? = null
        private set

    var items: List<Item>? = null
        private set
    private var types: MutableList<Type>? = null

    constructor(location: Location, date: Date, items: List<Item>, validateDates: Boolean) {
        this.location = location
        this.date = date
        this.items = items

        if (validateDates) {
            validateDates()
        }

        buildTypesArray()
    }

    constructor(location: Location, items: List<Item>) {
        this.location = location
        this.items = items

        buildTypesArray()
    }

    private fun buildTypesArray() {
        types = ArrayList()
        for (item in items!!) {
            val itemTyp = item.getType()
            if (!types!!.contains(itemTyp)) {
                types!!.add(itemTyp)
            }
        }
    }

    @Throws(IllegalStateException::class)
    private fun validateDates() {
        var calendarStart = Calendar.getInstance()
        calendarStart.time = date

        for (item in items!!) {
            val calendarNext = Calendar.getInstance()
            calendarNext.time = item.getDate()

            val isSameDay = calendarStart.get(Calendar.YEAR) == calendarNext.get(Calendar.YEAR) && calendarStart.get(Calendar.DAY_OF_YEAR) == calendarNext.get(Calendar.DAY_OF_YEAR)
            if (!isSameDay) {
                throw IllegalStateException("Items are not the same day plan!")
            }

            calendarStart = calendarNext
        }

        date = calendarStart.time
    }

    fun getTypes(): List<Type> {
        return types!!
    }
}
