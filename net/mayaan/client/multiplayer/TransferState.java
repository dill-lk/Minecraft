/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.multiplayer;

import java.util.Map;
import java.util.UUID;
import net.mayaan.client.multiplayer.PlayerInfo;
import net.mayaan.resources.Identifier;

public record TransferState(Map<Identifier, byte[]> cookies, Map<UUID, PlayerInfo> seenPlayers, boolean seenInsecureChatWarning) {
}

