package team.zeds.testkotlin

import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class ItemActivator(properties: Properties) : Item(properties) {
    override fun useOn(useOnContext: UseOnContext): InteractionResult {
        MyMultiblocks.myFirstMultiblock.place(
            useOnContext.level, useOnContext.clickedPos, useOnContext.clickedFace,
            useOnContext.level.getBlockState(useOnContext.clickedPos)
        )

        return if (MyMultiblocks.myFirstMultiblock.tryBuild(
                useOnContext.level, useOnContext.clickedPos, useOnContext.clickedFace,
                useOnContext.level.getBlockState(useOnContext.clickedPos)
            )
        ) {
            InteractionResult.SUCCESS
        } else {
            InteractionResult.FAIL
        }
    }
}