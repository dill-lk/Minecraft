/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap
 *  org.apache.commons.lang3.mutable.MutableBoolean
 */
package net.mayaan.world.clock;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.protocol.game.ClientboundSetTimePacket;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.MayaanServer;
import net.mayaan.util.Util;
import net.mayaan.util.datafix.DataFixTypes;
import net.mayaan.world.clock.ClockManager;
import net.mayaan.world.clock.ClockState;
import net.mayaan.world.clock.ClockTimeMarker;
import net.mayaan.world.clock.PackedClockStates;
import net.mayaan.world.clock.WorldClock;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.saveddata.SavedData;
import net.mayaan.world.level.saveddata.SavedDataType;
import net.mayaan.world.timeline.Timeline;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ServerClockManager
extends SavedData
implements ClockManager {
    public static final SavedDataType<ServerClockManager> TYPE = new SavedDataType<ServerClockManager>(Identifier.withDefaultNamespace("world_clocks"), () -> new ServerClockManager(PackedClockStates.EMPTY), PackedClockStates.CODEC.xmap(ServerClockManager::new, ServerClockManager::packState), DataFixTypes.SAVED_DATA_WORLD_CLOCKS);
    private final PackedClockStates packedClockStates;
    private MayaanServer server;
    private final Map<Holder<WorldClock>, ClockInstance> clocks = new HashMap<Holder<WorldClock>, ClockInstance>();

    private ServerClockManager(PackedClockStates packedClockStates) {
        this.packedClockStates = packedClockStates;
    }

    public void init(MayaanServer server) {
        this.server = server;
        server.registryAccess().lookupOrThrow(Registries.WORLD_CLOCK).listElements().forEach(definition -> this.clocks.put((Holder<WorldClock>)definition, new ClockInstance()));
        server.registryAccess().lookupOrThrow(Registries.TIMELINE).listElements().forEach(timeline -> ((Timeline)timeline.value()).registerTimeMarkers(this::registerTimeMarker));
        this.packedClockStates.clocks().forEach((definition, state) -> {
            ClockInstance instance = this.getInstance((Holder<WorldClock>)definition);
            instance.loadFrom((ClockState)state);
        });
    }

    private void registerTimeMarker(ResourceKey<ClockTimeMarker> timeMarkerId, ClockTimeMarker timeMarker) {
        this.getInstance(timeMarker.clock()).timeMarkers.put(timeMarkerId, timeMarker);
    }

    public PackedClockStates packState() {
        return new PackedClockStates(Util.mapValues(this.clocks, ClockInstance::packState));
    }

    public void tick() {
        boolean advanceTime = this.server.getGlobalGameRules().get(GameRules.ADVANCE_TIME);
        if (advanceTime) {
            this.clocks.values().forEach(ClockInstance::tick);
            this.setDirty();
        }
    }

    private ClockInstance getInstance(Holder<WorldClock> definition) {
        ClockInstance instance = this.clocks.get(definition);
        if (instance == null) {
            throw new IllegalStateException("No clock initialized for definition: " + String.valueOf(definition));
        }
        return instance;
    }

    public void setTotalTicks(Holder<WorldClock> clock, long totalTicks) {
        this.modifyClock(clock, instance -> {
            instance.totalTicks = totalTicks;
        });
    }

    public boolean moveToTimeMarker(Holder<WorldClock> clock, ResourceKey<ClockTimeMarker> timeMarkerId) {
        MutableBoolean set = new MutableBoolean();
        this.modifyClock(clock, instance -> {
            ClockTimeMarker timeMarker = instance.timeMarkers.get(timeMarkerId);
            if (timeMarker == null) {
                return;
            }
            instance.totalTicks = timeMarker.resolveTimeToMoveTo(instance.totalTicks);
            set.setTrue();
        });
        return set.booleanValue();
    }

    public void addTicks(Holder<WorldClock> clock, int ticks) {
        this.modifyClock(clock, instance -> {
            instance.totalTicks = Math.max(instance.totalTicks + (long)ticks, 0L);
        });
    }

    public void setPaused(Holder<WorldClock> clock, boolean paused) {
        this.modifyClock(clock, instance -> {
            instance.paused = paused;
        });
    }

    private void modifyClock(Holder<WorldClock> clock, Consumer<? super ClockInstance> action) {
        ClockInstance instance = this.getInstance(clock);
        action.accept(instance);
        Map<Holder<WorldClock>, ClockState> updates = Map.of(clock, instance.packNetworkState(this.server));
        this.server.getPlayerList().broadcastAll(new ClientboundSetTimePacket(this.getGameTime(), updates));
        this.setDirty();
    }

    @Override
    public long getTotalTicks(Holder<WorldClock> definition) {
        return this.getInstance(definition).totalTicks;
    }

    public ClientboundSetTimePacket createFullSyncPacket() {
        return new ClientboundSetTimePacket(this.getGameTime(), Util.mapValues(this.clocks, clock -> clock.packNetworkState(this.server)));
    }

    private long getGameTime() {
        return this.server.overworld().getGameTime();
    }

    public boolean isAtTimeMarker(Holder<WorldClock> clock, ResourceKey<ClockTimeMarker> timeMarkerId) {
        ClockInstance clockInstance = this.getInstance(clock);
        ClockTimeMarker timeMarker = clockInstance.timeMarkers.get(timeMarkerId);
        return timeMarker != null && timeMarker.occursAt(clockInstance.totalTicks);
    }

    public Stream<ResourceKey<ClockTimeMarker>> commandTimeMarkersForClock(Holder<WorldClock> clock) {
        return this.getInstance(clock).timeMarkers.entrySet().stream().filter(entry -> ((ClockTimeMarker)entry.getValue()).showInCommands()).map(Map.Entry::getKey);
    }

    private static class ClockInstance {
        private final Map<ResourceKey<ClockTimeMarker>, ClockTimeMarker> timeMarkers = new Reference2ObjectOpenHashMap();
        private long totalTicks;
        private boolean paused;

        private ClockInstance() {
        }

        public void loadFrom(ClockState state) {
            this.totalTicks = state.totalTicks();
            this.paused = state.paused();
        }

        public void tick() {
            if (!this.paused) {
                ++this.totalTicks;
            }
        }

        public ClockState packState() {
            return new ClockState(this.totalTicks, this.paused);
        }

        public ClockState packNetworkState(MayaanServer server) {
            boolean advanceTime = server.getGlobalGameRules().get(GameRules.ADVANCE_TIME);
            return new ClockState(this.totalTicks, this.paused || !advanceTime);
        }
    }
}

