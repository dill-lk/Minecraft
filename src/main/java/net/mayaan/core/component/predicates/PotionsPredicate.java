/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.core.component.predicates;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.mayaan.advancements.criterion.SingleComponentItemPredicate;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.component.DataComponentType;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.component.predicates.DataComponentPredicate;
import net.mayaan.core.registries.Registries;
import net.mayaan.world.item.alchemy.Potion;
import net.mayaan.world.item.alchemy.PotionContents;

public record PotionsPredicate(HolderSet<Potion> potions) implements SingleComponentItemPredicate<PotionContents>
{
    public static final Codec<PotionsPredicate> CODEC = RegistryCodecs.homogeneousList(Registries.POTION).xmap(PotionsPredicate::new, PotionsPredicate::potions);

    @Override
    public DataComponentType<PotionContents> componentType() {
        return DataComponents.POTION_CONTENTS;
    }

    @Override
    public boolean matches(PotionContents potionContents) {
        Optional<Holder<Potion>> potion = potionContents.potion();
        return !potion.isEmpty() && this.potions.contains(potion.get());
    }

    public static DataComponentPredicate potions(HolderSet<Potion> potions) {
        return new PotionsPredicate(potions);
    }
}

