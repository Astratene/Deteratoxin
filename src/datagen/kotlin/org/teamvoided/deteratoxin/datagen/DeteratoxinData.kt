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
import org.teamvoided.deteratoxin.datagen.assets.ModEnLangProvider
import org.teamvoided.deteratoxin.datagen.assets.ModModelProvider
import org.teamvoided.deteratoxin.datagen.data.ModRecipeProvider
import org.teamvoided.deteratoxin.datagen.data.registry.ModDamageTypes
import org.teamvoided.deteratoxin.datagen.data.registry.ModKnifeIngredients
import org.teamvoided.deteratoxin.datagen.data.tags.ModBlockTagsProvider
import org.teamvoided.deteratoxin.datagen.data.tags.ModDamageTypeTagProvider
import org.teamvoided.deteratoxin.datagen.data.tags.ModItemTagsProvider
import org.teamvoided.deteratoxin.init.DeteratoxinRegistries
import java.util.concurrent.CompletableFuture

object DeteratoxinData : DataGeneratorEntrypoint {

    override fun getEffectiveModId(): String = Deteratoxin.MODID

    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()
        log.info("Running \"${gen.modContainer.metadata.name}\" Datagen!")

        // Assets
        pack.addProvider(::ModEnLangProvider)
        pack.addProvider(::ModModelProvider)
        // Data
        pack.addProvider(::RegistryProvider)
        val blockTags = pack.addProvider(::ModBlockTagsProvider)
        pack.addProvider { o, p -> ModItemTagsProvider(o, p, blockTags) }
        pack.addProvider(::ModDamageTypeTagProvider)
        pack.addProvider(::ModRecipeProvider)
    }

    override fun buildRegistry(gen: RegistrySetBuilder) {
        gen.add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap)
        gen.add(DeteratoxinRegistries.KNIFE_INGREDIENT, ModKnifeIngredients::bootstrap)
    }

    class RegistryProvider(o: FabricDataOutput, p: CompletableFuture<HolderLookup.Provider>) :
        FabricDynamicRegistryProvider(o, p) {

        override fun getName(): String = "Registry Gen"

        override fun configure(provider: HolderLookup.Provider, entries: Entries) {
            entries.addAll(provider.lookupOrThrow(Registries.DAMAGE_TYPE))
            entries.addEverything(provider.lookupOrThrow(DeteratoxinRegistries.KNIFE_INGREDIENT))
        }

        fun <T : Any> Entries.addEverything(registry: RegistryLookup<T>): MutableList<Holder<T>> {
            return registry.listElementIds().map { add(registry, it) }.toList().toMutableList()
        }

    }

}