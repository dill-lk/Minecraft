/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.structure.structures;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Vec3i;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.StructureManager;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.Heightmap;
import net.mayaan.world.level.levelgen.structure.BoundingBox;
import net.mayaan.world.level.levelgen.structure.StructurePiece;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePieceType;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;

public class BuriedTreasurePieces {

    public static class BuriedTreasurePiece
    extends StructurePiece {
        public BuriedTreasurePiece(BlockPos offset) {
            super(StructurePieceType.BURIED_TREASURE_PIECE, 0, new BoundingBox(offset));
        }

        public BuriedTreasurePiece(CompoundTag tag) {
            super(StructurePieceType.BURIED_TREASURE_PIECE, tag);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
            int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.boundingBox.minX(), this.boundingBox.minZ());
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(this.boundingBox.minX(), y, this.boundingBox.minZ());
            while (pos.getY() > level.getMinY()) {
                BlockState currentState = level.getBlockState(pos);
                BlockState belowState = level.getBlockState((BlockPos)pos.below());
                if (belowState.is(Blocks.SANDSTONE) || belowState.is(Blocks.STONE) || belowState.is(Blocks.ANDESITE) || belowState.is(Blocks.GRANITE) || belowState.is(Blocks.DIORITE)) {
                    BlockState softState = currentState.isAir() || BuriedTreasurePiece.isLiquid(currentState) ? Blocks.SAND.defaultBlockState() : currentState;
                    for (Direction direction : Direction.values()) {
                        Vec3i relativePos = pos.relative(direction);
                        BlockState relativeState = level.getBlockState((BlockPos)relativePos);
                        if (!relativeState.isAir() && !BuriedTreasurePiece.isLiquid(relativeState)) continue;
                        BlockPos belowRelativePos = ((BlockPos)relativePos).below();
                        BlockState belowRelativeState = level.getBlockState(belowRelativePos);
                        if ((belowRelativeState.isAir() || BuriedTreasurePiece.isLiquid(belowRelativeState)) && direction != Direction.UP) {
                            level.setBlock((BlockPos)relativePos, belowState, 3);
                            continue;
                        }
                        level.setBlock((BlockPos)relativePos, softState, 3);
                    }
                    this.boundingBox = new BoundingBox(pos);
                    this.createChest(level, chunkBB, random, pos, BuiltInLootTables.BURIED_TREASURE, null);
                    return;
                }
                pos.move(0, -1, 0);
            }
        }

        private static boolean isLiquid(BlockState blockState) {
            return blockState.is(Blocks.WATER) || blockState.is(Blocks.LAVA);
        }
    }
}

