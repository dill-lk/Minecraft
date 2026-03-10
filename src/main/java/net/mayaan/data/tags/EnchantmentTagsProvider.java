/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.tags;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.PackOutput;
import net.mayaan.data.tags.KeyTagProvider;
import net.mayaan.resources.ResourceKey;
import net.mayaan.tags.EnchantmentTags;
import net.mayaan.world.item.enchantment.Enchantment;

public abstract class EnchantmentTagsProvider
extends KeyTagProvider<Enchantment> {
    public EnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.ENCHANTMENT, lookupProvider);
    }

    protected void tooltipOrder(HolderLookup.Provider registries, ResourceKey<Enchantment> ... order) {
        this.tag(EnchantmentTags.TOOLTIP_ORDER).add(order);
        Set<ResourceKey<Enchantment>> set = Set.of(order);
        List unlisted = registries.lookupOrThrow(Registries.ENCHANTMENT).listElements().filter(e -> !set.contains(e.unwrapKey().get())).map(Holder::getRegisteredName).collect(Collectors.toList());
        if (!unlisted.isEmpty()) {
            throw new IllegalStateException("Not all enchantments were registered for tooltip ordering. Missing: " + String.join((CharSequence)", ", unlisted));
        }
    }
}

