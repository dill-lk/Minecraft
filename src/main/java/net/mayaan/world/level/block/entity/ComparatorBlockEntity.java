/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.block.entity;

import net.mayaan.core.BlockPos;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

public class ComparatorBlockEntity
extends BlockEntity {
    private static final int DEFAULT_OUTPUT = 0;
    private int output = 0;

    public ComparatorBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityType.COMPARATOR, worldPosition, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("OutputSignal", this.output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.output = input.getIntOr("OutputSignal", 0);
    }

    public int getOutputSignal() {
        return this.output;
    }

    public void setOutputSignal(int value) {
        this.output = value;
    }
}

