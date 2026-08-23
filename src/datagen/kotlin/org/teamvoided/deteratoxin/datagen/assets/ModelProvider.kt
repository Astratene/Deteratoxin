package org.teamvoided.deteratoxin.datagen.assets


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TexturedModel
import org.teamvoided.deteratoxin.init.DeteratoxinBlocks
import org.teamvoided.deteratoxin.init.DeteratoxinItems

class ModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {

    override fun generateBlockStateModels(gen: BlockModelGenerators) {
//        gen.createTrivialCube(DeteratoxinBlocks.APPLE_BLOCK)
//        gen.createTrivialBlock(DeteratoxinBlocks.SPECIAL_APPLE_BLOCK, TexturedModel.LEAVES)
    }

    val single = listOf(DeteratoxinItems.KNIFE)

    override fun generateItemModels(gen: ItemModelGenerators) {
        single.forEach {
            //gen.generateFlatItem(it, ModelTemplates.FLAT_HANDHELD_ITEM)
        }
    }

}