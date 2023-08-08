package team.zeds.structured.platform

import team.zeds.structured.Constants
import team.zeds.structured.platform.services.IPlatformHelper
import java.util.*

object Services {
    @JvmField
    val PLATFORM: IPlatformHelper = load(IPlatformHelper::class.java)

    @JvmStatic
    fun <T> load(clazz: Class<T>): T {
        val loadedService: T = ServiceLoader.load(clazz)
            .findFirst().orElseThrow()
        Constants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz)
        return loadedService
    }
}