package org.bigcraft.mobmoney.drop

import com.google.inject.Singleton
import me.wyne.wutils.common.loadable.Loadable
import me.wyne.wutils.common.loadable.LoadableMeta
import me.wyne.wutils.common.loadable.Loader
import org.bigcraft.mobmoney.MobMoney
import org.bigcraft.mobmoney.extension.DoubleRange
import org.bigcraft.mobmoney.extension.getDoubleRange
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Ambient
import org.bukkit.entity.Animals
import org.bukkit.entity.EntityType
import org.bukkit.entity.Monster
import org.bukkit.entity.WaterMob
import java.util.*
import kotlin.random.Random

@Singleton
@LoadableMeta(priority = 0)
class DropManager : Loadable {

    private val loadedMap = HashMap<String, DoubleRange>()
    private val entityToDrop = HashMap<EntityType, DoubleRange>()

    init {
        Loader.global.registerLoadable(this)
    }

    override fun load(config: ConfigurationSection) {
        loadedMap.clear()
        entityToDrop.clear()
        val section = config.getConfigurationSection("drop")!!
        section.getKeys(false).forEach { key ->
            MobMoney.log.debug("Loading key '{}' from '{}'", key, section.name)
            loadedMap[key] = section.getDoubleRange(key)
        }

        loadedMap.forEach { (key, value) ->
            kotlin.runCatching { EntityType.valueOf(key) }.getOrNull()?.let { entityType ->
                entityToDrop[entityType] = value
                return@forEach
            }
            if (!CATEGORY_MAP.containsKey(key))
                return@forEach
            val categorySet = CATEGORY_MAP[key]!!
            categorySet.forEach { entityType -> entityToDrop.putIfAbsent(entityType, value) }
        }
    }

    fun getDrop(entityType: EntityType): Double {
        val range = entityToDrop[entityType] ?: return 0.0
        return Random.nextDouble(range.start, range.endInclusive)
    }

    companion object EntityCategories {
        private val ANY: EnumSet<EntityType> = EnumSet.allOf(EntityType::class.java)
        private val MONSTERS = EntityType.values()
            .filter { it.entityClass != null }
            .filter { Monster::class.java.isAssignableFrom(it.entityClass!!) }
            .toSet()
        private val ANIMALS = EntityType.values()
            .filter { it.entityClass != null }
            .filter { Animals::class.java.isAssignableFrom(it.entityClass!!) }
            .toSet()
        private val AMBIENT = EntityType.values()
            .filter { it.entityClass != null }
            .filter { Ambient::class.java.isAssignableFrom(it.entityClass!!) }
            .toSet()
        private val WATER_ANIMALS = EntityType.values()
            .filter { it.entityClass != null }
            .filter { WaterMob::class.java.isAssignableFrom(it.entityClass!!) }
            .toSet()
        val CATEGORY_MAP = mapOf(
            "ANY" to ANY,
            "MONSTERS" to MONSTERS,
            "ANIMALS" to ANIMALS,
            "AMBIENT" to AMBIENT,
            "WATER_ANIMALS" to WATER_ANIMALS
        )
    }
}