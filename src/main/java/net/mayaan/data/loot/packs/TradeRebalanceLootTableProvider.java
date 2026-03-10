/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.loot.packs;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.mayaan.core.HolderLookup;
import net.mayaan.data.PackOutput;
import net.mayaan.data.loot.LootTableProvider;
import net.mayaan.data.loot.packs.TradeRebalanceChestLoot;
import net.mayaan.world.level.storage.loot.parameters.LootContextParamSets;

public class TradeRebalanceLootTableProvider {
    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(TradeRebalanceChestLoot::new, LootContextParamSets.CHEST)), registries);
    }
}

