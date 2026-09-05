package org.teamvoided.deteratoxin.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.teamvoided.deteratoxin.client.utils.DetoxHearts.*;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow
    private int displayHealth;

    @Inject(method = "renderHearts", at = @At("HEAD"))
    void setupForRender(GuiGraphics gui, Player player, int xLeft, int yLineBase, int healthRowHeight, int heartOffsetIndex, float maxHealth, int currentHealth, int oldHealth, int absorption, boolean blink,
                        CallbackInfo ci, @Local(ordinal = 0, argsOnly = true) LocalFloatRef maxHealthMod,
                        @Share("detox_hearts") LocalRef<Integer> detoxHearts,
                        @Share("potent_hearts") LocalRef<Integer> potentHearts,
                        @Share("immunity_hearts") LocalRef<Integer> immunityHearts
    ) {
        extractDetoxHearts(player, maxHealthMod, detoxHearts, displayHealth);
        extractPotentHearts(player, potentHearts);
        extractImmunityHearts(player, immunityHearts);
//        gui.drawCenteredString(Minecraft.getInstance().font, "imm: " + immunityHearts.get(), xLeft, yLineBase - 40, -1);
    }

    @Inject(
            method = "renderHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    void renderHeartOverlay(GuiGraphics gui, Player player, int xLeft, int yLineBase, int healthRowHeight, int heartOffsetIndex, float maxHealth, int currentHealth, int oldHealth, int absorption, boolean blink,
                            CallbackInfo ci, @Share("detox_hearts") LocalRef<Integer> detoxHearts,
                            @Share("potent_hearts") LocalRef<Integer> potentHearts,
                            @Share("immunity_hearts") LocalRef<Integer> immunityHearts,
                            @Local(ordinal = 7) int healthContainerCount,
                            @Local(ordinal = 10) int containerIndex,
                            @Local(ordinal = 13) int xo, @Local(ordinal = 14) int yo
    ) {
        renderCustomHeats(gui, potentHearts, detoxHearts, immunityHearts, healthContainerCount, containerIndex, xo, yo);
    }

}