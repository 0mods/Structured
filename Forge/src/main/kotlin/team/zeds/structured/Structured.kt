package team.zeds.structured

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment
import team.zeds.structured.init.Registries

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