package org.teamvoided.deteratoxin.effects

import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getDetox
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setDetox
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setDetoxCooldown
import org.teamvoided.deteratoxin.init.DeteratoxinEffects
import org.teamvoided.deteratoxin.util.customDamage

class PotentDeteratoxinEffect(type: MobEffectCategory, color: Int) : DeteratoxinBasicEffect(type, color) {

    override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean {
        return true
    }

    override fun applyEffectTick(entity: LivingEntity, amplifier: Int): Boolean {
        val existingDetox = entity.getDetox()
        val hp = ((entity.maxHealth - existingDetox) - (amplifier + 1))
        if (entity.health > hp) {
            val damage = entity.health - hp
            entity.customDamage(DeteratoxinDamageTypes.DETERIORATED, damage)
        }
        entity.setDetox(existingDetox + amplifier)
        entity.setDetoxCooldown(600)
        entity.removeEffect(DeteratoxinEffects.POTENT_DETERATOXIN)
        return true
    }

    override fun isInstantenous(): Boolean {
        return true
    }
}