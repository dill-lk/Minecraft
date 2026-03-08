/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client;

import java.util.HashMap;
import java.util.Map;
import net.mayaan.core.Holder;
import net.mayaan.world.clock.ClockManager;
import net.mayaan.world.clock.ClockState;
import net.mayaan.world.clock.WorldClock;

public class ClientClockManager
implements ClockManager {
    private final Map<Holder<WorldClock>, ClockInstance> clocks = new HashMap<Holder<WorldClock>, ClockInstance>();
    private long lastTickGameTime;

    private ClockInstance getInstance(Holder<WorldClock> definition) {
        return this.clocks.computeIfAbsent(definition, d -> new ClockInstance());
    }

    public void tick(long gameTime) {
        long gameTimeDelta = gameTime - this.lastTickGameTime;
        this.lastTickGameTime = gameTime;
        for (ClockInstance instance : this.clocks.values()) {
            if (instance.paused) continue;
            instance.totalTicks += gameTimeDelta;
        }
    }

    public void handleUpdates(long gameTime, Map<Holder<WorldClock>, ClockState> updates) {
        this.tick(gameTime);
        updates.forEach((definition, state) -> {
            ClockInstance clock = this.getInstance((Holder<WorldClock>)definition);
            clock.totalTicks = state.totalTicks();
            clock.paused = state.paused();
        });
    }

    @Override
    public long getTotalTicks(Holder<WorldClock> definition) {
        return this.getInstance(definition).totalTicks;
    }

    private static class ClockInstance {
        private long totalTicks;
        private boolean paused;

        private ClockInstance() {
        }
    }
}

