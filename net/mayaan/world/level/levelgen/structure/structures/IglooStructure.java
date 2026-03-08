/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.structure.structures;

import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.levelgen.Heightmap;
import net.mayaan.world.level.levelgen.WorldgenRandom;
import net.mayaan.world.level.levelgen.structure.Structure;
import net.mayaan.world.level.levelgen.structure.StructureType;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.mayaan.world.level.levelgen.structure.structures.IglooPieces;

public class IglooStructure
extends Structure {
    public static final MapCodec<IglooStructure> CODEC = IglooStructure.simpleCodec(IglooStructure::new);

    public IglooStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        return IglooStructure.onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> this.generatePieces((StructurePiecesBuilder)builder, context));
    }

    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        WorldgenRandom random = context.random();
        BlockPos startPos = new BlockPos(chunkPos.getMinBlockX(), 90, chunkPos.getMinBlockZ());
        Rotation rotation = Rotation.getRandom(random);
        IglooPieces.addPieces(context.structureTemplateManager(), startPos, rotation, builder, random);
    }

    @Override
    public StructureType<?> type() {
        return StructureType.IGLOO;
    }
}

