package org.teamvoided.deteratoxin.datagen.data.registry

import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageType
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes

object DamageTypes : RegistryBootstrapper<DamageType> {

    override fun BootstrapContext<DamageType>.init() {
        damage(DeteratoxinDamageTypes.STABBED, 0f)
        damage(DeteratoxinDamageTypes.DETOX, 0f)
        damage(DeteratoxinDamageTypes.DRINKNT, 0f)
        damage(DeteratoxinDamageTypes.DRINKETH, 0f)
        damage(DeteratoxinDamageTypes.DETERIORATED, 0f)
    }

    fun BootstrapContext<DamageType>.damage(
        key: ResourceKey<DamageType>,
        exhaustion: Float,
        effect: DamageEffects = DamageEffects.HURT,
    ) {
        register(key, DamageType(key.location().path, DamageScaling.NEVER, exhaustion, effect))
    }

}