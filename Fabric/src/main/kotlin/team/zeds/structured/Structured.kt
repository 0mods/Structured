package team.zeds.structured

import net.fabricmc.api.ModInitializer
import team.zeds.structured.MultiblockRegistries
import team.zeds.structured.init.Registries

class Structured: ModInitializer {
    override fun onInitialize() {
        Registries.init()
        MultiblockRegistries.setupMultiblockSystem()
    }
}