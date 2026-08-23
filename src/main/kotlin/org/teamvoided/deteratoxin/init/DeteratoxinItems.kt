package org.teamvoided.deteratoxin.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.Rarity
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.item.KnifeItem
import org.teamvoided.deteratoxin.util.getModEntries
import org.teamvoided.deteratoxin.util.register

object DeteratoxinItems {

    val ITEMS get() = getModEntries(BuiltInRegistries.ITEM)

    val KNIFE = register("knife", KnifeItem(Properties().rarity(Rarity.RARE).stacksTo(1)))

    fun init() = Unit

    fun register(id: String, item: Item): Item = BuiltInRegistries.ITEM.register(id(id), item)

}