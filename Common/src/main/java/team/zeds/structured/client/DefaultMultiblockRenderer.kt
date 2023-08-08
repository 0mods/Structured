package team.zeds.structured.client

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import team.zeds.structured.api.AbstractBlockEntityRenderer
import team.zeds.structured.block.entity.MultiCoreBlockEntity

class DefaultMultiblockRenderer(context: BlockEntityRendererProvider.Context) : AbstractBlockEntityRenderer<MultiCoreBlockEntity>(
    context
) {
    override fun render(blockEntity: MultiCoreBlockEntity, partialTick: Float, stack: PoseStack, buf: MultiBufferSource, xMouse: Int, yMouse: Int) {
        val facing: Direction = blockEntity.blockState.getValue(HorizontalDirectionalBlock.FACING)

        when(facing) {
            Direction.NORTH -> stack.translate(blockEntity.offset.x, blockEntity.offset.y, blockEntity.offset.z)
            Direction.SOUTH -> stack.translate(-blockEntity.offset.x, blockEntity.offset.y, -blockEntity.offset.z)
            Direction.EAST -> stack.translate(-blockEntity.offset.z, blockEntity.offset.y, blockEntity.offset.x)
            Direction.WEST -> stack.translate(blockEntity.offset.z, blockEntity.offset.y, -blockEntity.offset.x)
            else -> {}
        }
    }
}