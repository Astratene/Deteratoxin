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

fun doImmunity(entity: LivingEntity) {
    val deteratoxinEffect = entity.activeEffects.filter { it.effect.value() == DeteratoxinEffects.DETERATOXIN.value() }
    val potentDeteratoxin = entity.getPotent()
    var immunity = entity.getImmunity()
    if (deteratoxinEffect.isNotEmpty() && immunity <= 13000) {
        for (effect in deteratoxinEffect) {
            val amp = effect.amplifier + 1
            immunity += amp
        }
    }
    if (potentDeteratoxin > 0 && immunity > 12000) {
        if (immunity > 16000) {
            if (immunity <= 24000) {
                immunity += potentDeteratoxin * 600
            }
            if (!entity.level().isClientSide) {
                entity.addEffect(
                    MobEffectInstance(
                        DeteratoxinEffects.DETERATOXIN,
                        600,
                        max(potentDeteratoxin / 2, 1)
                    )
                )
            }
            entity.clearPotent()
        }
        immunity += potentDeteratoxin
    }
    if (potentDeteratoxin <= 0 && deteratoxinEffect.isEmpty()) {
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

fun doDetoxStuff(entity: LivingEntity) {
    if (entity.getPotent() > 0) {
        entity.setPotentCooldown(entity.getPotentCooldown() - 1)
        if (entity.getPotentCooldown() <= 0) {
            entity.setPotent(entity.getPotent() - 1)
            if (entity.getPotent() <= 0) {
                entity.clearPotent()
                return
            } else {
                entity.setPotentCooldown(600)
            }
        }
    }
    if (entity.getPotent() > 0 || entity.getImmunityHearts() > 0) {
        if (entity.health > (entity.maxHealth - entity.getPotent()) - entity.getImmunityHearts()) {
            entity.customDamage(
                DeteratoxinDamageTypes.DETERIORATED,
                entity.health - ((entity.maxHealth - entity.getPotent()) - entity.getImmunityHearts())
            )
        }
    }
}

fun dontHeal(entity: LivingEntity): Boolean {
    return if ((entity.health) < (entity.maxHealth - entity.getPotent()) - entity.getImmunityHearts()) {
        true
    } else {
        false
    }
}