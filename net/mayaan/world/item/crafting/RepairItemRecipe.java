/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.item.crafting;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.tags.EnchantmentTags;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.crafting.CraftingInput;
import net.mayaan.world.item.crafting.CustomRecipe;
import net.mayaan.world.item.crafting.RecipeSerializer;
import net.mayaan.world.item.enchantment.EnchantmentHelper;
import net.mayaan.world.item.enchantment.ItemEnchantments;
import net.mayaan.world.level.Level;
import org.jspecify.annotations.Nullable;

public class RepairItemRecipe
extends CustomRecipe {
    public static final RepairItemRecipe INSTANCE = new RepairItemRecipe();
    public static final MapCodec<RepairItemRecipe> MAP_CODEC = MapCodec.unit((Object)INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, RepairItemRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<RepairItemRecipe> SERIALIZER = new RecipeSerializer<RepairItemRecipe>(MAP_CODEC, STREAM_CODEC);

    private static @Nullable Pair<ItemStack, ItemStack> getItemsToCombine(CraftingInput input) {
        if (input.ingredientCount() != 2) {
            return null;
        }
        ItemStack first = null;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack itemStack = input.getItem(i);
            if (itemStack.isEmpty()) continue;
            if (first == null) {
                first = itemStack;
                continue;
            }
            return RepairItemRecipe.canCombine(first, itemStack) ? Pair.of((Object)first, (Object)itemStack) : null;
        }
        return null;
    }

    private static boolean canCombine(ItemStack first, ItemStack second) {
        return second.is(first.getItem()) && first.getCount() == 1 && second.getCount() == 1 && first.has(DataComponents.MAX_DAMAGE) && second.has(DataComponents.MAX_DAMAGE) && first.has(DataComponents.DAMAGE) && second.has(DataComponents.DAMAGE);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return RepairItemRecipe.getItemsToCombine(input) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        Pair<ItemStack, ItemStack> itemsToCombine = RepairItemRecipe.getItemsToCombine(input);
        if (itemsToCombine == null) {
            return ItemStack.EMPTY;
        }
        ItemStack first = (ItemStack)itemsToCombine.getFirst();
        ItemStack second = (ItemStack)itemsToCombine.getSecond();
        int durability = Math.max(first.getMaxDamage(), second.getMaxDamage());
        int remaining1 = first.getMaxDamage() - first.getDamageValue();
        int remaining2 = second.getMaxDamage() - second.getDamageValue();
        int remaining = remaining1 + remaining2 + durability * 5 / 100;
        ItemStack itemStack = new ItemStack(first.getItem());
        itemStack.set(DataComponents.MAX_DAMAGE, durability);
        itemStack.setDamageValue(Math.max(durability - remaining, 0));
        ItemEnchantments firstEnchants = EnchantmentHelper.getEnchantmentsForCrafting(first);
        ItemEnchantments secondEnchants = EnchantmentHelper.getEnchantmentsForCrafting(second);
        EnchantmentHelper.updateEnchantments(itemStack, newEnchantments -> {
            for (Holder enchantment : Sets.union(firstEnchants.keySet(), secondEnchants.keySet())) {
                if (!enchantment.is(EnchantmentTags.CURSE)) continue;
                int enchantLevel = Math.max(firstEnchants.getLevel(enchantment), secondEnchants.getLevel(enchantment));
                newEnchantments.set(enchantment, enchantLevel);
            }
        });
        return itemStack;
    }

    @Override
    public RecipeSerializer<RepairItemRecipe> getSerializer() {
        return SERIALIZER;
    }
}

