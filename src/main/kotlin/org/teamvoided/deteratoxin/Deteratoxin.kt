package org.teamvoided.deteratoxin

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.deteratoxin.config.DeteratoxinConfig
import org.teamvoided.deteratoxin.init.DeteratoxinBlocks
import org.teamvoided.deteratoxin.init.DeteratoxinItems

object Deteratoxin {

    const val MODID = "deteratoxin"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(Deteratoxin::class.simpleName)

    @JvmField
    var config = ConfigApi.registerAndLoadConfig(::DeteratoxinConfig)

    fun init() {
        log.info("Hello from Common ${config.commonEntry.get()}")
        DeteratoxinItems.init()
        DeteratoxinBlocks.init()
    }

    fun id(namespace: String, path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)
    fun mc(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)
    fun id(path: String) = id(MODID, path)

}