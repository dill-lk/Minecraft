/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.clock;

import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.clock.WorldClock;

public interface WorldClocks {
    public static final ResourceKey<WorldClock> OVERWORLD = WorldClocks.key("overworld");
    public static final ResourceKey<WorldClock> THE_END = WorldClocks.key("the_end");

    public static void bootstrap(BootstrapContext<WorldClock> context) {
        context.register(OVERWORLD, new WorldClock());
        context.register(THE_END, new WorldClock());
    }

    private static ResourceKey<WorldClock> key(String id) {
        return ResourceKey.create(Registries.WORLD_CLOCK, Identifier.withDefaultNamespace(id));
    }
}

