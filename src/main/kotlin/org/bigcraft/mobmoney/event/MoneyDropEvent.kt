package org.bigcraft.mobmoney.event

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent

class MoneyDropEvent(
    entityDeathEvent: EntityDeathEvent,
    val player: Player,
    var baseDrop: Double,
    var multiplier: Double
) : EntityDeathEvent(entityDeathEvent.entity, entityDeathEvent.drops, entityDeathEvent.droppedExp)