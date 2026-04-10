package org.teamvoided.deteratoxin.data.tags

import net.minecraft.core.registries.Registries
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.util.tag

object DeteratoxinBlockTags {

    val APPLE_LIKE = create("apple_like")

    fun create(id: String) = Registries.BLOCK.tag(id(id))

}