package team.zeds.testkotlin

import net.fabricmc.api.ModInitializer

class Main: ModInitializer {
    override fun onInitialize() {
        MyMultiblocks.init()
    }
}