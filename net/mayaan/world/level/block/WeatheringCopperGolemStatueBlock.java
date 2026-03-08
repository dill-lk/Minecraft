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
import net.mayaan.tags.ItemTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.animal.golem.CopperGolem;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.ChangeOverTimeBlock;
import net.mayaan.world.level.block.CopperGolemStatueBlock;
import net.mayaan.world.level.block.WeatheringCopper;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.CopperGolemStatueBlockEntity;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.BlockHitResult;

public class WeatheringCopperGolemStatueBlock
extends CopperGolemStatueBlock
implements WeatheringCopper {
    public static final MapCodec<WeatheringCopperGolemStatueBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge), WeatheringCopperGolemStatueBlock.propertiesCodec()).apply((Applicative)i, WeatheringCopperGolemStatueBlock::new));

    public MapCodec<WeatheringCopperGolemStatueBlock> codec() {
        return CODEC;
    }

    public WeatheringCopperGolemStatueBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(weatherState, properties);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return this.getWeatheringState();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CopperGolemStatueBlockEntity) {
            CopperGolemStatueBlockEntity copperGolemStatueBlockEntity = (CopperGolemStatueBlockEntity)blockEntity;
            if (itemStack.is(ItemTags.AXES)) {
                if (this.getAge().equals(WeatheringCopper.WeatherState.UNAFFECTED)) {
                    CopperGolem copperGolem = copperGolemStatueBlockEntity.removeStatue(state);
                    itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
                    if (copperGolem != null) {
                        level.addFreshEntity(copperGolem);
                        level.removeBlock(pos, false);
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                if (itemStack.is(Items.HONEYCOMB)) {
                    return InteractionResult.PASS;
                }
                this.updatePose(level, state, pos, player);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}

