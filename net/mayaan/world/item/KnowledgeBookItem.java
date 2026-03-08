/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  org.slf4j.Logger
 */
package net.mayaan.world.item;

import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.mayaan.core.component.DataComponents;
import net.mayaan.resources.ResourceKey;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.crafting.RecipeHolder;
import net.mayaan.world.item.crafting.RecipeManager;
import net.mayaan.world.level.Level;
import org.slf4j.Logger;

public class KnowledgeBookItem
extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();

    public KnowledgeBookItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        List recipeIds = itemStack.getOrDefault(DataComponents.RECIPES, List.of());
        itemStack.consume(1, player);
        if (recipeIds.isEmpty()) {
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide()) {
            RecipeManager recipeManager = level.getServer().getRecipeManager();
            ArrayList recipes = new ArrayList(recipeIds.size());
            for (ResourceKey recipeId : recipeIds) {
                Optional<RecipeHolder<?>> recipe = recipeManager.byKey(recipeId);
                if (recipe.isPresent()) {
                    recipes.add(recipe.get());
                    continue;
                }
                LOGGER.error("Invalid recipe: {}", (Object)recipeId);
                return InteractionResult.FAIL;
            }
            player.awardRecipes(recipes);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResult.SUCCESS;
    }
}

