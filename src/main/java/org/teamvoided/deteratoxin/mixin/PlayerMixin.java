package org.teamvoided.deteratoxin.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.teamvoided.deteratoxin.util.MixinHelpersKt.*;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void run(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        doDetoxStuff(entity);
        doImmunity(entity);
        //log.info("Hello from server Mixin");
    }

    @ModifyExpressionValue(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    public boolean tick(boolean original) {
        LivingEntity entity = (LivingEntity) (Object) this;
        return original && dontHeal(entity);
    }

    @WrapOperation(method = "causeFoodExhaustion(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V"))
    private void causeFoodExhaustion(FoodData instance, float f, Operation<Void> original) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (dontHeal(entity)){
            return;
        }
    }

}
