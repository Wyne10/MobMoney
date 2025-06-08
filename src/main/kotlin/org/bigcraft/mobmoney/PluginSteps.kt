package org.bigcraft.mobmoney

import com.google.gson.Gson
import com.google.inject.CreationException
import com.google.inject.Guice
import com.google.inject.Stage
import me.wyne.wutils.common.loadable.Loader
import me.wyne.wutils.common.plugin.CompositeStep
import me.wyne.wutils.common.plugin.PluginStep
import me.wyne.wutils.common.plugin.Step
import me.wyne.wutils.common.plugin.StepScope
import me.wyne.wutils.config.Config
import me.wyne.wutils.i18n.I18n
import me.wyne.wutils.i18n.language.component.BukkitComponentAudience
import me.wyne.wutils.i18n.language.interpretation.ComponentInterpreters
import me.wyne.wutils.i18n.language.validation.EmptyValidator
import me.wyne.wutils.log.*
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bigcraft.mobmoney.MobMoney.Companion.EMPTY_CONFIGURATION
import org.bigcraft.mobmoney.MobMoney.Companion.log
import org.bigcraft.mobmoney.module.CommandModule
import org.bigcraft.mobmoney.module.PlaceholderModule
import org.bigcraft.mobmoney.module.PluginModule
import java.io.File
import java.util.concurrent.Executors

@Step(priority = 0, scope = StepScope.ENABLE)
object LoadDefaultConfig : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        plugin.saveDefaultConfig()
        plugin.config.setDefaults(EMPTY_CONFIGURATION)
    }
}

@Step(priority = 1, scope = StepScope.ENABLE)
class InitializeLogger(private val logDirectory: File) : PluginStep<MobMoney> {
    @Suppress("DEPRECATION")
    override fun run(plugin: MobMoney) {
        Log.global = Log.builder()
            .setLogger(plugin.logger)
            .setConfig(
                ConfigurableLogConfig(
                    "Global",
                    Config.global,
                    BasicLogConfig(true, true, false, true, true, false)
                )
            )
            .setLogDirectory(logDirectory)
            .setFileWriteExecutor(Executors.newSingleThreadExecutor())
            .build()
        Log.global.deleteOlderLogs()

        log = Log4jFactory.createLogger(
            plugin,
            Log4jFactory.DEFAULT_FILE_MESSAGE_PATTERN,
            Level.valueOf(plugin.config.getString("log-level", "INFO")!!),
            logDirectory.path,
            Log.global
        )
    }
}

@Step(priority = 3, scope = StepScope.ENABLE)
object InitializeI18n : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        I18n.global.apply {
            log = MobMoney.log
            audiences = BukkitComponentAudience(BukkitAudiences.create(plugin))
            clearLanguageMap()
            loadLanguage("lang/ru.yml", plugin)
            loadLanguage("lang/en.yml", plugin)
            loadDefaultResourceLanguage(plugin)
            loadLanguages(plugin)
            setDefaultLanguage(getDefaultLanguageCode(plugin))
            setComponentInterpreter(
                ComponentInterpreters.valueOf(
                    plugin.config.getString("serializer", "MINI_MESSAGE")!!
                ).get(EmptyValidator())
            )
            usePlayerLanguage = plugin.config.getBoolean("usePlayerLanguage", true)
        }
    }
}

@Step(priority = 4, scope = StepScope.ENABLE)
object InitializeInjector : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        try {
            MobMoney.instance.injector = Guice.createInjector(
                Stage.PRODUCTION,
                PluginModule(plugin),
                PlaceholderModule,
                CommandModule
            )
        } catch (e: CreationException) {
            log.error("Guice injector creation exception", e)
        }
    }
}

@Step(priority = 5, scope = StepScope.ENABLE)
object InitializeConfig : CompositeStep<MobMoney>(ReloadConfig) {
    override fun before(plugin: MobMoney) {
        Config.global.apply {
            log = MobMoney.log
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
        Loader.global.load()
    }
}

@Step(scope = StepScope.RELOAD)
object Reload : CompositeStep<MobMoney>(ReloadConfig, InitializeLoader, InitializeI18n, Load)

object ReloadConfig : PluginStep<MobMoney> {
    override fun run(plugin: MobMoney) {
        plugin.reloadConfig()
        plugin.config.setDefaults(EMPTY_CONFIGURATION)
        Config.global.reloadConfig(plugin.config)
    }
}