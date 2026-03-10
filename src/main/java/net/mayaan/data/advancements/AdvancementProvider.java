/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.advancements;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.mayaan.advancements.Advancement;
import net.mayaan.advancements.AdvancementHolder;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.CachedOutput;
import net.mayaan.data.DataProvider;
import net.mayaan.data.PackOutput;
import net.mayaan.data.advancements.AdvancementSubProvider;

public class AdvancementProvider
implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final List<AdvancementSubProvider> subProviders;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public AdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, List<AdvancementSubProvider> subProviders) {
        this.pathProvider = output.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
        this.subProviders = subProviders;
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.registries.thenCompose(lookup -> {
            HashSet allAdvancements = new HashSet();
            ArrayList tasks = new ArrayList();
            Consumer<AdvancementHolder> consumer = holder -> {
                if (!allAdvancements.add(holder.id())) {
                    throw new IllegalStateException("Duplicate advancement " + String.valueOf(holder.id()));
                }
                Path path = this.pathProvider.json(holder.id());
                tasks.add(DataProvider.saveStable(cache, lookup, Advancement.CODEC, holder.value(), path));
            };
            for (AdvancementSubProvider subProvider : this.subProviders) {
                subProvider.generate((HolderLookup.Provider)lookup, consumer);
            }
            return CompletableFuture.allOf((CompletableFuture[])tasks.toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public final String getName() {
        return "Advancements";
    }
}

