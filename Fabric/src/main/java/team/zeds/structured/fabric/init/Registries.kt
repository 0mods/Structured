package team.zeds.structured.fabric.init

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import team.zeds.structured.Constants.reloc
import team.zeds.structured.block.MultiCoreBlock
import team.zeds.structured.block.MultiModuleBlock
import team.zeds.structured.block.entity.MultiCoreBlockEntity
import team.zeds.structured.block.entity.MultiModuleBlockEntity

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object Registries {
    val multiCoreBlock = MultiCoreBlock()
    val multiModuleBlock = MultiModuleBlock(BlockBehaviour.Properties.of().strength(0.3f).noOcclusion())

    val multiCoreBlockEntity: BlockEntityType<MultiCoreBlockEntity> =
        BlockEntityType.Builder.of({ pos, state -> MultiCoreBlockEntity(pos, state) }, multiCoreBlock).build(null)
    val multiModuleBlockEntity: BlockEntityType<MultiModuleBlockEntity> =
        BlockEntityType.Builder.of({ pos, state -> MultiModuleBlockEntity(pos, state) }, multiModuleBlock).build(null)

    @JvmStatic
    fun init() {
        Registry.register(BuiltInRegistries.BLOCK, reloc("multi_core_block"), multiCoreBlock)
        Registry.register(BuiltInRegistries.BLOCK, reloc("multi_module_block"), multiModuleBlock)
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, reloc("multi_core_block"), multiModuleBlockEntity)
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, reloc("multi_module_block"), multiModuleBlockEntity)
    }
}