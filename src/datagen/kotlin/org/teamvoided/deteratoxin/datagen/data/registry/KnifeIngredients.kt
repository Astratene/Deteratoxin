package org.teamvoided.deteratoxin.datagen.data.registry

import net.minecraft.core.Holder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import org.teamvoided.deteratoxin.init.DeteratoxinEffects
import org.teamvoided.deteratoxin.init.DeteratoxinItems
import org.teamvoided.deteratoxin.item.KnifeIngredient
import org.teamvoided.deteratoxin.item.KnifeIngredient.Companion.getKey

object KnifeIngredients : RegistryBootstrapper<KnifeIngredient> {

    override fun BootstrapContext<KnifeIngredient>.init() {
        knifeIngredient(Items.POPPY, MobEffects.NIGHT_VISION, 100, 0)
        knifeIngredient(Items.ALLIUM, MobEffects.FIRE_RESISTANCE, 60, 0)
        knifeIngredient(Items.AZURE_BLUET, MobEffects.BLINDNESS, 220, 0)
        knifeIngredient(Items.BLUE_ORCHID, MobEffects.SATURATION, 1, 3)
        knifeIngredient(Items.DANDELION, MobEffects.SATURATION, 1, 3)
        knifeIngredient(Items.CORNFLOWER, MobEffects.CONFUSION, 140, 0)
        knifeIngredient(Items.TORCHFLOWER, MobEffects.NIGHT_VISION, 100, 0)
        knifeIngredient(Items.ORANGE_TULIP, MobEffects.WEAKNESS, 140, 0)
        knifeIngredient(Items.PINK_TULIP, MobEffects.WEAKNESS, 140, 0)
        knifeIngredient(Items.RED_TULIP, MobEffects.WEAKNESS, 140, 0)
        knifeIngredient(Items.WHITE_TULIP, MobEffects.WEAKNESS, 140, 0)
        knifeIngredient(Items.WITHER_ROSE, MobEffects.WITHER, 140, 0)

        //potion ingredients
        knifeIngredient(Items.SUGAR, MobEffects.MOVEMENT_SPEED, 200, 0)
        knifeIngredient(Items.RABBIT_FOOT, MobEffects.JUMP, 200, 0)
        knifeIngredient(Items.SPIDER_EYE, MobEffects.POISON, 100, 0)
        knifeIngredient(Items.BLAZE_POWDER, MobEffects.DAMAGE_BOOST, 200, 0)
        knifeIngredient(Items.GOLDEN_CARROT, MobEffects.NIGHT_VISION, 200, 0)
        knifeIngredient(Items.GHAST_TEAR, MobEffects.REGENERATION, 100, 0)
        knifeIngredient(Items.PUFFERFISH, MobEffects.POISON, 100, 0)
        knifeIngredient(Items.MAGMA_CREAM, MobEffects.FIRE_RESISTANCE, 200, 0)
        knifeIngredient(Items.TURTLE_SCUTE, MobEffects.MOVEMENT_SLOWDOWN, 200, 0)
        knifeIngredient(Items.TURTLE_HELMET, MobEffects.DAMAGE_RESISTANCE, 200, 0)
        knifeIngredient(Items.PHANTOM_MEMBRANE, MobEffects.SLOW_FALLING, 200, 0)
        knifeIngredient(Items.BREEZE_ROD, MobEffects.WIND_CHARGED, 200, 0)
        knifeIngredient(Items.COBWEB, MobEffects.WEAVING, 200, 0)
        knifeIngredient(Items.STONE, MobEffects.INFESTED, 200, 0)
        knifeIngredient(Items.FERMENTED_SPIDER_EYE, MobEffects.WEAKNESS, 200, 0)
        knifeIngredient(Items.SLIME_BLOCK, MobEffects.OOZING, 200, 0)

        //other
        knifeIngredient(Items.ROTTEN_FLESH, MobEffects.HUNGER, 200, 0)
        knifeIngredient(Items.GOLDEN_APPLE, MobEffects.ABSORPTION, 1200, 0)
        knifeIngredient(Items.ENCHANTED_GOLDEN_APPLE, MobEffects.ABSORPTION, 1200, 3)
        knifeIngredient(Items.POISONOUS_POTATO, MobEffects.POISON, 100, 0)
        knifeIngredient(Items.CHICKEN, MobEffects.HUNGER, 200, 0)

        knifeIngredient(DeteratoxinItems.DETERATOXIN_EXTRACT, DeteratoxinEffects.DETERATOXIN, 500, 3)
        knifeIngredient(DeteratoxinItems.POTENT_DETERATOXIN_EXTRACT, DeteratoxinEffects.POTENT_DETERATOXIN, 1, 0)
    }

    fun BootstrapContext<KnifeIngredient>.knifeIngredient(
        item: Item,
        effect: Holder<MobEffect>,
        duration: Int,
        amplifier: Int
    ) {
        register(getKey(item), KnifeIngredient(effect, duration, amplifier))
    }

}