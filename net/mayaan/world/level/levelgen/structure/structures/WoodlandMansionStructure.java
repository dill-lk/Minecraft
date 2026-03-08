/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import java.util.LinkedList;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.StructureManager;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.structure.BoundingBox;
import net.mayaan.world.level.levelgen.structure.Structure;
import net.mayaan.world.level.levelgen.structure.StructureType;
import net.mayaan.world.level.levelgen.structure.pieces.PiecesContainer;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.mayaan.world.level.levelgen.structure.structures.WoodlandMansionPieces;

public class WoodlandMansionStructure
extends Structure {
    public static final MapCodec<WoodlandMansionStructure> CODEC = WoodlandMansionStructure.simpleCodec(WoodlandMansionStructure::new);

    public WoodlandMansionStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        Rotation rotation = Rotation.getRandom(context.random());
        BlockPos startPos = this.getLowestYIn5by5BoxOffset7Blocks(context, rotation);
        if (startPos.getY() < 60) {
            return Optional.empty();
        }
        return Optional.of(new Structure.GenerationStub(startPos, builder -> this.generatePieces((StructurePiecesBuilder)builder, context, startPos, rotation)));
    }

    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context, BlockPos startPos, Rotation rotation) {
        LinkedList wmPieces = Lists.newLinkedList();
        WoodlandMansionPieces.generateMansion(context.structureTemplateManager(), startPos, rotation, wmPieces, context.random());
        wmPieces.forEach(builder::addPiece);
    }

    @Override
    public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, PiecesContainer pieces) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int minY = level.getMinY();
        BoundingBox boundingBox = pieces.calculateBoundingBox();
        int yStart = boundingBox.minY();
        for (int x = chunkBB.minX(); x <= chunkBB.maxX(); ++x) {
            block1: for (int z = chunkBB.minZ(); z <= chunkBB.maxZ(); ++z) {
                pos.set(x, yStart, z);
                if (level.isEmptyBlock(pos) || !boundingBox.isInside(pos) || !pieces.isInsidePiece(pos)) continue;
                for (int y = yStart - 1; y > minY; --y) {
                    pos.setY(y);
                    if (!level.isEmptyBlock(pos) && !level.getBlockState(pos).liquid()) continue block1;
                    level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 2);
                }
            }
        }
    }

    @Override
    public StructureType<?> type() {
        return StructureType.WOODLAND_MANSION;
    }
}

