/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.storage.loot;

import net.mayaan.core.HolderGetter;
import net.mayaan.util.ProblemReporter;
import net.mayaan.util.context.ContextKeySet;
import net.mayaan.world.level.storage.loot.ValidationContext;
import net.mayaan.world.level.storage.loot.parameters.LootContextParamSets;
import org.jspecify.annotations.Nullable;

public class ValidationContextSource {
    private final ProblemReporter reporter;
    private final HolderGetter.Provider lootData;
    private @Nullable ValidationContext entityContext;

    public ValidationContextSource(ProblemReporter reporter, HolderGetter.Provider lootData) {
        this.reporter = reporter;
        this.lootData = lootData;
    }

    public ValidationContext context(ContextKeySet params) {
        return new ValidationContext(this.reporter, params, this.lootData);
    }

    public ValidationContext entityContext() {
        if (this.entityContext == null) {
            this.entityContext = this.context(LootContextParamSets.ADVANCEMENT_ENTITY);
        }
        return this.entityContext;
    }
}

