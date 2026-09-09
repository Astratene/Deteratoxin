package org.teamvoided.deteratoxin.util

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.clearImmunity
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.clearPotent
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getImmunity
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getImmunityHearts
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getPotent
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getPotentCooldown
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setImmunity
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setPotent
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setPotentCooldown
import org.teamvoided.deteratoxin.init.DeteratoxinEffects
import kotlin.math.max

fun tickImmunity(entity: LivingEntity) {
    val deteratoxinEffect = entity.getEffect(DeteratoxinEffects.DETERATOXIN)
    val potentDeteratoxin = entity.getPotent()
    var immunity = entity.getImmunity()
    if (deteratoxinEffect != null && immunity <= 13000) {
        immunity += deteratoxinEffect.amplifier + 1
    }
    if (potentDeteratoxin > 0 && immunity > 12000) {
        if (immunity > 16000) {
            if (immunity <= 24000) {
                immunity += potentDeteratoxin * 600
            }
            if (!entity.level().isClientSide) {
                entity.addEffect(MobEffectInstance(DeteratoxinEffects.DETERATOXIN, 600, max(potentDeteratoxin / 2, 1)))
            }
            entity.clearPotent()
        }
        immunity += potentDeteratoxin
    }
    if (potentDeteratoxin <= 0 && deteratoxinEffect == null) {
        if (immunity < 2000) {
            immunity -= 5
        } else {
            immunity--
        }
    }
    if (immunity <= 0) {
        entity.clearImmunity()
    } else {
        entity.setImmunity(immunity)
    }
}

fun tickDeteratoxin(entity: LivingEntity) {
    var potent = entity.getPotent()
    if (potent > 0) {
        entity.setPotentCooldown(entity.getPotentCooldown() - 1)
        if (entity.getPotentCooldown() <= 0) {
            potent--
            if (potent <= 0) {
                entity.clearPotent()
                return
            } else {
                entity.setPotent(potent)
                entity.setPotentCooldown(600)
            }
        }
    }
    if (entity.health > entity.getEffectiveMaxHeath()) {
        entity.customDamage(DeteratoxinDamageTypes.DETERIORATED, entity.health - entity.getEffectiveMaxHeath())
    }
}

fun allowHealing(entity: LivingEntity): Boolean {
    return entity.health < entity.getEffectiveMaxHeath()
}

fun LivingEntity.getEffectiveMaxHeath(): Float {
    return maxHealth - getPotent() - getImmunityHearts()
}