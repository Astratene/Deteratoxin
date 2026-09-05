package org.teamvoided.deteratoxin.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.teamvoided.deteratoxin.util.MixinHelpersKt.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void run(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        tickDeteratoxin(entity);
        tickImmunity(entity);
    }

    @ModifyExpressionValue(method = "setHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getMaxHealth()F"))
    float onlyHealEffectiveMax(float original) {
        return getEffectiveMaxHeath((LivingEntity) (Object) this);
    }

}