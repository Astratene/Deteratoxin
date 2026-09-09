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
        val random = featurePlaceContext.random()
        val originPos = featurePlaceContext.origin()
        val level = featurePlaceContext.level()
        var success = false
        // Make in to config and export to reef later
        val xzSpread = 10
        val ySpread = 10
        val attempts = 50
        val canPlaceOnTag = DeteratoxinBlockTags.SUPPORTS_LILY
        repeat(attempts) {
            val pos = originPos.offset(
                // use this instead? - random.nextInt(-xzSpread, xzSpread)
                random.nextInt(xzSpread) * 2 - xzSpread,
                random.nextInt(ySpread) * 2 - ySpread,
                random.nextInt(xzSpread) * 2 - xzSpread
            )
            if (level.getBlockState(pos).isAir) {
                for (dir in Direction.entries.shuffled()) {
                    if (level.getBlockState(pos.offset(dir.normal)).`is`(canPlaceOnTag)) {
                        level.setBlock(pos, config.state.trySetValue(BlockStateProperties.FACING, dir.opposite), 3)
                        success = true
                        break
                    }
                }
            }
        }
        return success
    }

}