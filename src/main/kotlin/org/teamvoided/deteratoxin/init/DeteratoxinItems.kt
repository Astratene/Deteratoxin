package org.teamvoided.deteratoxin.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.Tiers
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.item.DrinkableItem
import org.teamvoided.deteratoxin.item.KnifeItem
import org.teamvoided.deteratoxin.util.getModEntries
import org.teamvoided.deteratoxin.util.register

object DeteratoxinItems {

    val ITEMS get() = getModEntries(BuiltInRegistries.ITEM)

    val KNIFE = register(
        "knife",
        KnifeItem(
            Properties().rarity(Rarity.RARE).stacksTo(1)
                .attributes(KnifeItem.createAttributes(Tiers.NETHERITE, 0, -2.0F))
        )
    )

    val DETOX_EXTRACT: FoodProperties = FoodProperties.Builder()
        .nutrition(0)
        .saturationModifier(0f)
        .effect(MobEffectInstance(DeteratoxinEffects.DETERATOXIN, 200, 5), 1.0f)
        .alwaysEdible()
        .build()

    val DETERATOXIN_EXTRACT = register(
        "deteratoxin_extract",
        DrinkableItem(
            Properties().rarity(Rarity.RARE).food(DETOX_EXTRACT)
        )
    )

    val POTENT_DETERATOXIN_EXTRACT = register(
        "potent_deteratoxin_extract",
        DrinkableItem(
            Properties().rarity(Rarity.RARE).food(DETOX_EXTRACT),
        )
    )

    fun init() = Unit

    fun register(id: String, item: Item): Item = BuiltInRegistries.ITEM.register(id(id), item)

}