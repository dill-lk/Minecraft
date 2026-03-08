/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.block;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.BlockPos;
import net.mayaan.util.ColorRGBA;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.ColoredFallingBlock;
import net.mayaan.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;

public class SandBlock
extends ColoredFallingBlock {
    public static final MapCodec<SandBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ColorRGBA.CODEC.fieldOf("falling_dust_color").forGetter(b -> b.dustColor), SandBlock.propertiesCodec()).apply((Applicative)i, SandBlock::new));

    public MapCodec<SandBlock> codec() {
        return CODEC;
    }

    public SandBlock(ColorRGBA dustColor, BlockBehaviour.Properties properties) {
        super(dustColor, properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        AmbientDesertBlockSoundsPlayer.playAmbientSandSounds(level, pos, random);
    }
}

