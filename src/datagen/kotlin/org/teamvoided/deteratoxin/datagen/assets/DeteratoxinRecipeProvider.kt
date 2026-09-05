package org.teamvoided.deteratoxin.datagen.assets

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.*
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import org.teamvoided.deteratoxin.init.DeteratoxinItems
import java.util.concurrent.CompletableFuture

class DeteratoxinRecipeProvider(
    output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, registriesFuture) {
    override fun buildRecipes(exporter: RecipeOutput) = with(exporter) {
        generateCraftingRecipes()


    }

    private fun RecipeOutput.generateCraftingRecipes() {
        shaped(DeteratoxinItems.KNIFE)
            .pattern(" II")
            .pattern("IGP")
            .pattern("SS ")
            .define('I', Items.IRON_INGOT)
            .define('G', Items.GLASS_BOTTLE)
            .define('P', Items.GLASS_PANE)
            .define('S', Items.NETHERITE_SCRAP)
            .unlockedBy(getHasName(Items.NETHERITE_SCRAP), has(Items.NETHERITE_SCRAP))
            .save(this)

        shaped(DeteratoxinItems.POTENT_DETERATOXIN_EXTRACT)
            .pattern("DDD")
            .pattern("DLD")
            .pattern("DDD")
            .define('D', DeteratoxinItems.DETERATOXIN_EXTRACT)
            .define('L', Items.LILY_OF_THE_VALLEY)
            .unlockedBy(getHasName(DeteratoxinItems.DETERATOXIN_EXTRACT), has(DeteratoxinItems.DETERATOXIN_EXTRACT))
            .save(this)
    }
    fun RecipeBuilder.itemCriterion(item: ItemLike): RecipeBuilder =
        this.unlockedBy(getHasName(item), has(item))

    private fun shaped(
        result: ItemLike,
        category: RecipeCategory = RecipeCategory.MISC,
        count: Int = 1
    ): ShapedRecipeBuilder {
        return ShapedRecipeBuilder(category, result, count)
    }
    private fun simpleShapeless(
        result: ItemLike,
        category: RecipeCategory = RecipeCategory.MISC,
        count: Int = 1,
        ingredients: List<ItemLike>
    ): ShapelessRecipeBuilder {
        val factory = ShapelessRecipeBuilder(category, result, count)
        ingredients.forEach(factory::requires)
        ingredients.toSet().forEach {
            factory.unlockedBy(getHasName(it), has(it))
        }

        return factory
    }

    val Item.id get() = BuiltInRegistries.ITEM.getKey(this)
}