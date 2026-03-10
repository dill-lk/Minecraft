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
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.block.ChestBlock;
import net.mayaan.world.level.block.CopperChestBlock;
import net.mayaan.world.level.block.WeatheringCopper;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.ChestBlockEntity;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.ChestType;

public class WeatheringCopperChestBlock
extends CopperChestBlock
implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperChestBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperChestBlock::getState), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), WeatheringCopperChestBlock.propertiesCodec()).apply((Applicative)i, WeatheringCopperChestBlock::new));

    @Override
    public MapCodec<WeatheringCopperChestBlock> codec() {
        return CODEC;
    }

    public WeatheringCopperChestBlock(WeatheringCopper.WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties) {
        super(weatherState, openSound, closeSound, properties);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        ChestBlockEntity chestBlockEntity;
        BlockEntity blockEntity;
        if (!state.getValue(ChestBlock.TYPE).equals(ChestType.RIGHT) && (blockEntity = level.getBlockEntity(pos)) instanceof ChestBlockEntity && (chestBlockEntity = (ChestBlockEntity)blockEntity).getEntitiesWithContainerOpen().isEmpty()) {
            this.changeOverTime(state, level, pos, random);
        }
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return this.getState();
    }

    @Override
    public boolean isWaxed() {
        return false;
    }
}

