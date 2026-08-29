package org.teamvoided.deteratoxin.client.init

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.ai.attributes.Attributes
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.init.DeteratoxinEffects

object DeteratoxinHudRendering {

    fun init() = HudRenderCallback.EVENT.register(::hudRenderer)

    @JvmField
    var RenderingOver = false

    fun hudRenderer(graphics: GuiGraphics, @Suppress("UNUSED_PARAMETER") deltaTracker: DeltaTracker) {
        val client = Minecraft.getInstance() ?: return
        val player = client.player ?: return
        if (client.options.hideGui) return
        if (!client.options.cameraType.isFirstPerson) return

        graphics.pose().pushPose()
        RenderSystem.enableBlend()

        graphics.renderDeteratoxinHearts(player, graphics)

        RenderSystem.disableBlend()
        graphics.pose().popPose()
    }

    fun GuiGraphics.renderDeteratoxinHearts(player: LocalPlayer, guiGraphics: GuiGraphics) {

        if (player.getEffect(DeteratoxinEffects.WEAK_DETERATOXIN) != null) {
            val k: Int = guiGraphics.guiWidth() / 2 - 91
            val n: Int = guiGraphics.guiHeight() - 39
            val maxHp = player.attributes.getValue(Attributes.MAX_HEALTH)
            val hasHalfHp = (maxHp % 2).toInt() != 0
            val heartStartRow : Int = maxHp.toInt() / 20
            val startingOffset = n + (heartStartRow * (-10 + (heartStartRow - 1)))
            var deteratoxinHearts = player.getEffect(DeteratoxinEffects.WEAK_DETERATOXIN)!!.amplifier + 1
            if (hasHalfHp) {
                //TODO make the 1 half heart render
                deteratoxinHearts--
                val displacement = (((maxHp - 1) / 2) - (10 * heartStartRow)) * 8
                this.blitSprite(
                    id("hud/half_heart"),
                    (k + displacement).toInt(),
                    startingOffset,
                    9,
                    9
                )
            }
            deteratoxinHearts = (deteratoxinHearts.toFloat() / 2f).toInt()
            repeat(deteratoxinHearts) {
                val offset = (((((maxHp.toInt() - (if (hasHalfHp) 1 else 0)) / 2) + it + (if (hasHalfHp) 1 else 0))) / (10 - (heartStartRow - 1)))
                val barsOffset = (((((maxHp.toInt() - (if (hasHalfHp) 1 else 0)) / 2) + it + (if (hasHalfHp) 1 else 0))) / 10)
                val displacement = ((((maxHp - (if (hasHalfHp) 1 else 0)) / 2) + it + (if (hasHalfHp) 1 else 0)) - ((10) * heartStartRow) - (10*barsOffset)) * 8
                //TODO make them move in-line like hearts usually would
                this.blitSprite(
                    id("hud/heart"),
                    (k + displacement).toInt(),
                    (startingOffset) + (offset * -9),
                    9,
                    9
                )
            }
        }
    }
}