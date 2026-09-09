package org.teamvoided.deteratoxin.data.registry

import net.minecraft.core.registries.Registries
import org.teamvoided.deteratoxin.Deteratoxin
import org.teamvoided.deteratoxin.util.key


object DeteratoxinDamageTypes {

    val STABBED = key("stabbed")

    val DETOX = key("detox")
    val DRINKNT = key("drinknt")
    val DRINKETH = key("drinketh")
    val DETERIORATED = key("deteriorated")

    fun key(id: String) = Registries.DAMAGE_TYPE.key(Deteratoxin.id(id))

}