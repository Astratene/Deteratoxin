package org.teamvoided.deteratoxin.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.item.Item
import org.teamvoided.deteratoxin.init.DeteratoxinRegistries
import org.teamvoided.deteratoxin.util.key

data class KnifeIngredient(
    val effect: Holder<MobEffect>,
    val duration: Int,
    val amplifier: Int
) {

    companion object {

        val DIRECT_CODEC: Codec<KnifeIngredient> = RecordCodecBuilder.create { inst ->
            inst
                .group(
                    MobEffect.CODEC.fieldOf("effect").forGetter { it.effect },
                    ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter { it.duration },
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amplifier").forGetter { it.amplifier },
                )
                .apply(inst, ::KnifeIngredient)
        }

        val DIRECT_STREAM_CODEC =  StreamCodec.composite(
            MobEffect.STREAM_CODEC,KnifeIngredient::effect,
            ByteBufCodecs.INT, KnifeIngredient::duration,
            ByteBufCodecs.INT, KnifeIngredient::amplifier,
            ::KnifeIngredient
        )

        fun getKey(ingredient: Item): ResourceKey<KnifeIngredient> {
            return DeteratoxinRegistries.KNIFE_INGREDIENT.key(BuiltInRegistries.ITEM.getKey(ingredient))
        }
    }


}
