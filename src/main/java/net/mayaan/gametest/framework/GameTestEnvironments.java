/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import java.util.List;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.gametest.framework.TestEnvironmentDefinition;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;

public interface GameTestEnvironments {
    public static final String DEFAULT = "default";
    public static final ResourceKey<TestEnvironmentDefinition<?>> DEFAULT_KEY = GameTestEnvironments.create("default");

    private static ResourceKey<TestEnvironmentDefinition<?>> create(String name) {
        return ResourceKey.create(Registries.TEST_ENVIRONMENT, Identifier.withDefaultNamespace(name));
    }

    public static void bootstrap(BootstrapContext<TestEnvironmentDefinition<?>> context) {
        context.register(DEFAULT_KEY, new TestEnvironmentDefinition.AllOf(List.of()));
    }
}

