package org.teamvoided.deteratoxin.item


import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.*
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.Level
import org.teamvoided.deteratoxin.components.ImbuedEffect
import org.teamvoided.deteratoxin.components.KnifeData
import org.teamvoided.deteratoxin.components.KnifeData.Companion.getKnifeData
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getImmunity
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.IMBUED_EFFECT
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.KNIFE_DATA
import org.teamvoided.deteratoxin.init.DeteratoxinEffects
import org.teamvoided.deteratoxin.item.KnifeIngredient.Companion.getKnifeIngredient
import org.teamvoided.deteratoxin.util.customDamage
import org.teamvoided.deteratoxin.util.giveItem
import org.teamvoided.deteratoxin.util.playPlayerSound
import kotlin.math.max
import kotlin.math.min
import kotlin.time.DurationUnit
import kotlin.time.toDuration


// TODO fix durability and make it configurable
class KnifeItem(settings: Properties) : TieredItem(Tiers.NETHERITE, settings) {

    fun tryApplyEffects(
        stack: ItemStack, target: LivingEntity, user: LivingEntity, refImbued: ImbuedEffect,
    ): Boolean {
        val imbued = refImbued.copy()

        stack.set(KNIFE_DATA, null)
        if (imbued.isInstantenous()) {
            val isPotent = imbued.effect.value() == DeteratoxinEffects.POTENT_DETERATOXIN.value()
            target.addEffect(MobEffectInstance(imbued.effect, 1, if (isPotent) 3 else 0))
            if (isPotent) {
                user.addEffect(MobEffectInstance(imbued.effect, 1, 4))
            }
            imbued.duration--
            stack.setOrRemoveImbued(imbued)
            return true
        }

        val isDetox = imbued.effect == DeteratoxinEffects.DETERATOXIN
        val maxDuration = if (isDetox) 600 else max(100, 600 / (imbued.amplifier + 1))
        if (isDetox && target.getImmunity() > 12000) {
            imbued.amplifier /= 2
        }
        val existingEffect = target.getEffect(imbued.effect)
        // TODO merge these 4 ifs in to one
        if (existingEffect != null) {
            if (imbued.duration < maxDuration && (imbued.duration > existingEffect.duration || imbued.amplifier > existingEffect.amplifier)) {
                target.addEffect(MobEffectInstance(imbued.asEffectInstance()))
                stack.removeImbued()
                return true
            } else if (maxDuration > existingEffect.duration || imbued.amplifier > existingEffect.amplifier) {
                target.addEffect(MobEffectInstance(imbued.effect, maxDuration, imbued.amplifier))
                imbued.duration -= maxDuration
                stack.setOrRemoveImbued(imbued)

                return true
            }
        } else {
            if (imbued.duration < maxDuration) {
                target.addEffect(imbued.asEffectInstance())
                stack.removeImbued()

                return true
            } else {
                target.addEffect(MobEffectInstance(imbued.effect, maxDuration, imbued.amplifier))
                imbued.duration -= maxDuration
                stack.setOrRemoveImbued(imbued)

                return true
            }
        }

        return false
    }

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, user: LivingEntity): Boolean {
        val imbued = stack.get(IMBUED_EFFECT)
        if (imbued != null && stack.getKnifeData().readied()) {
            if (tryApplyEffects(stack, target, user, imbued)) {
                if (user is Player) {
                    user.cooldowns.addCooldown(stack.item, 450)
                }
                user.level().playSound(
                    null,
                    user.x, user.y, user.z,
                    SoundEvents.SILVERFISH_DEATH, if (user is Player) SoundSource.PLAYERS else SoundSource.HOSTILE,
                    2.0F, 2.0f
                )
            }
        }
        return super.hurtEnemy(stack, target, user)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (!player.level().isClientSide) {
            val stack = player.getItemInHand(hand)
            val imbued = stack.get(IMBUED_EFFECT)
            if (imbued != null) {
                if (stack.getKnifeData().readied()) {
                    if (tryApplyEffects(stack, player, player, imbued)) {
                        //TODO blood particles maybe?
                        player.customDamage(DeteratoxinDamageTypes.STABBED, 5f, player, player)
                        player.playPlayerSound(SoundEvents.SILVERFISH_DEATH)
                        player.cooldowns.addCooldown(stack.item, 450)
                    }
                } else {
                    stack.set(KNIFE_DATA, KnifeData(100))
                    player.cooldowns.addCooldown(stack.item, 5)
                    player.playPlayerSound(SoundEvents.VAULT_INSERT_ITEM)
                }
                return InteractionResultHolder.sidedSuccess(stack, player.level().isClientSide)
            }
        }
        return super.use(level, player, hand)
    }

    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, level, entity, slot, selected)
        val readyData = stack.get(KNIFE_DATA)
        if (readyData != null) {
            if (readyData.readied()) {
                stack.set(KNIFE_DATA, KnifeData(readyData.readyTime - 1))
            } else {
                stack.set(KNIFE_DATA, null)
                if (entity is Player && !entity.cooldowns.isOnCooldown(stack.item)) {
                    entity.cooldowns.addCooldown(stack.item, 160)
                }
            }
        }
    }

    override fun overrideOtherStackedOnMe(
        knife: ItemStack, ingStack: ItemStack, slot: Slot, action: ClickAction, player: Player, cursorInv: SlotAccess,
    ): Boolean {
        if ((knife.get(IMBUED_EFFECT)?.duration ?: 0) >= 9600) {
            player.level()
                .playLocalSound(player, SoundEvents.VAULT_REJECT_REWARDED_PLAYER, SoundSource.PLAYERS, 1.0f, 2.0f)
            return true
        }

        val imbuedEffect = getStackEffect(ingStack, player)
        if (imbuedEffect != null) {
            return thereIsDarknessInMeRightNow(player, knife, ingStack, imbuedEffect, cursorInv)
        }

        return super.overrideOtherStackedOnMe(knife, ingStack, slot, action, player, cursorInv)
    }

    fun getStackEffect(ingredient: ItemStack, player: Player): ImbuedEffect? {
        val potionContents = ingredient.get(DataComponents.POTION_CONTENTS)?.allEffects?.firstOrNull()
        if (potionContents != null) {
            return ImbuedEffect(potionContents)
        }

        val knifeIngredient = player.level().registryAccess().getKnifeIngredient(ingredient)?.value()
        if (knifeIngredient != null) {
            return ImbuedEffect(knifeIngredient)
        }

        return null
    }

    fun thereIsDarknessInMeRightNow(
        player: Player, knife: ItemStack, ingStack: ItemStack, imbuedStack: ImbuedEffect, cursorInv: SlotAccess,
    ): Boolean {
        val stackAmplifier = imbuedStack.amplifier
        var duration = imbuedStack.duration
        var appliedEffects = false

        val imbued = knife.get(IMBUED_EFFECT)
        val knifeEff = imbued?.effect
        var knifeDur = imbued?.duration ?: 0
        var knifeAmp = imbued?.amplifier ?: 0

        if (knifeEff == null) {
            knife.setOrRemoveImbued(
                if (imbuedStack.isInstantenous())
                    ImbuedEffect(imbuedStack.effect, min(duration + stackAmplifier, 8), 0)
                else
                    imbuedStack
            )
            appliedEffects = true
        } else if (knifeEff == imbuedStack.effect) {
            if (imbuedStack.isInstantenous()) {
                if (knifeDur >= 8) {
                    player.level().playLocalSound(
                        player, SoundEvents.VAULT_REJECT_REWARDED_PLAYER, SoundSource.PLAYERS, 1.0f, 2.0f
                    )
                    return true
                }
                if (knifeDur + duration + stackAmplifier <= 8) {
                    knifeDur += duration + stackAmplifier
                } else {
                    knifeDur = 8
                }
                knife.setOrRemoveImbued(ImbuedEffect(knifeEff, knifeDur, 0))
            } else {
                if (stackAmplifier > knifeAmp) {
                    repeat(stackAmplifier - knifeAmp) {
                        knifeDur /= 2
                    }
                    knifeAmp = stackAmplifier
                    if (knifeDur + duration <= 9600) {
                        knifeDur += duration
                    } else {
                        knifeDur = 9600
                    }
                } else if (stackAmplifier == knifeAmp) {
                    if (knifeDur + duration <= 9600) {
                        knifeDur += duration
                    } else {
                        knifeDur = 9600
                    }
                } else {
                    repeat(knifeAmp - stackAmplifier) {
                        duration /= 2
                    }
                    if (knifeDur + duration <= 9600) {
                        knifeDur += duration
                    } else {
                        knifeDur = 9600
                    }
                }
                knife.setOrRemoveImbued(ImbuedEffect(knifeEff, knifeDur, knifeAmp))
            }
            appliedEffects = true
        }

        if (appliedEffects) {
            val remainder = getCustomRemainder(ingStack)
            ingStack.consume(1, player)
            if (remainder != null) {
                if (ingStack.isEmpty) {
                    cursorInv.set(remainder)
                } else {
                    player.giveItem(remainder)
                }
            }
            player.level().playLocalSound(player, SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 1.0f, 1.25f)
            return true
        }

        return false
    }

    fun getCustomRemainder(ingredient: ItemStack): ItemStack? {
        val remainders = ingredient.item.getRecipeRemainder(ingredient)
        if (remainders == null || remainders.isEmpty) {
            if (ingredient.`is`(Items.TIPPED_ARROW)) {
                return Items.ARROW.defaultInstance
            }
            if (ingredient.`is`(ConventionalItemTags.BOTTLE_POTIONS)) {
                return Items.GLASS_BOTTLE.defaultInstance
            }
            return null
        }
        return remainders
    }

    override fun appendHoverText(
        stack: ItemStack, ctx: TooltipContext, tooltip: MutableList<Component>, flag: TooltipFlag,
    ) {
        val imbued = stack.get(IMBUED_EFFECT)
        if (imbued == null) {
            tooltip.add(Component.translatable("tooltip.deteratoxin.empty.tooltip").withStyle(ChatFormatting.GRAY))
        } else {
            val effect = imbued.effect.value()
            val tickDuration = imbued.duration
            val amplifier = imbued.amplifier
            val effectName = effect.displayName
            val duration =
                (tickDuration / 20).toDuration(DurationUnit.SECONDS).toComponents { _, minutes, seconds, _ ->
                    "$minutes:${if (seconds < 10) "0" else ""}$seconds"
                }

            if (effect.isInstantenous) {
                // `tickDuration` is use in instant effects
                tooltip.add(
                    Component.translatable(
                        "tooltip.deteratoxin.currentInstantEffect.tooltip",
                        effectName,
                        tickDuration
                    )
                        .withStyle(ChatFormatting.AQUA)
                )
            } else {
                tooltip.add(
                    Component.translatable(
                        "tooltip.deteratoxin.currentEffect.tooltip", effectName, amplifier + 1, duration
                    ).withStyle(ChatFormatting.AQUA)
                )
            }
            if (stack.getKnifeData().readied()) {
                tooltip.add(
                    Component.translatable("tooltip.deteratoxin.instructionToStab.tooltip")
                        .withStyle(ChatFormatting.GRAY)
                )
            } else {
                tooltip.add(
                    Component.translatable("tooltip.deteratoxin.instructionToReady.tooltip")
                        .withStyle(ChatFormatting.GRAY)
                )
            }
        }

        super.appendHoverText(stack, ctx, tooltip, flag)
    }

    fun ItemStack.removeImbued() {
        set(IMBUED_EFFECT, null)
    }

    @Deprecated("delete asp")
    fun applyImbuedToKnife(stack: ItemStack, effect: Holder<MobEffect>, dur: Int, amp: Int) {
        stack.set(IMBUED_EFFECT, ImbuedEffect(effect, dur, amp))
    }

    // Automatically removed itself if duration is 0
    fun ItemStack.setOrRemoveImbued(imbued: ImbuedEffect) {
        set(IMBUED_EFFECT, if (imbued.duration > 0) imbued else null)
    }

    companion object {

        fun createAttributes(material: Tier, baseAttackDamage: Int, attackSpeed: Float): ItemAttributeModifiers {
            return ItemAttributeModifiers.builder()
                .add(
                    Attributes.ATTACK_DAMAGE,
                    AttributeModifier(
                        BASE_ATTACK_DAMAGE_ID,
                        (baseAttackDamage.toFloat() + material.attackDamageBonus).toDouble(),
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND
                )
                .add(
                    Attributes.ATTACK_SPEED,
                    AttributeModifier(
                        BASE_ATTACK_SPEED_ID,
                        attackSpeed.toDouble(),
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND
                )
                .build()
        }

    }
}