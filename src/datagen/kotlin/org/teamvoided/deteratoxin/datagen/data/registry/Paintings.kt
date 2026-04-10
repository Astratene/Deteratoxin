package org.teamvoided.deteratoxin.datagen.data.registry

import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.decoration.PaintingVariant
import org.teamvoided.deteratoxin.data.registry.DeteratoxinPaintings

object Paintings : RegistryBootstrapper<PaintingVariant> {

    override fun BootstrapContext<PaintingVariant>.init() {
        painting(DeteratoxinPaintings.APPLE_PAINT, 1, 1)
    }

    fun BootstrapContext<PaintingVariant>.painting(key: ResourceKey<PaintingVariant>, width: Int, height: Int) {
        register(key, PaintingVariant(width, height, key.location()))
    }

}