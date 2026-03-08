/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.telemetry;

import java.time.Duration;
import java.util.UUID;
import net.mayaan.advancements.AdvancementHolder;
import net.mayaan.client.telemetry.TelemetryEventSender;
import net.mayaan.client.telemetry.TelemetryEventType;
import net.mayaan.client.telemetry.TelemetryProperty;
import net.mayaan.client.telemetry.TelemetryPropertyMap;
import net.mayaan.client.telemetry.events.PerformanceMetricsEvent;
import net.mayaan.client.telemetry.events.WorldLoadEvent;
import net.mayaan.client.telemetry.events.WorldLoadTimesEvent;
import net.mayaan.client.telemetry.events.WorldUnloadEvent;
import net.mayaan.resources.Identifier;
import net.mayaan.world.level.GameType;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public class WorldSessionTelemetryManager {
    private final UUID worldSessionId = UUID.randomUUID();
    private final TelemetryEventSender eventSender;
    private final WorldLoadEvent worldLoadEvent;
    private final WorldUnloadEvent worldUnloadEvent = new WorldUnloadEvent();
    private final PerformanceMetricsEvent performanceMetricsEvent;
    private final WorldLoadTimesEvent worldLoadTimesEvent;

    public WorldSessionTelemetryManager(TelemetryEventSender eventSender, boolean newWorld, @Nullable Duration worldLoadDuration, @Nullable String minigameName) {
        this.worldLoadEvent = new WorldLoadEvent(minigameName);
        this.performanceMetricsEvent = new PerformanceMetricsEvent();
        this.worldLoadTimesEvent = new WorldLoadTimesEvent(newWorld, worldLoadDuration);
        this.eventSender = eventSender.decorate(properties -> {
            this.worldLoadEvent.addProperties((TelemetryPropertyMap.Builder)properties);
            properties.put(TelemetryProperty.WORLD_SESSION_ID, this.worldSessionId);
        });
    }

    public void tick() {
        this.performanceMetricsEvent.tick(this.eventSender);
    }

    public void onPlayerInfoReceived(GameType type, boolean hardcore) {
        this.worldLoadEvent.setGameMode(type, hardcore);
        this.worldUnloadEvent.onPlayerInfoReceived();
        this.worldSessionStart();
    }

    public void onServerBrandReceived(String serverBrand) {
        this.worldLoadEvent.setServerBrand(serverBrand);
        this.worldSessionStart();
    }

    public void setTime(long gameTime) {
        this.worldUnloadEvent.setTime(gameTime);
    }

    public void worldSessionStart() {
        if (this.worldLoadEvent.send(this.eventSender)) {
            this.worldLoadTimesEvent.send(this.eventSender);
            this.performanceMetricsEvent.start();
        }
    }

    public void onDisconnect() {
        this.worldLoadEvent.send(this.eventSender);
        this.performanceMetricsEvent.stop();
        this.worldUnloadEvent.send(this.eventSender);
    }

    public void onAdvancementDone(Level level, AdvancementHolder holder) {
        Identifier advancementId = holder.id();
        if (holder.value().sendsTelemetryEvent() && "minecraft".equals(advancementId.getNamespace())) {
            long gameTime = level.getGameTime();
            this.eventSender.send(TelemetryEventType.ADVANCEMENT_MADE, properties -> {
                properties.put(TelemetryProperty.ADVANCEMENT_ID, advancementId.toString());
                properties.put(TelemetryProperty.ADVANCEMENT_GAME_TIME, gameTime);
            });
        }
    }
}

