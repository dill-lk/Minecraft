/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.logging.LogUtils
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.mayaan.server.network;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.network.Connection;
import net.mayaan.network.DisconnectionDetails;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.TickablePacketListener;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.PacketUtils;
import net.mayaan.network.protocol.common.ClientboundCustomPayloadPacket;
import net.mayaan.network.protocol.common.ClientboundServerLinksPacket;
import net.mayaan.network.protocol.common.ServerboundClientInformationPacket;
import net.mayaan.network.protocol.common.ServerboundResourcePackPacket;
import net.mayaan.network.protocol.common.custom.BrandPayload;
import net.mayaan.network.protocol.configuration.ClientboundUpdateEnabledFeaturesPacket;
import net.mayaan.network.protocol.configuration.ServerConfigurationPacketListener;
import net.mayaan.network.protocol.configuration.ServerboundAcceptCodeOfConductPacket;
import net.mayaan.network.protocol.configuration.ServerboundFinishConfigurationPacket;
import net.mayaan.network.protocol.configuration.ServerboundSelectKnownPacks;
import net.mayaan.network.protocol.game.GameProtocols;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.RegistryLayer;
import net.mayaan.server.ServerLinks;
import net.mayaan.server.level.ClientInformation;
import net.mayaan.server.network.CommonListenerCookie;
import net.mayaan.server.network.ConfigurationTask;
import net.mayaan.server.network.ServerCommonPacketListenerImpl;
import net.mayaan.server.network.config.JoinWorldTask;
import net.mayaan.server.network.config.PrepareSpawnTask;
import net.mayaan.server.network.config.ServerCodeOfConductConfigurationTask;
import net.mayaan.server.network.config.ServerResourcePackConfigurationTask;
import net.mayaan.server.network.config.SynchronizeRegistriesTask;
import net.mayaan.server.packs.repository.KnownPack;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.PlayerList;
import net.mayaan.world.flag.FeatureFlags;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class ServerConfigurationPacketListenerImpl
extends ServerCommonPacketListenerImpl
implements ServerConfigurationPacketListener,
TickablePacketListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Component DISCONNECT_REASON_INVALID_DATA = Component.translatable("multiplayer.disconnect.invalid_player_data");
    private static final Component DISCONNECT_REASON_CONFIGURATION_ERROR = Component.translatable("multiplayer.disconnect.configuration_error");
    private final GameProfile gameProfile;
    private final Queue<ConfigurationTask> configurationTasks = new ConcurrentLinkedQueue<ConfigurationTask>();
    private @Nullable ConfigurationTask currentTask;
    private ClientInformation clientInformation;
    private @Nullable SynchronizeRegistriesTask synchronizeRegistriesTask;
    private @Nullable PrepareSpawnTask prepareSpawnTask;

    public ServerConfigurationPacketListenerImpl(MayaanServer server, Connection connection, CommonListenerCookie cookie) {
        super(server, connection, cookie);
        this.gameProfile = cookie.gameProfile();
        this.clientInformation = cookie.clientInformation();
    }

    @Override
    protected GameProfile playerProfile() {
        return this.gameProfile;
    }

    @Override
    public void onDisconnect(DisconnectionDetails details) {
        LOGGER.info("{} ({}) lost connection: {}", new Object[]{this.gameProfile.name(), this.gameProfile.id(), details.reason().getString()});
        if (this.prepareSpawnTask != null) {
            this.prepareSpawnTask.close();
            this.prepareSpawnTask = null;
        }
        super.onDisconnect(details);
    }

    @Override
    public boolean isAcceptingMessages() {
        return this.connection.isConnected();
    }

    public void startConfiguration() {
        this.send(new ClientboundCustomPayloadPacket(new BrandPayload(this.server.getServerModName())));
        ServerLinks serverLinks = this.server.serverLinks();
        if (!serverLinks.isEmpty()) {
            this.send(new ClientboundServerLinksPacket(serverLinks.untrust()));
        }
        LayeredRegistryAccess<RegistryLayer> registries = this.server.registries();
        List<KnownPack> knownPacks = this.server.getResourceManager().listPacks().flatMap(packResources -> packResources.location().knownPackInfo().stream()).toList();
        this.send(new ClientboundUpdateEnabledFeaturesPacket(FeatureFlags.REGISTRY.toNames(this.server.getWorldData().enabledFeatures())));
        this.synchronizeRegistriesTask = new SynchronizeRegistriesTask(knownPacks, registries);
        this.configurationTasks.add(this.synchronizeRegistriesTask);
        this.addOptionalTasks();
        this.returnToWorld();
    }

    public void returnToWorld() {
        this.prepareSpawnTask = new PrepareSpawnTask(this.server, new NameAndId(this.gameProfile));
        this.configurationTasks.add(this.prepareSpawnTask);
        this.configurationTasks.add(new JoinWorldTask());
        this.startNextTask();
    }

    private void addOptionalTasks() {
        Map<String, String> codeOfConducts = this.server.getCodeOfConducts();
        if (!codeOfConducts.isEmpty()) {
            this.configurationTasks.add(new ServerCodeOfConductConfigurationTask(() -> {
                String codeOfConduct = (String)codeOfConducts.get(this.clientInformation.language().toLowerCase(Locale.ROOT));
                if (codeOfConduct == null) {
                    codeOfConduct = (String)codeOfConducts.get("en_us");
                }
                if (codeOfConduct == null) {
                    codeOfConduct = (String)codeOfConducts.values().iterator().next();
                }
                return codeOfConduct;
            }));
        }
        this.server.getServerResourcePack().ifPresent(info -> this.configurationTasks.add(new ServerResourcePackConfigurationTask((MayaanServer.ServerResourcePackInfo)info)));
    }

    @Override
    public void handleClientInformation(ServerboundClientInformationPacket packet) {
        this.clientInformation = packet.information();
    }

    @Override
    public void handleResourcePackResponse(ServerboundResourcePackPacket packet) {
        super.handleResourcePackResponse(packet);
        if (packet.action().isTerminal()) {
            this.finishCurrentTask(ServerResourcePackConfigurationTask.TYPE);
        }
    }

    @Override
    public void handleSelectKnownPacks(ServerboundSelectKnownPacks packet) {
        PacketUtils.ensureRunningOnSameThread(packet, this, this.server.packetProcessor());
        if (this.synchronizeRegistriesTask == null) {
            throw new IllegalStateException("Unexpected response from client: received pack selection, but no negotiation ongoing");
        }
        this.synchronizeRegistriesTask.handleResponse(packet.knownPacks(), this::send);
        this.finishCurrentTask(SynchronizeRegistriesTask.TYPE);
    }

    @Override
    public void handleAcceptCodeOfConduct(ServerboundAcceptCodeOfConductPacket packet) {
        this.finishCurrentTask(ServerCodeOfConductConfigurationTask.TYPE);
    }

    @Override
    public void handleConfigurationFinished(ServerboundFinishConfigurationPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, this, this.server.packetProcessor());
        this.finishCurrentTask(JoinWorldTask.TYPE);
        this.connection.setupOutboundProtocol(GameProtocols.CLIENTBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator(this.server.registryAccess())));
        try {
            PlayerList playerList = this.server.getPlayerList();
            if (playerList.getPlayer(this.gameProfile.id()) != null) {
                this.disconnect(PlayerList.DUPLICATE_LOGIN_DISCONNECT_MESSAGE);
                return;
            }
            Component loginError = playerList.canPlayerLogin(this.connection.getRemoteAddress(), new NameAndId(this.gameProfile));
            if (loginError != null) {
                this.disconnect(loginError);
                return;
            }
            Objects.requireNonNull(this.prepareSpawnTask).spawnPlayer(this.connection, this.createCookie(this.clientInformation));
        }
        catch (Exception e) {
            LOGGER.error("Couldn't place player in world", (Throwable)e);
            this.disconnect(DISCONNECT_REASON_INVALID_DATA);
        }
    }

    @Override
    public void tick() {
        this.keepConnectionAlive();
        ConfigurationTask task = this.currentTask;
        if (task != null) {
            try {
                if (task.tick()) {
                    this.finishCurrentTask(task.type());
                }
            }
            catch (Exception e) {
                LOGGER.error("Failed to tick configuration task {}", (Object)task.type(), (Object)e);
                this.disconnect(DISCONNECT_REASON_CONFIGURATION_ERROR);
            }
        }
        if (this.prepareSpawnTask != null) {
            this.prepareSpawnTask.keepAlive();
        }
    }

    private void startNextTask() {
        if (this.currentTask != null) {
            throw new IllegalStateException("Task " + this.currentTask.type().id() + " has not finished yet");
        }
        if (!this.isAcceptingMessages()) {
            return;
        }
        ConfigurationTask task = this.configurationTasks.poll();
        if (task != null) {
            this.currentTask = task;
            try {
                task.start(this::send);
            }
            catch (Exception e) {
                LOGGER.error("Failed to start configuration task {}", (Object)task.type(), (Object)e);
                this.disconnect(DISCONNECT_REASON_CONFIGURATION_ERROR);
            }
        }
    }

    private void finishCurrentTask(ConfigurationTask.Type taskTypeToFinish) {
        ConfigurationTask.Type currentTaskType;
        ConfigurationTask.Type type = currentTaskType = this.currentTask != null ? this.currentTask.type() : null;
        if (!taskTypeToFinish.equals(currentTaskType)) {
            throw new IllegalStateException("Unexpected request for task finish, current task: " + String.valueOf(currentTaskType) + ", requested: " + String.valueOf(taskTypeToFinish));
        }
        this.currentTask = null;
        this.startNextTask();
    }
}

