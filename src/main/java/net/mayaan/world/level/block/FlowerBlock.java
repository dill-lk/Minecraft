/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.util.Mth;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.item.component.SuspiciousStewEffects;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.SuspiciousEffectHolder;
import net.mayaan.world.level.block.VegetationBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class FlowerBlock
extends VegetationBlock
implements SuspiciousEffectHolder {
    protected static final MapCodec<SuspiciousStewEffects> EFFECTS_FIELD = SuspiciousStewEffects.CODEC.fieldOf("suspicious_stew_effects");
    public static final MapCodec<FlowerBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects), FlowerBlock.propertiesCodec()).apply((Applicative)i, FlowerBlock::new));
    private static final VoxelShape SHAPE = Block.column(6.0, 0.0, 10.0);
    private final SuspiciousStewEffects suspiciousStewEffects;

    public MapCodec<? extends FlowerBlock> codec() {
        return CODEC;
    }

    public FlowerBlock(Holder<MobEffect> suspiciousStewEffect, float effectSeconds, BlockBehaviour.Properties properties) {
        this(FlowerBlock.makeEffectList(suspiciousStewEffect, effectSeconds), properties);
    }

    public FlowerBlock(SuspiciousStewEffects suspiciousStewEffects, BlockBehaviour.Properties properties) {
        super(properties);
        this.suspiciousStewEffects = suspiciousStewEffects;
    }

    protected static SuspiciousStewEffects makeEffectList(Holder<MobEffect> suspiciousStewEffect, float effectSeconds) {
        return new SuspiciousStewEffects(List.of(new SuspiciousStewEffects.Entry(suspiciousStewEffect, Mth.floor(effectSeconds * 20.0f))));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE.move(state.getOffset(pos));
    }

    @Override
    public SuspiciousStewEffects getSuspiciousEffects() {
        return this.suspiciousStewEffects;
    }

    public @Nullable MobEffectInstance getBeeInteractionEffect() {
        return null;
    }
}

