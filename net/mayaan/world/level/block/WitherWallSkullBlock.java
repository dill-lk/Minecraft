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
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.SkullBlock;
import net.mayaan.world.level.block.WallSkullBlock;
import net.mayaan.world.level.block.WitherSkullBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class WitherWallSkullBlock
extends WallSkullBlock {
    public static final MapCodec<WitherWallSkullBlock> CODEC = WitherWallSkullBlock.simpleCodec(WitherWallSkullBlock::new);

    public MapCodec<WitherWallSkullBlock> codec() {
        return CODEC;
    }

    protected WitherWallSkullBlock(BlockBehaviour.Properties properties) {
        super(SkullBlock.Types.WITHER_SKELETON, properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        WitherSkullBlock.checkSpawn(level, pos);
    }
}

