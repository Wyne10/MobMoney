package me.wyne.mobmoney.module

import com.google.inject.AbstractModule
import me.wyne.mobmoney.MobMoney
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin

class PluginModule(private val plugin: MobMoney) : AbstractModule() {
    override fun configure() {
        bind(MobMoney::class.java)
            .toInstance(plugin)
        bind(Plugin::class.java)
            .toInstance(plugin)
        bind(FileConfiguration::class.java)
            .toInstance(plugin.getConfig())
    }
}
