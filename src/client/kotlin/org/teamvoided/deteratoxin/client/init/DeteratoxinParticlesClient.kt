package org.teamvoided.deteratoxin.client.init

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.PendingParticleFactory
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import org.teamvoided.deteratoxin.client.particle.DetoxPoofParticle
import org.teamvoided.deteratoxin.init.DeteratoxinParticles

object DeteratoxinParticlesClient {

    fun init() {
        register(DeteratoxinParticles.DETOX_POOF, DetoxPoofParticle::Factory)
        register(DeteratoxinParticles.SMALL_DETOX_POOF, DetoxPoofParticle::EffectFactory)
        register(DeteratoxinParticles.SMALL_POTENT_DETOX_POOF, DetoxPoofParticle::EffectFactory)
    }

    fun <T : ParticleOptions> register(type: ParticleType<T>, factory: PendingParticleFactory<T>) {
        ParticleFactoryRegistry.getInstance().register(type, factory)
    }

}