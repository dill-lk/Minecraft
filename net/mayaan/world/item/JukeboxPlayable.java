/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.world.item;

import com.mojang.serialization.Codec;
import java.util.function.Consumer;
import net.mayaan.ChatFormatting;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentUtils;
import net.mayaan.network.chat.Style;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.JukeboxSong;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipProvider;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.JukeboxBlock;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.JukeboxBlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;

public record JukeboxPlayable(Holder<JukeboxSong> song) implements TooltipProvider
{
    public static final Codec<JukeboxPlayable> CODEC = JukeboxSong.CODEC.xmap(JukeboxPlayable::new, JukeboxPlayable::song);
    public static final StreamCodec<RegistryFriendlyByteBuf, JukeboxPlayable> STREAM_CODEC = StreamCodec.composite(JukeboxSong.STREAM_CODEC, JukeboxPlayable::song, JukeboxPlayable::new);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(ComponentUtils.mergeStyles(this.song.value().description(), Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    public static InteractionResult tryInsertIntoJukebox(Level level, BlockPos pos, ItemStack toInsert, Player player) {
        JukeboxPlayable jukeboxPlayable = toInsert.get(DataComponents.JUKEBOX_PLAYABLE);
        if (jukeboxPlayable == null) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        BlockState state = level.getBlockState(pos);
        if (!state.is(Blocks.JUKEBOX) || state.getValue(JukeboxBlock.HAS_RECORD).booleanValue()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!level.isClientSide()) {
            ItemStack inserted = toInsert.consumeAndReturn(1, player);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof JukeboxBlockEntity) {
                JukeboxBlockEntity jukebox = (JukeboxBlockEntity)blockEntity;
                jukebox.setTheItem(inserted);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
            }
            player.awardStat(Stats.PLAY_RECORD);
        }
        return InteractionResult.SUCCESS;
    }
}

