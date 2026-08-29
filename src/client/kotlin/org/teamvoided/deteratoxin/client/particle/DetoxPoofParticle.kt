package org.teamvoided.deteratoxin.client.particle

import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.particles.SimpleParticleType

class DetoxPoofParticle (
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    val provider: SpriteSet
) :
    TextureSheetParticle(world, x, y, z) {

    init {
        this.xd = velocityX * 0.8
        this.yd = velocityY * 0.8
        this.zd = velocityZ * 0.8
        setSpriteFromAge(provider)
        lifetime = 22
        this.alpha = 0.5f
    }

    override fun tick() {
        super.tick()
        setSpriteFromAge(provider)
    }

//    override fun getBrightness(tint: Float): Int {
//        return 120
//    }

    override fun getQuadSize(tickDelta: Float): Float {
        return 0.5f
    }

    override fun getLightColor(tint: Float): Int {
        return LightTexture.FULL_BRIGHT
    }

    override fun getRenderType(): ParticleRenderType? {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    class Factory(val provider: SpriteSet) : ParticleProvider<SimpleParticleType> {

        override fun createParticle(
            effect: SimpleParticleType?,
            world: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return DetoxPoofParticle(world, x, y, z, velocityX, velocityY, velocityZ, provider)
        }
    }
}
