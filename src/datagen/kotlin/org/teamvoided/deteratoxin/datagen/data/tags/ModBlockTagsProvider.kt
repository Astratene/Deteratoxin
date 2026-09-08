package org.teamvoided.deteratoxin.datagen.data.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.tags.BlockTags
import org.teamvoided.deteratoxin.data.tags.DeteratoxinBlockTags
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(o: FabricDataOutput, p: CompletableFuture<HolderLookup.Provider>) : BlockTagProvider(o, p) {

    override fun addTags(lookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(DeteratoxinBlockTags.LILY_PLACABLE)
            .forceAddTag(BlockTags.SCULK_REPLACEABLE_WORLD_GEN)
            .forceAddTag(BlockTags.LOGS)
    }

}