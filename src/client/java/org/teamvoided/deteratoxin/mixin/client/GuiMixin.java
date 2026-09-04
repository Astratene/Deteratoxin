package org.teamvoided.deteratoxin.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.deteratoxin.effects.DeteratoxinBasicEffect;
import org.teamvoided.deteratoxin.init.DeteratoxinEffects;

import static org.teamvoided.deteratoxin.client.utils.DeteratoxinHearts.renderHeart;

@Debug(export = true)
@Mixin(Gui.class)
public class GuiMixin {

    @Shadow
    private int displayHealth;

    @Inject(method = "renderHearts", at = @At("HEAD"))
    void setupForRender(GuiGraphics gui, Player player, int xLeft, int yLineBase, int healthRowHeight, int heartOffsetIndex, float maxHealth, int currentHealth, int oldHealth, int absorption, boolean blink,
                        CallbackInfo ci, @Local(ordinal = 0, argsOnly = true) LocalFloatRef maxHealthMod, @Share("detox_hearts") LocalRef<Integer> hearts) {

//        gui.drawCenteredString(Minecraft.getInstance().font, "rawMXH:" + maxHealth, xLeft, yLineBase - 20, -1);

        hearts.set(-1);

        var deteratoxin = player.getEffect(DeteratoxinEffects.DETERATOXIN);
        if (deteratoxin == null) {
            return;
        }

        hearts.set(deteratoxin.getAmplifier() + 1);
        var healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute == null) {
            return;
        }
        var detoxModifier = healthAttribute.getModifier(DeteratoxinBasicEffect.DETOX_ATTRIBUTE);
        if (detoxModifier == null) {
            return;
        }
        var unmodifiedMaxHealth = (float) (healthAttribute.getValue() + (detoxModifier.amount() * -1f));
        if (displayHealth <= unmodifiedMaxHealth) {
            maxHealthMod.set(unmodifiedMaxHealth);
        }
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
                            CallbackInfo ci, @Share("detox_hearts") LocalRef<Integer> heartRef,
                            @Local(ordinal = 7) int healthContainerCount,
                            @Local(ordinal = 10) int containerIndex,
                            @Local(ordinal = 13) int xo, @Local(ordinal = 14) int yo) {
        if (heartRef.get() == -1) {
            return;
        }

        var toxinIndex = healthContainerCount - containerIndex;

        var toxinHeartCount = Mth.ceil((heartRef.get()) / 2.0);
        if (toxinHeartCount < toxinIndex) {
            return;
        }

        var isHalfHeart = (toxinHeartCount == toxinIndex) && heartRef.get() % 2 != 0;
        renderHeart(gui, isHalfHeart, xo, yo);
    }

}