/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BlockStateConfiguration
implements FeatureConfiguration {
    public static final Codec<BlockStateConfiguration> CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockStateConfiguration::new, c -> c.state).codec();
    public final BlockState state;

    public BlockStateConfiguration(BlockState state) {
        this.state = state;
    }
}

