/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.mayaan.server.dedicated.DedicatedServer;
import net.mayaan.server.jsonrpc.JsonRpcLogger;
import net.mayaan.server.jsonrpc.internalapi.MayaanAllowListService;
import net.mayaan.server.jsonrpc.internalapi.MayaanAllowListServiceImpl;
import net.mayaan.server.jsonrpc.internalapi.MayaanBanListService;
import net.mayaan.server.jsonrpc.internalapi.MayaanBanListServiceImpl;
import net.mayaan.server.jsonrpc.internalapi.MayaanExecutorService;
import net.mayaan.server.jsonrpc.internalapi.MayaanExecutorServiceImpl;
import net.mayaan.server.jsonrpc.internalapi.MayaanGameRuleService;
import net.mayaan.server.jsonrpc.internalapi.MayaanGameRuleServiceImpl;
import net.mayaan.server.jsonrpc.internalapi.MayaanOperatorListService;
import net.mayaan.server.jsonrpc.internalapi.MayaanOperatorListServiceImpl;
import net.mayaan.server.jsonrpc.internalapi.MayaanPlayerListService;
import net.mayaan.server.jsonrpc.internalapi.MayaanPlayerListServiceImpl;
import net.mayaan.server.jsonrpc.internalapi.MayaanServerSettingsService;
import net.mayaan.server.jsonrpc.internalapi.MayaanServerSettingsServiceImpl;
import net.mayaan.server.jsonrpc.internalapi.MayaanServerStateService;
import net.mayaan.server.jsonrpc.internalapi.MayaanServerStateServiceImpl;
import net.mayaan.server.notifications.NotificationManager;

public class MayaanApi {
    private final NotificationManager notificationManager;
    private final MayaanAllowListService allowListService;
    private final MayaanBanListService banListService;
    private final MayaanPlayerListService minecraftPlayerListService;
    private final MayaanGameRuleService gameRuleService;
    private final MayaanOperatorListService minecraftOperatorListService;
    private final MayaanServerSettingsService minecraftServerSettingsService;
    private final MayaanServerStateService minecraftServerStateService;
    private final MayaanExecutorService executorService;

    public MayaanApi(NotificationManager notificationManager, MayaanAllowListService allowListService, MayaanBanListService banListService, MayaanPlayerListService minecraftPlayerListService, MayaanGameRuleService gameRuleService, MayaanOperatorListService minecraftOperatorListService, MayaanServerSettingsService minecraftServerSettingsService, MayaanServerStateService minecraftServerStateService, MayaanExecutorService executorService) {
        this.notificationManager = notificationManager;
        this.allowListService = allowListService;
        this.banListService = banListService;
        this.minecraftPlayerListService = minecraftPlayerListService;
        this.gameRuleService = gameRuleService;
        this.minecraftOperatorListService = minecraftOperatorListService;
        this.minecraftServerSettingsService = minecraftServerSettingsService;
        this.minecraftServerStateService = minecraftServerStateService;
        this.executorService = executorService;
    }

    public <V> CompletableFuture<V> submit(Supplier<V> supplier) {
        return this.executorService.submit(supplier);
    }

    public CompletableFuture<Void> submit(Runnable runnable) {
        return this.executorService.submit(runnable);
    }

    public MayaanAllowListService allowListService() {
        return this.allowListService;
    }

    public MayaanBanListService banListService() {
        return this.banListService;
    }

    public MayaanPlayerListService playerListService() {
        return this.minecraftPlayerListService;
    }

    public MayaanGameRuleService gameRuleService() {
        return this.gameRuleService;
    }

    public MayaanOperatorListService operatorListService() {
        return this.minecraftOperatorListService;
    }

    public MayaanServerSettingsService serverSettingsService() {
        return this.minecraftServerSettingsService;
    }

    public MayaanServerStateService serverStateService() {
        return this.minecraftServerStateService;
    }

    public NotificationManager notificationManager() {
        return this.notificationManager;
    }

    public static MayaanApi of(DedicatedServer server) {
        JsonRpcLogger jsonrpcLogger = new JsonRpcLogger();
        MayaanAllowListServiceImpl allowListService = new MayaanAllowListServiceImpl(server, jsonrpcLogger);
        MayaanBanListServiceImpl banListService = new MayaanBanListServiceImpl(server, jsonrpcLogger);
        MayaanPlayerListServiceImpl playerListService = new MayaanPlayerListServiceImpl(server, jsonrpcLogger);
        MayaanGameRuleServiceImpl gameRuleService = new MayaanGameRuleServiceImpl(server, jsonrpcLogger);
        MayaanOperatorListServiceImpl operatorListService = new MayaanOperatorListServiceImpl(server, jsonrpcLogger);
        MayaanServerSettingsServiceImpl serverSettingsService = new MayaanServerSettingsServiceImpl(server, jsonrpcLogger);
        MayaanServerStateServiceImpl serverStateService = new MayaanServerStateServiceImpl(server, jsonrpcLogger);
        MayaanExecutorServiceImpl executorService = new MayaanExecutorServiceImpl(server);
        return new MayaanApi(server.notificationManager(), allowListService, banListService, playerListService, gameRuleService, operatorListService, serverSettingsService, serverStateService, executorService);
    }
}

