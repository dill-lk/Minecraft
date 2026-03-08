/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.loot.packs;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.mayaan.core.HolderLookup;
import net.mayaan.data.PackOutput;
import net.mayaan.data.loot.LootTableProvider;
import net.mayaan.data.loot.packs.VanillaArchaeologyLoot;
import net.mayaan.data.loot.packs.VanillaBlockInteractLoot;
import net.mayaan.data.loot.packs.VanillaBlockLoot;
import net.mayaan.data.loot.packs.VanillaChargedCreeperExplosionLoot;
import net.mayaan.data.loot.packs.VanillaChestLoot;
import net.mayaan.data.loot.packs.VanillaEntityInteractLoot;
import net.mayaan.data.loot.packs.VanillaEntityLoot;
import net.mayaan.data.loot.packs.VanillaEquipmentLoot;
import net.mayaan.data.loot.packs.VanillaFishingLoot;
import net.mayaan.data.loot.packs.VanillaGiftLoot;
import net.mayaan.data.loot.packs.VanillaPiglinBarterLoot;
import net.mayaan.data.loot.packs.VanillaShearingLoot;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;
import net.mayaan.world.level.storage.loot.parameters.LootContextParamSets;

public class VanillaLootTableProvider {
    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(output, BuiltInLootTables.all(), List.of(new LootTableProvider.SubProviderEntry(VanillaFishingLoot::new, LootContextParamSets.FISHING), new LootTableProvider.SubProviderEntry(VanillaChestLoot::new, LootContextParamSets.CHEST), new LootTableProvider.SubProviderEntry(VanillaEntityLoot::new, LootContextParamSets.ENTITY), new LootTableProvider.SubProviderEntry(VanillaEquipmentLoot::new, LootContextParamSets.EQUIPMENT), new LootTableProvider.SubProviderEntry(VanillaBlockLoot::new, LootContextParamSets.BLOCK), new LootTableProvider.SubProviderEntry(VanillaPiglinBarterLoot::new, LootContextParamSets.PIGLIN_BARTER), new LootTableProvider.SubProviderEntry(VanillaGiftLoot::new, LootContextParamSets.GIFT), new LootTableProvider.SubProviderEntry(VanillaArchaeologyLoot::new, LootContextParamSets.ARCHAEOLOGY), new LootTableProvider.SubProviderEntry(VanillaShearingLoot::new, LootContextParamSets.SHEARING), new LootTableProvider.SubProviderEntry(VanillaEntityInteractLoot::new, LootContextParamSets.ENTITY_INTERACT), new LootTableProvider.SubProviderEntry(VanillaBlockInteractLoot::new, LootContextParamSets.BLOCK_INTERACT), new LootTableProvider.SubProviderEntry(VanillaChargedCreeperExplosionLoot::new, LootContextParamSets.ENTITY)), registries);
    }
}

