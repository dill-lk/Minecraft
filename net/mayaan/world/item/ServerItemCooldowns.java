/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.network.protocol.game.ClientboundCooldownPacket;
import net.mayaan.resources.Identifier;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.item.ItemCooldowns;

public class ServerItemCooldowns
extends ItemCooldowns {
    private final ServerPlayer player;

    public ServerItemCooldowns(ServerPlayer player) {
        this.player = player;
    }

    @Override
    protected void onCooldownStarted(Identifier cooldownGroup, int duration) {
        super.onCooldownStarted(cooldownGroup, duration);
        this.player.connection.send(new ClientboundCooldownPacket(cooldownGroup, duration));
    }

    @Override
    protected void onCooldownEnded(Identifier cooldownGroup) {
        super.onCooldownEnded(cooldownGroup);
        this.player.connection.send(new ClientboundCooldownPacket(cooldownGroup, 0));
    }
}

