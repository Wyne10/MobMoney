package org.bigcraft.mobmoney.config

import com.google.inject.Inject
import com.google.inject.Singleton
import me.wyne.infpoints.api.IPApi
import me.wyne.infpoints.api.Point
import me.wyne.wutils.config.Config
import me.wyne.wutils.config.ConfigEntry
import me.wyne.wutils.i18n.kotlin.andThen
import me.wyne.wutils.i18n.kotlin.replace
import me.wyne.wutils.i18n.language.replacement.TextReplacement

@Singleton
class CurrencyConfig @Inject constructor() {

    @ConfigEntry(section = "Currency")
    val currencyKey = "primary"

    val currency: Point
        get() = IPApi.getInstance().getPoint(currencyKey)
            ?: throw IllegalArgumentException("Invalid currency $currencyKey")

    private val currencyReplacement: TextReplacement
        get() = "currency-key" replace currencyKey

    init {
        Config.global.registerConfigObject(this)
    }

    fun getReplacements(amount: Double): TextReplacement =
        currencyReplacement andThen getAmountReplacement(amount)

    private fun getAmountReplacement(amount: Double): TextReplacement =
        "amount" replace currency.visualConfig.decimalFormat().format(amount)

}
