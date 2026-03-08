/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.tags;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.tags.TagKey;
import net.mayaan.world.item.Instrument;

public interface InstrumentTags {
    public static final TagKey<Instrument> REGULAR_GOAT_HORNS = InstrumentTags.create("regular_goat_horns");
    public static final TagKey<Instrument> SCREAMING_GOAT_HORNS = InstrumentTags.create("screaming_goat_horns");
    public static final TagKey<Instrument> GOAT_HORNS = InstrumentTags.create("goat_horns");

    private static TagKey<Instrument> create(String name) {
        return TagKey.create(Registries.INSTRUMENT, Identifier.withDefaultNamespace(name));
    }
}

