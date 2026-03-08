/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.tags;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.tags.TagKey;
import net.mayaan.world.item.alchemy.Potion;

public class PotionTags {
    public static final TagKey<Potion> TRADEABLE = PotionTags.create("tradeable");

    private PotionTags() {
    }

    private static TagKey<Potion> create(String name) {
        return TagKey.create(Registries.POTION, Identifier.withDefaultNamespace(name));
    }
}

