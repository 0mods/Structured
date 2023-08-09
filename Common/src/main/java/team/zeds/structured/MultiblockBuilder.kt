package team.zeds.structured

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import team.zeds.structured.api.annots.DontUse
import team.zeds.structured.api.annots.RenamedTo
import team.zeds.structured.api.annots.Required
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
     * Method [layer] responsible for which blocks will be installed on a specific multiblock layer.
     * @param function sets a lines for layer
     */
    fun layer(function: MultiblockLayer.() -> Unit): MultiblockBuilder {
        this.layers.add(function)
        return this
    }

    /**
     * Method [configure] is the main one for creating a multiblock.
     *
     * It sets where the main block will be, what will happen when multiblock is used and whether multiblock will tick
     *
     * @param config sets a config
     */
    @RenamedTo(value = "config", since = "1.1.0")
    @Required
    fun configure(config: MultiblockConfig.()-> Unit): MultiblockBuilder {
        this.config = MultiblockConfig().apply(config)
        configList.add(this.config)
        return this
    }

    companion object {
        @DontUse
        @JvmStatic
        val configList: MutableList<MultiblockConfig> = mutableListOf()
    }
}