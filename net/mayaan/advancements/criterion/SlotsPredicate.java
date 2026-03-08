/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  it.unimi.dsi.fastutil.ints.IntList
 */
package net.mayaan.advancements.criterion;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Map;
import net.mayaan.advancements.criterion.ItemPredicate;
import net.mayaan.world.entity.SlotAccess;
import net.mayaan.world.entity.SlotProvider;
import net.mayaan.world.inventory.SlotRange;
import net.mayaan.world.inventory.SlotRanges;

public record SlotsPredicate(Map<SlotRange, ItemPredicate> slots) {
    public static final Codec<SlotsPredicate> CODEC = Codec.unboundedMap(SlotRanges.CODEC, ItemPredicate.CODEC).xmap(SlotsPredicate::new, SlotsPredicate::slots);

    public boolean matches(SlotProvider slotProvider) {
        for (Map.Entry<SlotRange, ItemPredicate> entry : this.slots.entrySet()) {
            if (SlotsPredicate.matchSlots(slotProvider, entry.getValue(), entry.getKey().slots())) continue;
            return false;
        }
        return true;
    }

    private static boolean matchSlots(SlotProvider slotProvider, ItemPredicate test, IntList slots) {
        for (int i = 0; i < slots.size(); ++i) {
            int slotId = slots.getInt(i);
            SlotAccess slot = slotProvider.getSlot(slotId);
            if (slot == null || !test.test(slot.get())) continue;
            return true;
        }
        return false;
    }
}

