package team.zeds.structured.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.joml.Vector3d
import team.zeds.structured.api.BlockEntityBase
import team.zeds.structured.platform.Services
import team.zeds.structured.settings.MultiBlockStorage

class MultiCoreBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntityBase<MultiCoreBlockEntity>(
    Services.PLATFORM.getMultiCoreBlockEntityType(), blockPos, blockState
) {
    var onOpen: (Player, BlockPos) -> Unit = { _, _ -> }
    var tickOnServer: (Level, BlockPos, BlockState, MultiCoreBlockEntity) -> Unit = { _, _, _, _ -> }
    var tickOnClient: (Level, BlockPos, BlockState, MultiCoreBlockEntity) -> Unit = { _, _, _, _ -> }
    var name = "default_multiblock_name"
    var offset = Vector3d(1.0, 0.0, 0.0)
    var modules: MutableList<BlockPos> = mutableListOf()
    var isDestroying = false

    fun breakMultiBlock() {
        if (level == null) return
        isDestroying = true

        modules.forEach {
            level!!.destroyBlock(it, false)
        }

        level!!.destroyBlock(worldPosition, false)
    }

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)
        if (modules.isNotEmpty()) {
            tag.putInt("modulesCount", modules.size)
            for (i in modules.indices) {
                tag.putInt("moduleX$i", modules[i].x)
                tag.putInt("moduleY$i", modules[i].y)
                tag.putInt("moduleZ$i", modules[i].z)
            }
        }
        tag.putString("name", name)
        tag.putDouble("offsetX", offset.x)
        tag.putDouble("offsetY", offset.y)
        tag.putDouble("offsetZ", offset.z)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        if (tag.contains("modulesCount")) {
            val modulesCount = tag.getInt("modulesCount")
            for (i in 0 until modulesCount) {
                modules.add(BlockPos(tag.getInt("moduleX$i"), tag.getInt("moduleY$i"), tag.getInt("moduleZ$i")))
            }
        }
        name = tag.getString("name")
        onOpen = MultiBlockStorage.multiBlockActions[name] ?: { _, _ -> }
        offset = Vector3d(tag.getDouble("offsetX"), tag.getDouble("offsetY"), tag.getDouble("offsetZ"))
    }

    override fun tickOnServer(level: Level, pos: BlockPos, state: BlockState, entity: MultiCoreBlockEntity) {
        this.tickOnServer.invoke(level, pos, state, entity)
    }

    override fun tickOnClient(level: Level, pos: BlockPos, state: BlockState, entity: MultiCoreBlockEntity) {
        this.tickOnClient.invoke(level, pos, state, entity)
    }
}