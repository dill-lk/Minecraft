/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.world.level.block.SkullBlock;
import net.mayaan.world.level.block.WallSkullBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;

public class PlayerWallHeadBlock
extends WallSkullBlock {
    public static final MapCodec<PlayerWallHeadBlock> CODEC = PlayerWallHeadBlock.simpleCodec(PlayerWallHeadBlock::new);

    public MapCodec<PlayerWallHeadBlock> codec() {
        return CODEC;
    }

    protected PlayerWallHeadBlock(BlockBehaviour.Properties properties) {
        super(SkullBlock.Types.PLAYER, properties);
    }
}

