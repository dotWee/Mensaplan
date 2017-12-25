package de.dotwee.rgb.canteen.model

import java.util.*

/**
 * Created by lukas on 25.12.17.
 */
class Item(name: String) {

    private var name: String = ""
    private val date: Date? = null
    private val weekday: String? = null

    var info: String? = null
        private set

    private val labels: Array<Label>? = null

    private val priceEmployee: String = ""
    private val priceGuest: String = ""
    private val priceStudent: String = ""
    private val priceAll: String = ""

    private val tag: String? = null
    private val type: Type? = null

    init {
        setName(getName(name))
        info = getInfo(name)
    }

    private fun setName(name: String) {
        this.name = name
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
}