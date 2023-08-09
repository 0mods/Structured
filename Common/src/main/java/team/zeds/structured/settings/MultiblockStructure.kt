package team.zeds.structured.settings

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import team.zeds.structured.Constants
import team.zeds.structured.block.entity.MultiCoreBlockEntity
import team.zeds.structured.block.entity.MultiModuleBlockEntity

class MultiblockStructure(private val structure: HashMap<BlockPos, Block>, private val config: MultiblockConfig) {
    /**
     * Method [tryBuild] is used in special block/item for generating multiblock structure
     */
    fun tryBuild(level: Level, pos: BlockPos, front: Direction, state: BlockState): Boolean {
        if (!this.hasBlock(level.getBlockState(pos).block)) return false

        if (canPlaceAt(level, pos, front)) {
            place(level, pos, front, state)
            return true
        } else {
            if (canPlaceAt(level, pos, Direction.NORTH)) {
                place(level, pos, Direction.NORTH, state)
                return true
            } else if (canPlaceAt(level, pos, Direction.EAST)) {
                place(level, pos, Direction.EAST, state)
                return true
            } else if (canPlaceAt(level, pos, Direction.SOUTH)) {
                place(level, pos, Direction.SOUTH, state)
                return true
            } else if (canPlaceAt(level, pos, Direction.WEST)) {
                place(level, pos, Direction.WEST, state)
                return true
            }
        }

        return false
    }

    fun place(level: Level, pos: BlockPos, front: Direction, state: BlockState) {
        val cornerPos = getCorner(level, pos, front)

        level.setBlockAndUpdate(
            cornerPos, state.setValue(
                HorizontalDirectionalBlock.FACING, front
            )
        )

        forEachBlock(cornerPos, front) { modulePos ->
            level.setBlockAndUpdate(modulePos, state)

            level.getBlockEntity(modulePos)?.let { (it as MultiModuleBlockEntity).corePos = cornerPos }
            level.getBlockEntity(cornerPos)?.let { (it as MultiCoreBlockEntity).modules.add(modulePos) }
        }

        level.getBlockEntity(cornerPos)?.let {
            val core = it as MultiCoreBlockEntity
            core.isDestroying = false
            core.name = config.modelName
            core.offset = config.offset
            core.onOpen = config.onOpen
            core.tickOnClient = config.tickOnClient
            core.tickOnServer = config.tickOnServer
            core.setChanged()
        }
    }

    private fun getCorner(level: Level, pos: BlockPos, front: Direction): BlockPos {
        val offset = OffsetMultiBlockPos(getWidth(), getHeight(), getDepth(), front)

        while (true) {
            if (checkStructure(level, pos.offset(offset.pos()), front)) {
                return pos.offset(offset.pos())
            }

            if (!offset.next()) break
        }

        Constants.LOGGER.warn("Can't find corner of structure at $pos")
        return pos
    }

    private fun canPlaceAt(level: Level, pos: BlockPos, front: Direction): Boolean {
        val offset = OffsetMultiBlockPos(getWidth(), getHeight(), getDepth(), front)

        while (true) {
            if (checkStructure(level, pos.offset(offset.pos()), front)) {
                return true
            }

            if (!offset.next()) break
        }

        return false
    }

    private fun checkStructure(level: Level, cornerPos: BlockPos, facing: Direction): Boolean {
        when (facing) {
            Direction.EAST -> {
                for ((blockPos, block) in structure) {
                    val checkBlock = level.getBlockState(blockPos.offset(cornerPos)).block
                    if (checkBlock != block) return false
                }
            }

            Direction.WEST -> {
                for ((blockPos, block) in structure) {
                    val checkPos = cornerPos.offset(-blockPos.x, blockPos.y, -blockPos.z)

                    val checkBlock = level.getBlockState(checkPos).block
                    if (checkBlock != block) return false
                }
            }

            Direction.NORTH -> {
                for ((blockPos, block) in structure) {
                    val checkPos = cornerPos.offset(blockPos.z, blockPos.y, -blockPos.x)

                    val checkBlock = level.getBlockState(checkPos).block
                    if (checkBlock != block) return false
                }
            }

            Direction.SOUTH -> {
                for ((blockPos, block) in structure) {
                    val checkPos = cornerPos.offset(-blockPos.z, blockPos.y, blockPos.x)

                    val checkBlock = level.getBlockState(checkPos).block
                    if (checkBlock != block) return false
                }
            }

            else -> return false
        }
        return true
    }

    private fun hasBlock(pos: Block): Boolean {
        return structure.containsValue(pos)
    }

    private fun getWidth(): Int {
        var maxX = 0

        for (pos in structure.keys) {
            if (pos.x > maxX) maxX = pos.x
        }

        return maxX
    }

    private fun getHeight(): Int {
        var maxY = 0

        for (pos in structure.keys) {
            if (pos.y > maxY) maxY = pos.y
        }

        return maxY
    }

    private fun getDepth(): Int {
        var maxZ = 0

        for (pos in structure.keys) {
            if (pos.z > maxZ) maxZ = pos.z
        }

        return maxZ
    }

    private fun forEachBlock(cornerPos: BlockPos, direction: Direction, action: (BlockPos) -> Unit) {
        when (direction) {
            Direction.EAST -> {
                for (blockPos in structure.keys) {
                    val pos = blockPos.offset(cornerPos)
                    if (pos == cornerPos || structure[pos] == Blocks.AIR) continue

                    action(pos)
                }
            }

            Direction.WEST -> {
                for (blockPos in structure.keys) {
                    val pos = cornerPos.offset(-blockPos.x, blockPos.y, -blockPos.z)
                    if (pos == cornerPos || structure[pos] == Blocks.AIR) continue

                    action(pos)
                }
            }

            Direction.NORTH -> {
                for (blockPos in structure.keys) {
                    val pos = cornerPos.offset(blockPos.z, blockPos.y, -blockPos.x)
                    if (pos == cornerPos || structure[pos] == Blocks.AIR) continue

                    action(pos)
                }
            }

            Direction.SOUTH -> {
                for (blockPos in structure.keys) {
                    val pos = cornerPos.offset(-blockPos.z, blockPos.y, blockPos.x)
                    if (pos == cornerPos || structure[pos] == Blocks.AIR) continue

                    action(pos)
                }
            }

            else -> {
                Constants.LOGGER.warn("Can't iterate structure with direction $direction")
            }
        }
    }

    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        builder.append("\n")

        for (y in 0..getHeight()) {
            for (z in 0..getDepth()) {
                for (x in 0..getWidth()) {
                    val pos = BlockPos(x, y, z)
                    val block = structure[pos]

                    if (block != null) {
                        builder.append(block.name.string + "\t")
                    } else {
                        builder.append("_\t")
                    }
                }

                builder.append("\n")
            }

            builder.append("\n")
        }

        return builder.toString()
    }
}