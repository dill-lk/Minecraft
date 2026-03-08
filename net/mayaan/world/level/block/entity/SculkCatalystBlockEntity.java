/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 */
package net.mayaan.world.level.block.entity;

import com.google.common.annotations.VisibleForTesting;
import net.mayaan.Optionull;
import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Holder;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.RandomSource;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.SculkCatalystBlock;
import net.mayaan.world.level.block.SculkSpreader;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.BlockPositionSource;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.gameevent.GameEventListener;
import net.mayaan.world.level.gameevent.PositionSource;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.phys.Vec3;

public class SculkCatalystBlockEntity
extends BlockEntity
implements GameEventListener.Provider<CatalystListener> {
    private final CatalystListener catalystListener;

    public SculkCatalystBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.SCULK_CATALYST, worldPosition, blockState);
        this.catalystListener = new CatalystListener(blockState, new BlockPositionSource(worldPosition));
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SculkCatalystBlockEntity entity) {
        entity.catalystListener.getSculkSpreader().updateCursors(level, pos, level.getRandom(), true);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.catalystListener.sculkSpreader.load(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        this.catalystListener.sculkSpreader.save(output);
        super.saveAdditional(output);
    }

    @Override
    public CatalystListener getListener() {
        return this.catalystListener;
    }

    public static class CatalystListener
    implements GameEventListener {
        public static final int PULSE_TICKS = 8;
        private final SculkSpreader sculkSpreader;
        private final BlockState blockState;
        private final PositionSource positionSource;

        public CatalystListener(BlockState blockState, PositionSource positionSource) {
            this.blockState = blockState;
            this.positionSource = positionSource;
            this.sculkSpreader = SculkSpreader.createLevelSpreader();
        }

        @Override
        public PositionSource getListenerSource() {
            return this.positionSource;
        }

        @Override
        public int getListenerRadius() {
            return 8;
        }

        @Override
        public GameEventListener.DeliveryMode getDeliveryMode() {
            return GameEventListener.DeliveryMode.BY_DISTANCE;
        }

        @Override
        public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
            Entity entity;
            if (event.is(GameEvent.ENTITY_DIE) && (entity = context.sourceEntity()) instanceof LivingEntity) {
                LivingEntity mob = (LivingEntity)entity;
                if (!mob.wasExperienceConsumed()) {
                    DamageSource lastDamageSource = mob.getLastDamageSource();
                    int experienceWouldDrop = mob.getExperienceReward(level, Optionull.map(lastDamageSource, DamageSource::getEntity));
                    if (mob.shouldDropExperience() && experienceWouldDrop > 0) {
                        this.sculkSpreader.addCursors(BlockPos.containing(sourcePosition.relative(Direction.UP, 0.5)), experienceWouldDrop);
                        this.tryAwardItSpreadsAdvancement(level, mob);
                    }
                    mob.skipDropExperience();
                    this.positionSource.getPosition(level).ifPresent(vec3 -> this.bloom(level, BlockPos.containing(vec3), this.blockState, level.getRandom()));
                }
                return true;
            }
            return false;
        }

        @VisibleForTesting
        public SculkSpreader getSculkSpreader() {
            return this.sculkSpreader;
        }

        private void bloom(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
            level.setBlock(pos, (BlockState)state.setValue(SculkCatalystBlock.PULSE, true), 3);
            level.scheduleTick(pos, state.getBlock(), 8);
            level.sendParticles(ParticleTypes.SCULK_SOUL, (double)pos.getX() + 0.5, (double)pos.getY() + 1.15, (double)pos.getZ() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
            level.playSound(null, pos, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 2.0f, 0.6f + random.nextFloat() * 0.4f);
        }

        private void tryAwardItSpreadsAdvancement(Level level, LivingEntity mob) {
            LivingEntity lastHurtByMob = mob.getLastHurtByMob();
            if (lastHurtByMob instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)lastHurtByMob;
                DamageSource damageSource = mob.getLastDamageSource() == null ? level.damageSources().playerAttack(player) : mob.getLastDamageSource();
                CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST.trigger(player, mob, damageSource);
            }
        }
    }
}

