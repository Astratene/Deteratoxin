package org.teamvoided.deteratoxin.init

import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.effects.DeteratoxinEffect
import org.teamvoided.deteratoxin.effects.PotentDeteratoxinEffect
import org.teamvoided.deteratoxin.util.registerHolder

object DeteratoxinEffects {

    @JvmField
    val DETERATOXIN = register(
        "deteratoxin", DeteratoxinEffect(MobEffectCategory.HARMFUL, 0xdb2d00, DeteratoxinParticles.SMALL_DETOX_POOF)
            .addAttributeModifier(
                Attributes.MAX_HEALTH, DeteratoxinEffect.DETOX_ATTRIBUTE,
                -1.0, AttributeModifier.Operation.ADD_VALUE
            )
    )

    val POTENT_DETERATOXIN = register(
        "potent_deteratoxin", PotentDeteratoxinEffect(MobEffectCategory.HARMFUL, 0x377f13)
    )

    fun init() = Unit

    fun register(id: String, entry: MobEffect): Holder<MobEffect> {
        return BuiltInRegistries.MOB_EFFECT.registerHolder(id(id), entry)
    }

}