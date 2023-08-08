package team.zeds.structured

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import team.zeds.structured.MultiblockBuilder
import team.zeds.structured.init.Registries

class StructuredClient: ClientModInitializer {
    override fun onInitializeClient() {
        for (entry in MultiblockBuilder.configList) {
            BlockEntityRenderers.register(Registries.multiCoreBlockEntity) { a -> entry.render.invoke(a) }
        }
    }
}