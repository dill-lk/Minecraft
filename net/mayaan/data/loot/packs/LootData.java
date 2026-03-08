/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.mayaan.data.loot.packs;

import com.google.common.collect.Maps;
import java.util.Map;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.level.ItemLike;
import net.mayaan.world.level.block.Blocks;

public interface LootData {
    public static final Map<DyeColor, ItemLike> WOOL_ITEM_BY_DYE = Maps.newEnumMap(Map.ofEntries(Map.entry(DyeColor.WHITE, Blocks.WHITE_WOOL), Map.entry(DyeColor.ORANGE, Blocks.ORANGE_WOOL), Map.entry(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL), Map.entry(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL), Map.entry(DyeColor.YELLOW, Blocks.YELLOW_WOOL), Map.entry(DyeColor.LIME, Blocks.LIME_WOOL), Map.entry(DyeColor.PINK, Blocks.PINK_WOOL), Map.entry(DyeColor.GRAY, Blocks.GRAY_WOOL), Map.entry(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL), Map.entry(DyeColor.CYAN, Blocks.CYAN_WOOL), Map.entry(DyeColor.PURPLE, Blocks.PURPLE_WOOL), Map.entry(DyeColor.BLUE, Blocks.BLUE_WOOL), Map.entry(DyeColor.BROWN, Blocks.BROWN_WOOL), Map.entry(DyeColor.GREEN, Blocks.GREEN_WOOL), Map.entry(DyeColor.RED, Blocks.RED_WOOL), Map.entry(DyeColor.BLACK, Blocks.BLACK_WOOL)));
}

