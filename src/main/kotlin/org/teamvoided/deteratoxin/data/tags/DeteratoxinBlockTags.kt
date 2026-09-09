package org.teamvoided.deteratoxin.data.tags

import net.minecraft.core.registries.Registries
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.util.tag

object DeteratoxinBlockTags {

    val SUPPORTS_LILY = tag("supports/lily")

    fun tag(id: String) = Registries.BLOCK.tag(id(id))

}