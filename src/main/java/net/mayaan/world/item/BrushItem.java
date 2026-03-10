/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 */
package net.mayaan.world.item;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.particles.BlockParticleOption;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.EntitySelector;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.HumanoidArm;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.projectile.ProjectileUtil;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemUseAnimation;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.BrushableBlock;
import net.mayaan.world.level.block.RenderShape;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BrushableBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.phys.Vec3;

public class BrushItem
extends Item {
    public static final int ANIMATION_DURATION = 10;
    private static final int USE_DURATION = 200;

    public BrushItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && this.calculateHitResult(player).getType() == HitResult.Type.BLOCK) {
            player.startUsingItem(context.getHand());
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.BRUSH;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 200;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        boolean isLastTickBeforeBackswing;
        BlockHitResult blockHitResult;
        Player player;
        block11: {
            block10: {
                if (ticksRemaining < 0 || !(livingEntity instanceof Player)) {
                    livingEntity.releaseUsingItem();
                    return;
                }
                player = (Player)livingEntity;
                HitResult hitResult = this.calculateHitResult(player);
                if (!(hitResult instanceof BlockHitResult)) break block10;
                blockHitResult = (BlockHitResult)hitResult;
                if (hitResult.getType() == HitResult.Type.BLOCK) break block11;
            }
            livingEntity.releaseUsingItem();
            return;
        }
        int timeElapsed = this.getUseDuration(itemStack, livingEntity) - ticksRemaining + 1;
        boolean bl = isLastTickBeforeBackswing = timeElapsed % 10 == 5;
        if (isLastTickBeforeBackswing) {
            SoundEvent brushSound;
            Block block;
            HumanoidArm brushingArm;
            BlockPos pos = blockHitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            HumanoidArm humanoidArm = brushingArm = livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            if (state.shouldSpawnTerrainParticles() && state.getRenderShape() != RenderShape.INVISIBLE) {
                this.spawnDustParticles(level, blockHitResult, state, livingEntity.getViewVector(0.0f), brushingArm);
            }
            if ((block = state.getBlock()) instanceof BrushableBlock) {
                BrushableBlock brushableBlock = (BrushableBlock)block;
                brushSound = brushableBlock.getBrushSound();
            } else {
                brushSound = SoundEvents.BRUSH_GENERIC;
            }
            level.playSound(player, pos, brushSound, SoundSource.BLOCKS);
            if (level instanceof ServerLevel) {
                BrushableBlockEntity brushableBlockEntity;
                boolean brushingUpdatedState;
                ServerLevel serverLevel = (ServerLevel)level;
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof BrushableBlockEntity && (brushingUpdatedState = (brushableBlockEntity = (BrushableBlockEntity)blockEntity).brush(level.getGameTime(), serverLevel, player, blockHitResult.getDirection(), itemStack))) {
                    EquipmentSlot equippedHand = itemStack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    itemStack.hurtAndBreak(1, (LivingEntity)player, equippedHand);
                }
            }
        }
    }

    private HitResult calculateHitResult(Player player) {
        return ProjectileUtil.getHitResultOnViewVector(player, EntitySelector.CAN_BE_PICKED, player.blockInteractionRange());
    }

    private void spawnDustParticles(Level level, BlockHitResult hitResult, BlockState state, Vec3 viewVector, HumanoidArm brushingArm) {
        double deltaScale = 3.0;
        int flip = brushingArm == HumanoidArm.RIGHT ? 1 : -1;
        int particles = level.getRandom().nextInt(7, 12);
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
        Direction hitDirection = hitResult.getDirection();
        DustParticlesDelta dustParticlesDelta = DustParticlesDelta.fromDirection(viewVector, hitDirection);
        Vec3 hitLocation = hitResult.getLocation();
        for (int i = 0; i < particles; ++i) {
            level.addParticle(particle, hitLocation.x - (double)(hitDirection == Direction.WEST ? 1.0E-6f : 0.0f), hitLocation.y, hitLocation.z - (double)(hitDirection == Direction.NORTH ? 1.0E-6f : 0.0f), dustParticlesDelta.xd() * (double)flip * 3.0 * level.getRandom().nextDouble(), 0.0, dustParticlesDelta.zd() * (double)flip * 3.0 * level.getRandom().nextDouble());
        }
    }

    private record DustParticlesDelta(double xd, double yd, double zd) {
        private static final double ALONG_SIDE_DELTA = 1.0;
        private static final double OUT_FROM_SIDE_DELTA = 0.1;

        public static DustParticlesDelta fromDirection(Vec3 viewVector, Direction hitDirection) {
            double yd = 0.0;
            return switch (hitDirection) {
                default -> throw new MatchException(null, null);
                case Direction.DOWN, Direction.UP -> new DustParticlesDelta(viewVector.z(), 0.0, -viewVector.x());
                case Direction.NORTH -> new DustParticlesDelta(1.0, 0.0, -0.1);
                case Direction.SOUTH -> new DustParticlesDelta(-1.0, 0.0, 0.1);
                case Direction.WEST -> new DustParticlesDelta(-0.1, 0.0, -1.0);
                case Direction.EAST -> new DustParticlesDelta(0.1, 0.0, 1.0);
            };
        }
    }
}

