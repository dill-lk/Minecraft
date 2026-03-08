/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.JsonOps
 */
package net.mayaan.data.info;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.component.DataComponentPatch;
import net.mayaan.core.component.TypedDataComponent;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.data.CachedOutput;
import net.mayaan.data.DataProvider;
import net.mayaan.data.PackOutput;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.RegistryOps;

public class RegistryComponentsReport
implements DataProvider {
    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public RegistryComponentsReport(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.output = output;
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.registries.thenCompose(registries -> {
            RegistryOps registryOps = registries.createSerializationContext(JsonOps.INSTANCE);
            ArrayList writes = new ArrayList();
            BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.build((HolderLookup.Provider)registries).forEach(pendingComponents -> {
                PackOutput.PathProvider registryPathProvider = this.output.createRegistryComponentPathProvider(pendingComponents.key());
                pendingComponents.forEach((element, components) -> {
                    Identifier elementId = element.key().identifier();
                    Path elementPath = registryPathProvider.json(elementId);
                    DataComponentPatch patch = DataComponentPatch.builder().set((Iterable<TypedDataComponent<?>>)components).build();
                    JsonObject root = new JsonObject();
                    root.add("components", (JsonElement)DataComponentPatch.CODEC.encodeStart((DynamicOps)registryOps, (Object)patch).getOrThrow(err -> new IllegalStateException("Failed to encode components for item " + String.valueOf(elementId) + ": " + err)));
                    writes.add(DataProvider.saveStable(cache, (JsonElement)root, elementPath));
                });
            });
            return CompletableFuture.allOf((CompletableFuture[])writes.toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public final String getName() {
        return "Default Components";
    }
}

