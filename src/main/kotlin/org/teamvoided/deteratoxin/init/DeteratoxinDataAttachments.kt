package org.teamvoided.deteratoxin.init

import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate.targetOnly
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
        it
            .persistent(Codec.INT)
            .copyOnDeath()
            .syncWith(ByteBufCodecs.INT, targetOnly())
    }
    val POTENT_DETERATOXIN = register("potent_deteratoxin")
    val DETOX_COOLDOWN = register("detox_cooldown")

    // region Helpers
    fun LivingEntity.getImmunity(): Int = getAttachedOrElse(IMMUNITY, 0)
    fun LivingEntity.setImmunity(value: Int) = setAttached(IMMUNITY, value)
    fun LivingEntity.clearImmunity() = removeAttached(IMMUNITY)

    fun LivingEntity.getImmunityHearts(): Int {
        val immunity = getImmunity()
        return if (immunity >= 18000)
            4
        else if (immunity >= 12000)
            2
        else
            0
    }

    fun LivingEntity.getPotent(): Int = getAttachedOrElse(POTENT_DETERATOXIN, 0)
    fun LivingEntity.setPotent(value: Int) = setAttached(POTENT_DETERATOXIN, value)

    fun LivingEntity.getPotentCooldown(): Int = getAttachedOrElse(DETOX_COOLDOWN, 0)
    fun LivingEntity.setPotentCooldown(value: Int) = setAttached(DETOX_COOLDOWN, value)

    fun LivingEntity.clearPotent() {
        removeAttached(POTENT_DETERATOXIN)
        removeAttached(DETOX_COOLDOWN)
    }
    // endregion

    fun register(id: String): AttachmentType<Int> {
        return register(id) {
            it
                .persistent(Codec.INT)
                .syncWith(ByteBufCodecs.INT, targetOnly())
        }
    }

    fun <T : Any> register(id: String, builder: Consumer<AttachmentRegistry.Builder<T>>): AttachmentType<T> {
        return AttachmentRegistry.create(id(id), builder)
    }

}