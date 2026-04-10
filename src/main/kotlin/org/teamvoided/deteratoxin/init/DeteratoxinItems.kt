package org.teamvoided.deteratoxin.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.Rarity
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.util.getModEntries
import org.teamvoided.deteratoxin.util.register

object DeteratoxinItems {

    val ITEMS get() = getModEntries(BuiltInRegistries.ITEM)

    val APPLE_2 = register("apple_2", Item(Properties().rarity(Rarity.RARE)))

    fun init() = Unit

    fun register(id: String, item: Item): Item = BuiltInRegistries.ITEM.register(id(id), item)

}