package org.teamvoided.deteratoxin.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.stats.Stats
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import org.teamvoided.deteratoxin.Deteratoxin
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes
import org.teamvoided.deteratoxin.util.customDamage

class DrinkableItem(properties: Item.Properties) : Item(properties) {
    override fun getUseAnimation(itemStack: ItemStack): UseAnim {
        return UseAnim.DRINK
    }

    override fun getEatingSound(): SoundEvent? {
        return SoundEvents.HONEY_DRINK
    }

    override fun getDrinkingSound(): SoundEvent? {
        return SoundEvents.HONEY_DRINK
    }

    override fun finishUsingItem(itemStack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        super.finishUsingItem(itemStack, level, livingEntity)
        if (livingEntity is ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(livingEntity, itemStack)
            livingEntity.awardStat(Stats.ITEM_USED.get(this))
        }

        if (!level.isClientSide && Deteratoxin.config.drinkingDeteratoxinKillsYou) {
            val random = level.random.nextInt(1, 4)
            val damageType =
                if (random == 1) DeteratoxinDamageTypes.DETOX else if (random == 2) DeteratoxinDamageTypes.DRINKNT else DeteratoxinDamageTypes.DRINKETH
            livingEntity.customDamage(damageType, 100000f)
        }

        if (itemStack.isEmpty()) {
            return ItemStack(Items.GLASS_BOTTLE)
        } else {
            if (livingEntity is Player && !livingEntity.hasInfiniteMaterials()) {
                val itemStack2 = ItemStack(Items.GLASS_BOTTLE)
                if (!livingEntity.getInventory().add(itemStack2)) {
                    livingEntity.drop(itemStack2, false)
                }
            }

            return itemStack
        }
    }

    override fun getUseDuration(itemStack: ItemStack, livingEntity: LivingEntity): Int {
        return 20
    }
}