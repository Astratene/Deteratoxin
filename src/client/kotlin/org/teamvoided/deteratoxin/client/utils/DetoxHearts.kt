package org.teamvoided.deteratoxin.client.utils

import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef
import com.llamalad7.mixinextras.sugar.ref.LocalRef
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.effects.DeteratoxinBasicEffect
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getDetox
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getImmunityHearts
import org.teamvoided.deteratoxin.init.DeteratoxinEffects

object DetoxHearts {

    val DETOX_FULL = heart("detox_full")
    val DETOX_HALF = heart("detox_half")

    val POTENT_DETOX_FULL = heart("potent_detox_full")
    val POTENT_DETOX_HALF = heart("potent_detox_half")

    val IMMUNITY_FULL = heart("immunity_full")
    val IMMUNITY_HALF = heart("immunity_half")

    fun heart(name: String) = id("hud/heart/$name")

    fun renderHeart(gui: GuiGraphics, halfHeart: Boolean, x: Int, y: Int, type: HType) {
        RenderSystem.enableBlend()
        gui.blitSprite(type.texture(halfHeart), x, y, 9, 9)
        RenderSystem.disableBlend()
    }

    enum class HType {

        DETOX, POTENT, IMMUNITY;

        fun texture(halfHeart: Boolean): ResourceLocation {
            return when (this) {
                DETOX -> if (halfHeart) DETOX_HALF else DETOX_FULL
                POTENT -> if (halfHeart) POTENT_DETOX_HALF else POTENT_DETOX_FULL
                IMMUNITY -> if (halfHeart) IMMUNITY_HALF else IMMUNITY_FULL
            }
        }

    }


    @JvmStatic
    fun extractDetoxHearts(player: Player, maxHealthMod: LocalFloatRef, hearts: LocalRef<Int>, displayHealth: Int) {
        hearts.set(0)

        val deteratoxin = player.getEffect(DeteratoxinEffects.DETERATOXIN) ?: return

        hearts.set(deteratoxin.amplifier + 1)
        val healthAttribute = player.getAttribute(Attributes.MAX_HEALTH) ?: return
        val detoxModifier = healthAttribute.getModifier(DeteratoxinBasicEffect.DETOX_ATTRIBUTE) ?: return

        val unmodifiedMaxHealth = (healthAttribute.getValue() + (detoxModifier.amount() * -1f)).toFloat()
        if (displayHealth <= unmodifiedMaxHealth) {
            maxHealthMod.set(unmodifiedMaxHealth)
        }
    }

    @JvmStatic
    fun extractPotentHearts(player: Player, hearts: LocalRef<Int>) {
        hearts.set(0)
        val potent = player.getDetox()
        if (potent > 0) {
            hearts.set(potent)
        }
    }

    @JvmStatic
    fun extractImmunityHearts(player: Player, hearts: LocalRef<Int>) {
        hearts.set(0)
        val potent = player.getImmunityHearts()
        if (potent > 0) {
            hearts.set(potent)
        }
    }

    @JvmStatic
    fun GuiGraphics.renderCustomHeats(
        potentHealth: LocalRef<Int>, detoxHealth: LocalRef<Int>, immunityHealth: LocalRef<Int>,
        healthContainerCount: Int, containerIndex: Int, xo: Int, yo: Int,
    ) {
        val detox = detoxHealth.get()
        val potent = potentHealth.get()
        val immunity = immunityHealth.get()

        if (detox + potent + immunity == 0) {
            return
        }

        val reverseIndex = healthContainerCount - containerIndex

        val detoxIndex = Mth.ceil(detox / 2.0)
        val maxIndexCount = Mth.floor(potent / 2.0) + detoxIndex
        if (maxIndexCount < reverseIndex) {
            return
        }

        val isHalfDetox = (detoxIndex == reverseIndex) && detox % 2 != 0
        if (potent != 0 && Mth.floor(detox / 2.0) < reverseIndex) {
            val isHalfPotent = (maxIndexCount == reverseIndex) &&
                    if (detox % 2 == 0) (potent % 2 != 0) else (potent % 2 == 0)

            renderHeart(this, isHalfPotent, xo, yo, HType.POTENT)
        }

        if (detox != 0 && detoxIndex >= reverseIndex) {
            renderHeart(this, isHalfDetox, xo, yo, HType.DETOX)
        }
    }

}