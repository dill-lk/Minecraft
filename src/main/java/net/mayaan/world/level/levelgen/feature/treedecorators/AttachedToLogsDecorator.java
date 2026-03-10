/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.levelgen.feature.treedecorators;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.util.ExtraCodecs;
import net.mayaan.util.RandomSource;
import net.mayaan.util.Util;
import net.mayaan.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.mayaan.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.mayaan.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class AttachedToLogsDecorator
extends TreeDecorator {
    public static final MapCodec<AttachedToLogsDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.floatRange((float)0.0f, (float)1.0f).fieldOf("probability").forGetter(p -> Float.valueOf(p.probability)), (App)BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(p -> p.blockProvider), (App)ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter(p -> p.directions)).apply((Applicative)i, AttachedToLogsDecorator::new));
    private final float probability;
    private final BlockStateProvider blockProvider;
    private final List<Direction> directions;

    public AttachedToLogsDecorator(float probability, BlockStateProvider blockProvider, List<Direction> directions) {
        this.probability = probability;
        this.blockProvider = blockProvider;
        this.directions = directions;
    }

    @Override
    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();
        for (BlockPos logsPos : Util.shuffledCopy(context.logs(), random)) {
            Direction direction = Util.getRandom(this.directions, random);
            BlockPos placementPos = logsPos.relative(direction);
            if (!(random.nextFloat() <= this.probability) || !context.isAir(placementPos)) continue;
            context.setBlock(placementPos, this.blockProvider.getState(context.level(), random, placementPos));
        }
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.ATTACHED_TO_LOGS;
    }
}

