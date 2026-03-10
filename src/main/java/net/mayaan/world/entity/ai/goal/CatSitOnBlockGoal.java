/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.goal;

import net.mayaan.core.BlockPos;
import net.mayaan.tags.BlockTags;
import net.mayaan.world.entity.ai.goal.MoveToBlockGoal;
import net.mayaan.world.entity.animal.feline.Cat;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.BedBlock;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.FurnaceBlock;
import net.mayaan.world.level.block.entity.ChestBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.BedPart;

public class CatSitOnBlockGoal
extends MoveToBlockGoal {
    private final Cat cat;

    public CatSitOnBlockGoal(Cat cat, double speedModifier) {
        super(cat, speedModifier, 8);
        this.cat = cat;
    }

    @Override
    public boolean canUse() {
        return this.cat.isTame() && !this.cat.isOrderedToSit() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.cat.setInSittingPose(false);
    }

    @Override
    public void stop() {
        super.stop();
        this.cat.setInSittingPose(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.cat.setInSittingPose(this.isReachedTarget());
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        if (!level.isEmptyBlock(pos.above())) {
            return false;
        }
        BlockState blockState = level.getBlockState(pos);
        if (blockState.is(Blocks.CHEST)) {
            return ChestBlockEntity.getOpenCount(level, pos) < 1;
        }
        if (blockState.is(Blocks.FURNACE) && blockState.getValue(FurnaceBlock.LIT).booleanValue()) {
            return true;
        }
        return blockState.is(BlockTags.BEDS, s -> s.getOptionalValue(BedBlock.PART).map(v -> v != BedPart.HEAD).orElse(true));
    }
}

