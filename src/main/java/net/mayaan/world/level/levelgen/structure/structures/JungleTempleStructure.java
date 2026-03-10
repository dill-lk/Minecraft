/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.structure.structures;

import com.mojang.serialization.MapCodec;
import net.mayaan.world.level.levelgen.structure.SinglePieceStructure;
import net.mayaan.world.level.levelgen.structure.Structure;
import net.mayaan.world.level.levelgen.structure.StructureType;
import net.mayaan.world.level.levelgen.structure.structures.JungleTemplePiece;

public class JungleTempleStructure
extends SinglePieceStructure {
    public static final MapCodec<JungleTempleStructure> CODEC = JungleTempleStructure.simpleCodec(JungleTempleStructure::new);

    public JungleTempleStructure(Structure.StructureSettings settings) {
        super(JungleTemplePiece::new, 12, 15, settings);
    }

    @Override
    public StructureType<?> type() {
        return StructureType.JUNGLE_TEMPLE;
    }
}

