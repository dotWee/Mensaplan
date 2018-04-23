package de.dotwee.rgb.canteen.model

import java.util.*

/**
 * Created by lukas on 25.12.17.
 */
class Item(name: String) {

    private var name: String = ""
    private var date: Date? = null

    var info: String? = null
        private set

    private var label: Array<Label>? = null

    private var priceEmployee: String = ""
    private var priceGuest: String = ""
    private var priceStudent: String = ""
    private var priceAll: String = ""

    private var tag: String? = null
    private var type: Type? = null

    init {
        setName(getName(name))
        info = getInfo(name)
    }

    private fun setName(name: String) {
        this.name = name
    }

    fun setDate(date: Date) {
        this.date = date
    }

    fun setTag(tag: String) {
        this.tag = tag
    }

    fun setType(type: Type) {
        this.type = type
    }

    fun setLabel(label: Array<Label>) {
        this.label = label
    }

    fun setPrice(price: Price, priceVal: String) {
        when (price) {
            Price.STUDENT -> priceStudent = priceVal
            Price.GUEST -> priceGuest = priceVal
            Price.EMPLOYEE -> priceEmployee = priceVal
            else -> priceAll = priceVal
        }
    }

    fun getPrice(price: Price): String {
        when (price) {

            Price.STUDENT -> return priceStudent

            Price.GUEST -> return priceGuest

            Price.EMPLOYEE -> return priceEmployee

            else -> return priceAll
        }
    }

    private fun getName(text: String): String {
        return text.substring(0, getIndexOfStartBracket(text))
    }

    private fun getInfo(text: String): String {
        return text.substring(getIndexOfStartBracket(text))
    }

    private fun getIndexOfStartBracket(text: String): Int {
        val index = text.indexOf("(")
        return if (index != -1) index else text.length
    }

    fun getDate(): Date {
        return date!!;
    }

    fun getType(): Type {
        return type!!
    }
}