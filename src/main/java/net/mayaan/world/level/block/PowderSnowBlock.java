/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.tags.EntityTypeTags;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.InsideBlockEffectApplier;
import net.mayaan.world.entity.InsideBlockEffectType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.item.FallingBlockEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.BucketPickup;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.pathfinder.PathComputationType;
import net.mayaan.world.phys.Vec3;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.EntityCollisionContext;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class PowderSnowBlock
extends Block
implements BucketPickup {
    public static final MapCodec<PowderSnowBlock> CODEC = PowderSnowBlock.simpleCodec(PowderSnowBlock::new);
    private static final float HORIZONTAL_PARTICLE_MOMENTUM_FACTOR = 0.083333336f;
    private static final float IN_BLOCK_HORIZONTAL_SPEED_MULTIPLIER = 0.9f;
    private static final float IN_BLOCK_VERTICAL_SPEED_MULTIPLIER = 1.5f;
    private static final float NUM_BLOCKS_TO_FALL_INTO_BLOCK = 2.5f;
    private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.9f, 1.0);
    private static final double MINIMUM_FALL_DISTANCE_FOR_SOUND = 4.0;
    private static final double MINIMUM_FALL_DISTANCE_FOR_BIG_SOUND = 7.0;

    public MapCodec<PowderSnowBlock> codec() {
        return CODEC;
    }

    public PowderSnowBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
        if (neighborState.is(this)) {
            return true;
        }
        return super.skipRendering(state, neighborState, direction);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (!(entity instanceof LivingEntity) || entity.getInBlockState().is(this)) {
            entity.makeStuckInBlock(state, new Vec3(0.9f, 1.5, 0.9f));
            if (level.isClientSide()) {
                boolean isMoving;
                RandomSource random = level.getRandom();
                boolean bl = isMoving = entity.xOld != entity.getX() || entity.zOld != entity.getZ();
                if (isMoving && random.nextBoolean()) {
                    level.addParticle(ParticleTypes.SNOWFLAKE, entity.getX(), pos.getY() + 1, entity.getZ(), Mth.randomBetween(random, -1.0f, 1.0f) * 0.083333336f, 0.05f, Mth.randomBetween(random, -1.0f, 1.0f) * 0.083333336f);
                }
            }
        }
        BlockPos position = pos.immutable();
        effectApplier.runBefore(InsideBlockEffectType.EXTINGUISH, e -> {
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                if (e.isOnFire() && (serverLevel.getGameRules().get(GameRules.MOB_GRIEFING).booleanValue() || e instanceof Player) && e.mayInteract(serverLevel, position)) {
                    level.destroyBlock(position, false);
                }
            }
        });
        effectApplier.apply(InsideBlockEffectType.FREEZE);
        effectApplier.apply(InsideBlockEffectType.EXTINGUISH);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        if (fallDistance < 4.0 || !(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entity;
        LivingEntity.Fallsounds entityFallsounds = livingEntity.getFallSounds();
        SoundEvent fallSound = fallDistance < 7.0 ? entityFallsounds.small() : entityFallsounds.big();
        entity.playSound(fallSound, 1.0f, 1.0f);
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        VoxelShape collisionShape = this.getCollisionShape(state, level, pos, CollisionContext.of(entity));
        return collisionShape.isEmpty() ? Shapes.block() : collisionShape;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        EntityCollisionContext entityCollisionContext;
        Entity entity;
        if (!context.isPlacement() && context instanceof EntityCollisionContext && (entity = (entityCollisionContext = (EntityCollisionContext)context).getEntity()) != null) {
            if (entity.fallDistance > 2.5) {
                return FALLING_COLLISION_SHAPE;
            }
            boolean isFallingBlock = entity instanceof FallingBlockEntity;
            if (isFallingBlock || PowderSnowBlock.canEntityWalkOnPowderSnow(entity) && context.isAbove(Shapes.block(), pos, false) && !context.isDescending()) {
                return super.getCollisionShape(state, level, pos, context);
            }
        }
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    public static boolean canEntityWalkOnPowderSnow(Entity entity) {
        if (entity.is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
            return true;
        }
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS);
        }
        return false;
    }

    @Override
    public ItemStack pickupBlock(@Nullable LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
        if (!level.isClientSide()) {
            level.levelEvent(2001, pos, Block.getId(state));
        }
        return new ItemStack(Items.POWDER_SNOW_BUCKET);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL_POWDER_SNOW);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }
}

