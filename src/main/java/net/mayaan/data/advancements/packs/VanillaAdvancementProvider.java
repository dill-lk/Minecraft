/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.advancements.packs;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.mayaan.core.HolderLookup;
import net.mayaan.data.PackOutput;
import net.mayaan.data.advancements.AdvancementProvider;
import net.mayaan.data.advancements.packs.VanillaAdventureAdvancements;
import net.mayaan.data.advancements.packs.VanillaHusbandryAdvancements;
import net.mayaan.data.advancements.packs.VanillaNetherAdvancements;
import net.mayaan.data.advancements.packs.VanillaStoryAdvancements;
import net.mayaan.data.advancements.packs.VanillaTheEndAdvancements;

public class VanillaAdvancementProvider {
    public static AdvancementProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new AdvancementProvider(output, registries, List.of(new VanillaTheEndAdvancements(), new VanillaHusbandryAdvancements(), new VanillaAdventureAdvancements(), new VanillaNetherAdvancements(), new VanillaStoryAdvancements()));
    }
}

