package org.bigcraft.mobmoney.module

import com.google.inject.AbstractModule
import org.bigcraft.mobmoney.MobMoney
import org.bigcraft.mobmoney.command.MobMoneyCommand

private class OptionalModule(
    private val className: String,
    private val exceptionMessage: String = "$className not found, module ignored",
    private vararg val modules: Class<out Any>
) : AbstractModule() {
    override fun configure() {
        try {
            Class.forName(className)
            modules.forEach { bind(it) }
        } catch (e: ClassNotFoundException) {
            MobMoney.log.warn(exceptionMessage)
        }
    }
}

val PlaceholderModule: AbstractModule = OptionalModule(
    className = "me.clip.placeholderapi.PlaceholderAPI",
    exceptionMessage = "PlaceholderAPI not found, placeholders are not registered"
)

val CommandModule: AbstractModule = OptionalModule(
    className = "dev.jorel.commandapi.CommandAPI",
    exceptionMessage = "CommandAPI not found, commands are not registered",
    MobMoneyCommand::class.java
)