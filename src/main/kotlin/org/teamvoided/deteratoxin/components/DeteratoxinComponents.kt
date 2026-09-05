package org.teamvoided.deteratoxin.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class KnifeData(val readied: Boolean, val readyTime: Int) : SimpleStorageComponent {

    // this part fixes the constant ticking caused by this data
    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is KnifeData) super.equals(other)
        else true
    }
    // this is the end of it

    companion object {
        val DEFAULT: KnifeData = KnifeData(false, 0)
        val CODEC: Codec<KnifeData> = RecordCodecBuilder.create<KnifeData> { builder ->
            builder.group(
                Codec.BOOL.fieldOf("readied").forGetter { it.readied },
                Codec.INT.fieldOf("ready_time").forGetter { it.readyTime }
            ).apply(builder, ::KnifeData)
        }
        val STREAM_CODEC: StreamCodec<ByteBuf, KnifeData> = StreamCodec.composite(
            ByteBufCodecs.BOOL, { it.readied },
            ByteBufCodecs.INT, { it.readyTime },
            ::KnifeData
        )
    }
}

// getter:
// val data = stack.getOrDefault(DeteratoxinDataComponents.KNIFE_DATA, KnifeDataComponent.DEFAULT)

// setter:
// stack.set(DeteratoxinDataComponents.KnifeData, DataComponent(value1, value2))

// value1 must be a boolean(true/false) and value 2 must always be an integer(1, 2, 3 etc. no decimals)