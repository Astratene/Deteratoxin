package org.teamvoided.deteratoxin.datagen.assets

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import org.teamvoided.deteratoxin.util.getModHolders
import java.util.concurrent.CompletableFuture

class EnLangProvider(var output: FabricDataOutput, p: CompletableFuture<HolderLookup.Provider>) :
    FabricLanguageProvider(output, p) {

    override fun generateTranslations(lookup: HolderLookup.Provider, gen: TranslationBuilder) {
        getModHolders(BuiltInRegistries.ITEM).forEach {
            gen.add(it.value(), genLang(it.key().location()))
        }
        getModHolders(BuiltInRegistries.BLOCK).forEach {
            trySafe { gen.add(it.value(), genLang(it.key().location())) }
        }

        gen.add(
            "tooltip.deteratoxin.empty.tooltip",
            "Right click with a potion or material to apply its effect."
        )
        gen.add(
            "tooltip.deteratoxin.instructionToReady.tooltip",
            "Right click in your hand to ready the knife"
        )
        gen.add(
            "tooltip.deteratoxin.instructionToStab.tooltip",
            "Attack someone to inject contents into them, right click to inject them into yourself."
        )
        gen.add(
            "tooltip.deteratoxin.currentEffect.tooltip",
            "Current Effect: %s %s (%s)"
        )
        gen.add(
            "tooltip.deteratoxin.currentInstantEffect.tooltip",
            "Current Effect: %s. uses: %s"
        )
        gen.add("death.attack.stabbed", "%s drove their knife too deep")

        gen.add("effect.deteratoxin.weak_deteratoxin", "deteratoxin")
    }

    private fun genLang(id: ResourceLocation): String =
        id.path.split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }


    fun trySafe(fn: () -> Unit) {
        try {
            fn()
        } catch (e: Exception) {
            if (output.isStrictValidationEnabled) {
                LOGGER.warn("Exception found when lang gen: ", e)
            }
        }
    }

}