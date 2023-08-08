package team.zeds.structured

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import team.zeds.structured.block.entity.MultiCoreBlockEntity
import team.zeds.structured.settings.MultiBlockStorage
import team.zeds.structured.settings.MultiblockStructure
import team.zeds.structured.settings.MultiblockConfig
import team.zeds.structured.settings.MultiblockLayer

class MultiblockBuilder {
    private lateinit var config: MultiblockConfig
    private val layers = ArrayList<MultiblockLayer.() -> Unit>()
    private val structure = HashMap<BlockPos, Block>()
    private var yIndex = 0

    /**
     * Method [build] is the final construction of the [MultiblockBuilder] and returns [MultiblockStructure]
     * @return [MultiblockStructure]
     */
    fun build(): MultiblockStructure {
        for (layer in layers.reversed()) {
            val layerImpl = MultiblockLayer(yIndex)
            layer.invoke(layerImpl)
            structure.putAll(layerImpl.layer)

            yIndex++
        }

        MultiBlockStorage.multiBlockActions[config.modelName] = config.onOpen
        return MultiblockStructure(structure, config)
    }

    /**
     * Method [layer] sets which blocks will be used for the multiblock.
     */
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