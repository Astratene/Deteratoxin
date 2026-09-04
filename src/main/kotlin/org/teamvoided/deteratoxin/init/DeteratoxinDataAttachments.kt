package org.teamvoided.deteratoxin.init

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.entity.LivingEntity
import org.teamvoided.deteratoxin.Deteratoxin.id
import java.util.function.Consumer

@Suppress("UnstableApiUsage")
object DeteratoxinDataAttachments {

    fun init() = Unit

    @JvmField
    val IMMUNITY = register("immunity") {
        it.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.targetOnly())
    }

    fun LivingEntity.getImmunity() = getAttachedOrElse(IMMUNITY, 0)

    fun LivingEntity.setImmunity(value: Int) = setAttached(IMMUNITY, value)

    fun LivingEntity.clearImmunity() = removeAttached(IMMUNITY)

    val IMMUNITY_HEARTS = register("potent_deteratoxin") {
        it.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.targetOnly())
    }

    fun LivingEntity.getImmunityHearts() = getAttachedOrElse(IMMUNITY_HEARTS, 0)

    fun LivingEntity.setImmunityHearts(value: Int) = setAttached(IMMUNITY_HEARTS, value)

    fun LivingEntity.clearImmunityHearts() = removeAttached(IMMUNITY_HEARTS)

    val POTENT_DETERATOXIN = register("potent_deteratoxin") {
        it.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.targetOnly())
    }

    fun LivingEntity.getDetox() = getAttachedOrElse(POTENT_DETERATOXIN, 0)

    fun LivingEntity.setDetox(value: Int) = setAttached(POTENT_DETERATOXIN, value)

    fun LivingEntity.clearDetox() = removeAttached(POTENT_DETERATOXIN)

    val DETOX_COOLDOWN = register("detox_cooldown") {
        it.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.targetOnly())
    }

    fun LivingEntity.getDetoxCooldown() = getAttachedOrElse(DETOX_COOLDOWN, 0)

    fun LivingEntity.setDetoxCooldown(value: Int) = setAttached(DETOX_COOLDOWN, value)

    fun LivingEntity.clearDetoxCooldown() = removeAttached(DETOX_COOLDOWN)


    fun <T : Any> register(id: String, builder: Consumer<AttachmentRegistry.Builder<T>>): AttachmentType<T> {
        return AttachmentRegistry.create(id(id), builder)
    }
    
}