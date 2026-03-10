/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.tags;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.tags.TagKey;
import net.mayaan.world.entity.decoration.painting.PaintingVariant;

public class PaintingVariantTags {
    public static final TagKey<PaintingVariant> PLACEABLE = PaintingVariantTags.create("placeable");

    private PaintingVariantTags() {
    }

    private static TagKey<PaintingVariant> create(String name) {
        return TagKey.create(Registries.PAINTING_VARIANT, Identifier.withDefaultNamespace(name));
    }
}

