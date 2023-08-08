package team.zeds.structured.api

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class BlockEntityBase<T: BlockEntityBase<T>>(
    blockEntityType: BlockEntityType<*>,
    blockPos: BlockPos,
    blockState: BlockState
): BlockEntity(
    blockEntityType,
    blockPos,
    blockState
), ITickedBlockEntity<T> {
    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? =
        ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata)

    override fun getUpdateTag(): CompoundTag = saveWithFullMetadata()
}