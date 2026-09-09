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
import org.teamvoided.deteratoxin.effects.DeteratoxinEffect
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getPotent
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
        val detoxModifier = healthAttribute.getModifier(DeteratoxinEffect.DETOX_ATTRIBUTE) ?: return

        val unmodifiedMaxHealth = (healthAttribute.getValue() + (detoxModifier.amount() * -1f)).toFloat()
        if (displayHealth <= unmodifiedMaxHealth) {
            maxHealthMod.set(unmodifiedMaxHealth)
        }
    }

    @JvmStatic
    fun extractPotentHearts(player: Player, hearts: LocalRef<Int>) {
        hearts.set(player.getPotent())
    }

    @JvmStatic
    fun extractImmunityHearts(player: Player, hearts: LocalRef<Int>) {
        hearts.set(player.getImmunityHearts())
    }

    @JvmStatic
    fun GuiGraphics.renderCustomHeats(
        potentHealth: LocalRef<Int>, detoxHealth: LocalRef<Int>, immunityHealth: LocalRef<Int>,
        healthContainerCount: Int, containerIndex: Int, xo: Int, yo: Int,
    ) {
        val potent = potentHealth.get()
        val detox = detoxHealth.get()
        val immunity = immunityHealth.get()

        if (potent + detox + immunity == 0) {
            return
        }

        val index = healthContainerCount - containerIndex

        val immunityIndex = Mth.ceil(immunity / 2.0)
        val detoxIndex = Mth.ceil(immunity / 2.0 + detox / 2.0)
        val potentIndex = Mth.ceil(potent / 2.0 + immunity / 2.0 + detox / 2.0)
        if (potentIndex < index) {
            return
        }

        if (potent != 0 && immunityIndex + Mth.floor(detox / 2.0) < index) {
            val isHalf = (potent + detox + immunity) % 2 != 0
            renderHeart(this, potentIndex == index && isHalf, xo, yo, HType.POTENT)
        }
        if (detox != 0 && detoxIndex >= index && Mth.floor(immunity / 2.0) < index) {
            val isHalf = (detox + immunity) % 2 != 0
            renderHeart(this, detoxIndex == index && isHalf, xo, yo, HType.DETOX)
        }
        if (immunity != 0 && immunityIndex >= index) {
            val isHalf = immunity % 2 != 0
            renderHeart(this, immunityIndex == index && isHalf, xo, yo, HType.IMMUNITY)
        }
    }

}