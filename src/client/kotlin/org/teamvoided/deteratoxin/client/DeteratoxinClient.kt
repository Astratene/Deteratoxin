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
import org.teamvoided.deteratoxin.init.*
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.KNIFE_DATA
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.KNIFE_POTION_EFFECT
import org.teamvoided.deteratoxin.item.KnifeIngredient.Companion.getKey
import kotlin.jvm.optionals.getOrNull

object DeteratoxinClient {

    fun init() {
        DeteratoxinParticlesClient.init()

        BlockRenderLayerMap.INSTANCE.putBlock(DeteratoxinBlocks.STARLETTE_LILY, RenderType.cutout())
        EntityRendererRegistry.register(DeteratoxinEntities.DETERATOXIN_CLOUD, ::NoopRenderer)

        ColorProviderRegistry.ITEM.register(::getKnifeColor, DeteratoxinItems.KNIFE)
        ItemProperties.register(DeteratoxinItems.KNIFE, id("knife_effect")) { stack, _, _, _ ->
            if (stack.get(KNIFE_POTION_EFFECT) != null) 1f else 0f
        }
        ItemProperties.register(DeteratoxinItems.KNIFE, id("knife_state")) { stack, _, _, _ ->
            if (stack.get(KNIFE_DATA)?.readied == true) 1f else 0f
        }
        ItemTooltipCallback.EVENT.register { stack, ctx, _, tooltip -> appendKnifeTooltip(ctx, stack, tooltip) }
    }

    var INGREDIENT_TOOLTIP = "tooltip.$MODID.ingredient"

    fun appendKnifeTooltip(ctx: Item.TooltipContext, stack: ItemStack, tooltip: MutableList<Component>) {
        val player = Minecraft.getInstance().player ?: return
        if (!player.inventory.contains { it.`is`(DeteratoxinItems.KNIFE) }) {
            return
        }
        val ing = ctx.registries()?.lookup(DeteratoxinRegistries.KNIFE_INGREDIENT)
            ?.getOrNull()
            ?.get(getKey(stack.item))
            ?.getOrNull()
        if (ing == null) {
            return
        }
        val text = Component.translatable(INGREDIENT_TOOLTIP).withStyle(ChatFormatting.GRAY)
        if (tooltip.isNotEmpty()) tooltip.add(1, text) else tooltip.addLast(text)

    }

    fun getKnifeColor(stack: ItemStack, layer: Int): Int {
        if (layer != 1) {
            return -1
        }
        val effect = stack.get(KNIFE_POTION_EFFECT)?.value() ?: return -1
        return ARGB32.opaque(effect.color)
    }

}