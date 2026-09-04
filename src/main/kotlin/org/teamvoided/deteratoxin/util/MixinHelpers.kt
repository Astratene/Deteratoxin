package org.teamvoided.deteratoxin.util

import net.minecraft.world.entity.LivingEntity
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.clearDetox
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getDetox
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getDetoxCooldown
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setDetox
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setDetoxCooldown

fun doImmunity(player: LivingEntity) {
    val playerEffects = player.activeEffects

}

fun doDetoxStuff(player: LivingEntity) {
    if (player.getDetox() > 0) {
        player.setDetoxCooldown(player.getDetoxCooldown() - 1)
        if (player.getDetoxCooldown() <= 0) {
            player.setDetox(player.getDetox() - 1)
            if (player.getDetox() <= 0) {
                player.clearDetox()
                return
            } else {
                player.setDetoxCooldown(600)
            }
        }

        if (player.health > player.maxHealth - player.getDetox()) {
            player.customDamage(
                DeteratoxinDamageTypes.DETERIORATED,
                player.health - (player.maxHealth - player.getDetox())
            )
        }
    }
}