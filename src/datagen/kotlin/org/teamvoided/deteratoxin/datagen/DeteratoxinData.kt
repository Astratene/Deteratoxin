package org.teamvoided.deteratoxin.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderLookup.RegistryLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import org.teamvoided.deteratoxin.Deteratoxin
import org.teamvoided.deteratoxin.Deteratoxin.log
import org.teamvoided.deteratoxin.datagen.assets.EnLangProvider
import org.teamvoided.deteratoxin.datagen.assets.ModelProvider
import org.teamvoided.deteratoxin.datagen.data.RecipeProvider
import org.teamvoided.deteratoxin.datagen.data.registry.DamageTypes
import org.teamvoided.deteratoxin.datagen.data.registry.KnifeIngredients
import org.teamvoided.deteratoxin.datagen.data.registry.Paintings
import org.teamvoided.deteratoxin.datagen.data.tags.BlockTagsProvider
import org.teamvoided.deteratoxin.datagen.data.tags.ItemTagsProvider
import org.teamvoided.deteratoxin.init.DeteratoxinRegistries
import java.util.concurrent.CompletableFuture

object DeteratoxinData : DataGeneratorEntrypoint {

    override fun getEffectiveModId(): String = Deteratoxin.MODID

    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()
        log.info("Running \"${gen.modContainer.metadata.name}\" Datagen!")

        // Assets
        pack.addProvider(::EnLangProvider)
        pack.addProvider(::ModelProvider)
        // Data
        pack.addProvider(::RegistryProvider)
        val blockTags = pack.addProvider(::BlockTagsProvider)
        pack.addProvider { o, f -> ItemTagsProvider(o, f, blockTags) }
        pack.addProvider(::RecipeProvider)

    }

    override fun buildRegistry(gen: RegistrySetBuilder) {
        gen.add(Registries.PAINTING_VARIANT, Paintings::bootstrap)
        gen.add(Registries.DAMAGE_TYPE, DamageTypes::bootstrap)
        gen.add(DeteratoxinRegistries.KNIFE_INGREDIENT, KnifeIngredients::bootstrap)
    }

    class RegistryProvider(o: FabricDataOutput, p: CompletableFuture<HolderLookup.Provider>) :
        FabricDynamicRegistryProvider(o, p) {

        override fun getName(): String = "Registry Gen"

        override fun configure(provider: HolderLookup.Provider, entries: Entries) {
            entries.addAll(provider.lookupOrThrow(Registries.PAINTING_VARIANT))
            entries.addAll(provider.lookupOrThrow(Registries.DAMAGE_TYPE))
            entries.addEverything(provider.lookupOrThrow(DeteratoxinRegistries.KNIFE_INGREDIENT))
        }

        fun <T : Any> Entries.addEverything(registry: RegistryLookup<T>): MutableList<Holder<T>> {
            return registry.listElementIds().map { add(registry, it) }.toList()
        }

    }
}