package org.teamvoided.deteratoxin.effects

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory

open class DeteratoxinBasicEffect  : MobEffect {


    constructor(type: MobEffectCategory, color: Int) : super(type, color) {
    }
}