package team.zeds.structured.platform

import net.minecraft.world.level.block.entity.BlockEntityType
import team.zeds.structured.block.entity.MultiCoreBlockEntity
import team.zeds.structured.block.entity.MultiModuleBlockEntity
import team.zeds.structured.init.Registries
import team.zeds.structured.platform.services.IPlatformHelper

class FabricPlatformHelper: IPlatformHelper {
    override fun getMultiCoreBlockEntityType(): BlockEntityType<MultiCoreBlockEntity> = Registries.multiCoreBlockEntity

    override fun getMultiModuleBlockEntityType(): BlockEntityType<MultiModuleBlockEntity> = Registries.multiModuleBlockEntity
}