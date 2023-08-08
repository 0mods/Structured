package team.zeds.structured.api

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class EntityBlockBase<E: BlockEntity>(private val blockEntity: (BlockPos, BlockState) -> E, properties: Properties):
    Block(properties), EntityBlock {

    override fun newBlockEntity(p0: BlockPos, p1: BlockState): BlockEntity =
        this.blockEntity.invoke(p0, p1)

    @Suppress("UNCHECKED_CAST")
    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T> = BlockEntityTicker<T> { tickerLevel, pos, tickerState, entity ->
        if (entity is ITickedBlockEntity<*>) {
            if (tickerLevel.isClientSide) (entity as ITickedBlockEntity<T>).tickOnClient(tickerLevel, pos, tickerState, entity)
            else (entity as ITickedBlockEntity<T>).tickOnServer(tickerLevel, pos, tickerState, entity)
        }
    }
}