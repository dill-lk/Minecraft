/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.multiplayer;

import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.UUID;
import net.mayaan.client.gui.components.ChatComponent;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.multiplayer.LevelLoadTracker;
import net.mayaan.client.multiplayer.PlayerInfo;
import net.mayaan.client.multiplayer.ServerData;
import net.mayaan.client.telemetry.WorldSessionTelemetryManager;
import net.mayaan.core.RegistryAccess;
import net.mayaan.resources.Identifier;
import net.mayaan.server.ServerLinks;
import net.mayaan.world.flag.FeatureFlagSet;
import org.jspecify.annotations.Nullable;

public record CommonListenerCookie(LevelLoadTracker levelLoadTracker, GameProfile localGameProfile, WorldSessionTelemetryManager telemetryManager, RegistryAccess.Frozen receivedRegistries, FeatureFlagSet enabledFeatures, @Nullable String serverBrand, @Nullable ServerData serverData, @Nullable Screen postDisconnectScreen, Map<Identifier, byte[]> serverCookies, @Nullable ChatComponent.State chatState, Map<String, String> customReportDetails, ServerLinks serverLinks, Map<UUID, PlayerInfo> seenPlayers, boolean seenInsecureChatWarning) {
}

