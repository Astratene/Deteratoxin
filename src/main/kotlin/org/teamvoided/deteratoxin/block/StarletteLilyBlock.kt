package org.teamvoided.deteratoxin.block

import com.mojang.serialization.MapCodec
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.ItemStack
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RodBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import org.joml.Vector3f


class StarletteLilyBlock(settings: BlockBehaviour.Properties) : RodBlock(settings) {


    init {
        this.registerDefaultState(this.stateDefinition.any().setValue<Direction?, Direction?>(FACING, Direction.UP))
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
        when ((state!!.getValue<Direction?>(FACING) as Direction)) {
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
        return if (blockState.`is`(this) && blockState.getValue<Direction?>(FACING) == direction)
            this.defaultBlockState().setValue<Direction?, Direction?>(FACING, direction.getOpposite())
        else
            this.defaultBlockState().setValue<Direction?, Direction?>(FACING, direction)
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
//        if (random.nextIntBetweenInclusive(0, 6) == 0) {
//            val d = pos.x + 0.55 + (random.nextFloat().minus(0.5) * 20f)
//            val e = pos.y + 0.55 + (random.nextFloat().minus(0.5) * 20f)
//            val f = pos.z + 0.55 + (random.nextFloat().minus(0.5) * 20f)
//            val colours = mutableListOf<Vector3f>(
//                Vector3f(255f / 255f, 255f / 255f, 255f / 255f),
//                Vector3f(224f / 255f, 171f / 255f, 255f / 255f),
//                Vector3f(176f / 255f, 252f / 255f, 255f / 255f),
//                Vector3f(60f / 255f, 161f / 255f, 216f / 255f),
//                Vector3f(107f / 255f, 198f / 255f, 247f / 255f),
//                Vector3f(155f / 255f, 219f / 255f, 255f / 255f),
//            )
//            colours.shuffle()
//            world.sendParticles(
//                AstralParticleOptions(
//                    colours.first(),
//                    Vector3f(0f,0f,0f),
//                    2f,
//                    false,
//                    true,
//                    false,
//                    true,
//                    world.random.nextFloat().minus(0.5f).times(0.01f),
//                    100,
//                    -0.0001f,
//                    0.9f
//                ), d, e, f, 1, 0.0, 0.0, 0.0, 0.0
//            )
//        }
//        if (!world.isDay) {
//            world.scheduleTick(pos, this, 1)
//        }
    }


    override fun setPlacedBy(
        world: Level,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        if (!world.isDay) {
            world.scheduleTick(pos, this, 1)
        }
        super.setPlacedBy(world, pos, state, placer, itemStack)
    }

    override fun randomTick(state: BlockState?, world: ServerLevel, pos: BlockPos?, random: RandomSource) {
        if (!world.isDay) {
            world.scheduleTick(pos, this, random.nextIntBetweenInclusive(100, 500))
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }

    override fun codec(): MapCodec<out RodBlock?>? {
        return null
    }

}