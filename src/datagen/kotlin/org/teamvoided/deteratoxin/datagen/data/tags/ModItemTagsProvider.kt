package org.teamvoided.deteratoxin.datagen.data.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.init.DeteratoxinItems
import org.teamvoided.deteratoxin.util.tag
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
    o: FabricDataOutput, p: CompletableFuture<HolderLookup.Provider>, blockTag: BlockTagProvider,
) : ItemTagProvider(o, p, blockTag) {

    override fun addTags(lookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(ConventionalItemTags.MELEE_WEAPON_TOOLS)
            .add(DeteratoxinItems.KNIFE)
        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE)
            .add(DeteratoxinItems.KNIFE)
        getOrCreateTagBuilder(createTag(id("farmersdelight", "enchantable/knife")))
            .add(DeteratoxinItems.KNIFE)
    }

    fun createTag(id: ResourceLocation) = Registries.ITEM.tag(id)

}