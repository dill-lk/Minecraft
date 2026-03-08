/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.material;

import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.InsideBlockEffectApplier;
import net.mayaan.world.entity.InsideBlockEffectType;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.LiquidBlock;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.material.FlowingFluid;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

public abstract class WaterFluid
extends FlowingFluid {
    @Override
    public Fluid getFlowing() {
        return Fluids.FLOWING_WATER;
    }

    @Override
    public Fluid getSource() {
        return Fluids.WATER;
    }

    @Override
    public Item getBucket() {
        return Items.WATER_BUCKET;
    }

    @Override
    public void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {
        if (!fluidState.isSource() && !fluidState.getValue(FALLING).booleanValue()) {
            if (random.nextInt(64) == 0) {
                level.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.WATER_AMBIENT, SoundSource.AMBIENT, random.nextFloat() * 0.25f + 0.75f, random.nextFloat() + 0.5f, false);
            }
        } else if (random.nextInt(10) == 0) {
            level.addParticle(ParticleTypes.UNDERWATER, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public @Nullable ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    @Override
    protected boolean canConvertToSource(ServerLevel level) {
        return level.getGameRules().get(GameRules.WATER_SOURCE_CONVERSION);
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }

    @Override
    protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
        effectApplier.apply(InsideBlockEffectType.EXTINGUISH);
    }

    @Override
    public int getSlopeFindDistance(LevelReader level) {
        return 4;
    }

    @Override
    public BlockState createLegacyBlock(FluidState fluidState) {
        return (BlockState)Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, WaterFluid.getLegacyLevel(fluidState));
    }

    @Override
    public boolean isSame(Fluid other) {
        return other == Fluids.WATER || other == Fluids.FLOWING_WATER;
    }

    @Override
    public int getDropOff(LevelReader level) {
        return 1;
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 5;
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
        return direction == Direction.DOWN && !other.is(FluidTags.WATER);
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0f;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    public static class Flowing
    extends WaterFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }
    }

    public static class Source
    extends WaterFluid {
        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }
    }
}

