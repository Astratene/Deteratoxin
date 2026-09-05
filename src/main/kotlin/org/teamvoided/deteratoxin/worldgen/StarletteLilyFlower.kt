package org.teamvoided.deteratoxin.worldgen

import com.mojang.serialization.Codec
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration
import org.teamvoided.deteratoxin.data.tags.DeteratoxinBlockTags

class StarletteLilyFlower(codec: Codec<BlockStateConfiguration>) : Feature<BlockStateConfiguration>(codec) {
    override fun place(featurePlaceContext: FeaturePlaceContext<BlockStateConfiguration>): Boolean {
        val config = featurePlaceContext.config()
        val rand = featurePlaceContext.random()
        val blockPos = featurePlaceContext.origin()
        val world = featurePlaceContext.level()
        var success = false
        val xz = 10
        val y = 10
        repeat(50) {
            val pos = blockPos.offset(
                rand.nextInt(xz) * 2 - xz,
                rand.nextInt(y) * 2 - y,
                rand.nextInt(xz) * 2 - xz
            )
            if (world.getBlockState(pos).isAir) {
                for (dir in Direction.entries.shuffled()) {
                    if (world.getBlockState(pos.offset(dir.normal)).`is`(DeteratoxinBlockTags.LILY_PLACABLE)) {
                        world.setBlock(pos, config.state.trySetValue(BlockStateProperties.FACING, dir.opposite), 3)
                        success = true
                        break
                    }
                }
            }
        }
        return success
    }
}