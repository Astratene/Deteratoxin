package org.teamvoided.deteratoxin.effects

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import org.teamvoided.deteratoxin.Deteratoxin.id

open class DeteratoxinEffect : MobEffect {

    constructor(type: MobEffectCategory, color: Int) : super(type, color)

    constructor(type: MobEffectCategory, color: Int, particle: ParticleOptions) : super(type, color, particle)

    companion object {

        @JvmField
        val DETOX_ATTRIBUTE = id("effect.deteratoxin")

    }
}