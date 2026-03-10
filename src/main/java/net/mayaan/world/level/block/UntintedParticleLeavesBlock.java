/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.block;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.BlockPos;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.util.ParticleUtils;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.LeavesBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;

public class UntintedParticleLeavesBlock
extends LeavesBlock {
    public static final MapCodec<UntintedParticleLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.floatRange(0.0f, 1.0f).fieldOf("leaf_particle_chance").forGetter(e -> Float.valueOf(e.leafParticleChance)), (App)ParticleTypes.CODEC.fieldOf("leaf_particle").forGetter(e -> e.leafParticle), UntintedParticleLeavesBlock.propertiesCodec()).apply((Applicative)i, UntintedParticleLeavesBlock::new));
    protected final ParticleOptions leafParticle;

    public UntintedParticleLeavesBlock(float leafParticleChance, ParticleOptions leafParticle, BlockBehaviour.Properties properties) {
        super(leafParticleChance, properties);
        this.leafParticle = leafParticle;
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
        ParticleUtils.spawnParticleBelow(level, pos, random, this.leafParticle);
    }

    public MapCodec<UntintedParticleLeavesBlock> codec() {
        return CODEC;
    }
}

