package org.bigcraft.mobmoney.drop

import com.google.inject.Singleton
import me.wyne.wutils.common.kotlin.config.getDoubleRange
import me.wyne.wutils.common.kotlin.range.DoubleRange
import me.wyne.wutils.common.kotlin.range.randomOrNull
import me.wyne.wutils.common.loadable.LoadableMeta
import me.wyne.wutils.config.configurables.attribute.GenericFactory
import org.bigcraft.mobmoney.LoadableProvider
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Ambient
import org.bukkit.entity.Animals
import org.bukkit.entity.EntityType
import org.bukkit.entity.Monster
import org.bukkit.entity.WaterMob
import java.util.EnumMap
import java.util.EnumSet

@Singleton
@LoadableMeta(priority = 0)
class DropProvider : LoadableProvider<DoubleRange>("drop", Factory) {

    private val entityToDrop = EnumMap<EntityType, DoubleRange>(EntityType::class.java)

    override fun load(config: ConfigurationSection) {
        super.load(config)
        entityToDrop.clear()
        loadedMap.forEach { (key, range) ->
            val entityType = runCatching { EntityType.valueOf(key) }.getOrNull()
            if (entityType != null) {
                entityToDrop[entityType] = range
                return@forEach
            }
            CATEGORY_MAP[key]?.forEach { type -> entityToDrop.putIfAbsent(type, range) }
        }
    }

    fun getDrop(entityType: EntityType): Double =
        entityToDrop[entityType]?.randomOrNull() ?: 0.0

    object Factory : GenericFactory<DoubleRange> {
        override fun create(key: String, config: ConfigurationSection): DoubleRange =
            config.getDoubleRange(key)
    }

    companion object EntityCategories {
        private fun entitiesOf(category: Class<*>): Set<EntityType> =
            EntityType.entries
                .filter { it.entityClass != null }
                .filter { category.isAssignableFrom(it.entityClass!!) }
                .toSet()

        val CATEGORY_MAP: Map<String, Set<EntityType>> = mapOf(
            "ANY" to EnumSet.allOf(EntityType::class.java),
            "MONSTERS" to entitiesOf(Monster::class.java),
            "ANIMALS" to entitiesOf(Animals::class.java),
            "AMBIENT" to entitiesOf(Ambient::class.java),
            "WATER_ANIMALS" to entitiesOf(WaterMob::class.java)
        )
    }

}
