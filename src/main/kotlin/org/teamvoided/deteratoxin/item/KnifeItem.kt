package org.teamvoided.deteratoxin.item


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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TieredItem
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.Level
import org.teamvoided.deteratoxin.components.KnifeData
import org.teamvoided.deteratoxin.data.registry.DeteratoxinDamageTypes
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.getImmunity
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents
import org.teamvoided.deteratoxin.init.DeteratoxinEffects
import org.teamvoided.deteratoxin.init.DeteratoxinRegistries
import org.teamvoided.deteratoxin.item.KnifeIngredient.Companion.getKey
import org.teamvoided.deteratoxin.util.customDamage
import org.teamvoided.deteratoxin.util.giveItem
import kotlin.jvm.optionals.getOrNull
import kotlin.math.max
import kotlin.math.min
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class KnifeItem(settings: Properties) : TieredItem(Tiers.NETHERITE, settings) {



    fun applyApplyEffects(
        stack: ItemStack,
        target: LivingEntity,
        knifeEff: Holder<MobEffect>,
        dur: Int,
        amp: Int
    ) {
        var knifeDur = dur
        var knifeAmp = amp
        if (knifeEff.value().isInstantenous) {
            target.addEffect(MobEffectInstance(knifeEff, 1, 0))
            knifeDur--
            if (knifeDur <= 0) {
                nullAll(stack)
            } else {
                stack.set(DeteratoxinDataComponents.KNIFE_POTION_DURATION, knifeDur)
            }
        } else {
            val maxDuration = if (knifeEff.`is`(DeteratoxinEffects.DETERATOXIN)) 600 else max(100, 600 / (knifeAmp + 1))
            if (knifeEff.`is`(DeteratoxinEffects.DETERATOXIN) && target.getImmunity() > 12000) {
                knifeAmp /= 2
            }
            if (knifeDur < maxDuration) {
                target.addEffect(MobEffectInstance(knifeEff, knifeDur, knifeAmp))
                nullAll(stack)
            } else {
                target.addEffect(MobEffectInstance(knifeEff, maxDuration, knifeAmp))
                knifeDur -= maxDuration
                if (knifeDur <= 0) {
                    nullAll(stack)
                } else {
                    stack.set(DeteratoxinDataComponents.KNIFE_POTION_DURATION, knifeDur)
                }
            }
        }
    }

    fun applyEffects(stack: ItemStack, target: LivingEntity, knifeEff: Holder<MobEffect>, dur: Int, amp: Int) {
        var knifeDur = dur
        var knifeAmp = amp
        stack.set(DeteratoxinDataComponents.KNIFE_DATA, KnifeData(false, 0))
        val targetEffects = target.activeEffects.filter { it.effect == knifeEff }
        if (targetEffects.isEmpty()) {
            applyApplyEffects(stack, target, knifeEff, knifeDur, knifeAmp)
        } else {
            var newEffect = true
            for (eff in targetEffects) {
                if (eff.effect.value() == knifeEff.value()) {
                    newEffect = false
                    if (knifeEff.value().isInstantenous) {
                        target.addEffect(MobEffectInstance(knifeEff, 1, 0))
                        knifeDur--
                        if (knifeDur <= 0) {
                            nullAll(stack)
                        } else {
                            stack.set(DeteratoxinDataComponents.KNIFE_POTION_DURATION, knifeDur)
                        }
                    } else {
                        val maxDuration =
                            if (knifeEff.`is`(DeteratoxinEffects.DETERATOXIN)) 600 else max(100, 600 / (knifeAmp + 1))
                        if (knifeEff.`is`(DeteratoxinEffects.DETERATOXIN) && target.getImmunity() > 12000) {
                            knifeAmp /= 2
                        }
                        if (knifeDur < maxDuration && (knifeDur > eff.duration || knifeAmp > eff.amplifier)) {
                            target.addEffect(MobEffectInstance(knifeEff, knifeDur, knifeAmp))
                            nullAll(stack)
                        } else if (maxDuration > eff.duration || knifeAmp > eff.amplifier) {
                            target.addEffect(MobEffectInstance(knifeEff, maxDuration, knifeAmp))
                            knifeDur -= maxDuration
                            if (knifeDur <= 0) {
                                nullAll(stack)
                            } else {
                                stack.set(DeteratoxinDataComponents.KNIFE_POTION_DURATION, knifeDur)
                            }
                        }
                    }
                }
            }
            if (newEffect) {
                if (targetEffects.isEmpty()) {
                    applyApplyEffects(stack, target, knifeEff, knifeDur, knifeAmp)
                }
            }
        }
    }

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, user: LivingEntity): Boolean {
        if (user is Player) {
            val knifeAmp = stack.getOrDefault(DeteratoxinDataComponents.KNIFE_POTION_AMPLIFIER, 0)
            val knifeDur = stack.getOrDefault(DeteratoxinDataComponents.KNIFE_POTION_DURATION, 0)
            val knifeEff = stack.get(DeteratoxinDataComponents.KNIFE_POTION_EFFECT)
            val data = stack.getOrDefault(
                DeteratoxinDataComponents.KNIFE_DATA,
                KnifeData.DEFAULT
            )
            if (knifeEff != null) {
                if (data.readied) {
                    user.cooldowns.addCooldown(stack.item, 450)
                    user.level().playSound(
                        null,
                        user.x,
                        user.y,
                        user.z,
                        SoundEvents.SILVERFISH_DEATH,
                        SoundSource.PLAYERS,
                        2.0F,
                        2.0f
                    )
                    applyEffects(stack, target, knifeEff, knifeDur, knifeAmp)
                }
            }
        }
        return super.hurtEnemy(stack, target, user)
    }

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack?>? {
        if (!player.level().isClientSide) {
            val stack = player.getItemInHand(interactionHand)
            val knifeAmp = stack.getOrDefault(DeteratoxinDataComponents.KNIFE_POTION_AMPLIFIER, 0)
            val knifeDur = stack.getOrDefault(DeteratoxinDataComponents.KNIFE_POTION_DURATION, 0)
            val knifeEff = stack.get(DeteratoxinDataComponents.KNIFE_POTION_EFFECT)
            val data = stack.getOrDefault(
                DeteratoxinDataComponents.KNIFE_DATA,
                KnifeData.DEFAULT
            )
            if (knifeEff != null) {
                if (data.readied) {
                    //todo knife stab sound
                    //todo blood particles maybe?
                    player.customDamage(DeteratoxinDamageTypes.STABBED, 5f, player, player)
                    player.level().playSound(
                        null,
                        player.x,
                        player.y,
                        player.z,
                        SoundEvents.SILVERFISH_DEATH,
                        SoundSource.PLAYERS,
                        2.0F,
                        2.0f
                    )
                    player.cooldowns.addCooldown(stack.item, 450)
                    applyEffects(stack, player, knifeEff, knifeDur, knifeAmp)
                } else {
                    //todo knife ready sound
                    stack.set(DeteratoxinDataComponents.KNIFE_DATA, KnifeData(true, 100))
                    player.cooldowns.addCooldown(stack.item, 5)
                    player.level().playSound(
                        null,
                        player.x,
                        player.y,
                        player.z,
                        SoundEvents.VAULT_INSERT_ITEM,
                        SoundSource.PLAYERS,
                        2.0F,
                        2.0f
                    )
                }
            }
        }
        return super.use(level, player, interactionHand)
    }

    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, i: Int, bl: Boolean) {
        super.inventoryTick(stack, level, entity, i, bl)
        val data = stack.getOrDefault(
            DeteratoxinDataComponents.KNIFE_DATA,
            KnifeData.DEFAULT
        )
        if (data.readied) {
            if (data.readyTime > 0) {
                stack.set(DeteratoxinDataComponents.KNIFE_DATA, KnifeData(data.readied, data.readyTime - 1))
            } else {
                stack.set(DeteratoxinDataComponents.KNIFE_DATA, KnifeData(false, 0))
                if (entity is Player && !entity.cooldowns.isOnCooldown(stack.item)) {
                    entity.cooldowns.addCooldown(stack.item, 160)
                }
            }
        }
    }

    override fun overrideOtherStackedOnMe(
        itemStack: ItemStack,
        itemStack2: ItemStack,
        slot: Slot,
        clickAction: ClickAction,
        player: Player,
        slotAccess: SlotAccess
    ): Boolean {
        var knifeAmp = itemStack.getOrDefault(DeteratoxinDataComponents.KNIFE_POTION_AMPLIFIER, 0)
        var knifeDur = itemStack.getOrDefault(DeteratoxinDataComponents.KNIFE_POTION_DURATION, 0)
        val knifeEff = itemStack.get(DeteratoxinDataComponents.KNIFE_POTION_EFFECT)
        val clickOnPotionData = itemStack2.get(DataComponents.POTION_CONTENTS)
        if (clickOnPotionData != null) {
            if (knifeDur >= 9600) {
                player.level().playLocalSound(
                    player,
                    SoundEvents.VAULT_REJECT_REWARDED_PLAYER,
                    SoundSource.PLAYERS,
                    1.0f,
                    2.0f
                )
                return true
            }
            var effectApplied = false
            for (potion in clickOnPotionData.allEffects) {
                val amplifier = potion.amplifier
                var duration = potion.duration
                val effect = potion.effect
                if (knifeEff == null) {
                    if (effect.value().isInstantenous) {
                        itemStack.set(DeteratoxinDataComponents.KNIFE_POTION_EFFECT, effect)
                        itemStack.set(
                            DeteratoxinDataComponents.KNIFE_POTION_DURATION,
                            min(duration + amplifier, 8)
                        )
                        effectApplied = true
                        break
                    }
                    applyEffectToKnife(itemStack, effect, amplifier, duration)
                    effectApplied = true
                    break
                } else if (knifeEff == effect) {
                    if (effect.value().isInstantenous) {
                        if (knifeDur >= 8) {
                            player.level().playLocalSound(
                                player,
                                SoundEvents.VAULT_REJECT_REWARDED_PLAYER,
                                SoundSource.PLAYERS,
                                1.0f,
                                2.0f
                            )
                            return true //super.overrideOtherStackedOnMe(itemStack, itemStack2, slot, clickAction, player, slotAccess)
                        }
                        if (knifeDur + duration + amplifier <= 8) {
                            knifeDur += duration + amplifier
                        } else {
                            knifeDur = 8
                        }
                        itemStack.set(DeteratoxinDataComponents.KNIFE_POTION_EFFECT, knifeEff)
                        itemStack.set(DeteratoxinDataComponents.KNIFE_POTION_DURATION, knifeDur)
                        effectApplied = true
                        break
                    } else {
                        if (amplifier > knifeAmp) {
                            repeat(amplifier - knifeAmp) {
                                knifeDur /= 2
                            }
                            knifeAmp = amplifier
                            if (knifeDur + duration <= 9600) {
                                knifeDur += duration
                            } else {
                                knifeDur = 9600
                            }
                        } else if (amplifier == knifeAmp) {
                            if (knifeDur + duration <= 9600) {
                                knifeDur += duration
                            } else {
                                knifeDur = 9600
                            }
                        } else {
                            repeat(knifeAmp - amplifier) {
                                duration /= 2
                            }
                            if (knifeDur + duration <= 9600) {
                                knifeDur += duration
                            } else {
                                knifeDur = 9600
                            }
                        }
                        applyEffectToKnife(itemStack, knifeEff, knifeAmp, knifeDur)
                        effectApplied = true
                        break
                    }
                }
            }
            if (effectApplied) {
                itemStack2.count--
                player.giveItem(Items.GLASS_BOTTLE.defaultInstance)
                player.level()
                    .playLocalSound(player, SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 1.0f, 1.25f)
                return true
            }
        }
        val ingredient = player.level().holderLookup(DeteratoxinRegistries.KNIFE_INGREDIENT)
            .get(getKey(itemStack2.item)).getOrNull()
        if (ingredient != null) {
            if (knifeDur >= 9600) {
                player.level().playLocalSound(
                    player,
                    SoundEvents.VAULT_REJECT_REWARDED_PLAYER,
                    SoundSource.PLAYERS,
                    1.0f,
                    2.0f
                )
                return true
            }
            var effectApplied = false
            val amplifier = ingredient.value().amplifier
            var duration = ingredient.value().duration
            val effect = ingredient.value().effect
            if (knifeEff == null) {
                if (effect.value().isInstantenous) {
                    itemStack.set(DeteratoxinDataComponents.KNIFE_POTION_EFFECT, effect)
                    itemStack.set(
                        DeteratoxinDataComponents.KNIFE_POTION_DURATION,
                        min(duration + amplifier, 8)
                    )
                    effectApplied = true
                }
                applyEffectToKnife(itemStack, effect, amplifier, duration)
                effectApplied = true
            } else if (knifeEff == effect) {
                if (effect.value().isInstantenous) {
                    if (knifeDur >= 8) {
                        player.level().playLocalSound(
                            player,
                            SoundEvents.VAULT_REJECT_REWARDED_PLAYER,
                            SoundSource.PLAYERS,
                            1.0f,
                            2.0f
                        )
                        return true //super.overrideOtherStackedOnMe(itemStack, itemStack2, slot, clickAction, player, slotAccess)
                    }
                    if (knifeDur + duration + amplifier <= 8) {
                        knifeDur += duration + amplifier
                    } else {
                        knifeDur = 8
                    }
                    itemStack.set(DeteratoxinDataComponents.KNIFE_POTION_EFFECT, knifeEff)
                    itemStack.set(DeteratoxinDataComponents.KNIFE_POTION_DURATION, knifeDur)
                    effectApplied = true
                } else {
                    if (amplifier > knifeAmp) {
                        repeat(amplifier - knifeAmp) {
                            knifeDur /= 2
                        }
                        knifeAmp = amplifier
                        if (knifeDur + duration <= 9600) {
                            knifeDur += duration
                        } else {
                            knifeDur = 9600
                        }
                    } else if (amplifier == knifeAmp) {
                        if (knifeDur + duration <= 9600) {
                            knifeDur += duration
                        } else {
                            knifeDur = 9600
                        }
                    } else {
                        repeat(knifeAmp - amplifier) {
                            duration /= 2
                        }
                        if (knifeDur + duration <= 9600) {
                            knifeDur += duration
                        } else {
                            knifeDur = 9600
                        }
                    }
                    applyEffectToKnife(itemStack, knifeEff, knifeAmp, knifeDur)
                    effectApplied = true
                }
            }
            if (effectApplied) {
                itemStack2.count--
                player.level()
                    .playLocalSound(player, SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 1.0f, 1.25f)
                return true
            }
        }
        return super.overrideOtherStackedOnMe(itemStack, itemStack2, slot, clickAction, player, slotAccess)
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        tooltipContext: TooltipContext,
        list: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {

        val knifeAmp = itemStack.getOrDefault(DeteratoxinDataComponents.KNIFE_POTION_AMPLIFIER, 0)
        val knifeDur = itemStack.getOrDefault(DeteratoxinDataComponents.KNIFE_POTION_DURATION, 0)
        val knifeEff = itemStack.get(DeteratoxinDataComponents.KNIFE_POTION_EFFECT)
        val data = itemStack.getOrDefault(
            DeteratoxinDataComponents.KNIFE_DATA,
            KnifeData.DEFAULT
        ).readied

        if (knifeEff == null) {
            list.add(
                Component.translatable("tooltip.deteratoxin.empty.tooltip").withStyle(ChatFormatting.GRAY)
            )
        } else {
            val stringEff = knifeEff.value().displayName
            val durMins = (knifeDur / 20).toDuration(DurationUnit.SECONDS)
            val str =
                durMins.toComponents { _, minutes, seconds, _ -> "$minutes:${if (seconds < 10) "0" else ""}$seconds" }

            if (knifeEff.value().isInstantenous) {
                list.add(
                    Component.translatable("tooltip.deteratoxin.currentInstantEffect.tooltip", stringEff, knifeDur)
                        .withStyle(ChatFormatting.AQUA)
                )
            } else {
                list.add(
                    Component.translatable("tooltip.deteratoxin.currentEffect.tooltip", stringEff, knifeAmp + 1, str)
                        .withStyle(ChatFormatting.AQUA)
                )
            }
            if (!data) {
                list.add(
                    Component.translatable("tooltip.deteratoxin.instructionToReady.tooltip")
                        .withStyle(ChatFormatting.GRAY)
                )
            } else {
                list.add(
                    Component.translatable("tooltip.deteratoxin.instructionToStab.tooltip")
                        .withStyle(ChatFormatting.GRAY)
                )
            }
        }

        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)
    }

    fun nullAll(stack: ItemStack) {
        stack.set(DeteratoxinDataComponents.KNIFE_POTION_EFFECT, null)
        stack.set(DeteratoxinDataComponents.KNIFE_POTION_AMPLIFIER, null)
        stack.set(DeteratoxinDataComponents.KNIFE_POTION_DURATION, null)
    }

    fun applyEffectToKnife(stack: ItemStack, effect: Holder<MobEffect>, amp: Int, dur: Int) {
        stack.set(DeteratoxinDataComponents.KNIFE_POTION_EFFECT, effect)
        stack.set(DeteratoxinDataComponents.KNIFE_POTION_AMPLIFIER, amp)
        stack.set(DeteratoxinDataComponents.KNIFE_POTION_DURATION, dur)
    }

    companion object {
        fun createAttributes(
            material: Tier,
            baseAttackDamageModifier: Int,
            attackSpeedModifier: Float,
        ): ItemAttributeModifiers {
            return ItemAttributeModifiers.builder().add(
                Attributes.ATTACK_DAMAGE, AttributeModifier(
                    BASE_ATTACK_DAMAGE_ID,
                    (baseAttackDamageModifier.toFloat() + material.attackDamageBonus).toDouble(),
                    AttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.MAINHAND
            ).add(
                Attributes.ATTACK_SPEED, AttributeModifier(
                    BASE_ATTACK_SPEED_ID, attackSpeedModifier.toDouble(), AttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.MAINHAND
            ).build()
        }
    }
}