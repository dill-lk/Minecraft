/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.mayaan.SharedConstants;
import net.mayaan.network.protocol.Packet;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.server.players.NameAndId;
import net.mayaan.util.debug.DebugSubscription;

public class ServerDebugSubscribers {
    private final MayaanServer server;
    private final Map<DebugSubscription<?>, List<ServerPlayer>> enabledSubscriptions = new HashMap();

    public ServerDebugSubscribers(MayaanServer server) {
        this.server = server;
    }

    private List<ServerPlayer> getSubscribersFor(DebugSubscription<?> subscription) {
        return this.enabledSubscriptions.getOrDefault(subscription, List.of());
    }

    public void tick() {
        this.enabledSubscriptions.values().forEach(List::clear);
        for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
            for (DebugSubscription<?> subscription : player.debugSubscriptions()) {
                this.enabledSubscriptions.computeIfAbsent(subscription, s -> new ArrayList()).add(player);
            }
        }
        this.enabledSubscriptions.values().removeIf(List::isEmpty);
    }

    public void broadcastToAll(DebugSubscription<?> subscription, Packet<?> packet) {
        for (ServerPlayer player : this.getSubscribersFor(subscription)) {
            player.connection.send(packet);
        }
    }

    public Set<DebugSubscription<?>> enabledSubscriptions() {
        return Set.copyOf(this.enabledSubscriptions.keySet());
    }

    public boolean hasAnySubscriberFor(DebugSubscription<?> subscription) {
        return !this.getSubscribersFor(subscription).isEmpty();
    }

    public boolean hasRequiredPermissions(ServerPlayer player) {
        NameAndId nameAndId = player.nameAndId();
        if (SharedConstants.IS_RUNNING_IN_IDE && this.server.isSingleplayerOwner(nameAndId)) {
            return true;
        }
        return this.server.getPlayerList().isOp(nameAndId);
    }
}

