package me.wyne.mobmoney.config

import com.google.inject.Inject
import com.google.inject.Singleton
import me.wyne.wutils.config.Config
import me.wyne.wutils.config.ConfigEntry

@Singleton
class MessageConfig @Inject constructor() {

    @ConfigEntry(section = "Message")
    val showChatMessage = false

    @ConfigEntry(section = "Message")
    val showActionBarMessage = true

    init {
        Config.global.registerConfigObject(this)
    }

}