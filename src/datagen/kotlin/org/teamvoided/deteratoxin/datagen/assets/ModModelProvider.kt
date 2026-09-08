package org.teamvoided.deteratoxin.datagen.assets


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelTemplates
import org.teamvoided.deteratoxin.init.DeteratoxinItems

class ModModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {

    override fun generateBlockStateModels(gen: BlockModelGenerators) = Unit

    val single = listOf(
        DeteratoxinItems.DETERATOXIN_EXTRACT,
        DeteratoxinItems.POTENT_DETERATOXIN_EXTRACT
    )

    override fun generateItemModels(gen: ItemModelGenerators) {
        for (item in single) {
            gen.generateFlatItem(item, ModelTemplates.FLAT_ITEM)
        }
    }

}