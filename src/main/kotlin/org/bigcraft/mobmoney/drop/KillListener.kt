package org.bigcraft.mobmoney.drop

import com.google.inject.Inject
import com.google.inject.Singleton
import me.wyne.wutils.i18n.I18n
import me.wyne.wutils.i18n.kotlin.placeholderComponent
import me.wyne.wutils.i18n.kotlin.replaceComponent
import net.kyori.adventure.text.Component
import org.bigcraft.mobmoney.MobMoney
import org.bigcraft.mobmoney.api.MoneyDropEvent
import org.bigcraft.mobmoney.config.CurrencyConfig
import org.bigcraft.mobmoney.config.MessageConfig
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

@Singleton
class KillListener @Inject constructor(
    plugin: MobMoney,
    private val dropManager: DropManager,
    private val multiplierManager: MultiplierManager,
    private val xpMultiplierManager: XpMultiplierManager,
    private val currencyConfig: CurrencyConfig,
    private val messageConfig: MessageConfig,
) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onEntityDeath(e: EntityDeathEvent) {
        if (e.entity is Player) return
        if (e.entity.killer == null) return
        if (e.entity.killer !is Player) return
        val player = e.entity.killer as Player
        val entityType = e.entityType
        val baseDrop = dropManager.getDrop(entityType)
        val multiplier = multiplierManager.getMultiplier(player)
        e.droppedExp = (e.droppedExp * xpMultiplierManager.getMultiplier(player)).toInt()
        val event = MoneyDropEvent(player, baseDrop, multiplier)
        if (!event.callEvent())
            return
        val drop = event.baseDrop * event.multiplier
        currencyConfig.currency.add(player.uniqueId, drop)

        val entityName: Component =
            if (I18n.global.contains(player.locale(), entityType.name)) player.placeholderComponent(entityType.name).get()
            else Component.text(e.entity.name)

        if (messageConfig.showChatMessage) {
            player.placeholderComponent("info-drop-chat", currencyConfig.getReplacements(drop))
                .replace("entity" replaceComponent entityName).sendMessage(player)
        }

        if (messageConfig.showActionBarMessage) {
            player.placeholderComponent("info-drop-action-bar", currencyConfig.getReplacements(drop))
                .replace("entity" replaceComponent entityName).sendActionBar(player)
        }
    }

}