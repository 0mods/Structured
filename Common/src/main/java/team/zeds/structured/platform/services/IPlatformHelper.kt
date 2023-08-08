package team.zeds.structured.platform.services

import net.minecraft.world.level.block.entity.BlockEntityType
import team.zeds.structured.block.entity.MultiCoreBlockEntity
import team.zeds.structured.block.entity.MultiModuleBlockEntity

interface IPlatformHelper {
    fun getMultiCoreBlockEntityType(): BlockEntityType<MultiCoreBlockEntity>

    fun getMultiModuleBlockEntityType(): BlockEntityType<MultiModuleBlockEntity>
}