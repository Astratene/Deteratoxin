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

//    private fun GuiGraphics.renderOverArchIeverCircle(player: LocalPlayer) {
//        val hand = getOverArchIeverHand(player)
//        if (hand != null) {
//            this.blitSprite(
//                id("hud/overarchiever_uncharged"),
//                (this.guiWidth() / 2) - 7,
//                (this.guiHeight() / 2) - 7,
//                15,
//                15
//            )
//            val stack = player.getItemInHand(hand)
//            if (stack.item is OverarchieverItem) {
//                val item = stack.item as OverarchieverItem
//                var charge = stack.getOrDefault(
//                    StarbornSoundscapeDataComponents.CURRENT_USE_TIME,
//                    CurrentUseTime.DEFAULT
//                ).useTime
//                val smallRing = item.getMinChargeTicks(player, stack)
//                val bigRing = item.getChargeTicks(player, stack)
//
//                if (charge >= smallRing) {
//                    this.blitSprite(
//                        id("hud/overarchiever_half_charged"),
//                        (this.guiWidth() / 2) - 7,
//                        (this.guiHeight() / 2) - 7,
//                        15,
//                        15
//                    )
//                    charge -= smallRing
//                    if (charge >= (bigRing - smallRing)) {
//                        this.blitSprite(
//                            id("hud/overarchiever_full_charged"),
//                            (this.guiWidth() / 2) - 7,
//                            (this.guiHeight() / 2) - 7,
//                            15,
//                            15
//                        )
//                    } else {
//                        var bigRingPixels = (((charge.toFloat()) / (bigRing.toFloat() - smallRing.toFloat())) * 40f).roundToInt()
//                        repeat(min(bigRingPixels, 6)) {
//                            this.blitSprite(
//                                id("hud/full_charge_full_dot"),
//                                ((this.guiWidth() / 2)) + it,
//                                ((this.guiHeight() / 2)) - 5,
//                                1,
//                                1
//                            )
//                        }
//                        bigRingPixels -= 6
//                        if (bigRingPixels > 0){
//                            repeat(min(bigRingPixels, 10)) {
//                                this.blitSprite(
//                                    id("hud/full_charge_full_dot"),
//                                    ((this.guiWidth() / 2)) + 5,
//                                    ((this.guiHeight() / 2) - 4) + it,
//                                    1,
//                                    1
//                                )
//                            }
//                            bigRingPixels -= 10
//                            if (bigRingPixels > 0){
//                                repeat(min(bigRingPixels, 10)) {
//                                    this.blitSprite(
//                                        id("hud/full_charge_full_dot"),
//                                        ((this.guiWidth() / 2) + 4) - it,
//                                        ((this.guiHeight() / 2) + 5),
//                                        1,
//                                        1
//                                    )
//                                }
//                                bigRingPixels -= 10
//                                if (bigRingPixels > 0){
//                                    repeat(min(bigRingPixels, 10)) {
//                                        this.blitSprite(
//                                            id("hud/full_charge_full_dot"),
//                                            ((this.guiWidth() / 2) - 5),
//                                            ((this.guiHeight() / 2) + 4) - it,
//                                            1,
//                                            1
//                                        )
//                                    }
//                                    bigRingPixels -= 10
//                                    if (bigRingPixels > 0){
//                                        repeat(min(bigRingPixels, 4)) {
//                                            this.blitSprite(
//                                                id("hud/full_charge_full_dot"),
//                                                ((this.guiWidth() / 2) - 4) + it,
//                                                ((this.guiHeight() / 2) - 5),
//                                                1,
//                                                1
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    var smallRingPixels = ((charge.toFloat() / smallRing.toFloat()) * 32f).roundToInt()
//                    repeat(min(smallRingPixels, 5)) {
//                        this.blitSprite(
//                            id("hud/full_charge_half_dot"),
//                            ((this.guiWidth() / 2)) + it,
//                            ((this.guiHeight() / 2)) - 4,
//                            1,
//                            1
//                        )
//                    }
//                    smallRingPixels -= 5
//                    if (smallRingPixels > 0){
//                        repeat(min(smallRingPixels, 8)) {
//                            this.blitSprite(
//                                id("hud/full_charge_half_dot"),
//                                ((this.guiWidth() / 2)) + 4,
//                                (((this.guiHeight() / 2)) - 3) + it,
//                                1,
//                                1
//                            )
//                        }
//                        smallRingPixels -= 8
//                        if (smallRingPixels > 0){
//                            repeat(min(smallRingPixels, 8)) {
//                                this.blitSprite(
//                                    id("hud/full_charge_half_dot"),
//                                    (((this.guiWidth() / 2)) + 3) - it,
//                                    (((this.guiHeight() / 2)) + 4),
//                                    1,
//                                    1
//                                )
//                            }
//                            smallRingPixels -= 8
//                            if (smallRingPixels > 0){
//                                repeat(min(smallRingPixels, 8)) {
//                                    this.blitSprite(
//                                        id("hud/full_charge_half_dot"),
//                                        (((this.guiWidth() / 2)) - 4),
//                                        (((this.guiHeight() / 2)) + 3) - it,
//                                        1,
//                                        1
//                                    )
//                                }
//                                smallRingPixels -= 8
//                                if (smallRingPixels > 0){
//                                    repeat(min(smallRingPixels, 3)) {
//                                        this.blitSprite(
//                                            id("hud/full_charge_half_dot"),
//                                            (((this.guiWidth() / 2)) - 3) + it,
//                                            (((this.guiHeight() / 2)) - 4),
//                                            1,
//                                            1
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

//    public fun getOverArchIeverHand(player: LocalPlayer): InteractionHand? {
//        val heldItem = player.getItemInHand(InteractionHand.MAIN_HAND)
//        val offhandItem = player.getItemInHand(InteractionHand.OFF_HAND)
//        if (heldItem.item is OverarchieverItem) {
//            return InteractionHand.MAIN_HAND
//        } else if (offhandItem.item is OverarchieverItem) {
//            return InteractionHand.OFF_HAND
//        } else return null
//    }

}