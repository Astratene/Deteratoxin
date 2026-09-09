package org.teamvoided.deteratoxin.block

import com.mojang.serialization.MapCodec
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RodBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.deteratoxin.entity.DeteratoxinCloudEntity
import org.teamvoided.deteratoxin.util.createShapeMap


class StarletteLilyBlock(properties: Properties) : RodBlock(properties) {

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(FACING, Direction.UP)
                .setValue(DISARMED, false)
                .setValue(CAN_SUMMON, false)
        )
    }

    override fun codec() = CODEC

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(FACING, DISARMED, CAN_SUMMON)
    }

    override fun canSurvive(state: BlockState, level: LevelReader, blockPos: BlockPos): Boolean {
        val dir = state.getValue(FACING)
        val pos = blockPos.relative(dir.opposite)
        return isFaceFull(level.getBlockState(pos).getCollisionShape(level, pos), dir)
    }

    override fun updateShape(
        state: BlockState, dir: Direction, neighborState: BlockState,
        level: LevelAccessor, pos: BlockPos, neighborPos: BlockPos,
    ): BlockState {
        return if (state.getValue(FACING).opposite == dir && !state.canSurvive(level, pos))
            Blocks.AIR.defaultBlockState()
        else
            super.updateShape(state, dir, neighborState, level, pos, neighborPos)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, ctx: CollisionContext): VoxelShape {
        return SHAPES[state.getValue(FACING)] ?: Shapes.block()
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val dir = ctx.clickedFace
        val state = ctx.level.getBlockState(ctx.clickedPos.relative(dir.opposite))
        // Should this stay? It only adds placement for one very specific direction
        return if (state.`is`(this) && state.getValue(FACING) == dir)
            defaultBlockState().setValue(FACING, dir.opposite)
        else
            defaultBlockState().setValue(FACING, dir)
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(CAN_SUMMON) && !state.getValue(DISARMED)) {
            level.setBlockAndUpdate(pos, state.setValue(CAN_SUMMON, true))
        }
    }

    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(DISARMED)) {
            return
        }
        if (state.getValue(CAN_SUMMON)) {
            world.scheduleTick(pos, this, random.nextIntBetweenInclusive(200, 300))
        } else {
            val cloud = DeteratoxinCloudEntity(world)
            cloud.setPos(pos.x + 0.5, pos.y - 1.5, pos.z + 0.5)
            world.addFreshEntity(cloud)
        }
    }

    override fun useItemOn(
        stack: ItemStack, state: BlockState, level: Level, pos: BlockPos,
        player: Player, hand: InteractionHand, hit: BlockHitResult,
    ): ItemInteractionResult {
        if (stack.`is`(ConventionalItemTags.SHEAR_TOOLS) && !state.getValue(DISARMED)) {
            level.setBlockAndUpdate(
                pos, state.setValue(CAN_SUMMON, false).setValue(DISARMED, true)
            )
            level.playSound(
                null, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0f
            )
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand))
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit)
    }

    companion object {

        val CODEC: MapCodec<StarletteLilyBlock> = simpleCodec(::StarletteLilyBlock)

        val DISARMED: BooleanProperty = BlockStateProperties.DISARMED
        val CAN_SUMMON: BooleanProperty = BlockStateProperties.CAN_SUMMON

        val UP_SHAPE: VoxelShape = box(1.0, 0.0, 1.0, 15.0, 5.0, 15.0)
        val DOWN_SHAPE: VoxelShape = box(1.0, 11.0, 1.0, 15.0, 16.0, 15.0)
        val NORTH_SHAPE: VoxelShape = box(1.0, 1.0, 11.0, 15.0, 15.0, 16.0)
        val SHAPES = createShapeMap(DOWN_SHAPE, UP_SHAPE, NORTH_SHAPE)

    }
}