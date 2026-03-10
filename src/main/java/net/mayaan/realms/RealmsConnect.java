/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.mayaan.realms;

import com.mojang.logging.LogUtils;
import com.maayanlabs.realmsclient.dto.RealmsServer;
import java.net.InetSocketAddress;
import java.util.Objects;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.screens.DisconnectedScreen;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.mayaan.client.multiplayer.LevelLoadTracker;
import net.mayaan.client.multiplayer.chat.report.ReportEnvironment;
import net.mayaan.client.multiplayer.resolver.ServerAddress;
import net.mayaan.client.quickplay.QuickPlayLog;
import net.mayaan.client.resources.server.ServerPackManager;
import net.mayaan.network.Connection;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.login.ServerboundHelloPacket;
import net.mayaan.server.network.EventLoopGroupHolder;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class RealmsConnect {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Screen onlineScreen;
    private volatile boolean aborted;
    private @Nullable Connection connection;

    public RealmsConnect(Screen onlineScreen) {
        this.onlineScreen = onlineScreen;
    }

    public void connect(final RealmsServer server, ServerAddress hostAndPort) {
        final Mayaan minecraft = Mayaan.getInstance();
        minecraft.prepareForMultiplayer();
        minecraft.getNarrator().saySystemNow(Component.translatable("mco.connect.success"));
        final String hostname = hostAndPort.getHost();
        final int port = hostAndPort.getPort();
        new Thread(this, "Realms-connect-task"){
            final /* synthetic */ RealmsConnect this$0;
            {
                RealmsConnect realmsConnect = this$0;
                Objects.requireNonNull(realmsConnect);
                this.this$0 = realmsConnect;
                super(name);
            }

            @Override
            public void run() {
                InetSocketAddress address = null;
                try {
                    address = new InetSocketAddress(hostname, port);
                    if (this.this$0.aborted) {
                        return;
                    }
                    this.this$0.connection = Connection.connectToServer(address, EventLoopGroupHolder.remote(minecraft.options.useNativeTransport()), minecraft.getDebugOverlay().getBandwidthLogger());
                    if (this.this$0.aborted) {
                        return;
                    }
                    ClientHandshakePacketListenerImpl clientHandshakePacketListener = new ClientHandshakePacketListenerImpl(this.this$0.connection, minecraft, server.toServerData(hostname), this.this$0.onlineScreen, false, null, status -> {}, new LevelLoadTracker(), null);
                    if (server.isMinigameActive()) {
                        clientHandshakePacketListener.setMinigameName(server.minigameName);
                    }
                    if (this.this$0.aborted) {
                        return;
                    }
                    this.this$0.connection.initiateServerboundPlayConnection(hostname, port, clientHandshakePacketListener);
                    if (this.this$0.aborted) {
                        return;
                    }
                    this.this$0.connection.send(new ServerboundHelloPacket(minecraft.getUser().getName(), minecraft.getUser().getProfileId()));
                    minecraft.updateReportEnvironment(ReportEnvironment.realm(server));
                    minecraft.quickPlayLog().setWorldData(QuickPlayLog.Type.REALMS, String.valueOf(server.id), Objects.requireNonNullElse(server.name, "unknown"));
                    minecraft.getDownloadedPackSource().configureForServerControl(this.this$0.connection, ServerPackManager.PackPromptStatus.ALLOWED);
                }
                catch (Exception e) {
                    minecraft.getDownloadedPackSource().cleanupAfterDisconnect();
                    if (this.this$0.aborted) {
                        return;
                    }
                    LOGGER.error("Couldn't connect to world", (Throwable)e);
                    String message = e.toString();
                    if (address != null) {
                        String filter = String.valueOf(address) + ":" + port;
                        message = message.replaceAll(filter, "");
                    }
                    DisconnectedScreen screen = new DisconnectedScreen(this.this$0.onlineScreen, (Component)Component.translatable("mco.connect.failed"), Component.translatable("disconnect.genericReason", message), CommonComponents.GUI_BACK);
                    minecraft.execute(() -> minecraft.setScreen(screen));
                }
            }
        }.start();
    }

    public void abort() {
        this.aborted = true;
        if (this.connection != null && this.connection.isConnected()) {
            this.connection.disconnect(Component.translatable("disconnect.genericReason"));
            this.connection.handleDisconnection();
        }
    }

    public void tick() {
        if (this.connection != null) {
            if (this.connection.isConnected()) {
                this.connection.tick();
            } else {
                this.connection.handleDisconnection();
            }
        }
    }
}

