/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.gametest.framework;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.Registries;
import net.mayaan.gametest.framework.GameTestHelper;
import net.mayaan.gametest.framework.TestFunctionLoader;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;

public class BuiltinTestFunctions
extends TestFunctionLoader {
    public static final ResourceKey<Consumer<GameTestHelper>> ALWAYS_PASS = BuiltinTestFunctions.create("always_pass");
    public static final Consumer<GameTestHelper> ALWAYS_PASS_INSTANCE = GameTestHelper::succeed;

    private static ResourceKey<Consumer<GameTestHelper>> create(String name) {
        return ResourceKey.create(Registries.TEST_FUNCTION, Identifier.withDefaultNamespace(name));
    }

    public static Consumer<GameTestHelper> bootstrap(Registry<Consumer<GameTestHelper>> registry) {
        BuiltinTestFunctions.registerLoader(new BuiltinTestFunctions());
        BuiltinTestFunctions.runLoaders(registry);
        return ALWAYS_PASS_INSTANCE;
    }

    @Override
    public void load(BiConsumer<ResourceKey<Consumer<GameTestHelper>>, Consumer<GameTestHelper>> register) {
        register.accept(ALWAYS_PASS, ALWAYS_PASS_INSTANCE);
    }
}

