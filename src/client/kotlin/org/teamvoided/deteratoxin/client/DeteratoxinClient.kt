package org.teamvoided.deteratoxin.client

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.NoopRenderer
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.util.FastColor.ARGB32
import net.minecraft.world.item.ItemStack
import org.teamvoided.deteratoxin.Deteratoxin
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.client.init.DeteratoxinParticlesClient
import org.teamvoided.deteratoxin.init.DeteratoxinBlocks
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.KNIFE_POTION_EFFECT
import org.teamvoided.deteratoxin.init.DeteratoxinEntities
import org.teamvoided.deteratoxin.init.DeteratoxinItems

object DeteratoxinClient {

    fun init() {
        BlockRenderLayerMap.INSTANCE.putBlock(DeteratoxinBlocks.STARLETTE_LILY, RenderType.cutout())
        Deteratoxin.log.info("Hello from Client")
        DeteratoxinParticlesClient.init()
        EntityRendererRegistry.register(DeteratoxinEntities.DETERATOXIN_CLOUD, ::NoopRenderer)

        ColorProviderRegistry.ITEM.register(::getColor, DeteratoxinItems.KNIFE)

        ItemProperties.register(DeteratoxinItems.KNIFE, id("knife_effect")) { stack, _, _, _ ->
            val effect = stack.get(KNIFE_POTION_EFFECT)
            if (effect != null) 1f else 0f
        }
        ItemProperties.register(DeteratoxinItems.KNIFE, id("knife_state")) { stack, _, _, _ ->
            val data = stack.get(DeteratoxinDataComponents.KNIFE_DATA)
            if (data?.readied == true) 1f else 0f
        }
    }

    fun getColor(stack: ItemStack, layer: Int): Int {
        if (layer != 1) {
            return -1
        }
        val effect = stack.get(KNIFE_POTION_EFFECT)?.value() ?: return -1
        return ARGB32.opaque(effect.color)
    }

}