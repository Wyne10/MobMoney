package org.bigcraft.mobmoney.extension

import org.bukkit.configuration.ConfigurationSection

const val RANGE_DELIMITER = ".."

typealias DoubleRange = ClosedFloatingPointRange<Double>

val EMPTY_RANGE = 0.0..0.0

fun ConfigurationSection.getDoubleRange(path: String, def: DoubleRange = EMPTY_RANGE): DoubleRange {
    val args = getString(path)?.split(RANGE_DELIMITER)
    args?.let {
        return it.first().toDouble()..it.last().toDouble()
    }
    return def
}