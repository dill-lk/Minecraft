/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.core.component.predicates;

import com.mojang.serialization.Codec;
import net.mayaan.advancements.criterion.NbtPredicate;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.predicates.DataComponentPredicate;

public record CustomDataPredicate(NbtPredicate value) implements DataComponentPredicate
{
    public static final Codec<CustomDataPredicate> CODEC = NbtPredicate.CODEC.xmap(CustomDataPredicate::new, CustomDataPredicate::value);

    @Override
    public boolean matches(DataComponentGetter components) {
        return this.value.matches(components);
    }

    public static CustomDataPredicate customData(NbtPredicate value) {
        return new CustomDataPredicate(value);
    }
}

