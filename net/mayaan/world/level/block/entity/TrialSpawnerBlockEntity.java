/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.SharedConstants;
import net.mayaan.core.BlockPos;
import net.mayaan.core.HolderLookup;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.mayaan.util.RandomSource;
import net.mayaan.util.Util;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.Spawner;
import net.mayaan.world.level.block.TrialSpawnerBlock;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.trialspawner.PlayerDetector;
import net.mayaan.world.level.block.entity.trialspawner.TrialSpawner;
import net.mayaan.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

public class TrialSpawnerBlockEntity
extends BlockEntity
implements TrialSpawner.StateAccessor,
Spawner {
    private final TrialSpawner trialSpawner = this.createDefaultSpawner();

    public TrialSpawnerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.TRIAL_SPAWNER, worldPosition, blockState);
    }

    private TrialSpawner createDefaultSpawner() {
        PlayerDetector playerDetector = SharedConstants.DEBUG_TRIAL_SPAWNER_DETECTS_SHEEP_AS_PLAYERS ? PlayerDetector.SHEEP : PlayerDetector.NO_CREATIVE_PLAYERS;
        PlayerDetector.EntitySelector entitySelector = PlayerDetector.EntitySelector.SELECT_FROM_LEVEL;
        return new TrialSpawner(TrialSpawner.FullConfig.DEFAULT, this, playerDetector, entitySelector);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.trialSpawner.load(input);
        if (this.level != null) {
            this.markUpdated();
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.trialSpawner.store(output);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.trialSpawner.getStateData().getUpdateTag(this.getBlockState().getValue(TrialSpawnerBlock.STATE));
    }

    @Override
    public void setEntityId(EntityType<?> type, RandomSource random) {
        if (this.level == null) {
            Util.logAndPauseIfInIde("Expected non-null level");
            return;
        }
        this.trialSpawner.overrideEntityToSpawn(type, this.level);
        this.setChanged();
    }

    public TrialSpawner getTrialSpawner() {
        return this.trialSpawner;
    }

    @Override
    public TrialSpawnerState getState() {
        if (!this.getBlockState().hasProperty(BlockStateProperties.TRIAL_SPAWNER_STATE)) {
            return TrialSpawnerState.INACTIVE;
        }
        return this.getBlockState().getValue(BlockStateProperties.TRIAL_SPAWNER_STATE);
    }

    @Override
    public void setState(Level level, TrialSpawnerState state) {
        this.setChanged();
        level.setBlockAndUpdate(this.worldPosition, (BlockState)this.getBlockState().setValue(BlockStateProperties.TRIAL_SPAWNER_STATE, state));
    }

    @Override
    public void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }
}

