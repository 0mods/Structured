package team.zeds.testkotlin

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import team.zeds.structured.MultiblockBuilder
import team.zeds.structured.MultiblockRegistries.registerMultiblock
import team.zeds.structured.settings.MultiblockStructure

object MyMultiblocks {
    val myFirstMultiblock: MultiblockStructure

    init {
        myFirstMultiblock = registerMultiblock(
            MultiblockBuilder()
                .configure {
                    modelName = "my_first_multiblock"
                    onOpen = { player: Player, pos: BlockPos ->
                        //there your code
                    }
                }
                .build()
        )
    }
    @JvmStatic
    fun init() {}
}