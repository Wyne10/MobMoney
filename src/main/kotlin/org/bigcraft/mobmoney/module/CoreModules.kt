package org.bigcraft.mobmoney.module

import com.google.inject.AbstractModule
import org.bigcraft.mobmoney.config.CurrencyConfig
import org.bigcraft.mobmoney.config.MessageConfig
import org.bigcraft.mobmoney.drop.DropProvider
import org.bigcraft.mobmoney.drop.KillListener
import org.bigcraft.mobmoney.drop.MultiplierProvider
import org.bigcraft.mobmoney.drop.XpMultiplierProvider

//region Implementations

private class CoreModule(private vararg val modules: Class<out Any>) : AbstractModule() {
    override fun configure() {
        modules.forEach { bind(it) }
    }
}

//endregion

val ConfigModule: AbstractModule = CoreModule(
    CurrencyConfig::class.java,
    MessageConfig::class.java
)

val DropModule: AbstractModule = CoreModule(
    DropProvider::class.java,
    MultiplierProvider::class.java,
    XpMultiplierProvider::class.java,
    KillListener::class.java
)
