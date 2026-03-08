/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.enchantment.effects;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.Vec3i;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.item.enchantment.EnchantedItemInUse;
import net.mayaan.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.levelgen.blockpredicates.BlockPredicate;
import net.mayaan.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.mayaan.world.phys.Vec3;

public record ReplaceBlock(Vec3i offset, Optional<BlockPredicate> predicate, BlockStateProvider blockState, Optional<Holder<GameEvent>> triggerGameEvent) implements EnchantmentEntityEffect
{
    public static final MapCodec<ReplaceBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3i.CODEC.optionalFieldOf("offset", (Object)Vec3i.ZERO).forGetter(ReplaceBlock::offset), (App)BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(ReplaceBlock::predicate), (App)BlockStateProvider.CODEC.fieldOf("block_state").forGetter(ReplaceBlock::blockState), (App)GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(ReplaceBlock::triggerGameEvent)).apply((Applicative)i, ReplaceBlock::new));

    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
        BlockPos pos = BlockPos.containing(position).offset(this.offset);
        if (this.predicate.map(p -> p.test(serverLevel, pos)).orElse(true).booleanValue() && serverLevel.setBlockAndUpdate(pos, this.blockState.getState(serverLevel, entity.getRandom(), pos))) {
            this.triggerGameEvent.ifPresent(event -> serverLevel.gameEvent(entity, (Holder<GameEvent>)event, pos));
        }
    }

    public MapCodec<ReplaceBlock> codec() {
        return CODEC;
    }
}

