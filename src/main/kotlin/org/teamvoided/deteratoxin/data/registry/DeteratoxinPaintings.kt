package org.teamvoided.deteratoxin.data.registry

import net.minecraft.core.registries.Registries
import org.teamvoided.deteratoxin.Deteratoxin
import org.teamvoided.deteratoxin.util.key

object DeteratoxinPaintings {

    val APPLE_PAINT = create("apple_paint")

    fun create(id: String) = Registries.PAINTING_VARIANT.key(Deteratoxin.id(id))

}