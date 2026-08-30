package org.teamvoided.deteratoxin.client.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import org.teamvoided.deteratoxin.Deteratoxin.id

object DeteratoxinHearts {

    val DETOX_FULL = heart("detox_full")
    val DETOX_HALF = heart("detox_half")

    val POTENT_DETOX_FULL = heart("potent_detox_full")
    val POTENT_DETOX_HALF = heart("potent_detox_half")

    fun heart(name: String) = id("hud/heart/$name")

    @JvmStatic
    fun renderHeart(gui: GuiGraphics, halfHeart: Boolean, x: Int, y: Int) {
        RenderSystem.enableBlend()
        gui.blitSprite(pickHeart(false, halfHeart), x, y, 9, 9)
        RenderSystem.disableBlend()
    }

    fun pickHeart(isPotent: Boolean, halfHeart: Boolean): ResourceLocation {
        return if (isPotent) {
            if (halfHeart) POTENT_DETOX_HALF else POTENT_DETOX_FULL
        } else {
            if (halfHeart) DETOX_HALF else DETOX_FULL
        }
    }

}