package team.zeds.structured

import net.minecraft.resources.ResourceLocation
import org.slf4j.*

object Constants {
    const val MOD_NAME = "Structured!"
    const val KEY: String = "team/zeds/structured"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_NAME)
    @JvmStatic
    fun reloc(name: String): ResourceLocation = ResourceLocation(KEY, name)
}