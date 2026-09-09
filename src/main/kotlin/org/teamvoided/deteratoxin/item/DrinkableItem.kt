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
import org.teamvoided.deteratoxin.util.giveItem

class DrinkableItem(properties: Properties) : Item(properties) {

    override fun getUseAnimation(itemStack: ItemStack): UseAnim = UseAnim.DRINK

    override fun getEatingSound(): SoundEvent = SoundEvents.HONEY_DRINK

    override fun getDrinkingSound(): SoundEvent = SoundEvents.HONEY_DRINK

    override fun getUseDuration(stack: ItemStack, user: LivingEntity): Int = 20

    override fun finishUsingItem(stack: ItemStack, level: Level, user: LivingEntity): ItemStack {
        super.finishUsingItem(stack, level, user)
        if (user is ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(user, stack)
            user.awardStat(Stats.ITEM_USED.get(this))
        }

        if (!level.isClientSide && Deteratoxin.config.drinkingDeteratoxinKillsYou) {
            val damageType = when (level.random.nextInt(1, 4)) {
                1 -> DeteratoxinDamageTypes.DETOX
                2 -> DeteratoxinDamageTypes.DRINKNT
                else -> DeteratoxinDamageTypes.DRINKETH
            }
            user.customDamage(damageType, 100_000f)
        }

        if (stack.isEmpty) {
            return ItemStack(Items.GLASS_BOTTLE)
        }
        if (user is Player && !user.hasInfiniteMaterials()) {
            user.giveItem(ItemStack(Items.GLASS_BOTTLE))
        }
        return stack
    }

}