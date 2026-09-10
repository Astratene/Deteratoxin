package org.teamvoided.deteratoxin.init

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.simple
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.util.register

object DeteratoxinParticles {

    val DETOX_POOF: SimpleParticleType = register("detox_poof", simple())
    val SMALL_DETOX_POOF: SimpleParticleType = register("small_detox_poof", simple())

    fun init() = Unit

    fun <O : ParticleOptions, T : ParticleType<O>> register(id: String, particleType: T): T {
        return BuiltInRegistries.PARTICLE_TYPE.register(id(id), particleType)
    }

}