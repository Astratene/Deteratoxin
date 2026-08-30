package org.teamvoided.deteratoxin.client

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.NoopRenderer
import org.teamvoided.deteratoxin.Deteratoxin
import org.teamvoided.deteratoxin.client.init.DeteratoxinParticlesClient
import org.teamvoided.deteratoxin.init.DeteratoxinBlocks
import org.teamvoided.deteratoxin.init.DeteratoxinEntities

object DeteratoxinClient {

    fun init() {
        BlockRenderLayerMap.INSTANCE.putBlock(DeteratoxinBlocks.STARLETTE_LILY, RenderType.cutout())
        Deteratoxin.log.info("Hello from Client")
        DeteratoxinParticlesClient.init()
        EntityRendererRegistry.register(
            DeteratoxinEntities.DETERATOXIN_CLOUD,
            ::NoopRenderer
        )
    }

}