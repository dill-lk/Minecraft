/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.stream.Stream;
import net.mayaan.server.dedicated.DedicatedServer;
import net.mayaan.server.jsonrpc.JsonRpcLogger;
import net.mayaan.server.jsonrpc.internalapi.MayaanGameRuleService;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.jsonrpc.methods.GameRulesService;
import net.mayaan.world.level.gamerules.GameRule;
import net.mayaan.world.level.gamerules.GameRules;

public class MayaanGameRuleServiceImpl
implements MayaanGameRuleService {
    private final DedicatedServer server;
    private final GameRules gameRules;
    private final JsonRpcLogger jsonrpcLogger;

    public MayaanGameRuleServiceImpl(DedicatedServer server, JsonRpcLogger jsonrpcLogger) {
        this.server = server;
        this.gameRules = server.getGameRules();
        this.jsonrpcLogger = jsonrpcLogger;
    }

    @Override
    public <T> GameRulesService.GameRuleUpdate<T> updateGameRule(GameRulesService.GameRuleUpdate<T> update, ClientInfo clientInfo) {
        GameRule<T> gameRule = update.gameRule();
        T oldValue = this.gameRules.get(gameRule);
        T newValue = update.value();
        this.gameRules.set(gameRule, newValue, this.server);
        this.jsonrpcLogger.log(clientInfo, "Game rule '{}' updated from '{}' to '{}'", gameRule.id(), gameRule.serialize(oldValue), gameRule.serialize(newValue));
        return update;
    }

    @Override
    public <T> GameRulesService.GameRuleUpdate<T> getTypedRule(GameRule<T> gameRule, T value) {
        return new GameRulesService.GameRuleUpdate<T>(gameRule, value);
    }

    @Override
    public Stream<GameRule<?>> getAvailableGameRules() {
        return this.gameRules.availableRules();
    }

    @Override
    public <T> T getRuleValue(GameRule<T> gameRule) {
        return this.gameRules.get(gameRule);
    }
}

