package org.teamvoided.deteratoxin.entity

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MoverType
import net.minecraft.world.level.Level
import org.teamvoided.deteratoxin.init.DeteratoxinEffects
import org.teamvoided.deteratoxin.init.DeteratoxinEntities.DETERATOXIN_CLOUD
import org.teamvoided.deteratoxin.init.DeteratoxinParticles
import kotlin.math.min


class DeteratoxinCloudEntity : Entity {

    constructor(entityType: EntityType<out DeteratoxinCloudEntity>, level: Level) : super(entityType, level)

    constructor(level: Level) : super(DETERATOXIN_CLOUD, level)

    override fun defineSynchedData(builder: SynchedEntityData.Builder) = Unit

    override fun isNoGravity(): Boolean = true

    var maxAge = 200

    override fun tick() {
        if (tickCount == 1) {
            level().playSound(null, x, y, z, SoundEvents.AZALEA_LEAVES_HIT, SoundSource.PLAYERS, 2.0F, 0.7f)
        }
        if (tickCount % level().random.nextInt(1, 5) == 0) {
            level().playSound(
                null, x, y, z,
                SoundEvents.AZALEA_LEAVES_STEP, SoundSource.PLAYERS,
                0.05F, level().random.nextFloat() + 1.0f
            )
        }
        val height = type.dimensions.height / 2
        val width = type.dimensions.width / 2
        if (level().isClientSide) {
            clientTick(width, height)
        } else {
            if (tickCount >= maxAge) {
                discard()
            }
            val entities = level().getEntitiesOfClass(LivingEntity::class.java, boundingBox.move(0.0, 2.0, 0.0))
            for (entity in entities) {
                if (tickCount % EFFECT_COOLDOWN == 0) {
                    val lvl = entity.getEffect(DeteratoxinEffects.DETERATOXIN)?.amplifier ?: -1
                    entity.addEffect(MobEffectInstance(DeteratoxinEffects.DETERATOXIN, 100, min(lvl + 1, 9)))
                }
            }
        }
        move(MoverType.SELF, deltaMovement)
        super.tick()
    }

    fun clientTick(width: Float, height: Float) {
        repeat(3) {
            var xd = random.nextGaussian() * 0.05
            var yd = random.nextGaussian() * 0.05
            var zd = random.nextGaussian() * 0.05
            level().addParticle(DeteratoxinParticles.DETOX_POOF, x, y + 2, z, xd, yd, zd)
            xd = random.nextGaussian() * 0.05
            yd = random.nextGaussian() * 0.05
            zd = random.nextGaussian() * 0.05
            level().addParticle(DeteratoxinParticles.DETOX_POOF, x, y + 2, z, xd, yd, zd)
        }

        repeat(2) {
            val xOff = random.nextGaussian() * width * 0.5
            val yOff = random.nextGaussian() * height * 0.5
            val zOff = random.nextGaussian() * width * 0.5
            val xd = random.nextGaussian() * 0.01
            val yd = random.nextGaussian() * 0.01
            val zd = random.nextGaussian() * 0.01
            level().addParticle(
                DeteratoxinParticles.DETOX_POOF, x + xOff, y + 2 + yOff, z + zOff,
                xd, yd, zd
            )
        }
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        if (nbt.contains(KEY_MAX_AGE)) {
            maxAge = nbt.getInt(KEY_MAX_AGE)
        }
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        nbt.putInt(KEY_MAX_AGE, maxAge)
    }

    companion object {

        const val EFFECT_COOLDOWN = 30

        const val KEY_MAX_AGE = "max_age"

    }
}