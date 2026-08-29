package org.teamvoided.deteratoxin.init

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.Registry
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.entity.DeteratoxinCloudEntity

object DeteratoxinEntities {
    fun init() = Unit

    val DETERATOXIN_CLOUD = register(
        "deteratoxin_cloud",
        EntityType.Builder.of(EntityType.EntityFactory(::DeteratoxinCloudEntity), MobCategory.MISC)
            .sized(4f, 4f).clientTrackingRange(4).build()
    )

    private fun <T : Entity> register(path: String, entry: EntityType<T>): EntityType<T> {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id(path), entry)
    }
}