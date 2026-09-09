package org.teamvoided.deteratoxin.init

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.entity.DeteratoxinCloudEntity

object DeteratoxinEntities {

    val DETERATOXIN_CLOUD = register(
        "deteratoxin_cloud",
        EntityType.Builder.of(::DeteratoxinCloudEntity, MobCategory.MISC)
            .sized(4f, 4f)
            .clientTrackingRange(4)
            .build()
    )

    fun init() = Unit

    fun <T : Entity> register(name: String, entry: EntityType<T>): EntityType<T> {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id(name), entry)
    }

}