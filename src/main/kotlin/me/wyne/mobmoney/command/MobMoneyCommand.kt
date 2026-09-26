package me.wyne.mobmoney.command

import com.google.inject.Inject
import com.google.inject.Singleton
import dev.jorel.commandapi.CommandAPICommand
import me.wyne.mobmoney.MobMoney

@Singleton
class MobMoneyCommand @Inject constructor(private val plugin: MobMoney) {

    init {
        registerCommand()
    }

    private fun registerCommand() {
        CommandAPICommand("mobmoney")
            .withSubcommand(ReloadCommand(plugin)())
            .register(plugin)
    }

}
