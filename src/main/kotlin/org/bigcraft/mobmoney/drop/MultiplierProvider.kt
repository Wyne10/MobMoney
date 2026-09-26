package org.bigcraft.mobmoney.drop

import com.google.inject.Singleton
import me.wyne.wutils.common.kotlin.config.deep
import me.wyne.wutils.common.loadable.LoadableMeta
import me.wyne.wutils.config.configurables.attribute.GenericFactory
import org.bigcraft.mobmoney.LoadableProvider
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

data class Multiplier(val multiplier: Double, val permission: String) {
    object Factory : GenericFactory<Multiplier> {
        override fun create(key: String, config: ConfigurationSection): Multiplier {
            val section = config.deep(key)
            return Multiplier(
                section.getDouble("multiplier", 1.0),
                section.getString("permission", "group.default")!!
            )
        }
    }
}

abstract class MultiplierProviderBase(sectionKey: String) : LoadableProvider<Multiplier>(sectionKey, Multiplier.Factory) {
    fun getMultiplier(player: Player): Double =
        loadedMap.values
            .filter { player.hasPermission(it.permission) }
            .maxOfOrNull { it.multiplier } ?: 1.0
}

@Singleton
@LoadableMeta(priority = 0)
class MultiplierProvider : MultiplierProviderBase("multiplier")

@Singleton
@LoadableMeta(priority = 0)
class XpMultiplierProvider : MultiplierProviderBase("xp-multiplier")
