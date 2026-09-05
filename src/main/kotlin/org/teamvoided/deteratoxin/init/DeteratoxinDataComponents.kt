package org.teamvoided.deteratoxin.init

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.effect.MobEffect.CODEC
import net.minecraft.world.effect.MobEffect.STREAM_CODEC
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.components.KnifeData
import org.teamvoided.deteratoxin.util.register

object DeteratoxinDataComponents {
    fun init() = Unit

    val KNIFE_DATA = register("knife_data") {
        it
            .persistent(KnifeData.CODEC)
            .networkSynchronized(KnifeData.STREAM_CODEC)
            .build()
    }
    val KNIFE_POTION_DURATION = registerInt("knife_potion_duration")
    val KNIFE_POTION_AMPLIFIER = registerInt("knife_potion_amplifier")
    val KNIFE_POTION_EFFECT = register("knife_potion_effect") {
        it
            .persistent(CODEC)
            .networkSynchronized(STREAM_CODEC)
            .build()
    }

    fun registerInt(name: String): DataComponentType<Int> {
        return register(name) {
            it
                .persistent(ExtraCodecs.NON_NEGATIVE_INT)
                .networkSynchronized(ByteBufCodecs.VAR_INT)
                .build()
        }
    }
    fun <T> register(
        name: String, build: (DataComponentType.Builder<T>) -> DataComponentType<T>,
    ): DataComponentType<T> {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.register(id(name), build(DataComponentType.builder()))
    }

}