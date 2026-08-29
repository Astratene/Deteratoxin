package org.teamvoided.deteratoxin.block

import com.mojang.serialization.MapCodec
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
import net.minecraft.world.item.ShearsItem
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RodBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.deteratoxin.entity.DeteratoxinCloudEntity


class StarletteLilyBlock(settings: BlockBehaviour.Properties) : RodBlock(settings) {


    init {
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.UP)
            .setValue(DISARMED, false)
            .setValue(CAN_SUMMON, false))

    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        //return WallMountedBlock.canPlaceAt(world, pos, state.get(FACING).opposite)
        val direction = state.getValue(FACING)
        val blockPos = pos.relative(direction.opposite)
        return isFaceFull(world.getBlockState(blockPos).getCollisionShape(world, blockPos), direction)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: LevelAccessor?,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState? {
        return if (state.getValue(FACING).getOpposite() == direction && !state.canSurvive(world, pos))
            Blocks.AIR.defaultBlockState()
        else
            super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    val Y_SHAPE: VoxelShape? = box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0)
    val Z_SHAPE: VoxelShape? = box(2.0, 2.0, 0.0, 14.0, 14.0, 3.0)
    val X_SHAPE: VoxelShape? = box(0.0, 2.0, 2.0, 3.0, 14.0, 14.0)
    val Y_SHAPE2: VoxelShape? = box(2.0, 13.0, 2.0, 14.0, 16.0, 14.0)
    val Z_SHAPE2: VoxelShape? = box(2.0, 2.0, 13.0, 14.0, 14.0, 16.0)
    val X_SHAPE2: VoxelShape? = box(13.0, 2.0, 2.0, 16.0, 14.0, 14.0)

    override fun getShape(
        state: BlockState?,
        world: BlockGetter?,
        pos: BlockPos?,
        context: CollisionContext?
    ): VoxelShape? {
        when ((state!!.getValue(FACING) as Direction)) {
            Direction.EAST -> return X_SHAPE
            Direction.SOUTH -> return Z_SHAPE
            Direction.WEST -> return X_SHAPE2
            Direction.NORTH -> return Z_SHAPE2
            Direction.UP -> return Y_SHAPE
            Direction.DOWN -> return Y_SHAPE2
            else -> return X_SHAPE
        }
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val direction = ctx.clickedFace
        val blockState = ctx.level.getBlockState(ctx.clickedPos.relative(direction.getOpposite()))
        return if (blockState.`is`(this) && blockState.getValue(FACING) == direction)
            this.defaultBlockState().setValue(FACING, direction.getOpposite())
        else
            this.defaultBlockState().setValue(FACING, direction)
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(CAN_SUMMON) && !state.getValue(DISARMED)) {
            world.setBlock(pos, state.setValue(CAN_SUMMON, true), UPDATE_ALL)
        }
    }

    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(CAN_SUMMON) && !state.getValue(DISARMED)) {
            world.scheduleTick(pos, this, random.nextIntBetweenInclusive(200, 300))
        } else if (!state.getValue(DISARMED)) {
            val cloud = DeteratoxinCloudEntity(world)
            cloud.setPos(pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5 - 2, pos.z.toDouble() + 0.5)
            world.addFreshEntity(cloud)
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(FACING, DISARMED, CAN_SUMMON)
    }

    override fun codec(): MapCodec<out RodBlock?>? {
        return null
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        world: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        blockHitResult: BlockHitResult
    ): ItemInteractionResult? {
        if (stack.item is ShearsItem && !state.getValue(DISARMED)){
            world.setBlock(pos, state.setValue(CAN_SUMMON, false), UPDATE_ALL)
            world.setBlock(pos, state.setValue(DISARMED, true), UPDATE_ALL)
            world.playSound(
                null,
                pos.x + 0.5,
                pos.y + 0.5,
                pos.z + 0.5,
                SoundEvents.SHEEP_SHEAR,
                SoundSource.PLAYERS,
                1.0F,
                1.0f
            )
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand))
            return ItemInteractionResult.sidedSuccess(world.isClientSide)
        }
        return super.useItemOn(stack, state, world, pos, player, hand, blockHitResult)
    }

    companion object {
        val DISARMED: BooleanProperty = BlockStateProperties.DISARMED
        val CAN_SUMMON: BooleanProperty = BlockStateProperties.CAN_SUMMON
    }
}