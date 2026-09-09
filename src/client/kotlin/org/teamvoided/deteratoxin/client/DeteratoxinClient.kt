package org.teamvoided.deteratoxin.client

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.NoopRenderer
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.network.chat.Component
import net.minecraft.util.FastColor.ARGB32
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import org.teamvoided.deteratoxin.Deteratoxin.MODID
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.client.init.DeteratoxinParticlesClient
import org.teamvoided.deteratoxin.init.DeteratoxinBlocks
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.IMBUED_EFFECT
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.KNIFE_DATA
import org.teamvoided.deteratoxin.init.DeteratoxinEntities
import org.teamvoided.deteratoxin.init.DeteratoxinItems
import org.teamvoided.deteratoxin.item.KnifeIngredient.Companion.getKnifeIngredient

object DeteratoxinClient {

    fun init() {
        DeteratoxinParticlesClient.init()

        BlockRenderLayerMap.INSTANCE.putBlock(DeteratoxinBlocks.STARLETTE_LILY, RenderType.cutout())
        EntityRendererRegistry.register(DeteratoxinEntities.DETERATOXIN_CLOUD, ::NoopRenderer)

        ColorProviderRegistry.ITEM.register(::getKnifeColor, DeteratoxinItems.KNIFE)
        ItemProperties.register(DeteratoxinItems.KNIFE, id("knife_effect")) { stack, _, _, _ ->
            if (stack.get(IMBUED_EFFECT) != null) 1f else 0f
        }
        ItemProperties.register(DeteratoxinItems.KNIFE, id("knife_state")) { stack, _, _, _ ->
            if (stack.get(KNIFE_DATA)?.readied() == true) 1f else 0f
        }
        ItemTooltipCallback.EVENT.register { stack, ctx, _, tooltip -> appendKnifeTooltip(ctx, stack, tooltip) }
    }

    var INGREDIENT_TOOLTIP = "tooltip.$MODID.ingredient"

    fun appendKnifeTooltip(ctx: Item.TooltipContext, stack: ItemStack, tooltip: MutableList<Component>) {
        val player = Minecraft.getInstance().player ?: return
        if (!player.inventory.contains { it.`is`(DeteratoxinItems.KNIFE) }) {
            return
        }
        @Suppress("UnusedVariable", "unused")
        val ingredient = ctx.registries()?.getKnifeIngredient(stack) ?: return
        val text = Component.translatable(INGREDIENT_TOOLTIP).withStyle(ChatFormatting.GRAY)
        if (tooltip.isNotEmpty()) tooltip.add(1, text) else tooltip.addLast(text)
        // TODO show creative / dev mode players effects?
    }

    fun getKnifeColor(stack: ItemStack, layer: Int): Int {
        if (layer != 1) {
            return -1
        }
        val effect = stack.get(IMBUED_EFFECT)?.effect?.value() ?: return -1
        return ARGB32.opaque(effect.color)
    }

}