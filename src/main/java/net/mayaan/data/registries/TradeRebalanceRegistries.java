/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.registries;

import java.util.concurrent.CompletableFuture;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.RegistrySetBuilder;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.registries.RegistryPatchGenerator;
import net.mayaan.world.item.trading.TradeRebalanceVillagerTrades;

public class TradeRebalanceRegistries {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.VILLAGER_TRADE, TradeRebalanceVillagerTrades::bootstrap);

    public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(CompletableFuture<HolderLookup.Provider> vanilla) {
        return RegistryPatchGenerator.createLookup(vanilla, BUILDER);
    }
}

