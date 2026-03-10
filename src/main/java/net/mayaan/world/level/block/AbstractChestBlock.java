/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseEntityBlock;
import net.mayaan.world.level.block.DoubleBlockCombiner;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.ChestBlockEntity;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;

public abstract class AbstractChestBlock<E extends BlockEntity>
extends BaseEntityBlock {
    protected final Supplier<BlockEntityType<? extends E>> blockEntityType;

    protected AbstractChestBlock(BlockBehaviour.Properties properties, Supplier<BlockEntityType<? extends E>> blockEntityType) {
        super(properties);
        this.blockEntityType = blockEntityType;
    }

    protected abstract MapCodec<? extends AbstractChestBlock<E>> codec();

    public abstract DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState var1, Level var2, BlockPos var3, boolean var4);
}

