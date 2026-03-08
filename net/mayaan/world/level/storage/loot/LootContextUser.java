/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.storage.loot;

import java.util.Set;
import net.mayaan.util.context.ContextKey;
import net.mayaan.world.level.storage.loot.Validatable;
import net.mayaan.world.level.storage.loot.ValidationContext;

public interface LootContextUser
extends Validatable {
    default public Set<ContextKey<?>> getReferencedContextParams() {
        return Set.of();
    }

    @Override
    default public void validate(ValidationContext context) {
        context.validateContextUsage(this);
    }
}

