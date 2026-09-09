@file:Suppress("unused")

package org.teamvoided.deteratoxin.util

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.Registry

import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.deteratoxin.Deteratoxin.MODID


fun isDev() = FabricLoader.getInstance().isDevelopmentEnvironment

fun <T : Any> isModHolder(holder: Holder<T>) = holder.`is` { it.location().namespace == MODID }

fun <T : Any> getModHolders(registry: Registry<T>): List<Holder.Reference<T>> = registry.holders()
    .filter(::isModHolder)
    .toList()

fun <T : Any> getModEntries(registry: Registry<T>): List<T> = registry.holders()
    .filter(::isModHolder)
    .map(Holder<T>::value)
    .toList()

fun <V : Any, T : V> Registry<V>.register(id: ResourceLocation, entry: T): T = Registry.register(this, id, entry)
fun <V : Any, T : V> Registry<T>.registerHolder(id: ResourceLocation, entry: T): Holder.Reference<T> =
    Registry.registerForHolder(this, id, entry)

fun <T : Any, R : Registry<T>> ResourceKey<R>.tag(id: ResourceLocation): TagKey<T> = TagKey.create(this, id)
fun <T : Any, R : Registry<T>> ResourceKey<R>.key(id: ResourceLocation): ResourceKey<T> = ResourceKey.create(this, id)

fun Player.giveItem(stack: ItemStack) {
    if (!addItem(stack)) {
        drop(stack, false)
    }
}

fun Entity.customDamage(
    type: ResourceKey<DamageType>, amount: Float, source: Entity? = null, attacker: Entity? = null,
): Boolean {
    return hurt(damageSources().source(type, source, attacker), amount)
}

fun createShapeMap(down: VoxelShape, up: VoxelShape, side: VoxelShape): Map<Direction, VoxelShape> {
    return Direction.entries.associateWith { dir ->
        when (dir) {
            Direction.DOWN -> down
            Direction.UP -> up
            else -> side.rotate(dir.opposite.get2DDataValue())
        }
    }
}

fun VoxelShape.rotate(times: Int): VoxelShape {
    val shapes = arrayOf(this, Shapes.empty())
    for (i in 0 until times) {
        shapes[0].forAllBoxes { minX, minY, minZ, maxX, maxY, maxZ ->
            shapes[1] = Shapes.or(
                shapes[1], Shapes.box(
                    1 - maxZ, minY, minX,
                    1 - minZ, maxY, maxX
                )
            )
        }
        shapes[0] = shapes[1]
        shapes[1] = Shapes.empty()
    }
    return shapes[0]
}

fun modifyTab(tab: ResourceKey<CreativeModeTab>, modifyEntries: FabricItemGroupEntries.() -> Unit) {
    ItemGroupEvents.modifyEntriesEvent(tab).register(modifyEntries)
}

fun Player.playPlayerSound(event: SoundEvent) {
    level().playSound(null, this.x, this.y, this.z, event, SoundSource.PLAYERS, 2.0F, 2.0f)
}
