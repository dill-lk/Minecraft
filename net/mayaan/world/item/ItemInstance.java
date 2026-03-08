/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.TypedInstance;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponents;
import net.mayaan.world.item.Item;

public interface ItemInstance
extends TypedInstance<Item>,
DataComponentGetter {
    public static final String FIELD_ID = "id";
    public static final String FIELD_COUNT = "count";
    public static final String FIELD_COMPONENTS = "components";

    public int count();

    default public int getMaxStackSize() {
        return this.getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
    }
}

