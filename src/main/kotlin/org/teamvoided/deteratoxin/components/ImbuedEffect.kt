package org.teamvoided.deteratoxin.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import org.teamvoided.deteratoxin.item.KnifeIngredient

data class ImbuedEffect(var effect: Holder<MobEffect>, var duration: Int, var amplifier: Int) {

    constructor(ingredient: KnifeIngredient) : this(ingredient.effect, ingredient.duration, ingredient.amplifier)
    constructor(effectInstance: MobEffectInstance) : this(effectInstance.effect, effectInstance.duration, effectInstance.amplifier)

    fun asEffectInstance(): MobEffectInstance {
        return MobEffectInstance(effect, duration, amplifier)
    }

    fun copy(): ImbuedEffect {
        return ImbuedEffect(effect, duration, amplifier)
    }

    fun isInstantenous() = effect.value().isInstantenous

    companion object {

        val CODEC: Codec<ImbuedEffect> = RecordCodecBuilder.create { inst ->
            inst
                .group(
                    MobEffect.CODEC.fieldOf("effect").forGetter { it.effect },
                    Codec.INT.fieldOf("duration").forGetter { it.duration },
                    ExtraCodecs.intRange(0, 255).fieldOf("amplifier").forGetter { it.amplifier },
                )
                .apply(inst, ::ImbuedEffect)
        }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ImbuedEffect> = StreamCodec.composite(
            MobEffect.STREAM_CODEC, ImbuedEffect::effect,
            ByteBufCodecs.INT, ImbuedEffect::duration,
            ByteBufCodecs.INT, ImbuedEffect::amplifier,
            ::ImbuedEffect
        )

    }
}
