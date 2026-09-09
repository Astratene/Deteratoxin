package org.teamvoided.deteratoxin.init

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.components.ImbuedEffect
import org.teamvoided.deteratoxin.components.KnifeData
import org.teamvoided.deteratoxin.util.register

object DeteratoxinDataComponents {

    // TODO make be called only in KnifeData.class
    val KNIFE_DATA = register("knife_data") {
        it
            .persistent(KnifeData.CODEC)
            .networkSynchronized(KnifeData.STREAM_CODEC)
            .build()
    }

    val IMBUED_EFFECT = register("imbued_effect") {
        it
            .persistent(ImbuedEffect.CODEC)
            .networkSynchronized(ImbuedEffect.STREAM_CODEC)
            .build()
    }

    fun init() = Unit

    fun <T> register(
        name: String, build: (DataComponentType.Builder<T>) -> DataComponentType<T>,
    ): DataComponentType<T> {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.register(id(name), build(DataComponentType.builder()))
    }

}