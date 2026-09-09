package org.teamvoided.deteratoxin.effects

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import org.teamvoided.deteratoxin.Deteratoxin.id

open class DeteratoxinEffect : MobEffect {

    constructor(type: MobEffectCategory, color: Int) : super(type, color)

    companion object {

        @JvmField
        val DETOX_ATTRIBUTE = id("effect.deteratoxin")

    }
}