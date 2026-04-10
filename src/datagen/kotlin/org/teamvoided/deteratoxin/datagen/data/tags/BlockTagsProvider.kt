package org.teamvoided.deteratoxin.datagen.data.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags
import net.minecraft.core.HolderLookup
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import org.teamvoided.deteratoxin.data.tags.DeteratoxinBlockTags
import org.teamvoided.deteratoxin.init.DeteratoxinBlocks
import java.util.concurrent.CompletableFuture

class BlockTagsProvider(o: FabricDataOutput, p: CompletableFuture<HolderLookup.Provider>) : BlockTagProvider(o, p) {

    override fun addTags(lookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(BlockTags.LOGS)
            .add(DeteratoxinBlocks.SPECIAL_APPLE_BLOCK)

        getOrCreateTagBuilder(DeteratoxinBlockTags.APPLE_LIKE)
            .add(DeteratoxinBlocks.APPLE_BLOCK, Blocks.RED_TERRACOTTA)

        getOrCreateTagBuilder(ConventionalBlockTags.STORAGE_BLOCKS)
            .forceAddTag(DeteratoxinBlockTags.APPLE_LIKE)
    }

}