package org.teamvoided.deteratoxin.data.registry

import net.minecraft.core.registries.Registries
import org.teamvoided.deteratoxin.Deteratoxin
import org.teamvoided.deteratoxin.util.key


object DeteratoxinDamageTypes {

    val STABBED = create("stabbed")

    val DETOX = create("detox")
    val DRINKNT = create("drinknt")
    val DRINKETH = create("drinketh")

    fun create(id: String) = Registries.DAMAGE_TYPE.key(Deteratoxin.id(id))

}