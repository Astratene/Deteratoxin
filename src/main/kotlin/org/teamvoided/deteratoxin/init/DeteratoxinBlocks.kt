package org.teamvoided.deteratoxin.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.block.StarletteLilyBlock
import org.teamvoided.deteratoxin.util.key
import org.teamvoided.deteratoxin.util.register
import java.util.function.Function

object DeteratoxinBlocks {

    val STARLETTE_LILY = register(
        "starlette_lily", ::StarletteLilyBlock,
        ofFullCopy(Blocks.SPORE_BLOSSOM)
            .emissiveRendering(Blocks::always)
            .randomTicks()
            .lightLevel { 2 }
    )

    fun init() = Unit

    fun <T : Block> register(id: String, block: Function<Properties, T>, properties: Properties): T {
        val regBlock = registerNoItem(id, block, properties)
        DeteratoxinItems.register(id, { BlockItem(regBlock, it) })
        return regBlock
    }

    fun <T : Block> registerNoItem(name: String, block: Function<Properties, T>, properties: Properties): T {
        val id = Registries.BLOCK.key(id(name))
//        properties.setId(id) // 1.21.11 code
        return BuiltInRegistries.BLOCK.register(id.location(), block.apply(properties))
    }

}