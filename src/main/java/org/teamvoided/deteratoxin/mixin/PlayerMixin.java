package org.teamvoided.deteratoxin.mixin;

import com.mojang.datafixers.DataFixer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

import static org.teamvoided.deteratoxin.Deteratoxin.log;
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
