package org.teamvoided.deteratoxin.datagen.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Items.*
import net.minecraft.world.level.ItemLike
import org.teamvoided.deteratoxin.init.DeteratoxinItems.DETERATOXIN_EXTRACT
import org.teamvoided.deteratoxin.init.DeteratoxinItems.KNIFE
import org.teamvoided.deteratoxin.init.DeteratoxinItems.POTENT_DETERATOXIN_EXTRACT
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(o: FabricDataOutput, p: CompletableFuture<HolderLookup.Provider>) :
    FabricRecipeProvider(o, p) {

    override fun buildRecipes(output: RecipeOutput) {
        shaped(KNIFE)
            .pattern(" II")
            .pattern("IGP")
            .pattern("SS ")
            .define('I', IRON_INGOT)
            .define('G', GLASS_BOTTLE)
            .define('P', GLASS_PANE)
            .define('S', NETHERITE_SCRAP)
            .unlockedBy(getHasName(NETHERITE_SCRAP), has(NETHERITE_SCRAP))
            .save(output)
        shaped(POTENT_DETERATOXIN_EXTRACT)
            .pattern("DDD")
            .pattern("DLD")
            .pattern("DDD")
            .define('D', DETERATOXIN_EXTRACT)
            .define('L', LILY_OF_THE_VALLEY)
            .unlockedBy(getHasName(DETERATOXIN_EXTRACT), has(DETERATOXIN_EXTRACT))
            .save(output)
    }

    fun shaped(
        result: ItemLike, category: RecipeCategory = RecipeCategory.MISC, count: Int = 1,
    ): ShapedRecipeBuilder {
        return ShapedRecipeBuilder.shaped(category, result, count)
    }

}