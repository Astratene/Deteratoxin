package org.teamvoided.deteratoxin.effects

import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getPotent
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setPotent
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setPotentCooldown
import org.teamvoided.deteratoxin.init.DeteratoxinEffects
import org.teamvoided.deteratoxin.util.customDamage

class PotentDeteratoxinEffect(type: MobEffectCategory, color: Int) : DeteratoxinEffect(type, color) {

    override fun isInstantenous(): Boolean = true

    override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean = true

    override fun applyEffectTick(entity: LivingEntity, amplifier: Int): Boolean {
        val potent = entity.getPotent()
        val effectiveMaxHealth = (entity.maxHealth - potent) - (amplifier + 1)
        if (entity.health > effectiveMaxHealth) {
            entity.customDamage(DeteratoxinDamageTypes.DETERIORATED, entity.health - effectiveMaxHealth)
        }
        entity.setPotent(potent + amplifier)
        entity.setPotentCooldown(600)
        entity.removeEffect(DeteratoxinEffects.POTENT_DETERATOXIN)
        return true
    }

}