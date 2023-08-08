package team.zeds.structured

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.spongepowered.asm.mixin.Mutable
import team.zeds.structured.block.entity.MultiCoreBlockEntity
import team.zeds.structured.settings.MultiBlockStorage
import team.zeds.structured.settings.MultiBlockStructure
import team.zeds.structured.settings.MultiblockConfig
import team.zeds.structured.settings.MultiblockLayer

class MultiblockBuilder {
    private lateinit var config: MultiblockConfig
    private val layers = ArrayList<MultiblockLayer.() -> Unit>()
    private val structure = HashMap<BlockPos, Block>()
    private var yIndex = 0

    fun build(): MultiBlockStructure {
        for (layer in layers.reversed()) {
            val layerImpl = MultiblockLayer(yIndex)
            layer.invoke(layerImpl)
            structure.putAll(layerImpl.layer)

            yIndex++
        }

        MultiBlockStorage.multiBlockActions[config.modelName] = config.onOpen
        return MultiBlockStructure(structure, config)
    }

    fun layer(function: MultiblockLayer.() -> Unit): MultiblockBuilder {
        this.layers.add(function)
        return this
    }

    fun serverTick(function: (Level, BlockPos, BlockState, MultiCoreBlockEntity) -> Unit): MultiblockBuilder {
        this.config.tickOnServer = function
        return this
    }

    fun clientTick(function: (Level, BlockPos, BlockState, MultiCoreBlockEntity) -> Unit): MultiblockBuilder {
        this.config.tickOnServer = function
        return this
    }

    fun configure(config: MultiblockConfig.()-> Unit): MultiblockBuilder {
        this.config = MultiblockConfig().apply(config)
        configList.add(this.config)
        return this
    }

    companion object {
        @JvmStatic
        val configList: MutableList<MultiblockConfig> = mutableListOf()
    }
}