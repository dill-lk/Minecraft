/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.levelgen.structure.templatesystem;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.levelgen.Heightmap;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.Nullable;

public class GravityProcessor
extends StructureProcessor {
    public static final MapCodec<GravityProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Heightmap.Types.CODEC.fieldOf("heightmap").orElse((Object)Heightmap.Types.WORLD_SURFACE_WG).forGetter(p -> p.heightmap), (App)Codec.INT.fieldOf("offset").orElse((Object)0).forGetter(p -> p.offset)).apply((Applicative)i, GravityProcessor::new));
    private final Heightmap.Types heightmap;
    private final int offset;

    public GravityProcessor(Heightmap.Types heightmap, int offset) {
        this.heightmap = heightmap;
        this.offset = offset;
    }

    @Override
    public  @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
        Heightmap.Types heightmap = level instanceof ServerLevel ? (this.heightmap == Heightmap.Types.WORLD_SURFACE_WG ? Heightmap.Types.WORLD_SURFACE : (this.heightmap == Heightmap.Types.OCEAN_FLOOR_WG ? Heightmap.Types.OCEAN_FLOOR : this.heightmap)) : this.heightmap;
        BlockPos pos = processedBlockInfo.pos();
        int height = level.getHeight(heightmap, pos.getX(), pos.getZ()) + this.offset;
        int delta = originalBlockInfo.pos().getY();
        return new StructureTemplate.StructureBlockInfo(new BlockPos(pos.getX(), height + delta, pos.getZ()), processedBlockInfo.state(), processedBlockInfo.nbt());
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.GRAVITY;
    }
}

