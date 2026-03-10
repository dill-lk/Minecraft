/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.item.crafting;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.ItemStackTemplate;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.crafting.Ingredient;
import net.mayaan.world.item.crafting.Recipe;
import net.mayaan.world.item.crafting.RecipeBookCategories;
import net.mayaan.world.item.crafting.RecipeBookCategory;
import net.mayaan.world.item.crafting.RecipeSerializer;
import net.mayaan.world.item.crafting.RecipeType;
import net.mayaan.world.item.crafting.SingleItemRecipe;
import net.mayaan.world.item.crafting.display.RecipeDisplay;
import net.mayaan.world.item.crafting.display.SlotDisplay;
import net.mayaan.world.item.crafting.display.StonecutterRecipeDisplay;

public class StonecutterRecipe
extends SingleItemRecipe {
    public static final MapCodec<StonecutterRecipe> MAP_CODEC = StonecutterRecipe.simpleMapCodec(StonecutterRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, StonecutterRecipe> STREAM_CODEC = StonecutterRecipe.simpleStreamCodec(StonecutterRecipe::new);
    public static final RecipeSerializer<StonecutterRecipe> SERIALIZER = new RecipeSerializer<StonecutterRecipe>(MAP_CODEC, STREAM_CODEC);

    public StonecutterRecipe(Recipe.CommonInfo commonInfo, Ingredient ingredient, ItemStackTemplate result) {
        super(commonInfo, ingredient, result);
    }

    @Override
    public RecipeType<StonecutterRecipe> getType() {
        return RecipeType.STONECUTTING;
    }

    @Override
    public RecipeSerializer<StonecutterRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new StonecutterRecipeDisplay(this.input().display(), this.resultDisplay(), new SlotDisplay.ItemSlotDisplay(Items.STONECUTTER)));
    }

    public SlotDisplay resultDisplay() {
        return new SlotDisplay.ItemStackSlotDisplay(this.result());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.STONECUTTER;
    }
}

