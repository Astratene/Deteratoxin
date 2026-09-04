package org.teamvoided.deteratoxin

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.item.Items
import net.minecraft.world.level.gameevent.GameEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.deteratoxin.config.DeteratoxinConfig
import org.teamvoided.deteratoxin.entity.DeteratoxinCloudEntity
import org.teamvoided.deteratoxin.init.*
import org.teamvoided.deteratoxin.util.giveItem

object Deteratoxin {

    const val MODID = "deteratoxin"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(Deteratoxin::class.simpleName)

    @JvmField
    var config = ConfigApi.registerAndLoadConfig(::DeteratoxinConfig)

    fun init() {
        log.info("Hello from Common ${config.commonEntry.get()}")
        DeteratoxinItems.init()
        DeteratoxinBlocks.init()
        DeteratoxinDataComponents.init()
        DeteratoxinRegistries.init()
        DeteratoxinParticles.init()
        DeteratoxinEffects.init()
        DeteratoxinEntities.init()
        DeteratoxinWorldGen.init()
        DeteratoxinDataAttachments.init()

        UseItemCallback.EVENT.register { player, level, hand ->
            val stack = player.getItemInHand(hand)
            if (stack.`is`(Items.GLASS_BOTTLE)) {
                val list = level.getEntitiesOfClass(
                    DeteratoxinCloudEntity::class.java,
                    player.getBoundingBox().inflate(2.0)
                )
                if (!list.isEmpty()) {
                    val cloud = list.get(0) as DeteratoxinCloudEntity
                    cloud.cloudLifespan -= 50
                    level.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.BOTTLE_FILL_DRAGONBREATH,
                        SoundSource.NEUTRAL,
                        1.0f,
                        1.0f
                    )
                    level.gameEvent(player, GameEvent.FLUID_PICKUP, player.position())
                    if (player is ServerPlayer) {
                        CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(player, stack, cloud)
                    }
                    stack.shrink(1)
                    player.giveItem(DeteratoxinItems.DETERATOXIN_EXTRACT.defaultInstance)
                    return@register InteractionResultHolder.sidedSuccess(
                        stack, level.isClientSide()
                    )
                }
            }
            InteractionResultHolder.pass(stack)
        }
    }

    fun id(namespace: String, path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)
    fun mc(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)
    fun id(path: String) = id(MODID, path)

}