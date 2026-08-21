import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "2.1.20"
    alias(libs.plugins.shadow)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.pluginYml)
}

kotlin {
    jvmToolchain(16)
}

dependencies {
    compileOnly(libs.paperApi)
    compileOnly(libs.commandApi)
    compileOnly(libs.infPoints)
    compileOnly("org.bigcraft:Storm-api:1.1.0")

    implementation(project(":api"))
    implementation(libs.guice)
    implementation(libs.adventureMini)
    implementation(libs.adventureBukkit)

    implementation(libs.wutilsConfig)
    implementation(libs.wutilsLog)
    implementation(libs.wutilsI18nKotlin)
    implementation(libs.wutilsCommon)
}

tasks {
    shadowJar {
        archiveBaseName.set(findProperty("name").toString())
        archiveClassifier.set("")
        minimize()
        if ((findProperty("debug") ?: false) == false) {
            relocate("com.google.inject", "org.bigcraft.mobmoney.shadow.google.guice")
            relocate("com.google.common", "org.bigcraft.mobmoney.shadow.google.common")
            relocate("net.kyori", "org.bigcraft.mobmoney.shadow.net.kyori")
            relocate("me.wyne.wutils", "org.bigcraft.mobmoney.shadow.wutils")
        }
    }

    runServer {
        downloadPlugins {
            url("https://ci.extendedclip.com/view/Plugins/job/PlaceholderAPI/197/artifact/build/libs/PlaceholderAPI-2.11.6.jar")
            url("https://download.luckperms.net/1584/bukkit/loader/LuckPerms-Bukkit-5.5.0.jar")
            url("https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/artifact/build/libs/ProtocolLib.jar")
            github("ViaVersion", "ViaVersion", "5.2.1", "ViaVersion-5.2.1.jar")
            github("ViaVersion", "ViaBackwards", "5.2.1", "ViaBackwards-5.2.1.jar")
            github("CommandAPI", "CommandAPI", "9.7.0", "CommandAPI-9.7.0.jar")
        }
        minecraftVersion("1.21.3")
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}

bukkit {
    name = findProperty("name").toString()
    version = getVersion().toString()
    website = findProperty("website").toString()
    author = findProperty("author").toString()
    main = "org.bigcraft.mobmoney.MobMoney"
    apiVersion = "1.16"
    softDepend = listOf("CommandAPI", "InfPoints", "Storm")
    permissions {
        register("mobmoney.*") {
            children = listOf("mobmoney.reload")
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("mobmoney.reload") {
            description = "Allows to reload plugin"
        }
    }
}