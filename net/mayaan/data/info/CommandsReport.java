/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.mojang.brigadier.CommandDispatcher
 */
package net.mayaan.data.info;

import com.google.gson.JsonElement;
import com.mojang.brigadier.CommandDispatcher;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.synchronization.ArgumentUtils;
import net.mayaan.core.HolderLookup;
import net.mayaan.data.CachedOutput;
import net.mayaan.data.DataProvider;
import net.mayaan.data.PackOutput;

public class CommandsReport
implements DataProvider {
    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public CommandsReport(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.output = output;
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("commands.json");
        return this.registries.thenCompose(provider -> {
            CommandDispatcher<CommandSourceStack> dispatcher = new Commands(Commands.CommandSelection.ALL, Commands.createValidationContext(provider)).getDispatcher();
            return DataProvider.saveStable(cache, (JsonElement)ArgumentUtils.serializeNodeToJson(dispatcher, dispatcher.getRoot()), path);
        });
    }

    @Override
    public final String getName() {
        return "Command Syntax";
    }
}

