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
import net.mayaan.core.BlockPos;
import net.mayaan.core.Vec3i;
import net.mayaan.world.level.WorldGenLevel;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.blockpredicates.BlockPredicate;
import net.mayaan.world.level.levelgen.blockpredicates.BlockPredicateType;

public class WouldSurvivePredicate
implements BlockPredicate {
    public static final MapCodec<WouldSurvivePredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3i.offsetCodec(16).optionalFieldOf("offset", (Object)Vec3i.ZERO).forGetter(c -> c.offset), (App)BlockState.CODEC.fieldOf("state").forGetter(c -> c.state)).apply((Applicative)i, WouldSurvivePredicate::new));
    private final Vec3i offset;
    private final BlockState state;

    protected WouldSurvivePredicate(Vec3i offset, BlockState state) {
        this.offset = offset;
        this.state = state;
    }

    @Override
    public boolean test(WorldGenLevel level, BlockPos origin) {
        return this.state.canSurvive(level, origin.offset(this.offset));
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.WOULD_SURVIVE;
    }
}

