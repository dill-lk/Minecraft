/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.crafting;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemStackTemplate;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.crafting.CraftingInput;
import net.mayaan.world.item.crafting.CraftingRecipe;
import net.mayaan.world.item.crafting.Ingredient;
import net.mayaan.world.item.crafting.NormalCraftingRecipe;
import net.mayaan.world.item.crafting.PlacementInfo;
import net.mayaan.world.item.crafting.Recipe;
import net.mayaan.world.item.crafting.RecipeSerializer;
import net.mayaan.world.item.crafting.ShapedRecipePattern;
import net.mayaan.world.item.crafting.display.RecipeDisplay;
import net.mayaan.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.mayaan.world.item.crafting.display.SlotDisplay;
import net.mayaan.world.level.Level;

public class ShapedRecipe
extends NormalCraftingRecipe {
    public static final MapCodec<ShapedRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo), (App)CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo), (App)ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)).apply((Applicative)i, ShapedRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> STREAM_CODEC = StreamCodec.composite(Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo, CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo, ShapedRecipePattern.STREAM_CODEC, o -> o.pattern, ItemStackTemplate.STREAM_CODEC, o -> o.result, ShapedRecipe::new);
    public static final RecipeSerializer<ShapedRecipe> SERIALIZER = new RecipeSerializer<ShapedRecipe>(MAP_CODEC, STREAM_CODEC);
    private final ShapedRecipePattern pattern;
    private final ItemStackTemplate result;

    public ShapedRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ShapedRecipePattern pattern, ItemStackTemplate result) {
        super(commonInfo, bookInfo);
        this.pattern = pattern;
        this.result = result;
    }

    @Override
    public RecipeSerializer<ShapedRecipe> getSerializer() {
        return SERIALIZER;
    }

    @VisibleForTesting
    public List<Optional<Ingredient>> getIngredients() {
        return this.pattern.ingredients();
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(this.pattern.ingredients());
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return this.pattern.matches(input);
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return this.result.create();
    }

    public int getWidth() {
        return this.pattern.width();
    }

    public int getHeight() {
        return this.pattern.height();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ShapedCraftingRecipeDisplay(this.pattern.width(), this.pattern.height(), this.pattern.ingredients().stream().map(e -> e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(), new SlotDisplay.ItemStackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }
}

