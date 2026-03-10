/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.mojang.serialization.Codec
 *  org.apache.commons.lang3.mutable.MutableBoolean
 */
package net.mayaan.world.level.levelgen.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.chunk.CarvingMask;
import net.mayaan.world.level.chunk.ChunkAccess;
import net.mayaan.world.level.levelgen.Aquifer;
import net.mayaan.world.level.levelgen.carver.CarvingContext;
import net.mayaan.world.level.levelgen.carver.CaveCarverConfiguration;
import net.mayaan.world.level.levelgen.carver.CaveWorldCarver;
import net.mayaan.world.level.material.Fluids;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class NetherWorldCarver
extends CaveWorldCarver {
    public NetherWorldCarver(Codec<CaveCarverConfiguration> configurationFactory) {
        super(configurationFactory);
        this.liquids = ImmutableSet.of((Object)Fluids.LAVA, (Object)Fluids.WATER);
    }

    @Override
    protected int getCaveBound() {
        return 10;
    }

    @Override
    protected float getThickness(RandomSource random) {
        return (random.nextFloat() * 2.0f + random.nextFloat()) * 2.0f;
    }

    @Override
    protected double getYScale() {
        return 5.0;
    }

    @Override
    protected boolean carveBlock(CarvingContext context, CaveCarverConfiguration configuration, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, CarvingMask mask, BlockPos.MutableBlockPos blockPos, BlockPos.MutableBlockPos helperPos, Aquifer aquifer, MutableBoolean hasGrass) {
        if (this.canReplaceBlock(configuration, chunk.getBlockState(blockPos))) {
            BlockState state = blockPos.getY() <= context.getMinGenY() + 31 ? LAVA.createLegacyBlock() : CAVE_AIR;
            chunk.setBlockState(blockPos, state);
            return true;
        }
        return false;
    }
}

