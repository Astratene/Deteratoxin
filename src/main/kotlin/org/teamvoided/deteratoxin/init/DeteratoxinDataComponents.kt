package org.teamvoided.deteratoxin.init

import com.mojang.serialization.Codec
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.effect.MobEffect
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.components.KnifeData
import org.teamvoided.deteratoxin.components.SimpleStorageComponent

object DeteratoxinDataComponents {
    fun init() = Unit

    val KNIFE_DATA = registerSimple("knife_data", KnifeData.CODEC)
    val KNIFE_POTION_DURATION = registerInt("knife_potion_duration")
    val KNIFE_POTION_AMPLIFIER = registerInt("knife_potion_amplifier")
    val KNIFE_POTION_EFFECT = registerEffect("knife_potion_effect")


    fun <T : SimpleStorageComponent> registerSimple(name: String, codec: Codec<T>): DataComponentType<T> =
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id(name), DataComponentType.builder<T>().persistent(codec).build())

    fun <T> register(
        name: String, build: (DataComponentType.Builder<T>) -> DataComponentType<T>
    ): DataComponentType<T> =
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id(name), build(DataComponentType.builder()))

    fun registerInt(name: String): DataComponentType<Int> =
        DeteratoxinDataComponents.register(name) {
            it
                .persistent(ExtraCodecs.NON_NEGATIVE_INT)
                .networkSynchronized(ByteBufCodecs.VAR_INT)
                .build()
        }

    fun registerEffect(name: String): DataComponentType<Holder<MobEffect>> =
        DeteratoxinDataComponents.register(name) {
            it
                .persistent(MobEffect.CODEC)
                .networkSynchronized(MobEffect.STREAM_CODEC)
                .build()
        }
}