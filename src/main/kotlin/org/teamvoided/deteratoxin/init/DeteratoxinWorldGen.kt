package org.teamvoided.deteratoxin.init

import net.fabricmc.fabric.api.biome.v1.BiomeModifications.create
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors.includeByKey
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors.tag
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.GenerationStep.Decoration
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import org.teamvoided.deteratoxin.Deteratoxin
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.util.key
import org.teamvoided.deteratoxin.worldgen.StarletteLilyFlower

object DeteratoxinWorldGen {

    val CAVE_SURFACE_PATCH = register("cave_surface_patch", StarletteLilyFlower(BlockStateConfiguration.CODEC))

    val FLOWER_CAVE = key("starlette_lily")
    val FLOWER_SURFACE = key("starlette_lily_dark_forest")
    fun init() {
        addVegetation("flower_cave", FLOWER_CAVE, BiomeTags.IS_OVERWORLD)
        addFeature("flower_surface", FLOWER_SURFACE, Biomes.DARK_FOREST)
    }

    internal fun addVegetation(id: String, feature: ResourceKey<PlacedFeature>, biome: TagKey<Biome>) {
        addFeature(id, Decoration.VEGETAL_DECORATION, feature, biome)
    }

    internal fun addFeature(id: String, step: GenerationStep.Decoration, feature: ResourceKey<PlacedFeature>, tag: TagKey<Biome>) {
        create(id("add_$id")).add(ModificationPhase.ADDITIONS, tag(tag)) {
            it.generationSettings.addFeature(step, feature)
        }
    }

    internal fun addFeature(id: String, feature: ResourceKey<PlacedFeature>, biome: ResourceKey<Biome>) {
        create(id("add_$id")).add(ModificationPhase.ADDITIONS, includeByKey(biome)) {
            it.generationSettings.addFeature(Decoration.VEGETAL_DECORATION, feature)
        }
    }

    private fun <C : FeatureConfiguration, F : Feature<C>> register(name: String, feature: F): F =
        Registry.register(BuiltInRegistries.FEATURE, Deteratoxin.id(name), feature)

    fun key(id: String) = Registries.PLACED_FEATURE.key(id(id))
}