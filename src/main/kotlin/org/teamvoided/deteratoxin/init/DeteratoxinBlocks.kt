package org.teamvoided.deteratoxin.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.block.StarletteLilyBlock
import org.teamvoided.deteratoxin.util.getModEntries
import org.teamvoided.deteratoxin.util.register

object DeteratoxinBlocks {

    val BLOCKS get() = getModEntries(BuiltInRegistries.BLOCK)

    val STARLETTE_LILY = register(
        "starlette_lily",
        StarletteLilyBlock(ofFullCopy(Blocks.SPORE_BLOSSOM).emissiveRendering(Blocks::always).randomTicks().lightLevel { 2 })
    )


    fun init() = Unit

    fun register(id: String, block: Block, properties: Item.Properties = Item.Properties()): Block {
        val registeredBlock = registerNoItem(id, block)
        DeteratoxinItems.register(id, BlockItem(registeredBlock, properties))
        return registeredBlock
    }

    fun registerNoItem(id: String, block: Block): Block = BuiltInRegistries.BLOCK.register(id(id), block)

}