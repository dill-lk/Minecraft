/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  org.slf4j.Logger
 */
package net.mayaan.server;

import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.mayaan.commands.CommandBuildContext;
import net.mayaan.commands.Commands;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.core.Registry;
import net.mayaan.core.component.DataComponentInitializers;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.server.RegistryLayer;
import net.mayaan.server.ReloadableServerRegistries;
import net.mayaan.server.ServerAdvancementManager;
import net.mayaan.server.ServerFunctionLibrary;
import net.mayaan.server.packs.resources.PreparableReloadListener;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.server.packs.resources.SimpleReloadInstance;
import net.mayaan.server.permissions.PermissionSet;
import net.mayaan.util.Unit;
import net.mayaan.world.flag.FeatureFlagSet;
import net.mayaan.world.item.crafting.RecipeManager;
import org.slf4j.Logger;

public class ReloadableServerResources {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final CompletableFuture<Unit> DATA_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);
    private final ReloadableServerRegistries.Holder fullRegistryHolder;
    private final Commands commands;
    private final RecipeManager recipes;
    private final ServerAdvancementManager advancements;
    private final ServerFunctionLibrary functionLibrary;
    private final List<Registry.PendingTags<?>> postponedTags;
    private final List<DataComponentInitializers.PendingComponents<?>> newComponents;

    private ReloadableServerResources(LayeredRegistryAccess<RegistryLayer> fullLayers, HolderLookup.Provider loadingContext, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, List<Registry.PendingTags<?>> postponedTags, PermissionSet functionCompilationPermissions, List<DataComponentInitializers.PendingComponents<?>> newComponents) {
        this.fullRegistryHolder = new ReloadableServerRegistries.Holder(fullLayers.compositeAccess());
        this.postponedTags = postponedTags;
        this.newComponents = newComponents;
        this.recipes = new RecipeManager(loadingContext);
        this.commands = new Commands(commandSelection, CommandBuildContext.simple(loadingContext, enabledFeatures));
        this.advancements = new ServerAdvancementManager(loadingContext);
        this.functionLibrary = new ServerFunctionLibrary(functionCompilationPermissions, this.commands.getDispatcher());
    }

    public ServerFunctionLibrary getFunctionLibrary() {
        return this.functionLibrary;
    }

    public ReloadableServerRegistries.Holder fullRegistries() {
        return this.fullRegistryHolder;
    }

    public RecipeManager getRecipeManager() {
        return this.recipes;
    }

    public Commands getCommands() {
        return this.commands;
    }

    public ServerAdvancementManager getAdvancements() {
        return this.advancements;
    }

    public List<PreparableReloadListener> listeners() {
        return List.of(this.recipes, this.functionLibrary, this.advancements);
    }

    public static CompletableFuture<ReloadableServerResources> loadResources(ResourceManager resourceManager, LayeredRegistryAccess<RegistryLayer> contextLayers, List<Registry.PendingTags<?>> updatedContextTags, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, PermissionSet functionCompilationPermissions, Executor backgroundExecutor, Executor mainThreadExecutor) {
        return ReloadableServerRegistries.reload(contextLayers, updatedContextTags, resourceManager, backgroundExecutor).thenCompose(fullRegistries -> CompletableFuture.supplyAsync(() -> BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.build(fullRegistries.lookupWithUpdatedTags()), backgroundExecutor).thenCompose(pendingComponents -> {
            ReloadableServerResources result = new ReloadableServerResources(fullRegistries.layers(), fullRegistries.lookupWithUpdatedTags(), enabledFeatures, commandSelection, updatedContextTags, functionCompilationPermissions, (List<DataComponentInitializers.PendingComponents<?>>)pendingComponents);
            return SimpleReloadInstance.create(resourceManager, result.listeners(), backgroundExecutor, mainThreadExecutor, DATA_RELOAD_INITIAL_TASK, LOGGER.isDebugEnabled()).done().thenApply(ignore -> result);
        }));
    }

    public void updateComponentsAndStaticRegistryTags() {
        this.postponedTags.forEach(Registry.PendingTags::apply);
        this.newComponents.forEach(DataComponentInitializers.PendingComponents::apply);
    }
}

