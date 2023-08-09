package team.zeds.structured.settings

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Direction.*
import net.minecraft.core.Vec3i
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.joml.Vector3d
import team.zeds.structured.block.entity.MultiCoreBlockEntity
import team.zeds.structured.client.DefaultMultiblockRenderer

object MultiBlockStorage {
    val multiBlockActions: HashMap<String, (Player, BlockPos) -> Unit> = HashMap()
}

private fun Vec3i.northI(): Vec3i {
    return Vec3i(this.x, this.y, -this.z)
}

private fun Vec3i.southI(): Vec3i {
    return Vec3i(-this.x, this.y, this.z)
}

private operator fun Vec3i.unaryMinus(): Vec3i {
    return Vec3i(-this.x, -this.y, -this.x)
}

class MultiblockConfig {
    var modelName = ""
    var offset = Vector3d(0.0, 0.0, 0.0)
    var onOpen: (Player, BlockPos) -> Unit = { _, _ -> }
    var render: (BlockEntityRendererProvider.Context) -> BlockEntityRenderer<MultiCoreBlockEntity> = { context -> DefaultMultiblockRenderer(context) }
        set(value) {
            rendersList.add(value)
            field = value
        }
    var tickOnServer: (Level, BlockPos, BlockState, MultiCoreBlockEntity) -> Unit = { _, _, _, _ -> }
    var tickOnClient: (Level, BlockPos, BlockState, MultiCoreBlockEntity) -> Unit = { _, _, _, _ -> }

    companion object {
        val rendersList: MutableList<(BlockEntityRendererProvider.Context) -> BlockEntityRenderer<MultiCoreBlockEntity>> = mutableListOf()
    }
}

class MultiblockLayer(private val yIndex: Int) {
    val layer = HashMap<BlockPos, Block>()
    private var zIndex = 0

    fun line(vararg blocks: Block) {
        blocks.indices.forEach {
            layer[BlockPos(zIndex, yIndex, it)] = blocks[it]
        }
        zIndex++
    }
}

class OffsetMultiBlockPos(private val maxX: Int, private val maxY: Int, private val maxZ: Int, private val direction: Direction) {
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0

    fun next(): Boolean {
        when (direction) {
            EAST -> {
                x--
                if (-x > maxX) {
                    x = 0
                    y--
                    if (-y > maxY) {
                        y = 0
                        z--
                        if (-z > maxZ) {
                            return false
                        }
                    }
                }
                return true
            }

            WEST -> {
                x++
                if (x > maxX) {
                    x = 0
                    y--
                    if (-y > maxY) {
                        y = 0
                        z++
                        if (z > maxZ) {
                            return false
                        }
                    }
                }
                return true
            }

            NORTH -> {
                x--
                if (-x > maxZ) {
                    x = 0
                    y--
                    if (-y > maxY) {
                        y = 0
                        z++
                        if (z > maxX) {
                            return false
                        }
                    }
                }
                return true
            }

            SOUTH -> {
                x++
                if (x > maxZ) {
                    x = 0
                    y--
                    if (-y > maxY) {
                        y = 0
                        z--
                        if (-z > maxX) {
                            return false
                        }
                    }
                }
                return true
            }

            else -> return false
        }
    }

    fun pos(): Vec3i {
        return Vec3i(x, y, z)
    }
}