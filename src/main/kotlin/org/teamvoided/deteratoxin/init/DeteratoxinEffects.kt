package org.teamvoided.deteratoxin.init

import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.effects.DeteratoxinBasicEffect
import org.teamvoided.deteratoxin.util.registerHolder

object DeteratoxinEffects {
    fun init() = Unit

    val WEAK_DETERATOXIN = register(
        "weak_deteratoxin", DeteratoxinBasicEffect(MobEffectCategory.BENEFICIAL, 0xdb2d00)
            .addAttributeModifier(
                Attributes.MAX_HEALTH, id("effect.weak_deteratoxin"),
                (-1.0), AttributeModifier.Operation.ADD_VALUE
            )
    )

    private fun register(id: String, entry: MobEffect): Holder<MobEffect> =
        BuiltInRegistries.MOB_EFFECT.registerHolder(id(id), entry)
}