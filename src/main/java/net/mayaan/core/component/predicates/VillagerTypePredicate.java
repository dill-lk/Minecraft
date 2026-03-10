/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.core.component.predicates;

import com.mojang.serialization.Codec;
import net.mayaan.advancements.criterion.SingleComponentItemPredicate;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.component.DataComponentType;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.registries.Registries;
import net.mayaan.world.entity.npc.villager.VillagerType;

public record VillagerTypePredicate(HolderSet<VillagerType> villagerTypes) implements SingleComponentItemPredicate<Holder<VillagerType>>
{
    public static final Codec<VillagerTypePredicate> CODEC = RegistryCodecs.homogeneousList(Registries.VILLAGER_TYPE).xmap(VillagerTypePredicate::new, VillagerTypePredicate::villagerTypes);

    @Override
    public DataComponentType<Holder<VillagerType>> componentType() {
        return DataComponents.VILLAGER_VARIANT;
    }

    @Override
    public boolean matches(Holder<VillagerType> villagerType) {
        return this.villagerTypes.contains(villagerType);
    }

    public static VillagerTypePredicate villagerTypes(HolderSet<VillagerType> villagerTypes) {
        return new VillagerTypePredicate(villagerTypes);
    }
}

