/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.core.component;

import net.mayaan.core.component.DataComponentType;
import net.mayaan.core.component.TypedDataComponent;
import org.jspecify.annotations.Nullable;

public interface DataComponentGetter {
    public <T> @Nullable T get(DataComponentType<? extends T> var1);

    default public <T> T getOrDefault(DataComponentType<? extends T> type, T defaultValue) {
        T value = this.get(type);
        return value != null ? value : defaultValue;
    }

    default public <T> @Nullable TypedDataComponent<T> getTyped(DataComponentType<T> type) {
        T value = this.get(type);
        return value != null ? new TypedDataComponent<T>(type, value) : null;
    }
}

