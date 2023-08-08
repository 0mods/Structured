package team.zeds.structured.init

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import team.zeds.structured.Constants
import team.zeds.structured.block.MultiCoreBlock
import team.zeds.structured.block.MultiModuleBlock
import team.zeds.structured.block.entity.MultiCoreBlockEntity
import team.zeds.structured.block.entity.MultiModuleBlockEntity

object Registries {
    @JvmStatic
    private val block: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.KEY)
    @JvmStatic
    private val blockEntity: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.KEY)

    @JvmField
    val multiCoreBlock: RegistryObject<MultiCoreBlock> = block.register("multi_core_block") { MultiCoreBlock() }
    @JvmField
    val multiModuleBlock: RegistryObject<MultiModuleBlock> = block.register("multi_module_block") {
        MultiModuleBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(0.3f).noOcclusion())
    }

    @JvmField
    val multiCoreBlockEntity: RegistryObject<BlockEntityType<MultiCoreBlockEntity>> = blockEntity.register("multi_core_block") {
        BlockEntityType.Builder.of({ pos, state -> MultiCoreBlockEntity(pos, state) }, multiCoreBlock.get()).build(null)
    }
    @JvmField
    val multiModuleBlockEntity: RegistryObject<BlockEntityType<MultiModuleBlockEntity>> = blockEntity.register("multi_module_block") {
        BlockEntityType.Builder.of({ pos, state -> MultiModuleBlockEntity(pos, state) }, multiModuleBlock.get()).build(null)
    }

    @JvmStatic
    fun init() {
        val bus = FMLJavaModLoadingContext.get().modEventBus

        block.register(bus)
        blockEntity.register(bus)
    }
}