/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.levelgen.blockpredicates;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.Vec3i;
import net.mayaan.core.registries.Registries;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.mayaan.world.level.levelgen.blockpredicates.StateTestingPredicate;
import net.mayaan.world.level.material.Fluid;

class MatchingFluidsPredicate
extends StateTestingPredicate {
    private final HolderSet<Fluid> fluids;
    public static final MapCodec<MatchingFluidsPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> MatchingFluidsPredicate.stateTestingCodec(i).and((App)RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(c -> c.fluids)).apply((Applicative)i, MatchingFluidsPredicate::new));

    public MatchingFluidsPredicate(Vec3i offset, HolderSet<Fluid> fluids) {
        super(offset);
        this.fluids = fluids;
    }

    @Override
    protected boolean test(BlockState state) {
        return state.getFluidState().is(this.fluids);
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.MATCHING_FLUIDS;
    }
}

