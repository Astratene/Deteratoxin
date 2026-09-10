package org.teamvoided.deteratoxin.client.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.particles.SimpleParticleType

class DetoxPoofParticle(
    world: ClientLevel,
    x: Double, y: Double, z: Double,
    velocityX: Double, velocityY: Double, velocityZ: Double,
    val provider: SpriteSet, small: Boolean = false,
) : TextureSheetParticle(world, x, y, z) {

    init {
        xd = velocityX * 0.8
        yd = velocityY * 0.8
        zd = velocityZ * 0.8
        setSpriteFromAge(provider)
        lifetime = 22
        alpha = 0.5f
        if (small) {
            xd = random.nextGaussian() * 0.05
            yd = random.nextGaussian() * 0.02
            zd = random.nextGaussian() * 0.05
            lifetime = 11
        }
    }

    override fun tick() {
        super.tick()
        setSpriteFromAge(provider)
    }

    override fun getQuadSize(tickDelta: Float): Float = 0.5f

    override fun getLightColor(tint: Float): Int = LightTexture.FULL_BRIGHT

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    class Factory(val provider: SpriteSet) : ParticleProvider<SimpleParticleType> {

        override fun createParticle(
            effect: SimpleParticleType,
            world: ClientLevel,
            x: Double, y: Double, z: Double,
            velocityX: Double, velocityY: Double, velocityZ: Double,
        ): Particle {
            return DetoxPoofParticle(world, x, y, z, velocityX, velocityY, velocityZ, provider)
        }

    }

    class EffectFactory(val provider: SpriteSet) : ParticleProvider<SimpleParticleType> {

        override fun createParticle(
            effect: SimpleParticleType,
            world: ClientLevel,
            x: Double, y: Double, z: Double,
            velocityX: Double, velocityY: Double, velocityZ: Double,
        ): Particle {
            return DetoxPoofParticle(world, x, y, z, velocityX, velocityY, velocityZ, provider, true)
        }

    }

}
