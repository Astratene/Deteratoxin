package org.teamvoided.deteratoxin.data.tags

import net.minecraft.core.registries.Registries
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.util.tag

object DeteratoxinBlockTags {

    val LILY_PLACABLE = create("lily_placable")

    fun create(id: String) = Registries.BLOCK.tag(id(id))

}