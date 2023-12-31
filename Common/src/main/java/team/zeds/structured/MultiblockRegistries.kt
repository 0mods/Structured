package team.zeds.structured

import team.zeds.structured.settings.MultiblockStructure
/**
 * Class [MultiblockRegistries] is a registrar for multiblocks
 *
 * Usage example:
 *
 * Java:
 *
 * ```java
 * public class MyRegistry {
 *      public MyRegistry() {
 *          MultiblockRegistries.registerMultiblock(
 *              new MultiblockBuilder().layer(layer -> {
 *                  // Multiblock layer of:
 *                  // IAI
 *                  // SIS
 *                  // IAI
 *                  // Where I - Iron Block, A - Andesite, S - Stone
 *                  layer.line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK);
 *                  layer.line(Blocks.STONE, Blocks.IRON_BLOCK, Blocks.STONE);
 *                  layer.line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK);
 *                  // Return Unit.INSTANCE otherwise java will throw an error. (I don't know why, if Unit is Void)
 *                  return Unit.INSTANCE;
 *              }).layer(layer -> {
 *                  layer.line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK);
 *                  layer.line(Blocks.STONE, Blocks.IRON_BLOCK, Blocks.STONE);
 *                  layer.line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK);
 *                  return Unit.INSTANCE;
 *              }).layer(layer -> {
 *                  layer.line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK);
 *                  layer.line(Blocks.STONE, Blocks.IRON_BLOCK, Blocks.STONE);
 *                  layer.line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK);
 *                  return Unit.INSTANCE;
 *              }).configure(config -> {
 *                  // REQUIRED
 *                  config.setModelName("my_first_multiblock");
 *                  config.setOffset(new Vector3d(/* Your vector pos */));
 *                  config.setOnOpen(player, pos -> {
 *                      // Your function, if you click on block right click
 *                      return Unit.INSTANCE;
 *                  });
 *                  // OPTIONAL
 *                  config.setRender(DefaultMultiblockRenderer::new /* replace DefaultMultiblockRenderer to your blockentity render. WARNING! BlockEntity for render - MultiCoreBlockEntity*/);
 *                  config.setTickOnServer(level, pos, state, entity -> {
 *                      // Your server Tick function
 *                      return Unit.INSTANCE;
 *                  }) // or setTickOnClient
 *                  return Unit.INSTANCE;
 *              }).build(); // Required for finalized multiblock
 *          );
 *      }
 * }
 *```
 *
 * Kotlin
 *
 * ```kotlin
 * class MyClass {
 *      val myFirstMultiblock: MultiblockStructure
 *      init {
 *          myFirstMultiblock = MultiblockRegistries.registerMultiblock(
 *              MultiblockBuilder()
 *                  // Multiblock layer of:
 *                  // IAI
 *                  // SIS
 *                  // IAI
 *                  // Where I - Iron Block, A - Andesite, S - Stone
 *                  .layer {
 *                      line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK)
 *                      line(Blocks.STONE, Blocks.IRON_BLOCK, Blocks.STONE)
 *                      line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK)
 *                  }
 *                  .layer {
 *                      line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK)
 *                      line(Blocks.STONE, Blocks.IRON_BLOCK, Blocks.STONE)
 *                      line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK)
 *                  }
 *                  .layer {
 *                      line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK)
 *                      line(Blocks.STONE, Blocks.IRON_BLOCK, Blocks.STONE)
 *                      line(Blocks.IRON_BLOCK, Blocks.ANDESITE, Blocks.IRON_BLOCK)
 *                  }
 *                  .configure {
 *                      // REQUIRED
 *                      modelName = "my_first_multiblock"
 *                      offset = Vector3d(/* Your vector pos */)
 *                      onOpen = { player, pos ->
 *                          // Your function, if you click on block right click
 *                      }
 *                      // OPTIONAL
 *                      render = {
 *                          DefaultMultiblockRenderer(it) // replace DefaultMultiblockRenderer to your blockentity render. WARNING! BlockEntity for render - MultiCoreBlockEntity
 *                      }
 *                      tickOnServer = { level, pos, state, entity ->
 *                          // Your Server tick function
 *                      } // or tickOnClient { ... }
 *                  }
 *                  .build() // Required for finalized multiblock
 *          )
 *      }
 * }
 * ```
 */
object MultiblockRegistries {
    private var multiblocks: MutableList<MultiblockStructure>? = null

    @JvmStatic
    fun setupMultiblockSystem() {
        multiblocks = mutableListOf()
    }

    @JvmStatic
    fun registerMultiblock(structure: MultiblockStructure): MultiblockStructure {
        multiblocks!!.add(structure)
        return structure
    }
}
