/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.advancements;

import java.util.function.Consumer;
import net.mayaan.advancements.Advancement;
import net.mayaan.advancements.AdvancementHolder;
import net.mayaan.core.HolderLookup;
import net.mayaan.resources.Identifier;

public interface AdvancementSubProvider {
    public void generate(HolderLookup.Provider var1, Consumer<AdvancementHolder> var2);

    public static AdvancementHolder createPlaceholder(String id) {
        return Advancement.Builder.advancement().build(Identifier.parse(id));
    }
}

