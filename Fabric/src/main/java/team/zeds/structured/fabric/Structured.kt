package team.zeds.structured.fabric

import net.fabricmc.api.ModInitializer
import team.zeds.structured.MultiblockBuilder
import team.zeds.structured.MultiblockRegistries
import team.zeds.structured.fabric.init.Registries

class Structured: ModInitializer {
    override fun onInitialize() {
        Registries.init()
        MultiblockRegistries.setupMultiblockSystem()
    }
}