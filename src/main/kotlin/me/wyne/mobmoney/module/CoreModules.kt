package me.wyne.mobmoney.module

import com.google.inject.AbstractModule
import me.wyne.mobmoney.config.CurrencyConfig
import me.wyne.mobmoney.config.MessageConfig
import me.wyne.mobmoney.drop.DropProvider
import me.wyne.mobmoney.drop.KillListener
import me.wyne.mobmoney.drop.MultiplierProvider
import me.wyne.mobmoney.drop.XpMultiplierProvider

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
