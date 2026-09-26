package me.wyne.mobmoney.module

import com.google.inject.AbstractModule
import me.wyne.mobmoney.MobMoney
import me.wyne.mobmoney.command.MobMoneyCommand
import me.wyne.mobmoney.drop.StormEffects

//region Implementations

private class OptionalModule(
    private val className: String,
    private val exceptionMessage: String = "$className not found, module ignored",
    private vararg val modules: Class<out Any>
) : AbstractModule() {
    override fun configure() {
        try {
            Class.forName(className)
            modules.forEach { bind(it) }
        } catch (_: ClassNotFoundException) {
            MobMoney.logger.warn(exceptionMessage)
        }
    }
}

private class ConfigurableModule(
    private val configurationPath: String,
    private vararg val modules: Class<out Any>
) : AbstractModule() {
    override fun configure() {
        val isActive = MobMoney.instance.config.getBoolean(configurationPath, false)
        if (isActive)
            modules.forEach { bind(it) }
    }
}

//endregion

val CommandModule: AbstractModule = OptionalModule(
    className = "dev.jorel.commandapi.CommandAPI",
    exceptionMessage = "CommandAPI not found, commands are not registered",
    MobMoneyCommand::class.java
)

val StormModule: AbstractModule = OptionalModule(
    className = "me.wyne.storm.api.StormApi",
    exceptionMessage = "Storm not found, multiplier effect is not registered",
    StormEffects::class.java
)
