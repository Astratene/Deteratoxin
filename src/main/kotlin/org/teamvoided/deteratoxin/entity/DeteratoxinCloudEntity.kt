package org.teamvoided.deteratoxin.entity

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MoverType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import org.teamvoided.deteratoxin.init.DeteratoxinEffects
import org.teamvoided.deteratoxin.init.DeteratoxinEntities.DETERATOXIN_CLOUD
import org.teamvoided.deteratoxin.init.DeteratoxinParticles
import kotlin.math.min

class DeteratoxinCloudEntity : Entity {


    constructor(entityType: EntityType<out DeteratoxinCloudEntity?>?, world: Level?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: Level?) :
            super(DETERATOXIN_CLOUD as EntityType<out Entity?>, world) {
    }

    val bigWidth = 2.0
    val bigHeight = 2.0
    var cloudLifespan = 200

    override fun tick() {
        if (this.tickCount == 1) {
            this.level().playSound(
                null,
                this.x,
                this.y,
                this.z,
                SoundEvents.AZALEA_LEAVES_HIT,
                SoundSource.PLAYERS,
                2.0F,
                0.7f
            )
        }
        if (this.tickCount % level().random.nextInt(1, 5) == 0) {
            this.level().playSound(
                null,
                this.x,
                this.y,
                this.z,
                SoundEvents.AZALEA_LEAVES_STEP,
                SoundSource.PLAYERS,
                0.05F,
                level().random.nextFloat() + 1.0f
            )
        }
        val height = bigHeight
        val width = bigWidth
        if (level() is ServerLevel) {
            val world = level() as ServerLevel
            world.sendParticles(
                DeteratoxinParticles.DETOX_POOF, this.x, this.y + 2, this.z,
                3,
                0.0, 0.0, 0.0,
//            width, height, width,
                0.05
            )
            world.sendParticles(
                DeteratoxinParticles.DETOX_POOF, this.x, this.y + 2, this.z,
                3,
                0.0, 0.0, 0.0,
//            width, height, width,
                0.08
            )
            world.sendParticles(
                DeteratoxinParticles.DETOX_POOF, this.x, this.y + 2, this.z,
                2,
                width * 0.5, height * 0.5, width * 0.5,
                0.01
            )
        }
        if (this.tickCount >= cloudLifespan) {
            this.discard()
        } else {
            val entities = mutableListOf<Entity>()
            entities.addAll(
                level().getEntities(
                    this, AABB(
                        this.x + width,
                        this.y + 2 + height,
                        this.z + width,
                        this.x - width,
                        this.y + 2 - height,
                        this.z - width
                    )
                )
                    .filter { it is LivingEntity }
            )
            for (entity in entities) {
                if (entity is LivingEntity && this.tickCount % 30 == 0) {
                    val shred = entity.getEffect(DeteratoxinEffects.DETERATOXIN)
                    val lvl = shred?.amplifier ?: -1
                    entity.addEffect(
                        MobEffectInstance(
                            DeteratoxinEffects.DETERATOXIN,
                            100, min(lvl + 1, 9),
                            false, true, true
                        )
                    )
                }
            }
        }
        this.move(MoverType.SELF, this.deltaMovement)
        super.tick()
    }


    override fun defineSynchedData(builder: SynchedEntityData.Builder?) {
    }

    override fun isNoGravity(): Boolean {
        return true
    }

    override fun readAdditionalSaveData(nbt: CompoundTag?) {
    }

    override fun addAdditionalSaveData(nbt: CompoundTag?) {
    }
}