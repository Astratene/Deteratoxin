package org.teamvoided.deteratoxin.init

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.clearImmunity
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.clearPotent
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setImmunity
import org.teamvoided.deteratoxin.init.DeteratoxinDataAttachments.setPotent

//TODO make command translatable
object DeteratoxinCommands {

    fun init() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            val detox = literal("detox").requires { it.hasPermission(4) }.build()

            dispatcher.root.addChild(detox)

            val clear = literal("clear").executes { clearPlayers(it, null) }.build()
            detox.addChild(clear)

            val clearEntities = argument("target", EntityArgument.entities())
                .executes { clearPlayers(it, EntityArgument.getEntities(it, "target")) }
                .build()
            clear.addChild(clearEntities)


            val immunity = literal("immunity").build()
            detox.addChild(immunity)

            val immunityAmount = argument("amount", IntegerArgumentType.integer())
                .executes {
                    val src = it.source
                    val player = src.player ?: return@executes -1
                    player.setImmunity(IntegerArgumentType.getInteger(it, "amount"))
                    0
                }
                .build()
            immunity.addChild(immunityAmount)


            val potent = literal("potent").build()
            detox.addChild(potent)

            val potentAmount = argument("amount", IntegerArgumentType.integer())
                .executes {
                    val src = it.source
                    val player = src.player ?: return@executes -1
                    player.setPotent(IntegerArgumentType.getInteger(it, "amount"))
                    0
                }
                .build()
            potent.addChild(potentAmount)
        }
    }

    fun clearPlayers(ctx: CommandContext<CommandSourceStack>, entities: Collection<Entity>?): Int {
        val src = ctx.source

        var targets = entities

        if (targets == null) {
            val player = src.player
            if (player == null) {
                src.sendFailure(text("Command must be run as player!"))
                return -1
            }
            targets = listOf(player)
        }

        if (targets.isEmpty()) {
            src.sendFailure(text("Command must have at least one target!"))
            return -1
        }

        for (entity in targets.filterIsInstance<LivingEntity>()) {
            entity.clearPotent()
            entity.clearImmunity()
        }

        val targetCount = targets.size
        if (targetCount > 1) {
            src.sendSuccess({ text("Clear all effects from %s entities", targetCount) }, true)
            return targetCount
        }

        src.sendSuccess({ text("Clear all effects from %s", targets.first().name) }, true)
        return Command.SINGLE_SUCCESS
    }

    fun text(message: String, vararg args: Any): MutableComponent = Component.translatable(message, *args)

}