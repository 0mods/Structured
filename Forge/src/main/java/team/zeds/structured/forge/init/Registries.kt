package team.zeds.structured.forge.init

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.registries.ForgeRegistries
import team.zeds.structured.Constants.reloc
import team.zeds.structured.block.MultiCoreBlock
import team.zeds.structured.block.MultiModuleBlock
import team.zeds.structured.block.entity.MultiCoreBlockEntity
import team.zeds.structured.block.entity.MultiModuleBlockEntity

object Registries {
    val multiCoreBlock = MultiCoreBlock()
    val multiModuleBlock =
        MultiModuleBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.3f).noOcclusion())

    val multiCoreBlockEntity: BlockEntityType<MultiCoreBlockEntity> =
        BlockEntityType.Builder.of({ pos, state -> MultiCoreBlockEntity(pos, state) }, multiCoreBlock).build(null)
    val multiModuleBlockEntity: BlockEntityType<MultiModuleBlockEntity> =
        BlockEntityType.Builder.of({ pos, state -> MultiModuleBlockEntity(pos, state) }, multiModuleBlock).build(null)

    @JvmStatic
    fun init() {
        ForgeRegistries.BLOCKS.register(reloc("multi_core_block"), multiCoreBlock)
        ForgeRegistries.BLOCKS.register(reloc("multi_module_block"), multiModuleBlock)
        ForgeRegistries.BLOCK_ENTITY_TYPES.register(reloc("multi_core_block"), multiCoreBlockEntity)
        ForgeRegistries.BLOCK_ENTITY_TYPES.register(reloc("multi_module_block"), multiModuleBlockEntity)
    }
}