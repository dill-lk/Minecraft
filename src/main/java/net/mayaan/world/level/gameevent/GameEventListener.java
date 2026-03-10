/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.gameevent;

import net.mayaan.core.Holder;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.gameevent.PositionSource;
import net.mayaan.world.phys.Vec3;

public interface GameEventListener {
    public PositionSource getListenerSource();

    public int getListenerRadius();

    public boolean handleGameEvent(ServerLevel var1, Holder<GameEvent> var2, GameEvent.Context var3, Vec3 var4);

    default public DeliveryMode getDeliveryMode() {
        return DeliveryMode.UNSPECIFIED;
    }

    public static enum DeliveryMode {
        UNSPECIFIED,
        BY_DISTANCE;

    }

    public static interface Provider<T extends GameEventListener> {
        public T getListener();
    }
}

