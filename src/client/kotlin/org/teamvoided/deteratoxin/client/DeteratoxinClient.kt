package org.teamvoided.deteratoxin.client

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.renderer.RenderType
import org.teamvoided.deteratoxin.Deteratoxin
import org.teamvoided.deteratoxin.client.init.DeteratoxinHudRendering
import org.teamvoided.deteratoxin.client.init.DeteratoxinParticlesClient
import org.teamvoided.deteratoxin.init.DeteratoxinBlocks

object DeteratoxinClient {

    fun init() {
        BlockRenderLayerMap.INSTANCE.putBlock(DeteratoxinBlocks.STARLETTE_LILY, RenderType.cutout())
        Deteratoxin.log.info("Hello from Client")
        DeteratoxinParticlesClient.init()
        DeteratoxinHudRendering.init()
    }

}