/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core.component.predicates;

import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponentType;
import net.mayaan.core.component.predicates.DataComponentPredicate;

public record AnyValue(DataComponentType<?> type) implements DataComponentPredicate
{
    @Override
    public boolean matches(DataComponentGetter components) {
        return components.get(this.type) != null;
    }
}

