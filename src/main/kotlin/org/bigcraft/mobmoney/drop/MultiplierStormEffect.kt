package org.bigcraft.mobmoney.drop

import org.bigcraft.mobmoney.api.MoneyDropEvent
import org.bigcraft.storm.api.StormEffect
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.event.EventHandler

class MultiplierStormEffect(config: ConfigurationSection) : StormEffect(config) {

    private val multiplier = config.getDouble("multiplier", 1.0)

    @EventHandler(ignoreCancelled = true)
    private fun onMoneyDrop(e: MoneyDropEvent) {
        if (!isAffected(e.player)) return
        e.baseDrop *= multiplier
    }

}