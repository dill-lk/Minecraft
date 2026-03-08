/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.item.slot;

import com.mojang.serialization.MapCodec;
import net.mayaan.world.item.slot.SlotCollection;
import net.mayaan.world.item.slot.SlotSource;
import net.mayaan.world.level.storage.loot.LootContext;

public record EmptySlotSource() implements SlotSource
{
    public static final MapCodec<EmptySlotSource> MAP_CODEC = MapCodec.unit((Object)new EmptySlotSource());

    public MapCodec<EmptySlotSource> codec() {
        return MAP_CODEC;
    }

    @Override
    public SlotCollection provide(LootContext context) {
        return SlotCollection.EMPTY;
    }
}

