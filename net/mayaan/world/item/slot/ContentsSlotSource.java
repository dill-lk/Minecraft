/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.slot;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.world.item.slot.SlotCollection;
import net.mayaan.world.item.slot.SlotSource;
import net.mayaan.world.item.slot.TransformedSlotSource;
import net.mayaan.world.level.storage.loot.ContainerComponentManipulator;
import net.mayaan.world.level.storage.loot.ContainerComponentManipulators;

public class ContentsSlotSource
extends TransformedSlotSource {
    public static final MapCodec<ContentsSlotSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> ContentsSlotSource.commonFields(i).and((App)ContainerComponentManipulators.CODEC.fieldOf("component").forGetter(t -> t.component)).apply((Applicative)i, ContentsSlotSource::new));
    private final ContainerComponentManipulator<?> component;

    private ContentsSlotSource(SlotSource slotSource, ContainerComponentManipulator<?> component) {
        super(slotSource);
        this.component = component;
    }

    public MapCodec<ContentsSlotSource> codec() {
        return MAP_CODEC;
    }

    @Override
    protected SlotCollection transform(SlotCollection slots) {
        return slots.flatMap(this.component::getSlots);
    }
}

