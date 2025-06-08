package org.bigcraft.mobmoney.module

import com.google.inject.AbstractModule

private class CoreModule(private vararg val modules: Class<out Any>) : AbstractModule() {
    override fun configure() {
        modules.forEach { bind(it) }
    }
}