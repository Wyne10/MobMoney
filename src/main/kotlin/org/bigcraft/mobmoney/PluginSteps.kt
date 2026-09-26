package org.bigcraft.mobmoney

import com.google.inject.CreationException
import com.google.inject.Guice
import com.google.inject.Stage
import me.wyne.wutils.common.loadable.Loader
import me.wyne.wutils.common.plugin.CompositeStep
import me.wyne.wutils.common.plugin.LevelWrapper
import me.wyne.wutils.common.plugin.LoggerWrapper
import me.wyne.wutils.common.plugin.PluginStep
import me.wyne.wutils.common.plugin.Step
import me.wyne.wutils.common.plugin.StepScope
import me.wyne.wutils.config.Config
import me.wyne.wutils.i18n.I18n
import me.wyne.wutils.i18n.PluginI18nBuilder
import me.wyne.wutils.i18n.language.component.BukkitComponentAudiences
import me.wyne.wutils.i18n.language.interpretation.ComponentInterpreters
import me.wyne.wutils.i18n.language.validation.EmptyValidator
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bigcraft.mobmoney.MobMoney.Companion.EMPTY_CONFIGURATION
import org.bigcraft.mobmoney.MobMoney.Companion.logger
import org.bigcraft.mobmoney.module.CommandModule
import org.bigcraft.mobmoney.module.ConfigModule
import org.bigcraft.mobmoney.module.DropModule
import org.bigcraft.mobmoney.module.PluginModule
import org.bigcraft.mobmoney.module.StormModule

@Step(priority = 0, scope = StepScope.ENABLE)
object LoadDefaultConfig : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        plugin.saveDefaultConfig()
        plugin.config.setDefaults(EMPTY_CONFIGURATION)
    }
}

@Step(priority = 1, scope = StepScope.ENABLE)
object InitializeLogger : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        logger = LoggerWrapper(plugin.slF4JLogger, LevelWrapper.valueOf(plugin.config.getString("logLevel", "INFO")!!))
    }
}

@Step(priority = 3, scope = StepScope.ENABLE)
object InitializeI18n : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        I18n.global = PluginI18nBuilder(plugin)
            .setLogger(logger)
            .setComponentAudience(BukkitComponentAudiences(BukkitAudiences.create(plugin)))
            .setComponentInterpreter(
                ComponentInterpreters.valueOf(
                    plugin.config.getString("serializer", "MINI_MESSAGE")!!
                ).get(EmptyValidator())
            )
            .setUsePlayerLanguage(plugin.config.getBoolean("usePlayerLanguage", true))
            .loadLanguage("lang/ru.yml")
            .loadLanguage("lang/en.yml")
            .build()
    }
}

@Step(priority = 4, scope = StepScope.ENABLE)
object InitializeInjector : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        try {
            MobMoney.instance.injector = Guice.createInjector(
                Stage.PRODUCTION,
                PluginModule(plugin),
                ConfigModule,
                DropModule,
                StormModule,
                CommandModule
            )
        } catch (e: CreationException) {
            logger.error("Guice injector creation exception", e)
        }
    }
}

@Step(priority = 5, scope = StepScope.ENABLE)
object InitializeConfig : CompositeStep<MobMoney>(ReloadConfig) {
    override fun before(plugin: MobMoney) {
        Config.global.apply {
            logger = MobMoney.logger
            setConfigGenerator(plugin, "config.yml")
            generateConfig()
        }
    }
}

@Step(priority = 6, scope = StepScope.ENABLE)
object InitializeLoader : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        Loader.global.registerConfig(Loader.DEFAULT_PATH, plugin.config)
    }
}

@Step(priority = 7, scope = StepScope.ENABLE)
object Load : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        Loader.global.load(plugin)
    }
}

@Step(scope = StepScope.RELOAD)
object Reload : CompositeStep<MobMoney>(ReloadConfig, InitializeLogger, InitializeLoader, InitializeI18n, Load)

object ReloadConfig : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        plugin.reloadConfig()
        plugin.config.setDefaults(EMPTY_CONFIGURATION)
        Config.global.reloadConfig(plugin.config)
    }
}
