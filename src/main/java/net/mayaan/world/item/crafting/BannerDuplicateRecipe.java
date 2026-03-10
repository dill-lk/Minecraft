/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.crafting;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.NonNullList;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.BannerItem;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemStackTemplate;
import net.mayaan.world.item.crafting.CraftingInput;
import net.mayaan.world.item.crafting.CustomRecipe;
import net.mayaan.world.item.crafting.Ingredient;
import net.mayaan.world.item.crafting.RecipeSerializer;
import net.mayaan.world.item.crafting.TransmuteRecipe;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.entity.BannerPatternLayers;

public class BannerDuplicateRecipe
extends CustomRecipe {
    public static final MapCodec<BannerDuplicateRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Ingredient.CODEC.fieldOf("banner").forGetter(o -> o.banner), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)).apply((Applicative)i, BannerDuplicateRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BannerDuplicateRecipe> STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, o -> o.banner, ItemStackTemplate.STREAM_CODEC, o -> o.result, BannerDuplicateRecipe::new);
    public static final RecipeSerializer<BannerDuplicateRecipe> SERIALIZER = new RecipeSerializer<BannerDuplicateRecipe>(MAP_CODEC, STREAM_CODEC);
    private final Ingredient banner;
    private final ItemStackTemplate result;

    public BannerDuplicateRecipe(Ingredient banner, ItemStackTemplate result) {
        this.banner = banner;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != 2) {
            return false;
        }
        DyeColor color = null;
        boolean hasTarget = false;
        boolean hasSource = false;
        for (int slot = 0; slot < input.size(); ++slot) {
            Item item;
            ItemStack itemStack = input.getItem(slot);
            if (itemStack.isEmpty()) continue;
            if (this.banner.test(itemStack) && (item = itemStack.getItem()) instanceof BannerItem) {
                BannerItem banner = (BannerItem)item;
                if (color == null) {
                    color = banner.getColor();
                } else if (color != banner.getColor()) {
                    return false;
                }
            } else {
                return false;
            }
            int patternCount = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY).layers().size();
            if (patternCount > 6) {
                return false;
            }
            if (patternCount > 0) {
                if (hasSource) {
                    return false;
                }
                hasSource = true;
                continue;
            }
            if (hasTarget) {
                return false;
            }
            hasTarget = true;
        }
        return hasSource && hasTarget;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        for (int slot = 0; slot < input.size(); ++slot) {
            int patternCount;
            ItemStack itemStack = input.getItem(slot);
            if (itemStack.isEmpty() || (patternCount = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY).layers().size()) <= 0 || patternCount > 6) continue;
            return TransmuteRecipe.createWithOriginalComponents(this.result, itemStack);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int slot = 0; slot < result.size(); ++slot) {
            ItemStack itemStack = input.getItem(slot);
            if (itemStack.isEmpty()) continue;
            ItemStackTemplate remainder = itemStack.getItem().getCraftingRemainder();
            if (remainder != null) {
                result.set(slot, remainder.create());
                continue;
            }
            if (itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY).layers().isEmpty()) continue;
            result.set(slot, itemStack.copyWithCount(1));
        }
        return result;
    }

    @Override
    public RecipeSerializer<BannerDuplicateRecipe> getSerializer() {
        return SERIALIZER;
    }
}

