/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.world.level.block.MultifaceBlock;
import net.mayaan.world.level.block.MultifaceSpreader;
import net.mayaan.world.level.block.state.BlockBehaviour;

public abstract class MultifaceSpreadeableBlock
extends MultifaceBlock {
    public MultifaceSpreadeableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public abstract MapCodec<? extends MultifaceSpreadeableBlock> codec();

    public abstract MultifaceSpreader getSpreader();
}

