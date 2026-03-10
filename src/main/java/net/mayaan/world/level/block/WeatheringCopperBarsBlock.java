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
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.block.IronBarsBlock;
import net.mayaan.world.level.block.WeatheringCopper;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;

public class WeatheringCopperBarsBlock
extends IronBarsBlock
implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperBarsBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperBarsBlock::getAge), WeatheringCopperBarsBlock.propertiesCodec()).apply((Applicative)i, WeatheringCopperBarsBlock::new));
    private final WeatheringCopper.WeatherState weatherState;

    public MapCodec<WeatheringCopperBarsBlock> codec() {
        return CODEC;
    }

    protected WeatheringCopperBarsBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }
}

