package team.zeds.structured.forge

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment
import team.zeds.structured.Constants
import team.zeds.structured.MultiblockBuilder
import team.zeds.structured.forge.init.Registries

@Mod(Constants.KEY)
class Structured {
    init {
        Registries.init()

        if (FMLEnvironment.dist.isClient) {
            for (entry in MultiblockBuilder.configList) {
                BlockEntityRenderers.register(Registries.multiCoreBlockEntity.get()) { a -> entry.render.invoke(a) }
            }
        }
    }
}