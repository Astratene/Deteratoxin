package org.teamvoided.deteratoxin.components

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.READY_TIME

// (ender) this still should stay and object to prevent item animation when updating values
data class ReadyTime(val ticks: Int) {

    fun readied(): Boolean {
        return ticks > 0
    }

    // this part fixes the constant ticking caused by this data
    override fun equals(other: Any?): Boolean {
        return other is ReadyTime || super.equals(other)
    }

    override fun hashCode(): Int = ticks

    companion object {

        val DEFAULT = ReadyTime(0)

        fun ItemStack.readied(): Boolean = getOrDefault(READY_TIME, DEFAULT).readied()
        fun ItemStack.setReadyTicks(value: Int?) {
            set(READY_TIME, value?.let { ReadyTime(value) })
        }

        val CODEC: Codec<ReadyTime> = Codec.INT.xmap(::ReadyTime, ReadyTime::ticks)
        val STREAM_CODEC: StreamCodec<ByteBuf, ReadyTime> =
            StreamCodec.composite(ByteBufCodecs.INT, ReadyTime::ticks, ::ReadyTime)

    }
}

// getter:
// val data = stack.getOrDefault(DeteratoxinDataComponents.READY_TIME, ReadyTime.DEFAULT)

// setter:
// stack.set(DeteratoxinDataComponents.READY_TIME, DataComponent(value1))

// value1 must always be an integer(1, 2, 3 etc. no decimals)