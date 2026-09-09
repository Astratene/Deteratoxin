package org.teamvoided.deteratoxin.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static org.teamvoided.deteratoxin.util.MixinHelpersKt.allowHealing;

@Mixin(FoodData.class)
public class FoodDataMixin {

    @ModifyExpressionValue(method = "tick(Lnet/minecraft/world/entity/player/Player;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    public boolean tick(boolean original, Player Player) {
        return original && allowHealing(Player);
    }
    
}
