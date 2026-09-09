package org.teamvoided.deteratoxin.init

import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.item.KnifeIngredient

object DeteratoxinRegistries {

    val KNIFE_INGREDIENT = key<KnifeIngredient>("knife_ingredient")

    fun init() {
        DynamicRegistries.registerSynced(KNIFE_INGREDIENT, KnifeIngredient.DIRECT_CODEC)
    }

    fun <T : Any> key(id: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey<T>(id(id))

}