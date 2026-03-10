/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.references;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.block.Block;

public class BlockIds {
    public static final ResourceKey<Block> PUMPKIN = BlockIds.createKey("pumpkin");
    public static final ResourceKey<Block> PUMPKIN_STEM = BlockIds.createKey("pumpkin_stem");
    public static final ResourceKey<Block> ATTACHED_PUMPKIN_STEM = BlockIds.createKey("attached_pumpkin_stem");
    public static final ResourceKey<Block> MELON = BlockIds.createKey("melon");
    public static final ResourceKey<Block> MELON_STEM = BlockIds.createKey("melon_stem");
    public static final ResourceKey<Block> ATTACHED_MELON_STEM = BlockIds.createKey("attached_melon_stem");
    public static final ResourceKey<Block> DIRT = BlockIds.createKey("dirt");

    private static ResourceKey<Block> createKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(name));
    }
}

