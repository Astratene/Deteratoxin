package org.teamvoided.deteratoxin.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.KNIFE_DATA

// (ender) this still should stay and object to prevent item animation when ticking
data class KnifeData(val readyTime: Int) {

    fun readied(): Boolean {
        return readyTime > 0
    }

    // this part fixes the constant ticking caused by this data
    override fun equals(other: Any?): Boolean {
        return other is KnifeData || super.equals(other)
    }

    override fun hashCode(): Int = readyTime

    companion object {

        val DEFAULT: KnifeData = KnifeData(0)

        fun ItemStack.getKnifeData(): KnifeData = getOrDefault(KNIFE_DATA, DEFAULT)!!
        fun ItemStack.setKnifeData(value: Int?) {
            set(KNIFE_DATA, value?.let { KnifeData(value) })
        }

        val CODEC: Codec<KnifeData> = RecordCodecBuilder.create { inst ->
            inst
                .group(Codec.INT.fieldOf("ready_time").forGetter { it.readyTime })
                .apply(inst, ::KnifeData)
        }
        val STREAM_CODEC: StreamCodec<ByteBuf, KnifeData> =
            StreamCodec.composite(ByteBufCodecs.INT, { it.readyTime }, ::KnifeData)
    }
}

// getter:
// val data = stack.getOrDefault(DeteratoxinDataComponents.KNIFE_DATA, KnifeDataComponent.DEFAULT)

// setter:
// stack.set(DeteratoxinDataComponents.KNIFE_DATA, DataComponent(value1, value2))

// value1 must be a boolean(true/false) and value 2 must always be an integer(1, 2, 3 etc. no decimals)