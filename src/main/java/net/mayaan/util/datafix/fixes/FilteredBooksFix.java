/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.Typed
 *  com.mojang.datafixers.schemas.Schema
 */
package net.mayaan.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import net.mayaan.util.Util;
import net.mayaan.util.datafix.fixes.ItemStackTagFix;

public class FilteredBooksFix
extends ItemStackTagFix {
    public FilteredBooksFix(Schema outputSchema) {
        super(outputSchema, "Remove filtered text from books", id -> id.equals("minecraft:writable_book") || id.equals("minecraft:written_book"));
    }

    @Override
    protected Typed<?> fixItemStackTag(Typed<?> tag) {
        return Util.writeAndReadTypedOrThrow(tag, tag.getType(), dynamic -> dynamic.remove("filtered_title").remove("filtered_pages"));
    }
}

