package org.bigcraft.mobmoney.drop

import me.wyne.storm.api.StormEffect
import org.bigcraft.mobmoney.api.MoneyDropEvent
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.event.EventHandler

const val MULTIPLIER_EFFECT_KEY = "rich"

class MultiplierStormEffect(config: ConfigurationSection) : StormEffect(config) {

    private val multiplier = config.getDouble("multiplier", 1.0)

    @EventHandler(ignoreCancelled = true)
    private fun onMoneyDrop(e: MoneyDropEvent) {
        if (!isAffected(e.player)) return
        e.baseDrop *= multiplier
    }

}
