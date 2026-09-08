package org.teamvoided.deteratoxin.datagen.data.registry

import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageType
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes

object ModDamageTypes : RegistryBootstrapper<DamageType> {

    override fun BootstrapContext<DamageType>.init() {
        damage(DeteratoxinDamageTypes.STABBED)
        damage(DeteratoxinDamageTypes.DETOX)
        damage(DeteratoxinDamageTypes.DRINKNT)
        damage(DeteratoxinDamageTypes.DRINKETH)
        damage(DeteratoxinDamageTypes.DETERIORATED)
    }

    fun BootstrapContext<DamageType>.damage(
        key: ResourceKey<DamageType>, exhaustion: Float = 0f, effect: DamageEffects = DamageEffects.HURT,
    ) {
        register(key, DamageType(key.location().path, DamageScaling.NEVER, exhaustion, effect))
    }

}