package org.bigcraft.mobmoney.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import me.wyne.wutils.i18n.kotlin.placeholderComponent
import org.bigcraft.mobmoney.MobMoney

abstract class SubCommand(argument: String) {
    open val command = CommandAPICommand(argument)
    operator fun invoke() = command
}

class ReloadCommand(plugin: MobMoney) : SubCommand("reload") {
    override val command: CommandAPICommand = super.command
        .withPermission("mobmoney.reload")
        .executes(CommandExecutor { sender, _ ->
            plugin.reload()
            sender.placeholderComponent("success-plugin-reload").sendMessagePlayer(sender)
            MobMoney.logger.info("Plugin reloaded")
        })
}
