package team.zeds.testjava;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class ItemActivator extends Item {
    public ItemActivator(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        MyMultiblocks.MY_FIRST_MULTIBLOCK.place(useOnContext.getLevel(), useOnContext.getClickedPos(), useOnContext.getClickedFace(),
                useOnContext.getLevel().getBlockState(useOnContext.getClickedPos()));

        if (MyMultiblocks.MY_FIRST_MULTIBLOCK.tryBuild(useOnContext.getLevel(), useOnContext.getClickedPos(), useOnContext.getClickedFace(),
                useOnContext.getLevel().getBlockState(useOnContext.getClickedPos()))) {
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }
}
