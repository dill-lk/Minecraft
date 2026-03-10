/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.tags;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.tags.TagKey;
import net.mayaan.world.level.block.entity.BannerPattern;

public class BannerPatternTags {
    public static final TagKey<BannerPattern> NO_ITEM_REQUIRED = BannerPatternTags.create("no_item_required");
    public static final TagKey<BannerPattern> PATTERN_ITEM_FLOWER = BannerPatternTags.create("pattern_item/flower");
    public static final TagKey<BannerPattern> PATTERN_ITEM_CREEPER = BannerPatternTags.create("pattern_item/creeper");
    public static final TagKey<BannerPattern> PATTERN_ITEM_SKULL = BannerPatternTags.create("pattern_item/skull");
    public static final TagKey<BannerPattern> PATTERN_ITEM_MOJANG = BannerPatternTags.create("pattern_item/mojang");
    public static final TagKey<BannerPattern> PATTERN_ITEM_GLOBE = BannerPatternTags.create("pattern_item/globe");
    public static final TagKey<BannerPattern> PATTERN_ITEM_PIGLIN = BannerPatternTags.create("pattern_item/piglin");
    public static final TagKey<BannerPattern> PATTERN_ITEM_FLOW = BannerPatternTags.create("pattern_item/flow");
    public static final TagKey<BannerPattern> PATTERN_ITEM_GUSTER = BannerPatternTags.create("pattern_item/guster");
    public static final TagKey<BannerPattern> PATTERN_ITEM_FIELD_MASONED = BannerPatternTags.create("pattern_item/field_masoned");
    public static final TagKey<BannerPattern> PATTERN_ITEM_BORDURE_INDENTED = BannerPatternTags.create("pattern_item/bordure_indented");

    private BannerPatternTags() {
    }

    private static TagKey<BannerPattern> create(String name) {
        return TagKey.create(Registries.BANNER_PATTERN, Identifier.withDefaultNamespace(name));
    }
}

