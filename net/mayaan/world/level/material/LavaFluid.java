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
import net.mayaan.world.attribute.EnvironmentAttributes;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.InsideBlockEffectApplier;
import net.mayaan.world.entity.InsideBlockEffectType;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.BaseFireBlock;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.LiquidBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.material.FlowingFluid;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

public abstract class LavaFluid
extends FlowingFluid {
    public static final int LIGHT_EMISSION = 15;
    public static final float MIN_LEVEL_CUTOFF = 0.44444445f;

    @Override
    public Fluid getFlowing() {
        return Fluids.FLOWING_LAVA;
    }

    @Override
    public Fluid getSource() {
        return Fluids.LAVA;
    }

    @Override
    public Item getBucket() {
        return Items.LAVA_BUCKET;
    }

    @Override
    public void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {
        BlockPos above = pos.above();
        if (level.getBlockState(above).isAir() && !level.getBlockState(above).isSolidRender()) {
            if (random.nextInt(100) == 0) {
                double xx = (double)pos.getX() + random.nextDouble();
                double yy = (double)pos.getY() + 1.0;
                double zz = (double)pos.getZ() + random.nextDouble();
                level.addParticle(ParticleTypes.LAVA, xx, yy, zz, 0.0, 0.0, 0.0);
                level.playLocalSound(xx, yy, zz, SoundEvents.LAVA_POP, SoundSource.AMBIENT, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
            }
            if (random.nextInt(200) == 0) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.AMBIENT, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
            }
        }
    }

    @Override
    public void randomTick(ServerLevel level, BlockPos pos, FluidState fluidState, RandomSource random) {
        if (!level.canSpreadFireAround(pos)) {
            return;
        }
        int passes = random.nextInt(3);
        if (passes > 0) {
            BlockPos testPos = pos;
            for (int pass = 0; pass < passes; ++pass) {
                if (!level.isLoaded(testPos = testPos.offset(random.nextInt(3) - 1, 1, random.nextInt(3) - 1))) {
                    return;
                }
                BlockState blockState = level.getBlockState(testPos);
                if (blockState.isAir()) {
                    if (!this.hasFlammableNeighbours(level, testPos)) continue;
                    level.setBlockAndUpdate(testPos, BaseFireBlock.getState(level, testPos));
                    return;
                }
                if (!blockState.blocksMotion()) continue;
                return;
            }
        } else {
            for (int i = 0; i < 3; ++i) {
                BlockPos testPos = pos.offset(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
                if (!level.isLoaded(testPos)) {
                    return;
                }
                if (!level.isEmptyBlock(testPos.above()) || !this.isFlammable(level, testPos)) continue;
                level.setBlockAndUpdate(testPos.above(), BaseFireBlock.getState(level, testPos));
            }
        }
    }

    @Override
    protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
        effectApplier.apply(InsideBlockEffectType.CLEAR_FREEZE);
        effectApplier.apply(InsideBlockEffectType.LAVA_IGNITE);
        effectApplier.runAfter(InsideBlockEffectType.LAVA_IGNITE, Entity::lavaHurt);
    }

    private boolean hasFlammableNeighbours(LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (!this.isFlammable(level, pos.relative(direction))) continue;
            return true;
        }
        return false;
    }

    private boolean isFlammable(LevelReader level, BlockPos pos) {
        if (level.isInsideBuildHeight(pos.getY()) && !level.hasChunkAt(pos)) {
            return false;
        }
        return level.getBlockState(pos).ignitedByLava();
    }

    @Override
    public @Nullable ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_LAVA;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        this.fizz(level, pos);
    }

    @Override
    public int getSlopeFindDistance(LevelReader level) {
        return LavaFluid.isFastLava(level) ? 4 : 2;
    }

    @Override
    public BlockState createLegacyBlock(FluidState fluidState) {
        return (BlockState)Blocks.LAVA.defaultBlockState().setValue(LiquidBlock.LEVEL, LavaFluid.getLegacyLevel(fluidState));
    }

    @Override
    public boolean isSame(Fluid other) {
        return other == Fluids.LAVA || other == Fluids.FLOWING_LAVA;
    }

    @Override
    public int getDropOff(LevelReader level) {
        return LavaFluid.isFastLava(level) ? 1 : 2;
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
        return state.getHeight(level, pos) >= 0.44444445f && other.is(FluidTags.WATER);
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return LavaFluid.isFastLava(level) ? 10 : 30;
    }

    @Override
    public int getSpreadDelay(Level level, BlockPos pos, FluidState oldFluidState, FluidState newFluidState) {
        int result = this.getTickDelay(level);
        if (!(oldFluidState.isEmpty() || newFluidState.isEmpty() || oldFluidState.getValue(FALLING).booleanValue() || newFluidState.getValue(FALLING).booleanValue() || !(newFluidState.getHeight(level, pos) > oldFluidState.getHeight(level, pos)) || level.getRandom().nextInt(4) == 0)) {
            result *= 4;
        }
        return result;
    }

    private void fizz(LevelAccessor level, BlockPos pos) {
        level.levelEvent(1501, pos, 0);
    }

    @Override
    protected boolean canConvertToSource(ServerLevel level) {
        return level.getGameRules().get(GameRules.LAVA_SOURCE_CONVERSION);
    }

    @Override
    protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState state, Direction direction, FluidState target) {
        if (direction == Direction.DOWN) {
            FluidState fluidState = level.getFluidState(pos);
            if (this.is(FluidTags.LAVA) && fluidState.is(FluidTags.WATER)) {
                if (state.getBlock() instanceof LiquidBlock) {
                    level.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
                }
                this.fizz(level, pos);
                return;
            }
        }
        super.spreadTo(level, pos, state, direction, target);
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0f;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
    }

    private static boolean isFastLava(LevelReader level) {
        return level.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA);
    }

    public static class Flowing
    extends LavaFluid {
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
    extends LavaFluid {
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

