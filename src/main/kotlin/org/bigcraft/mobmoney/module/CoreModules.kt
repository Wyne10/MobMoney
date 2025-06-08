package org.bigcraft.mobmoney.module

import com.google.inject.AbstractModule
import org.bigcraft.mobmoney.config.CurrencyConfig
import org.bigcraft.mobmoney.config.MessageConfig
import org.bigcraft.mobmoney.drop.DropManager
import org.bigcraft.mobmoney.drop.KillListener
import org.bigcraft.mobmoney.drop.MultiplierManager

private class CoreModule(private vararg val modules: Class<out Any>) : AbstractModule() {
    override fun configure() {
        modules.forEach { bind(it) }
    }
}

val ConfigModule: AbstractModule = CoreModule(CurrencyConfig::class.java, MessageConfig::class.java)

val DropModule: AbstractModule = CoreModule(DropManager::class.java, MultiplierManager::class.java, KillListener::class.java)