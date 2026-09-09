package org.teamvoided.deteratoxin.datagen.data.registry

import net.minecraft.core.Holder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items.*
import org.teamvoided.deteratoxin.init.DeteratoxinEffects
import org.teamvoided.deteratoxin.init.DeteratoxinItems
import org.teamvoided.deteratoxin.item.KnifeIngredient
import org.teamvoided.deteratoxin.item.KnifeIngredient.Companion.knifeKey

object ModKnifeIngredients : RegistryBootstrapper<KnifeIngredient> {

    override fun BootstrapContext<KnifeIngredient>.init() {
        knifeIngredient(POPPY, MobEffects.NIGHT_VISION, 100, 0)
        knifeIngredient(ALLIUM, MobEffects.FIRE_RESISTANCE, 60, 0)
        knifeIngredient(AZURE_BLUET, MobEffects.BLINDNESS, 220, 0)
        knifeIngredient(BLUE_ORCHID, MobEffects.SATURATION, 1, 3)
        knifeIngredient(DANDELION, MobEffects.SATURATION, 1, 3)
        knifeIngredient(CORNFLOWER, MobEffects.CONFUSION, 140, 0)
        knifeIngredient(TORCHFLOWER, MobEffects.NIGHT_VISION, 100, 0)
        knifeIngredient(ORANGE_TULIP, MobEffects.WEAKNESS, 140, 0)
        knifeIngredient(PINK_TULIP, MobEffects.WEAKNESS, 140, 0)
        knifeIngredient(RED_TULIP, MobEffects.WEAKNESS, 140, 0)
        knifeIngredient(WHITE_TULIP, MobEffects.WEAKNESS, 140, 0)
        knifeIngredient(WITHER_ROSE, MobEffects.WITHER, 140, 0)

        //potion ingredients
        knifeIngredient(SUGAR, MobEffects.MOVEMENT_SPEED, 200, 0)
        knifeIngredient(RABBIT_FOOT, MobEffects.JUMP, 200, 0)
        knifeIngredient(SPIDER_EYE, MobEffects.POISON, 100, 0)
        knifeIngredient(BLAZE_POWDER, MobEffects.DAMAGE_BOOST, 200, 0)
        knifeIngredient(GOLDEN_CARROT, MobEffects.NIGHT_VISION, 200, 0)
        knifeIngredient(GHAST_TEAR, MobEffects.REGENERATION, 100, 0)
        knifeIngredient(PUFFERFISH, MobEffects.POISON, 100, 0)
        knifeIngredient(MAGMA_CREAM, MobEffects.FIRE_RESISTANCE, 200, 0)
        knifeIngredient(TURTLE_SCUTE, MobEffects.MOVEMENT_SLOWDOWN, 200, 0)
        knifeIngredient(TURTLE_HELMET, MobEffects.DAMAGE_RESISTANCE, 200, 0)
        knifeIngredient(PHANTOM_MEMBRANE, MobEffects.SLOW_FALLING, 200, 0)
        knifeIngredient(BREEZE_ROD, MobEffects.WIND_CHARGED, 200, 0)
        knifeIngredient(COBWEB, MobEffects.WEAVING, 200, 0)
        knifeIngredient(STONE, MobEffects.INFESTED, 200, 0)
        knifeIngredient(FERMENTED_SPIDER_EYE, MobEffects.WEAKNESS, 200, 0)
        knifeIngredient(SLIME_BLOCK, MobEffects.OOZING, 200, 0)

        //other
        knifeIngredient(ROTTEN_FLESH, MobEffects.HUNGER, 200, 0)
        knifeIngredient(GOLDEN_APPLE, MobEffects.ABSORPTION, 1200, 0)
        knifeIngredient(ENCHANTED_GOLDEN_APPLE, MobEffects.ABSORPTION, 1200, 3)
        knifeIngredient(POISONOUS_POTATO, MobEffects.POISON, 100, 0)
        knifeIngredient(CHICKEN, MobEffects.HUNGER, 200, 0)

        knifeIngredient(DeteratoxinItems.DETERATOXIN_EXTRACT, DeteratoxinEffects.DETERATOXIN, 500, 3)
        knifeIngredient(DeteratoxinItems.POTENT_DETERATOXIN_EXTRACT, DeteratoxinEffects.POTENT_DETERATOXIN, 1, 0)
    }

    fun BootstrapContext<KnifeIngredient>.knifeIngredient(
        item: Item, effect: Holder<MobEffect>, duration: Int, amplifier: Int
    ) {
        register(knifeKey(item), KnifeIngredient(effect, duration, amplifier))
    }

}