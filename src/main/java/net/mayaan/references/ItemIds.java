/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.references;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.Item;

public class ItemIds {
    public static final ResourceKey<Item> PUMPKIN_SEEDS = ItemIds.createKey("pumpkin_seeds");
    public static final ResourceKey<Item> MELON_SEEDS = ItemIds.createKey("melon_seeds");

    private static ResourceKey<Item> createKey(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(name));
    }
}

