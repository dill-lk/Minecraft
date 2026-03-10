/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseEntityBlock;
import net.mayaan.world.level.block.GameMasterBlock;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.TestInstanceBlockEntity;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class TestInstanceBlock
extends BaseEntityBlock
implements GameMasterBlock {
    public static final MapCodec<TestInstanceBlock> CODEC = TestInstanceBlock.simpleCodec(TestInstanceBlock::new);

    public TestInstanceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new TestInstanceBlockEntity(worldPosition, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof TestInstanceBlockEntity)) {
            return InteractionResult.PASS;
        }
        TestInstanceBlockEntity testInstance = (TestInstanceBlockEntity)blockEntity;
        if (!player.canUseGameMasterBlocks()) {
            return InteractionResult.PASS;
        }
        if (player.level().isClientSide()) {
            player.openTestInstanceBlock(testInstance);
        }
        return InteractionResult.SUCCESS;
    }

    protected MapCodec<TestInstanceBlock> codec() {
        return CODEC;
    }
}

