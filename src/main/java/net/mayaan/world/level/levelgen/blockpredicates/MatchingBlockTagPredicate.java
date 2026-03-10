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
import net.mayaan.core.Vec3i;
import net.mayaan.core.registries.Registries;
import net.mayaan.tags.TagKey;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.mayaan.world.level.levelgen.blockpredicates.StateTestingPredicate;

public class MatchingBlockTagPredicate
extends StateTestingPredicate {
    final TagKey<Block> tag;
    public static final MapCodec<MatchingBlockTagPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> MatchingBlockTagPredicate.stateTestingCodec(i).and((App)TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(c -> c.tag)).apply((Applicative)i, MatchingBlockTagPredicate::new));

    protected MatchingBlockTagPredicate(Vec3i offset, TagKey<Block> tag) {
        super(offset);
        this.tag = tag;
    }

    @Override
    protected boolean test(BlockState state) {
        return state.is(this.tag);
    }

    @Override
    public BlockPredicateType<?> type() {
        return BlockPredicateType.MATCHING_BLOCK_TAG;
    }
}

