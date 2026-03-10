/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponents;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundSource;
import net.mayaan.stats.Stats;
import net.mayaan.util.Mth;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Instrument;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemUseAnimation;
import net.mayaan.world.item.component.InstrumentComponent;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.gameevent.GameEvent;

public class InstrumentItem
extends Item {
    public InstrumentItem(Item.Properties properties) {
        super(properties);
    }

    public static ItemStack create(Item item, Holder<Instrument> instrument) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(DataComponents.INSTRUMENT, new InstrumentComponent(instrument));
        return itemStack;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Optional<Holder<Instrument>> instrumentHolder = InstrumentItem.getInstrument(itemStack);
        if (instrumentHolder.isPresent()) {
            Instrument instrument = instrumentHolder.get().value();
            player.startUsingItem(hand);
            InstrumentItem.play(level, player, instrument);
            player.getCooldowns().addCooldown(itemStack, Mth.floor(instrument.useDuration() * 20.0f));
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        Optional<Holder<Instrument>> instrument = InstrumentItem.getInstrument(itemStack);
        return instrument.map(instrumentHolder -> Mth.floor(((Instrument)instrumentHolder.value()).useDuration() * 20.0f)).orElse(0);
    }

    private static Optional<Holder<Instrument>> getInstrument(ItemStack itemStack) {
        InstrumentComponent instrument = itemStack.get(DataComponents.INSTRUMENT);
        return instrument != null ? Optional.of(instrument.instrument()) : Optional.empty();
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.TOOT_HORN;
    }

    private static void play(Level level, Player player, Instrument instrument) {
        SoundEvent soundEvent = instrument.soundEvent().value();
        float volume = instrument.range() / 16.0f;
        level.playSound((Entity)player, player, soundEvent, SoundSource.RECORDS, volume, 1.0f);
        level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
    }
}

