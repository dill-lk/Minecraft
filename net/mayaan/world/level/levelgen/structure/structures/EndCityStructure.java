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
import java.util.ArrayList;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.levelgen.structure.Structure;
import net.mayaan.world.level.levelgen.structure.StructureType;
import net.mayaan.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.mayaan.world.level.levelgen.structure.structures.EndCityPieces;

public class EndCityStructure
extends Structure {
    public static final MapCodec<EndCityStructure> CODEC = EndCityStructure.simpleCodec(EndCityStructure::new);

    public EndCityStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        Rotation rotation = Rotation.getRandom(context.random());
        BlockPos startPos = this.getLowestYIn5by5BoxOffset7Blocks(context, rotation);
        if (startPos.getY() < 60) {
            return Optional.empty();
        }
        return Optional.of(new Structure.GenerationStub(startPos, builder -> this.generatePieces((StructurePiecesBuilder)builder, startPos, rotation, context)));
    }

    private void generatePieces(StructurePiecesBuilder builder, BlockPos startPos, Rotation rotation, Structure.GenerationContext context) {
        ArrayList pieces = Lists.newArrayList();
        EndCityPieces.startHouseTower(context.structureTemplateManager(), startPos, rotation, pieces, context.random());
        pieces.forEach(builder::addPiece);
    }

    @Override
    public StructureType<?> type() {
        return StructureType.END_CITY;
    }
}

