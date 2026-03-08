/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.item.crafting;

import com.mojang.serialization.MapCodec;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStackTemplate;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.crafting.AbstractCookingRecipe;
import net.mayaan.world.item.crafting.Ingredient;
import net.mayaan.world.item.crafting.Recipe;
import net.mayaan.world.item.crafting.RecipeBookCategories;
import net.mayaan.world.item.crafting.RecipeBookCategory;
import net.mayaan.world.item.crafting.RecipeSerializer;
import net.mayaan.world.item.crafting.RecipeType;

public class SmokingRecipe
extends AbstractCookingRecipe {
    public static final MapCodec<SmokingRecipe> MAP_CODEC = SmokingRecipe.cookingMapCodec(SmokingRecipe::new, 100);
    public static final StreamCodec<RegistryFriendlyByteBuf, SmokingRecipe> STREAM_CODEC = SmokingRecipe.cookingStreamCodec(SmokingRecipe::new);
    public static final RecipeSerializer<SmokingRecipe> SERIALIZER = new RecipeSerializer<SmokingRecipe>(MAP_CODEC, STREAM_CODEC);

    public SmokingRecipe(Recipe.CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
        super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
    }

    @Override
    protected Item furnaceIcon() {
        return Items.SMOKER;
    }

    @Override
    public RecipeType<SmokingRecipe> getType() {
        return RecipeType.SMOKING;
    }

    @Override
    public RecipeSerializer<SmokingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.SMOKER_FOOD;
    }
}

