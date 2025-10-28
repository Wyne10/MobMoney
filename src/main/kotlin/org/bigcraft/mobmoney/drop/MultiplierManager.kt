package org.bigcraft.mobmoney.drop

import com.google.inject.Singleton
import me.wyne.wutils.common.loadable.LoadableMeta
import org.bigcraft.mobmoney.AbstractManager
import org.bigcraft.mobmoney.ConfigurableFactory
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

data class Multiplier(val multiplier: Double, val permission: String) {
    object Factory : ConfigurableFactory<Multiplier> {
        override fun fromConfig(config: ConfigurationSection): Multiplier =
            Multiplier(
                config.getDouble("multiplier", 1.0),
                config.getString("permission", "group.default")!!
            )
    }
}

@Singleton
@LoadableMeta(priority = 0)
class MultiplierManager : AbstractManager<Multiplier>() {

    override val sectionKey = "multiplier"
    override val valueLoader = Multiplier.Factory

    fun getMultiplier(player: Player): Double =
        loadedMap.values
            .filter { player.hasPermission(it.permission) }
            .maxOfOrNull { it.multiplier } ?: 1.0

}

@Singleton
@LoadableMeta(priority = 0)
class XpMultiplierManager : AbstractManager<Multiplier>() {

    override val sectionKey = "xp-multiplier"
    override val valueLoader = Multiplier.Factory

    fun getMultiplier(player: Player): Double =
        loadedMap.values
            .filter { player.hasPermission(it.permission) }
            .maxOfOrNull { it.multiplier } ?: 1.0

}