package org.bigcraft.mobmoney.command

import com.google.inject.Inject
import com.google.inject.Singleton
import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.arguments.LiteralArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.wyne.wutils.i18n.kotlin.placeholderComponent
import org.bigcraft.mobmoney.MobMoney
import org.bukkit.command.CommandSender

@Singleton
class MobMoneyCommand @Inject constructor(private val plugin: MobMoney) {

    init {
        registerCommand()
    }

    private fun registerCommand() {
        CommandTree("mobmoney")
            .then(LiteralArgument("reload")
                    .withPermission("mobmoney.reload")
                    .executes(CommandExecutor { sender: CommandSender, _: CommandArguments? ->
                        plugin.reload()
                        sender.placeholderComponent("success-plugin-reload").sendMessage(sender)
                    })
            )
            .register(plugin)
    }

}
