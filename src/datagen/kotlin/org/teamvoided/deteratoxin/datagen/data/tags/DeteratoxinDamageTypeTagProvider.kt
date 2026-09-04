package org.teamvoided.deteratoxin.datagen.data.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageType
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes
import java.util.concurrent.CompletableFuture

class DeteratoxinDamageTypeTagProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<DamageType>(o, Registries.DAMAGE_TYPE, r) {

    override fun addTags(wrapperLookup: HolderLookup.Provider) {
        tag(DamageTypeTags.BYPASSES_COOLDOWN)
            .add(DeteratoxinDamageTypes.DETERIORATED)

        tag(DamageTypeTags.NO_KNOCKBACK)
            .add(DeteratoxinDamageTypes.DETERIORATED)

        tag(DamageTypeTags.BYPASSES_SHIELD)
            .add(DeteratoxinDamageTypes.DETERIORATED)

        tag(DamageTypeTags.BYPASSES_ARMOR)
            .add(DeteratoxinDamageTypes.DETERIORATED)

        tag(DamageTypeTags.BYPASSES_ENCHANTMENTS)
            .add(DeteratoxinDamageTypes.DETERIORATED)

        tag(DamageTypeTags.BYPASSES_EFFECTS)
            .add(DeteratoxinDamageTypes.DETERIORATED)
    }
}