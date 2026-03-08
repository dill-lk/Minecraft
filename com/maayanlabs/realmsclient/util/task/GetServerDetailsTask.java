/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  org.slf4j.Logger
 */
package com.maayanlabs.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.maayanlabs.realmsclient.client.RealmsClient;
import com.maayanlabs.realmsclient.dto.RealmsJoinInformation;
import com.maayanlabs.realmsclient.dto.RealmsServer;
import com.maayanlabs.realmsclient.exception.RealmsServiceException;
import com.maayanlabs.realmsclient.exception.RetryCallException;
import com.maayanlabs.realmsclient.gui.screens.RealmsBrokenWorldScreen;
import com.maayanlabs.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.maayanlabs.realmsclient.gui.screens.RealmsLongRunningMcoConnectTaskScreen;
import com.maayanlabs.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.maayanlabs.realmsclient.gui.screens.RealmsPopups;
import com.maayanlabs.realmsclient.gui.screens.RealmsTermsScreen;
import com.maayanlabs.realmsclient.util.task.ConnectTask;
import com.maayanlabs.realmsclient.util.task.LongRunningTask;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.PopupScreen;
import net.mayaan.client.gui.screens.GenericMessageScreen;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.resources.server.DownloadedPackSource;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import org.slf4j.Logger;

public class GetServerDetailsTask
extends LongRunningTask {
    private static final Component APPLYING_PACK_TEXT = Component.translatable("multiplayer.applyingPack");
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Component TITLE = Component.translatable("mco.connect.connecting");
    private final RealmsServer server;
    private final Screen lastScreen;

    public GetServerDetailsTask(Screen lastScreen, RealmsServer server) {
        this.lastScreen = lastScreen;
        this.server = server;
    }

    @Override
    public void run() {
        RealmsJoinInformation address;
        try {
            address = this.fetchServerAddress();
        }
        catch (CancellationException e) {
            LOGGER.info("User aborted connecting to realms");
            return;
        }
        catch (RealmsServiceException e) {
            switch (e.realmsError.errorCode()) {
                case 6002: {
                    GetServerDetailsTask.setScreen(new RealmsTermsScreen(this.lastScreen, this.server));
                    return;
                }
                case 6006: {
                    boolean isOwner = Mayaan.getInstance().isLocalPlayer(this.server.ownerUUID);
                    GetServerDetailsTask.setScreen(isOwner ? new RealmsBrokenWorldScreen(this.lastScreen, this.server.id, this.server.isMinigameActive()) : new RealmsGenericErrorScreen(Component.translatable("mco.brokenworld.nonowner.title"), Component.translatable("mco.brokenworld.nonowner.error"), this.lastScreen));
                    return;
                }
            }
            this.error(e);
            LOGGER.error("Couldn't connect to world", (Throwable)e);
            return;
        }
        catch (TimeoutException e) {
            this.error(Component.translatable("mco.errorMessage.connectionFailure"));
            return;
        }
        catch (Exception e) {
            LOGGER.error("Couldn't connect to world", (Throwable)e);
            this.error(e);
            return;
        }
        if (address.address() == null) {
            this.error(Component.translatable("mco.errorMessage.connectionFailure"));
            return;
        }
        boolean requiresResourcePack = address.resourcePackUrl() != null && address.resourcePackHash() != null;
        RealmsLongRunningMcoTaskScreen nextScreen = requiresResourcePack ? this.resourcePackDownloadConfirmationScreen(address, GetServerDetailsTask.generatePackId(this.server), this::connectScreen) : this.connectScreen(address);
        GetServerDetailsTask.setScreen(nextScreen);
    }

    private static UUID generatePackId(RealmsServer serverData) {
        if (serverData.minigameName != null) {
            return UUID.nameUUIDFromBytes(("minigame:" + serverData.minigameName).getBytes(StandardCharsets.UTF_8));
        }
        return UUID.nameUUIDFromBytes(("realms:" + Objects.requireNonNullElse(serverData.name, "") + ":" + serverData.activeSlot).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    private RealmsJoinInformation fetchServerAddress() throws RealmsServiceException, TimeoutException, CancellationException {
        RealmsClient client = RealmsClient.getOrCreate();
        for (int i = 0; i < 40; ++i) {
            if (this.aborted()) {
                throw new CancellationException();
            }
            try {
                return client.join(this.server.id);
            }
            catch (RetryCallException e) {
                GetServerDetailsTask.pause(e.delaySeconds);
                continue;
            }
        }
        throw new TimeoutException();
    }

    public RealmsLongRunningMcoTaskScreen connectScreen(RealmsJoinInformation address) {
        return new RealmsLongRunningMcoConnectTaskScreen(this.lastScreen, address, (LongRunningTask)new ConnectTask(this.lastScreen, this.server, address));
    }

    private PopupScreen resourcePackDownloadConfirmationScreen(RealmsJoinInformation address, UUID packId, Function<RealmsJoinInformation, Screen> onCompletionScreen) {
        MutableComponent popupMessage = Component.translatable("mco.configure.world.resourcepack.question");
        return RealmsPopups.infoPopupScreen(this.lastScreen, popupMessage, popupScreen -> {
            GetServerDetailsTask.setScreen(new GenericMessageScreen(APPLYING_PACK_TEXT));
            ((CompletableFuture)this.scheduleResourcePackDownload(address, packId).thenRun(() -> GetServerDetailsTask.setScreen((Screen)onCompletionScreen.apply(address)))).exceptionally(e -> {
                Mayaan.getInstance().getDownloadedPackSource().cleanupAfterDisconnect();
                LOGGER.error("Failed to download resource pack from {}", (Object)address, e);
                GetServerDetailsTask.setScreen(new RealmsGenericErrorScreen(Component.translatable("mco.download.resourcePack.fail"), this.lastScreen));
                return null;
            });
        });
    }

    private CompletableFuture<?> scheduleResourcePackDownload(RealmsJoinInformation address, UUID packId) {
        try {
            if (address.resourcePackUrl() == null) {
                return CompletableFuture.failedFuture(new IllegalStateException("resourcePackUrl was null"));
            }
            if (address.resourcePackHash() == null) {
                return CompletableFuture.failedFuture(new IllegalStateException("resourcePackHash was null"));
            }
            DownloadedPackSource packSource = Mayaan.getInstance().getDownloadedPackSource();
            CompletableFuture<Void> result = packSource.waitForPackFeedback(packId);
            packSource.allowServerPacks();
            packSource.pushPack(packId, new URL(address.resourcePackUrl()), address.resourcePackHash());
            return result;
        }
        catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}

