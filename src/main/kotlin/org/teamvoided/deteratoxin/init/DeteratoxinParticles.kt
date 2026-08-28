package org.teamvoided.deteratoxin.init

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.simple
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import org.teamvoided.deteratoxin.Deteratoxin.id

object DeteratoxinParticles {
    val DETOX_POOF: SimpleParticleType = simple()



    fun init() {
        register("detox_poof", DETOX_POOF)
    }

    fun register(id: String, particleType: ParticleType<*>): ParticleType<*> =
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, id(id), particleType)
}