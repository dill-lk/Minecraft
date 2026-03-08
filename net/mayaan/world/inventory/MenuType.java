/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.inventory;

import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.flag.FeatureElement;
import net.mayaan.world.flag.FeatureFlag;
import net.mayaan.world.flag.FeatureFlagSet;
import net.mayaan.world.flag.FeatureFlags;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.inventory.AnvilMenu;
import net.mayaan.world.inventory.BeaconMenu;
import net.mayaan.world.inventory.BlastFurnaceMenu;
import net.mayaan.world.inventory.BrewingStandMenu;
import net.mayaan.world.inventory.CartographyTableMenu;
import net.mayaan.world.inventory.ChestMenu;
import net.mayaan.world.inventory.CrafterMenu;
import net.mayaan.world.inventory.CraftingMenu;
import net.mayaan.world.inventory.DispenserMenu;
import net.mayaan.world.inventory.EnchantmentMenu;
import net.mayaan.world.inventory.FurnaceMenu;
import net.mayaan.world.inventory.GrindstoneMenu;
import net.mayaan.world.inventory.HopperMenu;
import net.mayaan.world.inventory.LecternMenu;
import net.mayaan.world.inventory.LoomMenu;
import net.mayaan.world.inventory.MerchantMenu;
import net.mayaan.world.inventory.ShulkerBoxMenu;
import net.mayaan.world.inventory.SmithingMenu;
import net.mayaan.world.inventory.SmokerMenu;
import net.mayaan.world.inventory.StonecutterMenu;

public class MenuType<T extends AbstractContainerMenu>
implements FeatureElement {
    public static final MenuType<ChestMenu> GENERIC_9x1 = MenuType.register("generic_9x1", ChestMenu::oneRow);
    public static final MenuType<ChestMenu> GENERIC_9x2 = MenuType.register("generic_9x2", ChestMenu::twoRows);
    public static final MenuType<ChestMenu> GENERIC_9x3 = MenuType.register("generic_9x3", ChestMenu::threeRows);
    public static final MenuType<ChestMenu> GENERIC_9x4 = MenuType.register("generic_9x4", ChestMenu::fourRows);
    public static final MenuType<ChestMenu> GENERIC_9x5 = MenuType.register("generic_9x5", ChestMenu::fiveRows);
    public static final MenuType<ChestMenu> GENERIC_9x6 = MenuType.register("generic_9x6", ChestMenu::sixRows);
    public static final MenuType<DispenserMenu> GENERIC_3x3 = MenuType.register("generic_3x3", DispenserMenu::new);
    public static final MenuType<CrafterMenu> CRAFTER_3x3 = MenuType.register("crafter_3x3", CrafterMenu::new);
    public static final MenuType<AnvilMenu> ANVIL = MenuType.register("anvil", AnvilMenu::new);
    public static final MenuType<BeaconMenu> BEACON = MenuType.register("beacon", BeaconMenu::new);
    public static final MenuType<BlastFurnaceMenu> BLAST_FURNACE = MenuType.register("blast_furnace", BlastFurnaceMenu::new);
    public static final MenuType<BrewingStandMenu> BREWING_STAND = MenuType.register("brewing_stand", BrewingStandMenu::new);
    public static final MenuType<CraftingMenu> CRAFTING = MenuType.register("crafting", CraftingMenu::new);
    public static final MenuType<EnchantmentMenu> ENCHANTMENT = MenuType.register("enchantment", EnchantmentMenu::new);
    public static final MenuType<FurnaceMenu> FURNACE = MenuType.register("furnace", FurnaceMenu::new);
    public static final MenuType<GrindstoneMenu> GRINDSTONE = MenuType.register("grindstone", GrindstoneMenu::new);
    public static final MenuType<HopperMenu> HOPPER = MenuType.register("hopper", HopperMenu::new);
    public static final MenuType<LecternMenu> LECTERN = MenuType.register("lectern", (containerId, inventory) -> new LecternMenu(containerId));
    public static final MenuType<LoomMenu> LOOM = MenuType.register("loom", LoomMenu::new);
    public static final MenuType<MerchantMenu> MERCHANT = MenuType.register("merchant", MerchantMenu::new);
    public static final MenuType<ShulkerBoxMenu> SHULKER_BOX = MenuType.register("shulker_box", ShulkerBoxMenu::new);
    public static final MenuType<SmithingMenu> SMITHING = MenuType.register("smithing", SmithingMenu::new);
    public static final MenuType<SmokerMenu> SMOKER = MenuType.register("smoker", SmokerMenu::new);
    public static final MenuType<CartographyTableMenu> CARTOGRAPHY_TABLE = MenuType.register("cartography_table", CartographyTableMenu::new);
    public static final MenuType<StonecutterMenu> STONECUTTER = MenuType.register("stonecutter", StonecutterMenu::new);
    private final FeatureFlagSet requiredFeatures;
    private final MenuSupplier<T> constructor;

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuSupplier<T> constructor) {
        return Registry.register(BuiltInRegistries.MENU, name, new MenuType<T>(constructor, FeatureFlags.VANILLA_SET));
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuSupplier<T> constructor, FeatureFlag ... flags) {
        return Registry.register(BuiltInRegistries.MENU, name, new MenuType<T>(constructor, FeatureFlags.REGISTRY.subset(flags)));
    }

    private MenuType(MenuSupplier<T> constructor, FeatureFlagSet requiredFeatures) {
        this.constructor = constructor;
        this.requiredFeatures = requiredFeatures;
    }

    public T create(int containerId, Inventory inventory) {
        return this.constructor.create(containerId, inventory);
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    private static interface MenuSupplier<T extends AbstractContainerMenu> {
        public T create(int var1, Inventory var2);
    }
}

