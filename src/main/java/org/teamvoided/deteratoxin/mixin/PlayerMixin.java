package org.teamvoided.deteratoxin.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.teamvoided.deteratoxin.util.MixinHelpersKt.doDetoxStuff;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void run(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        doDetoxStuff(entity);
        //log.info("Hello from server Mixin");
    }

}
