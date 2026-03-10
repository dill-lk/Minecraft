/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.mojang.serialization.Codec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.server.bossevents;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.server.bossevents.CustomBossEvent;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.util.Util;
import net.mayaan.util.datafix.DataFixTypes;
import net.mayaan.world.level.saveddata.SavedData;
import net.mayaan.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.Nullable;

public class CustomBossEvents
extends SavedData {
    private static final Codec<Map<Identifier, CustomBossEvent.Packed>> EVENTS_CODEC = Codec.unboundedMap(Identifier.CODEC, CustomBossEvent.Packed.CODEC);
    private static final Codec<CustomBossEvents> CODEC = EVENTS_CODEC.xmap(events -> {
        CustomBossEvents r = new CustomBossEvents();
        events.forEach((id, packed) -> r.events.put((Identifier)id, CustomBossEvent.load(UUID.randomUUID(), id, packed, r::setDirty)));
        return r;
    }, c -> Util.mapValues(c.events, CustomBossEvent::pack));
    public static final SavedDataType<CustomBossEvents> TYPE = new SavedDataType<CustomBossEvents>(Identifier.withDefaultNamespace("custom_boss_events"), CustomBossEvents::new, CODEC, DataFixTypes.SAVED_DATA_CUSTOM_BOSS_EVENTS);
    private final Map<Identifier, CustomBossEvent> events = Maps.newHashMap();

    public @Nullable CustomBossEvent get(Identifier id) {
        return this.events.get(id);
    }

    public CustomBossEvent create(RandomSource random, Identifier id, Component name) {
        CustomBossEvent result = new CustomBossEvent(Mth.createInsecureUUID(random), id, name, this::setDirty);
        this.events.put(id, result);
        this.setDirty();
        return result;
    }

    public void remove(CustomBossEvent event) {
        if (this.events.remove(event.customId()) != null) {
            this.setDirty();
        }
    }

    public Collection<Identifier> getIds() {
        return this.events.keySet();
    }

    public Collection<CustomBossEvent> getEvents() {
        return this.events.values();
    }

    public void onPlayerConnect(ServerPlayer player) {
        for (CustomBossEvent event : this.events.values()) {
            event.onPlayerConnect(player);
        }
    }

    public void onPlayerDisconnect(ServerPlayer player) {
        for (CustomBossEvent event : this.events.values()) {
            event.onPlayerDisconnect(player);
        }
    }
}

