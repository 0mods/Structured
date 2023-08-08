package team.zeds.structured.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState
import team.zeds.structured.api.BlockEntityBase
import team.zeds.structured.platform.Services

class MultiModuleBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntityBase<MultiModuleBlockEntity>(
    Services.PLATFORM.getMultiModuleBlockEntityType(), blockPos, blockState
) {
    var corePos: BlockPos? = null

    fun breakMultiBlock() {
        if (level == null || corePos == null) return

        level!!.getBlockEntity(corePos!!)?.let {
            val tile = it as MultiCoreBlockEntity

            if(!tile.isDestroying) {
                tile.breakMultiBlock()
            }
        }
    }

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)
        if (corePos != null) {
            tag.putInt("coreX", corePos!!.x)
            tag.putInt("coreY", corePos!!.y)
            tag.putInt("coreZ", corePos!!.z)
        }
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        if (tag.contains("coreX") && tag.contains("coreY") && tag.contains("coreZ")) {
            corePos = BlockPos(tag.getInt("coreX"), tag.getInt("coreY"), tag.getInt("coreZ"))
        }
    }
}