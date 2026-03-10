/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.data.models.model;

import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.resources.Identifier;
import net.mayaan.world.item.Item;
import net.mayaan.world.level.block.Block;

public class ModelLocationUtils {
    @Deprecated
    public static Identifier decorateBlockModelLocation(String id) {
        return Identifier.withDefaultNamespace("block/" + id);
    }

    public static Identifier decorateItemModelLocation(String id) {
        return Identifier.withDefaultNamespace("item/" + id);
    }

    public static Identifier getModelLocation(Block block, String suffix) {
        Identifier key = BuiltInRegistries.BLOCK.getKey(block);
        return key.withPath(path -> "block/" + path + suffix);
    }

    public static Identifier getModelLocation(Block block) {
        Identifier key = BuiltInRegistries.BLOCK.getKey(block);
        return key.withPrefix("block/");
    }

    public static Identifier getModelLocation(Item item) {
        Identifier key = BuiltInRegistries.ITEM.getKey(item);
        return key.withPrefix("item/");
    }

    public static Identifier getModelLocation(Item item, String suffix) {
        Identifier key = BuiltInRegistries.ITEM.getKey(item);
        return key.withPath(path -> "item/" + path + suffix);
    }
}

