package team.zeds.structured.api

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

interface ITickedBlockEntity<T: BlockEntity?> {
    fun tickOnClient(level: Level, pos: BlockPos, state: BlockState, entity: T) {}
    fun tickOnServer(level: Level, pos: BlockPos, state: BlockState, entity: T) {}
}