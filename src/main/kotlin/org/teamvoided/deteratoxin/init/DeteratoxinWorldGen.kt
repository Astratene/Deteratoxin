package org.teamvoided.deteratoxin.init

import net.fabricmc.fabric.api.biome.v1.BiomeModifications.create
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors.includeByKey
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors.tag
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.levelgen.GenerationStep.Decoration
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.util.key
import org.teamvoided.deteratoxin.worldgen.StarletteLilyFlower
import java.util.function.Predicate

object DeteratoxinWorldGen {

    val CAVE_SURFACE_PATCH = register("cave_surface_patch", StarletteLilyFlower(BlockStateConfiguration.CODEC))

    fun <C : FeatureConfiguration, F : Feature<C>> register(name: String, feature: F): F {
        return Registry.register(BuiltInRegistries.FEATURE, id(name), feature)
    }

    val FLOWER_CAVE = key("starlette_lily")
    val FLOWER_SURFACE = key("starlette_lily_dark_forest")

    fun key(id: String) = Registries.PLACED_FEATURE.key(id(id))

    fun init() {
        addFeature("flower_cave", FLOWER_CAVE, tag(BiomeTags.IS_OVERWORLD))
        addFeature("flower_surface", FLOWER_SURFACE, includeByKey(Biomes.DARK_FOREST))
    }

    fun addFeature(id: String, feature: ResourceKey<PlacedFeature>, predicate: Predicate<BiomeSelectionContext>) {
        create(id("add_$id")).add(ModificationPhase.ADDITIONS, predicate) {
            it.generationSettings.addFeature(Decoration.VEGETAL_DECORATION, feature)
        }
    }

}