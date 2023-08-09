package team.zeds.testjava;

import kotlin.Unit;
import team.zeds.structured.MultiblockBuilder;
import team.zeds.structured.MultiblockRegistries;
import team.zeds.structured.settings.MultiblockStructure;

public class MyMultiblocks {
    public static final MultiblockStructure MY_FIRST_MULTIBLOCK;

    static {
        MY_FIRST_MULTIBLOCK = MultiblockRegistries.registerMultiblock(
                new MultiblockBuilder()
                        .configure(config -> {
                            config.setModelName("my_first_multiblock");
                            config.setOnOpen((player, pos) -> {
                                //My code
                                return Unit.INSTANCE;
                            });
                            return Unit.INSTANCE;
                        })
                        .build()
        );
    }

    public static void init() {}
}
