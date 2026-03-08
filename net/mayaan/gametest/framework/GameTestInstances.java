/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import java.util.function.Consumer;
import net.mayaan.core.HolderGetter;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.gametest.framework.BuiltinTestFunctions;
import net.mayaan.gametest.framework.FunctionGameTestInstance;
import net.mayaan.gametest.framework.GameTestEnvironments;
import net.mayaan.gametest.framework.GameTestHelper;
import net.mayaan.gametest.framework.GameTestInstance;
import net.mayaan.gametest.framework.TestData;
import net.mayaan.gametest.framework.TestEnvironmentDefinition;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;

public interface GameTestInstances {
    public static final ResourceKey<GameTestInstance> ALWAYS_PASS = GameTestInstances.create("always_pass");

    public static void bootstrap(BootstrapContext<GameTestInstance> context) {
        HolderGetter<Consumer<GameTestHelper>> functions = context.lookup(Registries.TEST_FUNCTION);
        HolderGetter<TestEnvironmentDefinition<?>> batches = context.lookup(Registries.TEST_ENVIRONMENT);
        context.register(ALWAYS_PASS, new FunctionGameTestInstance(BuiltinTestFunctions.ALWAYS_PASS, new TestData(batches.getOrThrow(GameTestEnvironments.DEFAULT_KEY), Identifier.withDefaultNamespace("empty"), 1, 1, false)));
    }

    private static ResourceKey<GameTestInstance> create(String id) {
        return ResourceKey.create(Registries.TEST_INSTANCE, Identifier.withDefaultNamespace(id));
    }
}

