/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.crafting;

import net.mayaan.core.Registry;
import net.mayaan.world.item.crafting.BannerDuplicateRecipe;
import net.mayaan.world.item.crafting.BlastingRecipe;
import net.mayaan.world.item.crafting.BookCloningRecipe;
import net.mayaan.world.item.crafting.CampfireCookingRecipe;
import net.mayaan.world.item.crafting.DecoratedPotRecipe;
import net.mayaan.world.item.crafting.DyeRecipe;
import net.mayaan.world.item.crafting.FireworkRocketRecipe;
import net.mayaan.world.item.crafting.FireworkStarFadeRecipe;
import net.mayaan.world.item.crafting.FireworkStarRecipe;
import net.mayaan.world.item.crafting.ImbueRecipe;
import net.mayaan.world.item.crafting.MapExtendingRecipe;
import net.mayaan.world.item.crafting.RecipeSerializer;
import net.mayaan.world.item.crafting.RepairItemRecipe;
import net.mayaan.world.item.crafting.ShapedRecipe;
import net.mayaan.world.item.crafting.ShapelessRecipe;
import net.mayaan.world.item.crafting.ShieldDecorationRecipe;
import net.mayaan.world.item.crafting.SmeltingRecipe;
import net.mayaan.world.item.crafting.SmithingTransformRecipe;
import net.mayaan.world.item.crafting.SmithingTrimRecipe;
import net.mayaan.world.item.crafting.SmokingRecipe;
import net.mayaan.world.item.crafting.StonecutterRecipe;
import net.mayaan.world.item.crafting.TransmuteRecipe;

public class RecipeSerializers {
    public static Object bootstrap(Registry<RecipeSerializer<?>> registry) {
        Registry.register(registry, "crafting_shaped", ShapedRecipe.SERIALIZER);
        Registry.register(registry, "crafting_shapeless", ShapelessRecipe.SERIALIZER);
        Registry.register(registry, "crafting_dye", DyeRecipe.SERIALIZER);
        Registry.register(registry, "crafting_imbue", ImbueRecipe.SERIALIZER);
        Registry.register(registry, "crafting_transmute", TransmuteRecipe.SERIALIZER);
        Registry.register(registry, "crafting_decorated_pot", DecoratedPotRecipe.SERIALIZER);
        Registry.register(registry, "crafting_special_bookcloning", BookCloningRecipe.SERIALIZER);
        Registry.register(registry, "crafting_special_mapextending", MapExtendingRecipe.SERIALIZER);
        Registry.register(registry, "crafting_special_firework_rocket", FireworkRocketRecipe.SERIALIZER);
        Registry.register(registry, "crafting_special_firework_star", FireworkStarRecipe.SERIALIZER);
        Registry.register(registry, "crafting_special_firework_star_fade", FireworkStarFadeRecipe.SERIALIZER);
        Registry.register(registry, "crafting_special_bannerduplicate", BannerDuplicateRecipe.SERIALIZER);
        Registry.register(registry, "crafting_special_shielddecoration", ShieldDecorationRecipe.SERIALIZER);
        Registry.register(registry, "crafting_special_repairitem", RepairItemRecipe.SERIALIZER);
        Registry.register(registry, "smelting", SmeltingRecipe.SERIALIZER);
        Registry.register(registry, "blasting", BlastingRecipe.SERIALIZER);
        Registry.register(registry, "smoking", SmokingRecipe.SERIALIZER);
        Registry.register(registry, "campfire_cooking", CampfireCookingRecipe.SERIALIZER);
        Registry.register(registry, "stonecutting", StonecutterRecipe.SERIALIZER);
        Registry.register(registry, "smithing_transform", SmithingTransformRecipe.SERIALIZER);
        return Registry.register(registry, "smithing_trim", SmithingTrimRecipe.SERIALIZER);
    }
}

