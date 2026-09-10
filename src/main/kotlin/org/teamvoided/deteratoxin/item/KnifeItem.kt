package org.teamvoided.deteratoxin.item


import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.*
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.deteratoxin.Deteratoxin.config
import org.teamvoided.deteratoxin.components.ImbuedEffect
import org.teamvoided.deteratoxin.components.ReadyTime.Companion.readied
import org.teamvoided.deteratoxin.components.ReadyTime.Companion.setReadyTicks
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getImmunity
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.IMBUED_EFFECT
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents.READY_TIME
import org.teamvoided.deteratoxin.init.DeteratoxinEffects
import org.teamvoided.deteratoxin.item.KnifeIngredient.Companion.getKnifeIngredient
import org.teamvoided.deteratoxin.util.*
import kotlin.math.max
import kotlin.math.min


class KnifeItem(settings: Properties) : TieredItem(Tiers.NETHERITE, settings) {

    fun tryApplyEffects(
        stack: ItemStack, target: LivingEntity, user: LivingEntity, refImbued: ImbuedEffect,
    ): Boolean {
        val imbued = refImbued.copy()

        stack.setReadyTicks(null)
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

    override fun hurtEnemy(knife: ItemStack, target: LivingEntity, user: LivingEntity): Boolean = true

    override fun postHurtEnemy(knife: ItemStack, target: LivingEntity, user: LivingEntity) {
        super.postHurtEnemy(knife, target, user)
        val imbued = knife.get(IMBUED_EFFECT)
        if (imbued != null && knife.readied()) {
            if (tryApplyEffects(knife, target, user, imbued)) {
                if (user is Player) {
                    user.cooldowns.addCooldown(knife.item, 450)
                }
                user.level().playSound(
                    null,
                    user.x, user.y, user.z,
                    SoundEvents.SILVERFISH_DEATH, if (user is Player) SoundSource.PLAYERS else SoundSource.HOSTILE,
                    2.0F, 2.0f
                )
            }
        }
        knife.hurtIfAllowed(user, EquipmentSlot.MAINHAND)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (!player.level().isClientSide) {
            val knife = player.getItemInHand(hand)
            val imbued = knife.get(IMBUED_EFFECT)
            if (imbued != null) {
                if (knife.readied()) {
                    if (tryApplyEffects(knife, player, player, imbued)) {
                        //TODO blood particles maybe?
                        player.customDamage(DeteratoxinDamageTypes.STABBED, 5f, player, player)
                        player.playPlayerSound(SoundEvents.SILVERFISH_DEATH)
                        player.cooldowns.addCooldown(knife.item, 450)
                        knife.hurtIfAllowed(player, hand.toSlot())
                    }
                } else {
                    knife.setReadyTicks(100)
                    player.cooldowns.addCooldown(knife.item, 5)
                    player.playPlayerSound(SoundEvents.VAULT_INSERT_ITEM)
                }
                return InteractionResultHolder.sidedSuccess(knife, player.level().isClientSide)
            }
        }
        return super.use(level, player, hand)
    }

    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, level, entity, slot, selected)
        val readyTime = stack.get(READY_TIME)
        if (readyTime != null) {
            if (readyTime.readied()) {
                stack.setReadyTicks(readyTime.ticks - 1)
            } else {
                stack.setReadyTicks(null)
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
        knife: ItemStack, ctx: TooltipContext, tooltip: MutableList<Component>, flag: TooltipFlag,
    ) {
        val imbued = knife.get(IMBUED_EFFECT)
        if (imbued == null) {
            tooltip.add(Component.translatable("tooltip.deteratoxin.empty.tooltip").withStyle(ChatFormatting.GRAY))
        } else {
            val effect = imbued.effect.value()
            val tickDuration = imbued.duration
            val amplifier = imbued.amplifier
            val color = effect.color

            if (effect.isInstantenous) {
                // `tickDuration` is use in instant effects
                tooltip.add(
                    Component.translatable(
                        "tooltip.deteratoxin.currentInstantEffect.tooltip",
                        effect.displayName.copy().withColor(color),
                        Component.literal("$tickDuration").withColor(color)
                    )
                        .withStyle(ChatFormatting.AQUA)
                )
            } else {
                tooltip.add(
                    Component.translatable(
                        "tooltip.deteratoxin.currentEffect.tooltip",
                        getEffectTooltip(effect, amplifier, tickDuration, ctx.tickRate()).withColor(color)
                    ).withStyle(ChatFormatting.AQUA)
                )
            }
            if (knife.readied()) {
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

        super.appendHoverText(knife, ctx, tooltip, flag)
    }

    fun ItemStack.removeImbued() {
        set(IMBUED_EFFECT, null)
    }

    // Automatically removed itself if duration is 0
    fun ItemStack.setOrRemoveImbued(imbued: ImbuedEffect) {
        set(IMBUED_EFFECT, if (imbued.duration > 0) imbued else null)
    }

    fun ItemStack.hurtIfAllowed(user: LivingEntity, slot: EquipmentSlot) {
        if (config.enableDurability) {
            hurtAndBreak(1, user, slot)
        }
    }

    override fun canAttackBlock(state: BlockState, level: Level, pos: BlockPos, player: Player): Boolean {
        return !player.isCreative
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