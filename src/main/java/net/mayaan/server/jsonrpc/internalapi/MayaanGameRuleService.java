/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.stream.Stream;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.jsonrpc.methods.GameRulesService;
import net.mayaan.world.level.gamerules.GameRule;

public interface MayaanGameRuleService {
    public <T> GameRulesService.GameRuleUpdate<T> updateGameRule(GameRulesService.GameRuleUpdate<T> var1, ClientInfo var2);

    public <T> T getRuleValue(GameRule<T> var1);

    public <T> GameRulesService.GameRuleUpdate<T> getTypedRule(GameRule<T> var1, T var2);

    public Stream<GameRule<?>> getAvailableGameRules();
}

