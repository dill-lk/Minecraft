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
import net.mayaan.core.Holder;
import net.mayaan.core.IdMapper;
import net.mayaan.core.particles.ParticleOptions;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.tags.TagKey;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.InsideBlockEffectApplier;
import net.mayaan.world.item.Item;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.Vec3;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public abstract class Fluid {
    public static final IdMapper<FluidState> FLUID_STATE_REGISTRY = new IdMapper();
    protected final StateDefinition<Fluid, FluidState> stateDefinition;
    private FluidState defaultFluidState;
    private final Holder.Reference<Fluid> builtInRegistryHolder = BuiltInRegistries.FLUID.createIntrusiveHolder(this);

    protected Fluid() {
        StateDefinition.Builder<Fluid, FluidState> builder = new StateDefinition.Builder<Fluid, FluidState>(this);
        this.createFluidStateDefinition(builder);
        this.stateDefinition = builder.create(Fluid::defaultFluidState, FluidState::new);
        this.registerDefaultState(this.stateDefinition.any());
    }

    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
    }

    public StateDefinition<Fluid, FluidState> getStateDefinition() {
        return this.stateDefinition;
    }

    protected final void registerDefaultState(FluidState state) {
        this.defaultFluidState = state;
    }

    public final FluidState defaultFluidState() {
        return this.defaultFluidState;
    }

    public abstract Item getBucket();

    protected void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {
    }

    protected void tick(ServerLevel level, BlockPos pos, BlockState blockState, FluidState fluidState) {
    }

    protected void randomTick(ServerLevel level, BlockPos pos, FluidState fluidState, RandomSource random) {
    }

    protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
    }

    protected @Nullable ParticleOptions getDripParticle() {
        return null;
    }

    protected abstract boolean canBeReplacedWith(FluidState var1, BlockGetter var2, BlockPos var3, Fluid var4, Direction var5);

    protected abstract Vec3 getFlow(BlockGetter var1, BlockPos var2, FluidState var3);

    public abstract int getTickDelay(LevelReader var1);

    protected boolean isRandomlyTicking() {
        return false;
    }

    protected boolean isEmpty() {
        return false;
    }

    protected abstract float getExplosionResistance();

    public abstract float getHeight(FluidState var1, BlockGetter var2, BlockPos var3);

    public abstract float getOwnHeight(FluidState var1);

    protected abstract BlockState createLegacyBlock(FluidState var1);

    public abstract boolean isSource(FluidState var1);

    public abstract int getAmount(FluidState var1);

    public boolean isSame(Fluid other) {
        return other == this;
    }

    @Deprecated
    public boolean is(TagKey<Fluid> tag) {
        return this.builtInRegistryHolder.is(tag);
    }

    public abstract VoxelShape getShape(FluidState var1, BlockGetter var2, BlockPos var3);

    public @Nullable AABB getAABB(FluidState state, BlockGetter level, BlockPos pos) {
        if (this.isEmpty()) {
            return null;
        }
        float height = state.getHeight(level, pos);
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), (double)pos.getX() + 1.0, (float)pos.getY() + height, (double)pos.getZ() + 1.0);
    }

    public Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }

    @Deprecated
    public Holder.Reference<Fluid> builtInRegistryHolder() {
        return this.builtInRegistryHolder;
    }
}

