package team.zeds.structured

import team.zeds.structured.settings.MultiBlockStructure

object MultiblockRegistries {
    private var multiblocks: MutableList<MultiBlockStructure>? = null

    @JvmStatic
    fun setupMultiblockSystem() {
        multiblocks = mutableListOf()
    }

    @JvmStatic
    fun registerMultiblock(structure: MultiBlockStructure) {
        multiblocks!!.add(structure)
    }
}
