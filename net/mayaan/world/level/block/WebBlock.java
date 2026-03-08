/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.world.effect.MobEffects;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.InsideBlockEffectApplier;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.Vec3;

public class WebBlock
extends Block {
    public static final MapCodec<WebBlock> CODEC = WebBlock.simpleCodec(WebBlock::new);

    public MapCodec<WebBlock> codec() {
        return CODEC;
    }

    public WebBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        LivingEntity livingEntity;
        Vec3 speedMultiplier = new Vec3(0.25, 0.05f, 0.25);
        if (entity instanceof LivingEntity && (livingEntity = (LivingEntity)entity).hasEffect(MobEffects.WEAVING)) {
            speedMultiplier = new Vec3(0.5, 0.25, 0.5);
        }
        entity.makeStuckInBlock(state, speedMultiplier);
    }
}

