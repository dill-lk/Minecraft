/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.structure.pools;

import com.mojang.serialization.MapCodec;
import java.util.Collections;
import java.util.List;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Vec3i;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.StructureManager;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.levelgen.structure.BoundingBox;
import net.mayaan.world.level.levelgen.structure.pools.StructurePoolElement;
import net.mayaan.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.mayaan.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.mayaan.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class EmptyPoolElement
extends StructurePoolElement {
    public static final MapCodec<EmptyPoolElement> CODEC = MapCodec.unit(() -> INSTANCE);
    public static final EmptyPoolElement INSTANCE = new EmptyPoolElement();

    private EmptyPoolElement() {
        super(StructureTemplatePool.Projection.TERRAIN_MATCHING);
    }

    @Override
    public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) {
        return Vec3i.ZERO;
    }

    @Override
    public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, RandomSource random) {
        return Collections.emptyList();
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation) {
        throw new IllegalStateException("Invalid call to EmptyPoolElement.getBoundingBox, filter me!");
    }

    @Override
    public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos position, BlockPos referencePos, Rotation rotation, BoundingBox chunkBB, RandomSource random, LiquidSettings liquidSettings, boolean keepJigsaws) {
        return true;
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.EMPTY;
    }

    public String toString() {
        return "Empty";
    }
}

