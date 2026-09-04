package org.teamvoided.deteratoxin.util

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.clearDetox
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getDetox
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getDetoxCooldown
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getImmunity
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setDetox
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setDetoxCooldown
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setImmunity
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setImmunityHearts
import org.teamvoided.deteratoxin.init.DeteratoxinEffects

fun doImmunity(entity: LivingEntity) {
    val deteratoxinEffect = entity.activeEffects.filter { it.effect.value() == DeteratoxinEffects.POTENT_DETERATOXIN.value() }
    val potentDeteratoxin = entity.getDetox()
    var immunity = entity.getImmunity()
    if (deteratoxinEffect.isNotEmpty() && immunity <= 13000){
        for (effect in deteratoxinEffect){
            val amp = effect.amplifier + 1
            immunity += amp
        }
    }
    if (potentDeteratoxin > 0 && immunity > 12000 && immunity <= 24000) {
        if (immunity > 16000) {
            immunity += potentDeteratoxin * 600
            entity.clearDetox()
            entity.addEffect(MobEffectInstance(DeteratoxinEffects.POTENT_DETERATOXIN, 30, potentDeteratoxin / 2))
        }
        immunity += potentDeteratoxin
    }
    if (potentDeteratoxin <= 0 && deteratoxinEffect.isEmpty()){
        if (immunity < 2000) {
            immunity -= 5
        } else {
            immunity--
        }
    }
    entity.setImmunity(immunity)
    if (immunity in 12000..<18000) {
        entity.setImmunityHearts(2)
    } else if (immunity >= 18000) {
        entity.setImmunityHearts(4)
    }
}

fun doDetoxStuff(entity: LivingEntity) {
    if (entity.getDetox() > 0) {
        entity.setDetoxCooldown(entity.getDetoxCooldown() - 1)
        if (entity.getDetoxCooldown() <= 0) {
            entity.setDetox(entity.getDetox() - 1)
            if (entity.getDetox() <= 0) {
                entity.clearDetox()
                return
            } else {
                entity.setDetoxCooldown(600)
            }
        }

        if (entity.health > entity.maxHealth - entity.getDetox()) {
            entity.customDamage(
                DeteratoxinDamageTypes.DETERIORATED,
                entity.health - (entity.maxHealth - entity.getDetox())
            )
        }
    }
}