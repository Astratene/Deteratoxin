package org.teamvoided.deteratoxin.init

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.*
import net.minecraft.world.item.Item.Properties
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.item.DrinkableItem
import org.teamvoided.deteratoxin.item.KnifeItem
import org.teamvoided.deteratoxin.util.getModEntries
import org.teamvoided.deteratoxin.util.key
import org.teamvoided.deteratoxin.util.modifyTab
import java.util.function.Function


object DeteratoxinItems {

    val ITEMS get() = getModEntries(BuiltInRegistries.ITEM)

    val KNIFE = register(
        "knife", ::KnifeItem,
        Properties()
            .rarity(Rarity.RARE)
            .stacksTo(1)
            .attributes(KnifeItem.createAttributes(Tiers.NETHERITE, 0, -2.0F))
    )

    @JvmField
    val DETERATOXIN_EXTRACT = register(
        "deteratoxin_extract", ::DrinkableItem,
        Properties()
            .rarity(Rarity.RARE)
            .food(DetoxFoods.DETOX_EXTRACT)
            .craftRemainder(Items.GLASS_BOTTLE)
    )

    @JvmField
    val POTENT_DETERATOXIN_EXTRACT = register(
        "potent_deteratoxin_extract", ::DrinkableItem,
        Properties()
            .rarity(Rarity.RARE)
            .food(DetoxFoods.POTENT_DETOX_EXTRACT)
            .craftRemainder(Items.GLASS_BOTTLE)
    )

    fun init() {
        modifyTab(CreativeModeTabs.INGREDIENTS) {
            addAfter(Items.DRAGON_BREATH, DETERATOXIN_EXTRACT, POTENT_DETERATOXIN_EXTRACT)
        }
        modifyTab(CreativeModeTabs.COMBAT) {
            addAfter(Items.MACE, KNIFE)
        }
        modifyTab(CreativeModeTabs.NATURAL_BLOCKS) {
            addAfter(Items.SPORE_BLOSSOM, DeteratoxinBlocks.STARLETTE_LILY)
        }
    }

    fun register(name: String, item: Function<Properties, Item>, properties: Properties = Properties()): Item {
        val id = Registries.ITEM.key(id(name))
//        properties.setId(id) // 1.21.11 code
        return Registry.register(BuiltInRegistries.ITEM, id.location(), item.apply(properties))
    }

    object DetoxFoods {

        val DETOX_EXTRACT: FoodProperties = FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0f)
            .effect(MobEffectInstance(DeteratoxinEffects.DETERATOXIN, 200, 5), 1f)
            .alwaysEdible()
            .build()

        val POTENT_DETOX_EXTRACT: FoodProperties = FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0f)
            .effect(MobEffectInstance(DeteratoxinEffects.POTENT_DETERATOXIN, 200, 6), 1f)
            .alwaysEdible()
            .build()

    }

}